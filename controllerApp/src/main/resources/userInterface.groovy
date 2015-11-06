/**
 * Created by grant on 11/5/15.
 */
import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.registry.CommandRegistry
import net.iowntheinter.shadow.controller.core.impl.shellOperatorConsole
import net.iowntheinter.shadow.controller.core.impl.webOperatorConsole
import io.vertx.core.Vertx


def soc = new shellOperatorConsole(vertx,2224)


def builder = CommandBuilder.command("my-command")
builder.processHandler({ process ->

  // Write a message to the console
  process.write("Hello World")

  // End the process
  process.end()
})

// Register the command
def registry = CommandRegistry.get(vertx)
registry.registerCommand(builder.build())



