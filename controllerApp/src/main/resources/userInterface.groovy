/**
 * Created by grant on 11/5/15.
 */


import io.vertx.core.logging.LoggerFactory
import io.vertx.groovy.core.Vertx
import net.iowntheinter.shadow.controller.console.impl.shellOperatorConsole
import net.iowntheinter.shadow.controller.installer.shadowSetup
import net.iowntheinter.shadow.controller.console.util.commandDialouge

def logger = LoggerFactory.getLogger("userInterface")

//register setup command
def ss = new shadowSetup(vertx)
def soc = new shellOperatorConsole(vertx, 2224)
v = vertx as Vertx
eb = v.eventBus();

def qst = [
        "username" : "what is your username? ",
        "pubkey"   : "what is your public key? ",
        "sysdomain": "what is the system domain? "
]
def qstReacions = [
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

def finishAction = { data ->
    eb.send('questions', data)
    println("end of the line:${data}")
}
def sn = new commandDialouge(vertx, 'Shadow2', qst, qstReacions, finishAction)
