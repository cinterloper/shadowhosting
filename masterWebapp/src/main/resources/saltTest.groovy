/**
 * Created by grant on 10/20/15.
 */

import com.google.gson.reflect.TypeToken
import com.suse.saltstack.netapi.AuthModule
import com.suse.saltstack.netapi.calls.LocalAsyncResult
import com.suse.saltstack.netapi.calls.LocalCall
import com.suse.saltstack.netapi.calls.RunnerCall
import com.suse.saltstack.netapi.config.ClientConfig
import com.suse.saltstack.netapi.calls.modules.*
import com.suse.saltstack.netapi.event.EventListener
import com.suse.saltstack.netapi.event.EventStream
import com.suse.saltstack.netapi.client.SaltStackClient
import com.suse.saltstack.netapi.datatypes.Token
import com.suse.saltstack.netapi.datatypes.ScheduledJob
import com.suse.saltstack.netapi.datatypes.Job
import com.suse.saltstack.netapi.AuthModule
import com.suse.saltstack.netapi.datatypes.target.MinionList
import com.suse.saltstack.netapi.results.ResultInfo
import com.suse.saltstack.netapi.results.ResultInfoSet
import com.suse.saltstack.netapi.datatypes.target.NodeGroup

import java.util.concurrent.Future

println("hello slt")
//this should switch to https, but we need to register the ca with this lib to access it
URI uri = URI.create("http://127.0.0.1:8000");
SaltStackClient client = new SaltStackClient(uri);
def UN = "user"
def pass = "changeme"
def token;
def returnMap = [
        "zfs.list":new TypeToken<Map<String,List>>(){},
        "test.ping":new TypeToken<Boolean>(){},
        "cmd.run":new TypeToken<String>(){}
]

 //try {
     token = client.login(UN, pass, AuthModule.PAM);
     println("salt auth token: " + token.token + " Until: " + token.expire + " Perms : " + token.getPerms() )
     t = new MinionList("balthazarServer")
     cmd="test.ping"
     ri = client.callSync( new LocalCall(cmd, Optional.empty() ,Optional.empty(), returnMap[cmd] ),t)

    println("returnd : ${ri}")



     //println("jid: ${ar.getJid()} mins: ${ar.getMinions()}")
     //def jid = job.getJid()
     //println("job id: " + jid);

     //ResultInfoSet runjob = client.getJobResult(job)

     //println("this job: " )
     //runjob.each {it -> println(it.getResults())}
     //client.events()
     /*
     jobs.each({k,v ->
         println(k)
         println(jobs.get(k))
     })*/
/*& } catch(e){
   println("caught exception: ${e.toString()} \n"+e.getStackTrace())
 }*/


