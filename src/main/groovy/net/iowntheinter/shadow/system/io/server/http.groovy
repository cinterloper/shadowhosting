package net.iowntheinter.shadow.system.io.server

import io.vertx.core.AbstractVerticle
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.sockjs.BridgeOptions
import io.vertx.ext.web.handler.sockjs.PermittedOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import net.iowntheinter.kvdn.kvserver

/**
 * Created by grant on 4/30/16.
 */
class http extends AbstractVerticle {
     def s , router;

    @Override
    void start() throws Exception {
        super.start()
        s = new kvserver();
        router = Router.router(vertx)
        def sjsh = SockJSHandler.create(vertx)
        def options = new BridgeOptions().addOutboundPermitted(new PermittedOptions()
                .setAddressRegex(".*"));
        sjsh.bridge(options)
        router.route().handler(BodyHandler.create())
        router.route("/eb/*").handler(sjsh)

        s.init(router,vertx, {
            try {
                def server = vertx.createHttpServer()
                server.requestHandler(router.&accept).listen(9090)
                vertx.eventBus().send('_cornerstone:registration',
                        vertx.getOrCreateContext().config().getString('launchId'))
            } catch (e) {
                logger.error "could not setup http server:" + e.getMessage()
            }
        })
    }
}
