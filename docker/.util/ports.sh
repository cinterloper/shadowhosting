#!/bin/bash
UDPCNT=$( cat $1 | jq '.udp | length') 
TCPCNT=$( cat $1 | jq '.tcp | length') 
ADDR=0.0.0.0
for i in $( seq 0 $(echo $UDPCNT - 1 | bc))
do
	PORT=$(cat $1 | jq .udp[$i])
	echo -n \ -p $ADDR:$PORT:$PORT/udp 
done
for i in $( seq 0 $(echo $TCPCNT - 1 | bc))
do
	PORT=$(cat $1 | jq .tcp[$i])
	echo -n \ -p $ADDR:$PORT:$PORT 
done

