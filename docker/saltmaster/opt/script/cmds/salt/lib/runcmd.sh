salt --out=json $1 cmd.run $2 | jq -r .$1

