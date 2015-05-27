import org.crsh.cli.Argument
import org.crsh.cli.Command
import org.crsh.cli.Option
import org.crsh.cli.Required
import org.crsh.cli.Usage
import org.vertx.java.core.Handler
import org.vertx.java.core.json.JsonObject
import org.vertx.java.core.eventbus.EventBus
import org.vertx.java.core.eventbus.Message
import org.vertx.mods.Format
import org.vertx.mods.VertxCommand

import java.util.concurrent.atomic.AtomicReference

@Usage("interact with the Vert.Xecutor")
public class vexe extends VertxCommand {

  @Usage("list the available commands on this Vert.Xecutor instance")
  @Command
  public void cmdlst(
	@Usage("command to inspect")
      	@Argument(name =  "command")
       	String cmd) {
    def append = cmd ?: '/';
    config = new JsonObject(new File("./conf.json").text)
    new File( config.getObject("System").getString("ScriptsPath")+"/cmds/"+append).list().each() { v ->
      out << v + "\n";
    }
 }
  @Usage("execute a command")
  @Command
  public void exe(
      @Usage("the command to execute")
      @Argument(name =  "command")
      @Required String cmd,
      @Usage("the args to pass")
      @Argument(name =  "args")
      List<String> parts)
  {
	def String args = "";
      try{ args = join(parts); }
	catch(e){ args = '{"null":"null"}' }
      EventBus bus = getVertx().eventBus();
      out << args + "\n";
      def value=new JsonObject("{\"commands\":{\"${cmd}\":{\"null\":\"null\"}},\"vars\": ${args} }").toString()
      def address="executions";
      def config = new JsonObject(new File("./conf.json").text)
      final AtomicReference<Message> responseRef = new AtomicReference<Message>(null);
      def replyHandler = new Handler<Message<Object>>() {
        void handle(Message message) {
          synchronized (responseRef) {
            responseRef.set(message);
            responseRef.notifyAll();
          }
        }
      }

      (format?:Format.JSON).send(bus, address, value, replyHandler);
      synchronized (responseRef) {
        if (responseRef.get() == null) {
          try {
            responseRef.wait();
          }
          catch (InterruptedException cancelled) {
          }
        }
      }
      if (responseRef.get() != null) {
        Message<Object> response = responseRef.get();
        out << response.body;
      }


 }
  @Usage("get log output from an execution")
  @Command
  public void log(@Usage("the UUID of the execution")
                    @Argument(name =  "UUID")
                    @Required String UUID) {
    config = new JsonObject(new File("./conf.json").text)
     out << (new File( config.getObject("System").getString("LOGPATH")+"/${UUID}" ).getText())
 }
  @Usage("get error output from an execution")
  @Command
  public void err(@Usage("the UUID of the execution")
                    @Argument(name =  "UUID")
                    @Required String UUID) {
    config = new JsonObject(new File("./conf.json").text)
     out << (new File( config.getObject("System").getString("LOGPATH")+"/${UUID}.err" ).getText())
 }


}
