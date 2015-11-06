package net.iowntheinter.shadow.controller.salt.impl

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
class TreeMapSubscriptionManager implements SVXSubscriptionManager {
    private boolean cluster = vx.isClustered()
    private SharedData sd
    private EventBus eb
    private Logger log
    private subscriptionChannel
    def HashMap localMatchTable
    def TreeMap typeMatches
    def TreeMap identMatches
    def TreeMap verbMatches
    def TreeMap resultMatches


    TreeMapSubscriptionManager(SharedData s, EventBus e) {
        sd = s
        eb = e
        typeMatches = new TreeMap()
        identMatches = new TreeMap()
        verbMatches = new TreeMap()
        resultMatches = new TreeMap()

        log = LoggerFactory.getLogger("saltReactor:subscriptionManager")


        localMatchTable = new HashMap();

    }
    /*examples:
    got event: salt/cloud/edge-gamma/requesting
    got event: salt/job/20151028033249814239/ret/saucer
    got event: salt/key
        {"_stamp":"2015-10-28T03:13:36.135827","act":"reject","id":"unallocated.barefruit.co.uk","result":true}
     */

    public boolean process(Event e) {
        def data = e.getData()
        def tag = e.getTag()
        log.info("got event ${tag}")
        String type
        String ident
        String verb
        def result
        List fields = tag.tokenize('/')
        if (fields[0] != "salt") {
            type = fields[0]
            log.debug("data for this oddball: ${data}")
        } else
            type = fields[1]
        switch (type) {
            case 'minion_start':
                ident = data['id']
                verb = "minion_start"
                break;
            case 'auth':
                ident = data['id']
                verb = data["act"]
                result = data["result"]
                break;
            case 'job':
                ident = fields[2]
                break;
            case 'key':
                ident = data['id']
                verb = data['act']
                break;
            case 'cloud':
                ident = fields[2]
                verb = fields[3]
                break;

        }
        log.info("type ${type} data : ${data}")
        return (true)
    }


    private boolean sendToVertxBus(channel, pkg, cb) {
        cb(eb.send(channel, pkg))
    }

    private boolean sendToSaltBus(tag, data, cb) {
        cb(saltClient.sendEvent(tag, data))
    }
    //start listening for subscription requests on the salt event bus, mapping them to vertx eb channels
    /*  { "action": "matchfw",
          "match" : { "ident":"blah", "verb":"rah", "result": "nah", "type":"ska" },
          "strgy" : "any || all",
          "forward" : "some-vxeb-channel", "expire":"date"}
        //returns subscription id
        //remove by
        { "action":"remove",
          "remove" : "subscriptionid" }
        { "action":"list", "channel" : "name || *"}
        { "action saltPush",
          "saltPush" : { "tag" : data } }
        maybe this should have a ttl / expire time option?
    * */

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

    private boolean addMatchrule(rule, cb) {
        def response = ""
        try {
            localMatchTable.putIfAbsent(rule, fwchan)
            response = "added ${rule} local matcher fwd to ${fwchan}"
            log.info(response)
        } catch (e) {
            response = "error adding ${rule} for ${fwchan}"
            log.error(response)
        }
        cb(response)
    }

    private boolean dropMatchrule(rule, cb) {

    }

    private boolean checkMatch(Map sevt, fwcb) {

    }

    private boolean processEBReq(JsonObject req, cb) {
        def type = req.getString("action")
        def response
        switch (type) {

            case 'matchfw':
                def rule = req.getJsonObject('match').toString()
                def fwchan = req.getString('forward')

                break;
            case 'remove':

                try {
                    localMatchTable.putIfAbsent(rule, fwchan)
                    response = "added ${rule} local matcher fwd to ${fwchan}"
                    log.info(response)
                } catch (e) {
                    response = "error adding ${rule} for ${fwchan}"
                    log.error(response)
                }

                break;
            case 'list':
                break;
            case 'saltPush':
                break;
        }
        def matchRule = req.getJsonObject("match")
        def fwChannel = req.getString("forward")


    }
}