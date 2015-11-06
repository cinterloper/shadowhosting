package net.iowntheinter.shadow.controller.core.impl

import io.vertx.core.DeploymentOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.ShellService

/**
 * Created by grant on 10/28/15.
 */
class shellOperatorConsole {
    def cmdset
    def vertx
    def logger = LoggerFactory.getLogger("shellOperatorConsole")

    shellOperatorConsole(vx, port) {
        vertx = vx as Vertx
        logger.info("attermpting to start ssh service on ${port}")
        JsonObject options = new JsonObject().put("sshOptions",
                new JsonObject().
                        put("host", "localhost").
                        put("port", port).
                        put("keyPairOptions", new JsonObject().
                                put("path", "keystore.jks").
                                put("password", "wibble")).
                        put("shiroAuthOptions", new JsonObject().put("config",
                                new JsonObject().put("properties_path", "auth.properties"))

                        )
        );

        def service = ShellService.create(vertx, options.getMap())
        try {
            service.start({ result ->
                if (result.succeeded()) {
                    logger.info("i think shell service is running")
                } else {
                    logger.error("ssh may not be running: ${result}")
                }
            })
        } catch (e) {
            logger.error("could not start ssh server: ${e}")

        }

    }


}

