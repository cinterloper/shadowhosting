import org.vertx.java.core.json.JsonArray;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.logging.Logger;
import org.vertx.groovy.core.http.RouteMatcher
import org.vertx.java.core.*
import org.vertx.java.core.http.HttpClientRequest;
import org.vertx.java.core.http.HttpClientResponse;
import org.vertx.java.core.net.*;
import java.util.Map;
def rm = new RouteMatcher()
import durbin.util.RunBash
RunBash.enable()
def RawConfig = new File('./Config.json').text
def config = new JsonObject()
try { config =  new JsonObject(RawConfig); }
        catch (e) {  println("owtch bad config: "+e); }
def BasePath = config.getObject("Metadata").getString("BasePath");
def startupCmds = new JsonObject();
try {
    startupCmds = new JsonObject(new File('./startupCmds.json').text)
}catch(e) { println "hmm no startup commands? bad json syntax?"}

boolean reloadConfig()
{
    println "attempting to reload config"
    def RawConfig = new File('./Config.json').text
    try { config =  new JsonObject(RawConfig); }
    catch (e) {
        println("owtch bad config: "+e);
        return false
    }
    return true
}

String execute(req, cmds ,config )
{
    def  headers = new File('./pre.sh').text
    def  footer = new File('./post.sh').text
    def output = ""
    cmds.keySet().each() { it ->
        if (config.getObject(it) != null) {
            def CType = "Application/json";
            def process;
            def args = cmds[it]
            def PreProcess = headers
            JOB = it
            args.each() { key, value -> headers += "\n export ${key}=${value} \n" };
            try {
                process = 'export COMMAND="' + config.getObject(JOB).getObject(req.getMethod()).getValue("CMD").toString() + '";'
                process += '\nexport REQUEST_METHOD=' + req.getMethod() + '\n'
                process += '\nexport JOB=' + JOB + '\n'
                CType = config.getObject(JOB).getObject(req.getMethod()).getValue("CType").toString()
            } catch (e) {
                println "Load Config Exception: " + e
            }

            req.response.putHeader("Content-Type", CType)

            def baos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(baos);
            // IMPORTANT: Save the old System.out!
            PrintStream old = System.out;
            // Tell Java to use your special stream
            System.setOut(ps);
            // Print some output: goes to your special stream
            def CMD = headers + process + footer
            try {
                CMD.bash()
            }
            catch (e) {
                req.response.end '{ "error" : "Something went wrong during execution"}'; println "Bash Exception: " + e
            }
            System.out.flush();
            System.setOut(old);
            output += baos.toString()
        }
    }
        return output

}




 String  inSanity(input) {
  def output = input.replaceAll("[^a-zA-Z0-9\\_\\.\\-]", "");
  return(output)
 }


HashMap argParseSync(req)
{
 def ArgList
        try { ArgList = req.getQuery().tokenize('?') }
             catch(e) { req.response.end '{ "error":"Invalid Request while parsing " }' }

        argsMap = new HashMap();
        ArgList.each()  {
                def itM = it.tokenize('=')
                argsMap.put( inSanity(itM[0]) , inSanity(itM[1]) )
        };
  return(argsMap)

}

HashMap argParsePost(pjson, action)
{
  def argList = new HashMap();
  try { argList = new HashMap(pjson.getObject("actions").getObject(action).toMap()) }
  catch (e) { "error in argParse : " + e }
  return (argList)
}

HashMap actionParse(req,pdata)
{

 if (req.getMethod() == "POST")
 {
  def jsonData
  def actionList
  try {
	jsonData = new JsonObject(pdata.toString())
	actionList = jsonData.getObject("actions").getFieldNames()
  }catch (e) { println "error parsing json from post data" + e }
  def cmdLst = new HashMap()
  actionList.each() { cmdLst.put( it, argParsePost( jsonData,it)  ) }
  return cmdLst

  }
 else
 {
   cmdLst = new HashMap()
   args = argParseSync(req);
   cmdLst.put(args["action"], args);
   if(cmdLst.containsKey("reload"))
   {
       println "triggered reload"
       reloadConfig();
       return(cmdLst);
   }
     return(cmdLst)
 }


}


rm.all(config.getObject("Metadata").getString("BasePath")) { req ->
	req.bodyHandler { buffer ->
        	req.response.end execute(req, actionParse(req,buffer), config);

	 }
}
//config.getObject("Metadata").getObject()


println "setup http server on " + config.getObject("Metadata").getInteger("Port")
vertx.createHttpServer().requestHandler(rm.asClosure()).listen(config.getObject("Metadata").getInteger("Port"))


