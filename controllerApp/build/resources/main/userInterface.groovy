/**
 * Created by grant on 11/5/15.
 */


import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.Vertx
import net.iowntheinter.cintershell.impl.SSHOperatorConsole
import net.iowntheinter.cintershell.impl.commandDialouge
import net.iowntheinter.shadow.controller.installer.cli.TestCMD
def logger = LoggerFactory.getLogger("userInterface")

//register setup command
def soc = new SSHOperatorConsole(vertx, 2224)
v = vertx as Vertx
eb = v.eventBus();


def sn = new commandDialouge(vertx, 'Shadow2', TestCMD.INTRO, TestCMD.QUESTIONS, TestCMD.REACTIONS, TestCMD.FINISH)
