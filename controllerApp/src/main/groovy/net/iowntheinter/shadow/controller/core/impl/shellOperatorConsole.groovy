package net.iowntheinter.shadow.controller.core.impl

import io.vertx.core.DeploymentOptions
import io.vertx.core.json.JsonObject
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.ShellService

/**
 * Created by grant on 10/28/15.
 */
class shellOperatorConsole {
    def cmdset

    shellOperatorConsole(Vertx v,  int port) {
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
        vertx.deployVerticle("service:io.vertx.ext.shell", new DeploymentOptions().setConfig(options),{ar ->
            if (ar.succeeded()) {
                startFuture.succeeded();
            } else {
                startFuture.fail(ar.cause());
            }
        });
    }


}

