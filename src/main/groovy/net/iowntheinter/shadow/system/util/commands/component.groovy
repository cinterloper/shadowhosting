package net.iowntheinter.shadow.system.util.commands

import io.vertx.core.Vertx
import io.vertx.core.cli.Argument
import io.vertx.core.cli.CLI
import io.vertx.core.cli.CommandLine
import io.vertx.core.cli.Option
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.shareddata.LocalMap
import io.vertx.ext.shell.command.CommandProcess
import io.vertx.ext.shell.session.Session

/**
 * Created by grant on 11/17/15.
 */
class component {
    static String NAME = 'component'
    static String INTRO = new String(" this is a sample command \n\n")
    //you can actually rewrite the reactions through the process handle when questions is processed


    public static Closure COMMAND = { Map cmdctx, cb ->
        CommandProcess pr = cmdctx.p
        Session session = pr.session()
        ArrayList<String> args = session.get('Args')
        //walk all the validation handlers (defined below) and check their corespondant arguments
        //this should probably be a helper utility

        CLI cli = CLI.create(NAME)
        cli.setSummary(INTRO)
                .addOption(new Option()
                .setLongName("operation")
                .setShortName("o")
                .addChoice("deploy").addChoice("undeploy")
                .setDescription("operations"))
                .addOption(new Option()
                .setLongName("list")
                .setShortName("l")
                .addChoice("available").addChoice("deployed")
                .setDescription("deployments"))
                .addOption(new Option()
                .setArgName("help")
                .setShortName("h")
                .setLongName("help").setHelp(true));

        StringBuilder builder = new StringBuilder();
        cli.usage(builder);
        CommandLine commandLine = cli.parse(args);

        commandLine.allArguments().each { it ->
            pr.write("arg: " + it)
        }
        if (commandLine.isAskingForHelp())
            pr.write(builder.toString())
        if (commandLine.getOptionValue("list") == 'deployed')
        {
            LocalMap depl = cmdctx.v.sharedData().getLocalMap("cornerstone_deployments") as LocalMap

            depl.keySet().each{ key ->
                pr.write("key: ${key} value: ${depl.get(key)} \n")
            }
        } else  if (commandLine.getOptionValue("list") == 'available')
        {
            LocalMap comp = cmdctx.v.sharedData().getLocalMap("cornerstone_components") as LocalMap

            comp.keySet().each{ key ->
                pr.write("key: ${key} value: ${comp.get(key)} \n")
            }
        }
        cb([v: cmdctx.v, p: cmdctx.p, d: commandLine.getOptionValue('operation')])

    }


    public static Closure FINISH = { ctx ->
        def v = ctx.v as Vertx
        def p = ctx.p as CommandProcess
        def d = ctx.d
        def eb = v.eventBus();
        eb.send('questions', d)
        println("result:${d}")
        p.end()
    }


}
