/**
 * Created by grant on 11/5/15.
 */
import net.iowntheinter.shadow.controller.console.impl.shellOperatorConsole
import net.iowntheinter.shadow.controller.installer.shadowSetup

//register setup command
def ss = new shadowSetup(vertx)

def soc = new shellOperatorConsole(vertx,2224)


