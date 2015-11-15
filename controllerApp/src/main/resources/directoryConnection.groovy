import io.vertx.core.logging.LoggerFactory
import net.iowntheinter.shadow.controller.auth.impl.EmbeddedADS
import org.apache.directory.api.ldap.model.cursor.EntryCursor
import org.apache.directory.api.ldap.model.name.Dn
import org.apache.directory.ldap.client.api.LdapConnection
import org.apache.directory.ldap.client.api.LdapNetworkConnection

def logger = LoggerFactory.getLogger("directoryConnection")



try {
    final File workDir = new File(System.getProperty("java.io.tmpdir")
            + "/server-work");

    // Create the server
    EmbeddedADS ads = new EmbeddedADS(workDir);

    // optionally we can start a server too
    int port = 10389
    ads.startServer(port);
    LdapConnection connection = new LdapNetworkConnection( "localhost", port );
    connection.bind( "uid=admin,ou=system", "secret" );
    entry = connection.lookup(new Dn("uid=admin,ou=system"));

    adminlookup = ads.service.getAdminSession().lookup(new Dn("uid=admin,ou=system"))
    logger.info("" +
            "java/admin direct lookup :\n ${adminlookup.toString()} \n" +
            "remote/ldap api lookup :\n  ${entry.toString()}\n")
} catch (final Exception e) {
    // Ok, we have something wrong going on ...
    e.printStackTrace();
}