salt --out=json $1 cmd.run 'lsblk -J' | jq -r .$1 | jq .
