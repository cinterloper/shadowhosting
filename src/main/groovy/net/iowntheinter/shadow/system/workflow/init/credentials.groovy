package net.iowntheinter.shadow.system.workflow.init

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import io.vertx.core.Vertx
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.shell.command.CommandProcess
import net.iowntheinter.kvdn.storage.kv.impl.KvTx
import net.iowntheinter.kvdn.storage.kv.impl.kvdnSession

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
        VaultConfig vault_config = vertx.sharedData().getLocalMap("shadowd_config").get("VaultConfig")
        Vault vault = new Vault(vault_config)
        //walk all the validation handlers (defined below) and check their corespondant arguments
        //this should probably be a helper utility

        vault.logical().write("secret/linode/creds",d)// really this should do it

        d.each { k, v ->
            tx.set(k, v, { resPut ->
                if (resPut.error != null) {
                    LoggerFactory.getLogger(this.class.getName()).error("error: " + resPut.error)
                }
            })
        }
        LoggerFactory.getLogger(this.class.getName()).info("sysdomain command recieved: " + d)
        p.end()
    }

}
