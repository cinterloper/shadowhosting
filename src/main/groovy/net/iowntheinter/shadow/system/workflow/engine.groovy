package net.iowntheinter.shadow.system.workflow

import com.bettercloud.vault.VaultConfig
import io.vertx.core.AbstractVerticle
import net.iowntheinter.kvdn.storage.kv.impl.kvdnSession

/**
 * Created by grant on 5/10/16.
 */
class engine extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        initConfigurationWorkflow()
        vertx.eventBus().send('_cornerstone:registration',
                vertx.getOrCreateContext().config().getString('launchId'))
    }

    public void initConfigurationWorkflow(){

        setupHooks()

    }

    public void setupHooks(){
        def system_state = new kvdnSession(vertx)
        system_state.onWrite("shadow:config",{String key ->
            println("kvdn key (shadow:config)|${key} changed")
            switch(key){
                case 'sysdomain':
                    println("sysdomain config trigger")
                    //build ssl certificates

                    break;
                case "linode_provider_creds":
                    println()

            }
        })
    }




}
