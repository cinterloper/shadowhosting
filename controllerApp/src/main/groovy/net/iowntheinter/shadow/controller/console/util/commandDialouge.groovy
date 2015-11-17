package net.iowntheinter.shadow.controller.console.util

import io.vertx.groovy.core.Vertx
import io.vertx.core.buffer.Buffer
import io.vertx.core.logging.Logger
import io.vertx.core.logging.LoggerFactory
import io.vertx.core.spi.launcher.Command
import io.vertx.groovy.ext.shell.command.CommandProcess
import io.vertx.groovy.ext.shell.Session
import io.vertx.groovy.ext.shell.command.CommandBuilder
import io.vertx.groovy.ext.shell.process.Process
import io.vertx.groovy.ext.shell.process.ProcessContext
import io.vertx.groovy.ext.shell.registry.CommandRegistry

import javax.xml.ws.AsyncHandler

/**
 * Created by grant on 11/6/15.
 */
class commandDialouge {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    Vertx vertx
    Closure QuestionsLoader
    Map QuestionsResponses;
    Map ansAr = [:];
    Closure finish
    Logger log
    String intro = "";

    commandDialouge(v, String name, String intr = "", Closure questions, Map respHdl, Closure finishAction) {
        vertx = v as Vertx
        QuestionsLoader = questions  //this is a hack, need to figure out a clean way to get the inital key set
        finish = finishAction;
        intro = intr
        QuestionsResponses = respHdl;
        def builder = CommandBuilder.command(name)
        builder.processHandler(hdlr)
        // Register the command
        def registry = CommandRegistry.get(vertx)
        registry.registerCommand(builder.build())
        log = LoggerFactory.getLogger("shadowSetup cmd")
    }


    def hdlr = { CommandProcess pr ->
        Session se = pr.session()
        se.put('DialougeCounter', 0)
        se.put('ansAr', ansAr)
        se.put('QuestionsResponses', QuestionsResponses)
        QuestionsLoader(pr, { QuestionLoaderContext ->
            def process = QuestionLoaderContext.process as CommandProcess
            Session s = process.session()
            def Questions = QuestionLoaderContext.questions
            def QuestionKeySet = QuestionLoaderContext.questions.keySet()
            s.put('Questions', Questions)
            s.put('QuestionKeySet', QuestionKeySet)

            def buff = Buffer.buffer();
            process.write(intro)
            process.setStdin({ data ->
                if (data != '\n' && data != '\r') {
                    buff.appendString(data)
                    process.write(data)
                } else {
                    def QuestionsResponses = s.get('QuestionsResponses')
                    Closure chk = QuestionsResponses[QuestionKeySet[s.get('DialougeCounter') as int]]
                    s.put('resp', buff.toString())
                    buff = Buffer.buffer()
                    chk([process: process], { ctx ->
                        def InnerProcess = ctx.process as CommandProcess
                        def InnerSession = InnerProcess.session() as Session
                        def InnerAnswerSet = InnerSession.get('ansAr') as Map
                        def iQuestions = InnerSession.get('Questions') as Map
                        def iQuestionKeySet = InnerSession.get('QuestionKeySet') as Set
                        def InnerDiagCtr = InnerSession.get('DialougeCounter') as int
                        if (ctx['valid']) {
                            InnerAnswerSet[iQuestionKeySet[InnerDiagCtr]] = InnerSession.get('resp')
                            InnerSession.put('ansAr', InnerAnswerSet)
                            log.info(ANSI_WHITE + "\nReceived ${InnerAnswerSet[iQuestionKeySet[InnerDiagCtr]]}" + ANSI_RESET)

                            InnerDiagCtr++
                            InnerSession.put('DialougeCounter', InnerDiagCtr)
                            if (InnerDiagCtr > iQuestions.size() - 1) {
                                InnerProcess.write('\n collected: ' + InnerSession.get('ansAr'))
                                finish([v: vertx, p: InnerProcess, d: InnerSession.get('ansAr')])
                                ctx.process.end()
                            } else {
                                ctx.process.write('\n' + ANSI_CYAN + iQuestions[iQuestionKeySet[InnerDiagCtr]] + ANSI_RESET)
                            }
                        } else {
                            ctx.process.write('\n answer did not validatate \n');
                            ctx.process.write('\n' + ANSI_CYAN + iQuestions[iQuestionKeySet[InnerDiagCtr]] + ANSI_RESET)
                        }
                    })

                }
            })

            //dialogctr should always be zero here

            process.write(ANSI_CYAN + Questions[QuestionKeySet[0]] + ANSI_RESET)

        })
    }

}
