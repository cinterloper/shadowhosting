mkdir $LOGPATH 2>/dev/null
{
 ARGS='"ARGS":["TESTVAR"]'     #this default should be overrrdden in the command
 source $ScriptsPath/globals.sh
 source $ScriptsPath/cmds/$CMD  2>>$LOGPATH/$UUID.err
 echo $ARGS
 for arg in $(echo $ARGS | jq -r '.ARGS[]')
 do
   echo -n $arg : 
   [[ -v $(echo -n $arg) ]]
   if [ $? -eq 1 ]
   then 
	echo "$arg is missing?"
   fi
 done
 echo ""
 }||{

  echo '{"error":"initalization error", "comment":"problem loading command?"}'
  exit
}

CALLBACK ( )
{
  curl -XPOST -d$( echo $1 | jq -c . ) $CBPROTO://$CBHOST:$CBPORT/$CBPATH/$UUID >> $LOGPATH/$UUID 2>>$LOGPATH/$UUID.err
}
LOGMSG ( )
{
 curl -XPOST -d$( echo $1 | jq -c . ) $LBURL:$LBPORT/$UUID >> $LOGPATH/$UUID 2>>$LOGPATH/$UUID.err
}


{
     {
	RUN  1>$LOGPATH/$UUID 2>$LOGPATH/$UUID.err &
	PID=$!
	disown -h $PID
     }||{
	echo '{"error":"execution error", "comment":"problem running command?"}'
     }

     echo '{"UUID":"'$UUID'","PID":"'$PID'","COMMAND":"'$CMD'","DATE":"'$(date)'", "CTYPE":"'$(CTYPE)'", "ARGS":"'$ARGS'"}' | jq -c .
 }||{ # catch jq json encodeing exception
     echo '{"error":"internal JSON encodeing error", "comment":"bad command output?"}'
}

