package net.iowntheinter.shadow.controller.installer.cli

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.registry.CommandRegistry

/**
 * Created by grant on 11/6/15.
 */
class commandDialouge {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    Vertx v
    //answer lamdas?
    //Final compleation lambda?

    def qst = [
            "username" : "what is your username? ",
            "pubkey"   : "what is your public key? ",
            "sysdomain": "what is the system domain? "
    ]
    def qstReacions = [
            "username" : {resp, process ->

            },
            "pubkey"   : {},
            "sysdomain": {}
    ]

    def qks = qst.keySet();
    def ans = [:];
    Logger log

    commandDialouge(vertx) {
        def v = vertx as Vertx
        def builder = CommandBuilder.command("ShadowSetup")
        builder.processHandler(hdlr)
        // Register the command
        def registry = CommandRegistry.get(vertx)
        registry.registerCommand(builder.build())
        log = LoggerFactory.getLogger("shadowSetup cmd")


    }
    def validate(answer,question){
        return(true)
    }

    


    def hdlr = { process ->
        def p = process as CommandProcess
        def dialogctr = 0;
        def buff = Buffer.buffer();

        process.setStdin({ data ->
            if (data != '\n' && data != '\r') {
                buff.appendString(data)
                process.write(data)
            } else {

                ans[qks[dialogctr]] = buff.toString()
                buff = Buffer.buffer()
                log.info(ANSI_WHITE+"\nReceived ${ans[dialogctr]}"+ANSI_RESET)

                dialogctr++

                if (dialogctr > qst.size() - 1) {
                    process.write('\n'+ans)
                    process.end()
                } else {
                    process.write('\n'+ANSI_CYAN + qst[qks[dialogctr]]+ANSI_RESET)
                }
            }
        })

        //dialogctr should always be zero here
        process.write(ANSI_CYAN+qst[qks[dialogctr]]+ANSI_RESET)

    }


}
