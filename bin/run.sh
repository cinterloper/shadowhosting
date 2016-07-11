if [ "$KEYSTORE_PASS" == "" ]
then
  export  KEYSTORE_PASS="_UNINITALIZED"
fi
java  -Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory -jar build/libs/shadowd-3.3.0-fat.jar

