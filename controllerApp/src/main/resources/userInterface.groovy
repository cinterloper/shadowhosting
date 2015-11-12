/**
 * Created by grant on 11/5/15.
 */
import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.registry.CommandRegistry
import net.iowntheinter.shadow.controller.core.impl.shellOperatorConsole
import net.iowntheinter.shadow.controller.core.impl.webOperatorConsole
import io.vertx.core.Vertx
import net.iowntheinter.shadow.controller.installer.shadowSetup

//register setup command
def ss = new shadowSetup(vertx)

def soc = new shellOperatorConsole(vertx,2224)


