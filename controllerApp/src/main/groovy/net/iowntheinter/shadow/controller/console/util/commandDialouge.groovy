package net.iowntheinter.shadow.controller.console.util

import io.vertx.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.spi.launcher.Command
import io.vertx.groovy.ext.shell.command.CommandProcess
import io.vertx.groovy.ext.shell.Session
import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.process.Process
import io.vertx.groovy.ext.shell.process.ProcessContext
import io.vertx.groovy.ext.shell.registry.CommandRegistry

import javax.xml.ws.AsyncHandler

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


    def qst
    Map qstReacions;
    def ansAr = [:];
    def finish = {}
    Logger log
    Set qks

    commandDialouge(vertx, String name, Map questions, respHdl = [], finishAction) {
        def v = vertx as Vertx
        qst = questions
        finish = finishAction;
        qks = qst.keySet()
        qstReacions = respHdl;
        def builder = CommandBuilder.command(name)
        builder.processHandler(hdlr)
        // Register the command
        def registry = CommandRegistry.get(vertx)
        registry.registerCommand(builder.build())
        log = LoggerFactory.getLogger("shadowSetup cmd")
    }


    def hdlr = { CommandProcess process ->
        Session s = process.session()
        s.put('dctr', 0)
        s.put('ansAr', ansAr)
        s.put('qks', qks)
        s.put('qst', qst)
        def buff = Buffer.buffer();
        process.setStdin({ data ->
            if (data != '\n' && data != '\r') {
                buff.appendString(data)
                process.write(data)
            } else {
                Closure chk = qstReacions[qks[s.get('dctr') as int]]
                s.put('resp', buff.toString())
                buff = Buffer.buffer()
                chk([process:process], { ctx ->
                    def ip = ctx.process as CommandProcess
                    def is = ip.session() as Session
                    def iar = is.get('ansAr') as Map
                    def iqst = is.get('qst') as Map
                    def iqks = is.get('qks') as Set
                    def ictr = is.get('dctr') as int
                    if (ctx['valid']) {
                        iar[iqks[ictr]] = is.get('resp')
                        is.put('ansAr', iar)
                        log.info(ANSI_WHITE + "\nReceived ${iar[iqks[ictr]]}" + ANSI_RESET)

                        ictr++
                        is.put('dctr',ictr)
                        if (ictr > iqst.size() - 1) {
                            ip.write('\n collected: ' + is.get('ansAr'))
                            finish(is.get('ansAr'))
                            ctx.process.end()
                        } else {
                            ctx.process.write('\n' + ANSI_CYAN + iqst[iqks[ictr]] + ANSI_RESET)
                        }
                    } else {
                        ctx.process.write('\n answer did not validatate \n');
                        ctx.process.write('\n' + ANSI_CYAN + iqst[iqks[ictr]] + ANSI_RESET)
                    }
                })

            }
        })

        //dialogctr should always be zero here
        process.write(ANSI_CYAN + qst[qks[0]] + ANSI_RESET)

    }


}
