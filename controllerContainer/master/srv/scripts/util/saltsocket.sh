token=$(bash saltoken.sh | jq -r '.return[].token')
curl -NsS \
    -H "X-Auth-Token: $token" \
    -H 'Host: localhost:8000' \
    -H 'Connection: Upgrade' \
    -H 'Upgrade: websocket' \
    -H 'Origin: http://localhost:8000' \
    -H 'Sec-WebSocket-Version: 13' \
    -H 'Sec-WebSocket-Key: '"x3JJHMbDL1EzLkh9GBhXDw==" \
    localhost:8000/ws
