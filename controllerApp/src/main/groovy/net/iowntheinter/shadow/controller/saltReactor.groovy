package net.iowntheinter.shadow.controller

import com.suse.saltstack.netapi.calls.Client
import com.suse.saltstack.netapi.client.SaltStackClient

/**
 * Created by grant on 10/20/15.
 */
import com.suse.saltstack.netapi.event.EventListener
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.eventbus.EventBus
import io.vertx.groovy.core.shareddata.AsyncMap
import io.vertx.groovy.core.shareddata.LocalMap
import io.vertx.groovy.core.shareddata.SharedData

import javax.websocket.CloseReason;
import com.suse.saltstack.netapi.datatypes.Event;
import io.vertx.groovy.core.Vertx;
import io.vertx.core.json.JsonObject;

class saltReactor implements EventListener {
    private Vertx vx
    private SharedData sd
    private EventBus eb
    private JsonObject config
    private Logger log
    private SaltStackClient saltClient
    def mgr

    saltReactor(Vertx v, JsonObject c, SaltStackClient sc) {
        vx = v;
        saltClient=sc
        sd = v.sharedData()
        config = c
        eb = v.eventBus()
        log = LoggerFactory.getLogger("saltReactor")
        mgr = new subscriptionManager(sd, eb)
    }
    CloseReason closeReason;


    @Override
    void notify(Event event) {
        def d = event.getData()
        def t = event.getTag()
        mgr.process(event)
    }

    /**
     * Notify the listener that the backing event stream was closed.  Listener may
     * need to recreate the event stream or take other actions.
     * @param closeReason the close reason
     */
    @Override
    void eventStreamClosed(CloseReason closeReason) {
        println("saltReactor stream closed: ${closeReason}")
    }

    class subscriptionManager {
        private boolean cluster = vx.isClustered()
        private SharedData sd
        private EventBus eb
        private Logger log
        private subscriptionChannel
        def AsyncMap cmap
        def LocalMap lmap

        subscriptionManager(SharedData s, EventBus e) {
            sd = s
            eb = e

            log = LoggerFactory.getLogger("saltReactor:subscriptionManager")
            if (cluster) {
                sd.getClusterWideMap("saltEventConsumers", { res ->
                    if (res.succeeded()) {
                        cmap = res.result()
                    } else {
                        // Something went wrong!
                    }
                })
            } else {
                lmap = sd.getLocalMap("saltEventConsumers")
            }
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
            }
            else
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



        private boolean sendToVertxBus(channel,pkg,cb){
            cb(eb.send(channel,pkg))
        }
        private boolean sendToSaltBus(tag, data, cb){
            cb(saltClient.sendEvent(tag,data))
        }
        //start listening for subscription requests on the salt event bus, mapping them to vertx eb channels
        /*  { "match" : { "ident":"blah", "verb":"rah", "result": "nah", "type":"ska" }, "forward" : "some-vxeb-channel"}
            //returns subscription id
            //remove by
            { "remove" : "subscriptionid" }
            { "saltPush" : { "tag" : data } } // push a msg onto the salt bus (from vxbus)
            maybe this should have a ttl / expire time option?
        * */
        public boolean manage() {
            subscriptionChannel = eb.consumer("saltEventManger")
            subscriptionChannel.handler({ message ->

                println("I have received a message: ${message.body()}")
            }).completionHandler({ res ->
                if (res.succeeded()) {
                    log.info("The saltEventManger eb registration has reached all nodes")
                } else {
                    log.error("saltEventManger eb Registration failed!")
                }
            })
        }
        private boolean parseEBReq(JsonObject req,cb){
            def matchRule = req.getJsonObject("match")
            def fwChannel = req.getString("forward")
        }

        boolean getSubscriptions() {


        }

        boolean notifySubscriber(event) {

        }


    }


}