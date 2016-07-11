package net.iowntheinter.shadow.system.workflow.init

import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.shell.command.CommandProcess
import net.iowntheinter.cintershell.impl.commandDialouge
import net.iowntheinter.kvdn.storage.kv.impl.KvTx
import net.iowntheinter.kvdn.storage.kv.impl.kvdnSession
import net.iowntheinter.shadow.system.workflow.init.credentials
/**
 * Created by grant on 4/30/16.
 */
@Singleton
class sysdomain {

    static def INTRO = new String("Inital system sysdomain \n\n")
    //you can actually rewrite the reactions through the process handle when questions is processed

    public static Closure QUESTIONS = { CommandProcess process, Closure cb ->
        cb([process: process, questions:

                ["sysdomain": "what is the system domain? ","confirmation": "are you sure? "]
        ])
    }

    public static Map REACTIONS = [

            "sysdomain": { ctx, cb ->
                ctx['valid'] = true
                def session = ctx.process.session()
                cb(ctx)
            },
            "confirmation": { ctx, cb ->
                println("debug ${ctx.d}")
                if(ctx.resp == 'yes')
                    ctx['valid'] = true
                else
                {
                    def session = ctx.process.session()
                    int ctr = session.get('DiagCounter')
                    session.put('DiagCounter',--ctr)
                    ctx['valid']=false
                }
                cb(ctx)
            }
    ]

    public static Closure FINISH = { kvdnSession session, ctx ->

        def vertx = ctx.v as Vertx
        def p = ctx.p as CommandProcess
        def d = ctx.d as Map
        def eb = vertx.eventBus();
        KvTx tx = session.newTx("shadow:config")
        d.each { k, v ->
            tx.set(k, v, { resPut ->
                if (resPut.error != null) {
                    LoggerFactory.getLogger(this.class.getName()).error("error: " + resPut.error)
                }
            })
        }
        LoggerFactory.getLogger(this.class.getName()).info("sysdomain command recieved: " + d)
        def credentialsCMD = new commandDialouge(vertx, 'credentials', credentials.INTRO, credentials.QUESTIONS, credentials.REACTIONS, { ctx2 -> credentials.FINISH(session,ctx2)})

        p.end()
    }

}
