package net.iowntheinter.shadow.system.io.access

/**
 * Created by grant on 4/30/16.
 */

import com.bettercloud.vault.Vault
import com.bettercloud.vault.VaultConfig
import com.bettercloud.vault.response.LogicalResponse
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus

/**
 * Created by grant on 4/15/16.
 */
class VaultAccess {
    public REQUIRED_ATTRIBUTES = ["VAULT_CONFIGURED"]
    private VAULT_ADDR = System.getenv("VAULT_ENDPOINT")
    private VAULT_TOKEN = System.getenv("VAULT_TOKEN")
    Vault vault
    Vertx vertx
    EventBus eb = vertx.eventBus()
    VaultAccess(Vertx v) {
        vertx = v
        final VaultConfig vconfig =
                new VaultConfig().
                        address(VAULT_ADDR)                             // Defaults to "VAULT_ADDR" environment variable
                        .token(VAULT_TOKEN)                   // Defaults to "VAULT_TOKEN" environment variable
                        .openTimeout(5)                                 // Defaults to "VAULT_OPEN_TIMEOUT" environment variable
                        .readTimeout(30)                                // Defaults to "VAULT_READ_TIMEOUT" environment variable     //    See also: "sslPemUTF8()" and "sslPemResource()"
                        .sslVerify(false)                               // Defaults to "VAULT_SSL_VERIFY" environment variable
                        .build();
        vault = new Vault(vconfig);

    }

    void readSecret(String path, Closure cb) {
        final Map value = vault.logical()
                .read(path)
                .getData();
        cb value
    }

    void writeSecret(String path, Map data, Closure cb) {
        final LogicalResponse writeResponse
        try{
           writeResponse = vault.logical()
                .write(path, data);

            cb([result:writeResponse.data,error:null])
            ArrayList components = path.tokenize('/')
            eb.publish("_Vault_+${components[0]}/${components[1]}", path)
        }catch(e){
            cb([result:null,error:e])
        }
    }

    void onWrite(String strAddr, Closure cb) {
        eb.consumer("_Vault_+${strAddr}", { message -> //listen for updates
            cb(message.body())
        })
    }

    void onDelete(String strAddr, Closure cb) {
        eb.consumer("_Vault_-${strAddr}", { message -> //listen for updates
            cb(message.body())
        })
    }


}