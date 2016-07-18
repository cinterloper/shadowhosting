package net.iowntheinter.shadow.system.workflow.init

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.shell.command.CommandProcess
import net.iowntheinter.kvdn.storage.kv.impl.KvTx
import net.iowntheinter.kvdn.storage.kv.impl.kvdnSession
import net.iowntheinter.shadow.system.io.access.VaultAccess

/**
 * Created by grant on 4/30/16.
 */
@Singleton
class credentials {

    static def INTRO = new String("external system credential sysdomain \n\n")
    //you can actually rewrite the reactions through the process handle when questions is processed

    public static Closure QUESTIONS = { CommandProcess process, Closure cb ->
        process.session().put('passwordMode',true) //inital state hideing input text, first question is a password
        cb([process: process, questions:

                ["linodekey": "what is your linode api key? ","linodepass":"what is your linode password?","confirmation": "are you sure? "]
        ])
    }

    public static Map REACTIONS = [

            "linodekey": { ctx, cb ->
                ctx['valid'] = true
                cb(ctx)
            },
            "linodepass": { ctx, cb ->
                ctx['valid'] = true
                def session = ctx.process.session()
                session.put('passwordMode',false)//disable hiding of input text after secret input is finished
                cb(ctx)
            },
            "confirmation": { ctx, cb ->
                println("debug ${ctx.d}")
                if(ctx.resp == 'yes')
                    ctx['valid'] = true
                else
                {
                    def session = ctx.process.session()
                    session.put('DiagCounter',0)
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
        vlt = new VaultAccess(vertx)
        vlt.writeSecret('secret/linode/creds',d,{ Map result ->
           if(!result.error){
             tx.set("linodeCreds",'vault:secret/linode/creds',{
                 LoggerFactory.getLogger(this.class.getName()).info("sysdomain command recieved: " + d)
                 p.end()
             })
           }else{
               LoggerFactory.getLogger(this.class.getTypeName()).error(result.error)
           }
               p.end(-1)
        })


    }

}
