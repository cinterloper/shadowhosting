echo ""
if [ "$REQUEST_METHOD" = "POST" ]
then
	echo \{ \"command\": \"$JOB\", \"hostname\": \"$(hostname)\", \"method\": \"$REQUEST_METHOD\", \"date\": \"$(date)\", \"UUID\": \"$UUID\" \} > $LOGPATH/$UUID 2>&1
	eval $COMMAND >> $LOGPATH/$UUID 2>&1 &
	PID=$!
	ln $LOGPATH/$UUID $LOGPATH/_$PID.log
	echo \{ \"command\": \"$JOB\", \"hostname\": \"$(hostname)\", \"method\": \"$REQUEST_METHOD\", \"date\": \"$(date)\", \"PID\": \"$PID\" \}
	disown $PID 2>/dev/null
else
	eval $COMMAND 2>&1
fi
