import io.vertx.core.logging.LoggerFactory
import net.iowntheinter.shadow.controller.auth.impl.EmbeddedADS
import org.apache.directory.api.ldap.model.name.Dn
def logger = LoggerFactory.getLogger("directoryConnection")



try {
    final File workDir = new File(System.getProperty("java.io.tmpdir")
            + "/server-work");

    // Create the server
    EmbeddedADS ads = new EmbeddedADS(workDir);

    // optionally we can start a server too
    ads.startServer();
    lookup = ads.service.getAdminSession().lookup(new Dn("uid=admin,ou=system"))
    logger.info("lookup results: ${lookup.toString()}")
} catch (final Exception e) {
    // Ok, we have something wrong going on ...
    e.printStackTrace();
}