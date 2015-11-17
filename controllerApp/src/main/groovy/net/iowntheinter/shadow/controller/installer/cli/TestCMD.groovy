package net.iowntheinter.shadow.controller.installer.cli

import io.vertx.groovy.core.Vertx
import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.shell.command.CommandProcess

/**
 * Created by grant on 11/17/15.
 */
class TestCMD {
    def static QUESTIONS = [
            "username" : "what is your username? ",
            "pubkey"   : "what is your public key? ",
            "sysdomain": "what is the system domain? "
    ]
    def static REACTIONS = [
            "username" : { ctx, cb ->
                ctx['valid'] = true
                cb(ctx)
            },
            "pubkey"   : { ctx, cb ->
                ctx['valid'] = true
                cb(ctx)
            },
            "sysdomain": { ctx, cb ->
                ctx['valid'] = true
                cb(ctx)
            }
    ]

    static Closure FINISH = { ctx ->
        def v = ctx.v as Vertx
        def p = ctx.p as CommandProcess
        def d = ctx.d as Map
        def eb = v.eventBus();
        eb.send('questions', d)
        println("end of the line:${d}")
    }
}
