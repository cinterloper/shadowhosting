/**
 * Created by grant on 11/5/15.
 */


import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.Vertx
import net.iowntheinter.shadow.controller.console.impl.shellOperatorConsole
import net.iowntheinter.shadow.controller.installer.shadowSetup
import net.iowntheinter.shadow.controller.console.util.commandDialouge
import net.iowntheinter.shadow.controller.installer.cli.TestCMD
def logger = LoggerFactory.getLogger("userInterface")

//register setup command
def ss = new shadowSetup(vertx)
def soc = new shellOperatorConsole(vertx, 2224)
v = vertx as Vertx
eb = v.eventBus();


def sn = new commandDialouge(vertx, 'Shadow2',
        TestCMD.QUESTIONS, TestCMD.REACTIONS, TestCMD.FINISH)
