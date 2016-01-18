#!/bin/bash
for mnt in $(cat $1 | jq -r 'keys[]' ); 
do
	if [[ $mnt =~ ^/ ]]
	then
	  eval 'echo -n \ -v  $mnt:$(cat $1 | jq -r .[\"$mnt\"])  '
	else
	  eval "echo -n \ -v  $PWD/$mnt:$(cat $1 | jq -r .[\"$mnt\"])  "
	fi
done
