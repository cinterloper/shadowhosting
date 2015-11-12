package net.iowntheinter.shadow.controller.core.impl

import io.vertx.core.DeploymentOptions
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.ShellServiceOptionsConverter
import io.vertx.ext.shell.net.SSHOptions
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
    def sshOpts = new SSHOptions()
    def shadowmsg;
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    shellOperatorConsole(vx, port) {
        vertx = vx as Vertx
        logger.info("attermpting to start ssh service on ${port}")
        JsonObject joptions = new JsonObject().put("sshOptions",
                new JsonObject().
                        put("host", "0.0.0.0").
                        put("port", port).
                        put("keyPairOptions", new JsonObject().
                                put("path", "keystore.jks").
                                put("password", "wibble")).
                        put("shiroAuthOptions", new JsonObject().put("config",
                                new JsonObject().put("properties_path", "auth.properties"))

                        )
        );
         try {
             def bfile="shadow-banner.txt"
             def classloader = (URLClassLoader)(Thread.currentThread().getContextClassLoader())
             def bpth = classloader.findResource(bfile);
             logger.info("bfile ref: ${bpth}\n")

             shadowmsg = classloader.getResourceAsStream(bfile).getText()
             logger.info("${ANSI_RED+shadowmsg+ANSI_RESET}" )
         }catch(e){
             logger.error("could not get banner, ${e}")
             shadowmsg = "Welcome to Shadow Controller \n"
         }

        def ShellServiceOptions opts = new ShellServiceOptions()
        ShellServiceOptionsConverter.fromJson(joptions, opts)
        opts.setWelcomeMessage(ANSI_RED+shadowmsg+ANSI_RESET)
        ShellServiceOptionsConverter.toJson(opts,joptions)


        def service = ShellService.create(vertx, joptions.getMap())

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

