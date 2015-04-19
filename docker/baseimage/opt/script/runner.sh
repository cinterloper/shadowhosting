mkdir $LOGPATH 2>/dev/null

{
 source $ScriptsPath/cmds/$CMD  2>>$LOGPATH/$UUID.err
 }||{
  echo '{"error":"initalization error", "comment":"problem loading command?"}'
  exit
}

{
     {
	RUN  1>$LOGPATH/$UUID 2>$LOGPATH/$UUID.err &
	PID=$!
	disown -h $PID
     }||{
	echo '{"error":"execution error", "comment":"problem running command?"}'
     }
     echo '{"UUID":"'$UUID'","PID":"'$PID'","COMMAND":"'$CMD'","DATE":"'$(date)'", "CTYPE":"'$(CTYPE)'"}' | jq -c .

 }||{ # catch jq json encodeing exception
     echo '{"error":"internal JSON encodeing error", "comment":"bad command output?"}'
}
