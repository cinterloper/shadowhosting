#!/usr/bin/env bash
let START_DELAY="$MY_START_TIME"
let START_TRIES=5
source $STARTUP_HOOKS
export RUN_PID="$$"
source $LASH_PATH/bin/init.sh

function on_failure(){
  $FAILURE_HOOK $@
#  kill -9 $RUN_PID
}
function test_start(){
    let START_COUNT="0"

    while [[ $START_COUNT -lt $START_TRIES && "$STARTED" != "TRUE" ]]
    do
      healthcheck_func
      HEALTH_STAT="$?"
      echo "DEBUG: after health check, if you dont see this hc blocks forever"
      if [ "$HEALTH_STAT" -eq "0" ]
      then
        echo _C_REG: $(echo $CAPABILITIES | kvdnc http://$CORNERSTONE_HOST:6500/R/cornerstone/registration/$LAUNCHID)
        export STARTED="TRUE"
      else
        export START_COUNT=$((START_COUNT+1))
        echo "set sc $START_COUNT"
      fi
      echo FAILURES: $START_COUNT SLEEPING: $START_DELAY
      sleep $START_DELAY
    done

    if [ "$STARTED" != "TRUE" ]
    then
        on_failure "start tries exceeded, verify health check passes"
    fi
}

echo START: $STARTUP_HOOK
sleep $KVDN_START_TIME
#start the main task and the health check

{
  startup_func
  } || {
  on_failure "startup_func returned non 0"
}&

test_start &

#start the reactor to recieve new commands from the event system
cd /opt/lash/lib/chain/
CFG_PATH=/opt/lash/lib/chain/config/
source config/config.sh
source chain.sh
export METHOD=EB DECODE_SILENT=TRUE

vxc -c $CORNERSTONE_HOST:7000 -l -n $LAUNCHID | while read JSON_STRING
    do
        decodeJson && lookup_command && run_task
         if [ "$RETURN_ADDR" != "" ]
         then
           KEY_SET="$OUTPUT_KEYS OUTPUT" encodeJson | vxc -c $CORNERSTONE_HOST:7000 -n $RETURN_ADDR
           unset RETURN_ADDR OUTPUT_KEYS OUTPUT
         fi
        reset_chain
        export METHOD=EB
    done
