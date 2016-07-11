#!/usr/bin/env bash
startup_func() {
  $STARTUP_HOOK
}
healthcheck_func() {
    SALT_TOKEN=$(curl -s http://$HOSTNAME:8000/login \
    -H "Accept: application/json" \
    -d username='user' \
    --data-urlencode password=$SALT_PASS \
    -d eauth='pam' | jq -r '.return[].token')
    echo HC_TOKEN=$SALT_TOKEN
    PASS=$(curl -s http://$HOSTNAME:8000/hook -H "X-Auth-Token: $SALT_TOKEN"   -d a=b -d c=d | jq 'select(.success==true) | length > 0')
    if [ "$PASS" == "true" ]
    then
      echo PASSED HEALTH CHECK
      return 0
    else
      echo FAILED HEALTH CHECK
      return -1
     fi
}