package net.iowntheinter.shadow.system.io.access

/**
 * Created by grant on 4/30/16.
 */
import org.apache.directory.ldap.client.api.LdapConnection
import org.apache.directory.ldap.client.api.LdapNetworkConnection

/**
 * Created by grant on 4/15/16.
 */
class LDAPAccess {
    public REQUIRED_ATTRIBUTES = ["LDAP_CONFIGURED"]

    LdapConnection connection
    VaultAccess vault
    LDAPAccess(VaultAccess vault){
        connection = new LdapNetworkConnection( "localhost", 10389 ); //assuming embedded
        this.vault = vault
    }
    void connect(Closure cb){
        vault.readSecret("secret/system_controller/_dev/ldap/system_user",{Map result ->
            try{
                connection.bind( result.binddn as String, result.password as String  )
                cb([success:true,error:null])
            }catch(e){
                cb([success:false,error:e])
            }
        })
    }


}