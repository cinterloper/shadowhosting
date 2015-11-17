package net.iowntheinter.shadow.controller.salt.impl

import com.suse.saltstack.netapi.client.SaltStackClient
import com.suse.saltstack.netapi.datatypes.Event
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.eventbus.EventBus
import io.vertx.groovy.core.shareddata.SharedData
import net.iowntheinter.shadow.controller.salt.SVXSubscriptionManager

/**
 * Created by grant on 11/5/15.
 */
class SimplePipeSubscriptionManager implements SVXSubscriptionManager {
    private SharedData sd
    private EventBus eb
    private Logger log
    private subscriptionChannel
    private SaltStackClient saltClient

    SimplePipeSubscriptionManager(SharedData s, EventBus e, SaltStackClient c) {
        sd = s
        eb = e
        log = LoggerFactory.getLogger("saltReactor:subscriptionManager")
        saltClient = c
    }
    /*examples:
    //perhaps subscribe to sub-tag based address truncated at 2nd level, containing the full tag in the msg
      ex salt/cloud (all)
    got event: salt/cloud/edge-gamma/requesting
    got event: salt/job/20151028033249814239/ret/saucer
    got event: salt/key
        {"_stamp":"2015-10-28T03:13:36.135827","act":"reject","id":"unallocated.barefruit.co.uk","result":true}
     */

    @Override
    public boolean process(Event e) {
        def data = e.getData()
        def tag = e.getTag()
        log.info("got event ${tag}")
        String type
        String ident
        String verb
        def result
        def dstAddr
        List fields = tag.tokenize('/')
        if (fields.last() == fields.first()) {
            dstAddr = fields.first();
        } else { //addressed to salt/job or salt/cloud, simple
            dstAddr = fields[0] + '/' + fields[1]
        }
        if (fields[0] != "salt") {
            type = fields[0]
            log.debug("data for this oddball: ${data}")
        }
        //do the vulcan mind meld
        sendToVertxBus(dstAddr, ['tags': fields, 'data': data], { res ->
            log.info('result of vxbus send for ' + fields + " : " + res)
            return (true)
        })
        log.info("event: ${["type": type, "data": data, "ident": ident, "verb": verb]}")


    }


    private boolean sendToVertxBus(channel, pkg, cb) {
        def ret = true
        try {
            eb.publish(channel, pkg)
        } catch (e) {
            ret = false
            cb([status:ret, error:e.getMessage()])
        }
        if (ret)
            cb([status:ret, error:null])
    }

    private boolean sendToSaltBus(tag, data, cb) {
        def ret = true
        try {

            saltClient.sendEvent(tag, data) //we should switch this to sendEventAsync
        } catch (e) {
            ret = false
            cb([status:ret, error:e.getMessage()])
        }
        if (ret)
            cb([status:ret, error:null])
    }

    @Override
    public boolean manage(String vxchannel) {
        def subscriptionChannel = eb.consumer(vxchannel)
        subscriptionChannel.handler({ message ->
            JsonObject jreq = new JsonObject()
            try {
                jreq = message.body() as JsonObject
            } catch (e) {
                log.error("error ${e}")
            }
            processEBReq(jreq, { response ->
                log.info('processed ${response}')

            })
            println("I have received a message: ${message.body()}")
        }).completionHandler({ res ->
            if (res.succeeded()) {
                log.info("The saltEventManger eb registration has reached all nodes")
            } else {
                log.error("saltEventManger eb Registration failed!")
            }
        })
    }


    private boolean processEBReq(JsonObject req, cb) {
        def type = req.getString("action")
        def response
        switch (type) {
            case 'list':
                break;
            case 'saltPush': //perhaps should block loops of this message
                def addr = type.getString("addr")
                def data = type.getString("data")
                sendToSaltBus(addr, data, { res ->
                    log.info("result of sending to salt ${addr} : ${res} ")
                })
                break;
        }


    }
}