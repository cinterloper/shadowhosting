package net.iowntheinter.shadow.system.io.server

import io.vertx.core.AbstractVerticle
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import io.vertx.ext.auth.shiro.LDAPProviderConstants
import io.vertx.ext.auth.shiro.ShiroAuthOptions
import io.vertx.ext.auth.shiro.ShiroAuthRealmType
import io.vertx.ext.shell.ShellServiceOptions
import io.vertx.ext.shell.ShellServiceOptionsConverter
import io.vertx.ext.shell.term.HttpTermOptions
import net.iowntheinter.cintershell.impl.OperatorConsole
import net.iowntheinter.cintershell.impl.commandOneShot
import net.iowntheinter.kvdn.storage.kv.impl.kvdnSession
import net.iowntheinter.shadow.system.workflow.init.sysdomain
import net.iowntheinter.shadow.system.util.commands.component
import net.iowntheinter.cintershell.impl.commandDialouge
import io.vertx.ext.shell.command.CommandRegistry

/**
 * Created by grant on 4/30/16.
 */
class shell extends AbstractVerticle {
    def soc
    def hoc
    def auth = 'properties'
    @Override
    public void start() throws Exception {

        def logger = LoggerFactory.getLogger("userInterface")
        def eb = vertx.eventBus();
        def session = new kvdnSession(vertx)

        startSetupInsecureHttpTerm()

        def setupCMD = new commandDialouge(vertx, 'setup', sysdomain.INTRO, sysdomain.QUESTIONS, sysdomain.REACTIONS, { ctx -> sysdomain.FINISH(session,ctx)})
        def componentCMD = new commandOneShot(vertx,'component', component.INTRO, component.COMMAND, component.FINISH )
        def reg = CommandRegistry.getShared(vertx)
        ["echo","sleep","cd","pwd","ls"].each{ String cmd ->
            reg.unregisterCommand(cmd)
        }
        vertx.eventBus().send('_cornerstone:registration',
                vertx.getOrCreateContext().config().getString('launchId'))
    }

    void startSetupInsecureHttpTerm(){

        def ShellServiceOptions HTTPsso = new ShellServiceOptions()
                .setHttpOptions(new HttpTermOptions()
                .setHost("localhost")
                .setPort(2245).setAuthOptions(new ShiroAuthOptions().setConfig(new JsonObject().put("properties_path", "auth.properties"))));
        hoc = new OperatorConsole(vertx,HTTPsso)

    }
    void startAdminSecureSSHTerm(){
        def port = 2244

        def securityOpts = new JsonObject()

        if (auth == 'ldap') {
            securityOpts.put("type", ShiroAuthRealmType.LDAP)
                    .put("provider", "shiro")
                    .put("config", new JsonObject()
                    .put(LDAPProviderConstants.LDAP_URL, "ldap://127.0.0.1:10389")
                    .put(LDAPProviderConstants.LDAP_USER_DN_TEMPLATE_FIELD, "uid={0},ou=system")
                    .put(LDAPProviderConstants.LDAP_AUTHENTICATION_MECHANISM, "simple")
            )

        } else if (auth == 'properties') {
            securityOpts.put("type", ShiroAuthRealmType.PROPERTIES)
                    .put("provider", "shiro")
                    .put("config", (new JsonObject().put('properties_path', 'file:auth.properties')))

        }


        JsonObject SSHoptions =
                new JsonObject().put("sshOptions",
                        new JsonObject()
                                .put("host", "0.0.0.0")
                                .put("port", port)
                                .put("keyPairOptions",
                                new JsonObject()
                                        .put("path", "keystore.jks")
                                        .put("password", System.getenv('KEYSTORE_PASS')))
                                .put("authOptions", securityOpts));


        def ShellServiceOptions SSHsso = new ShellServiceOptions();

        ShellServiceOptionsConverter.fromJson(SSHoptions, SSHsso)
        soc = new OperatorConsole(vertx, SSHsso)

    }
}