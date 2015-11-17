bash getdisks.sh $1 | jq -r '.blockdevices[] | select(.name != "'$2'").name'
