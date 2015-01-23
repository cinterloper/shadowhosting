#!/bin/bash
for line in $(cat $1 | grep : | cut -d ',' -f 1 ); 
do
	eval "echo -n \ -v  $PWD/$line  "
done
