package net.iowntheinter.shadow.system.io.bridge

import com.suse.salt.netapi.AuthModule
import com.suse.salt.netapi.client.SaltClient
import com.suse.salt.netapi.config.ClientConfig
import com.suse.salt.netapi.event.EventListener
import com.suse.salt.netapi.event.EventStream
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject
import net.iowntheinter.saltReactor.impl.saltReactor
import net.iowntheinter.util.registrationHelper

/**
 * Created by grant on 4/30/16.
 */
class saltBridge extends AbstractVerticle {
    @Override
    void start() throws Exception {
//this should switch to https, but we need to register the ca with this lib to access it
        def rh = new registrationHelper()
        URI uri = URI.create("http://172.17.0.1:8000");
        SaltClient client ;

//auth as a unix user (PAM) on the salt-master system
        def UN = "user"
        def pass = "changeme"
        def token;

        rh.on_start_signial(vertx,{

            client = new SaltClient(uri);
            def cfg = client.getConfig()
            cfg.put(ClientConfig.SOCKET_TIMEOUT, 0)
            token = client.login(UN, pass, AuthModule.PAM);
            println("salt auth token: " + token.token + " Until: " + token.expire + " Perms : " + token.getPerms())

            EventListener sr = new saltReactor(vertx as Vertx, new JsonObject(), client)

            def es = new EventStream(cfg)
            es.addEventListener(sr as EventListener)

            (sr as saltReactor).mgr.manage("saltBridgeChannel")

            vertx.eventBus().send("saltBridgeChannel",
                    new JsonObject(
                            '{"action":"saltPush","addr":"dst/salt/address","data":{"another":"jstruct"}}'))
        })
        rh.notify_start_ready(vertx,{})

    }
}
