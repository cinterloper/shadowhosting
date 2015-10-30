package net.iowntheinter.shadow.controller.impl

import io.vertx.groovy.core.Vertx
import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.ShellService
/**
 * Created by grant on 10/28/15.
 */
class shellOperatorConsole {
    def cmdset
    shellOperatorConsole(Vertx v){

    }

    void OCProcHdlr(process){
        process.write("hello world\n")
        process.end()
    }

    def shdwcmd = CommandBuilder.command("ShadowCmd").processHandler(OCProcHdlr).build()
}

