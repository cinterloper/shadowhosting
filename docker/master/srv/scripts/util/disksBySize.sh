bash getdisks.sh $1 | jq -r '.blockdevices[] | select(.size == "'$2'").name'
