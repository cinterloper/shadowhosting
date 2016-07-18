package net.iowntheinter.shadow.system.io.bridge

import io.vertx.core.AbstractVerticle
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.bridge.BridgeOptions
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.eventbus.bridge.tcp.TcpEventBusBridge

/**
 * Created by grant on 5/10/16.
 */
class tcp extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        initBridge()
    }

    void initBridge() {
        TcpEventBusBridge bridge = TcpEventBusBridge.create(
                vertx,
                new BridgeOptions()
                        .addInboundPermitted(new PermittedOptions().setAddressRegex(".*"))
                        .addOutboundPermitted(new PermittedOptions().setAddressRegex(".*")));

        bridge.listen(7000, { res ->
            if (res.succeeded()) {
                LoggerFactory.getLogger(this.class.name).info("listening tcp/7000")
                vertx.eventBus().send('_cornerstone:registration',
                        vertx.getOrCreateContext().config().getString('launchId'))

            } else {
                LoggerFactory.getLogger(this.class.name).error("failure to listen on tcp/7000")
            }
        });
    }

}
