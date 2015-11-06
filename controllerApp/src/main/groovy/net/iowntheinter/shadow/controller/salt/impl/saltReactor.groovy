package net.iowntheinter.shadow.controller.salt.impl

import com.suse.saltstack.netapi.client.SaltStackClient

/**
 * Created by grant on 10/20/15.
 */
import com.suse.saltstack.netapi.event.EventListener
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.eventbus.EventBus
import io.vertx.groovy.core.shareddata.SharedData
import com.suse.saltstack.netapi.datatypes.Event;
import io.vertx.groovy.core.Vertx;
import io.vertx.core.json.JsonObject

import javax.websocket.CloseReason;

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
        saltClient = sc
        sd = v.sharedData()
        config = c
        eb = v.eventBus()
        log = LoggerFactory.getLogger("saltReactor")
        mgr = new TreeMapSubscriptionManager(sd, eb)
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



        boolean getSubscriptions() {


        }

        boolean notifySubscriber(event) {

        }


    }


