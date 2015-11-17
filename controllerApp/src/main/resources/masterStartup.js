var logger = Java.type("io.vertx.core.logging.LoggerFactory").getLogger("startup.js");
logger.info("Starting up...");
var eb = vertx.eventBus()
var fs = vertx.fileSystem()

var ctx = vertx.getOrCreateContext()
var config = ctx.config()
console.log("startup config: " + JSON.stringify(config));


var options = {
 "config" : config
};
vertx.deployVerticle("saltConnection.groovy",options);
vertx.deployVerticle("directoryConnection.groovy",options);
vertx.deployVerticle("userInterface.groovy",options);

