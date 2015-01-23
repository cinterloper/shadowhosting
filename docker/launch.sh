#!/bin/bash
#pass in the name of the docker shard folder you want to run

BASEREPO='shadowhosting'

docker run -d $(bash .util/ports.sh $1/ports.json)  $(bash .util/binds.sh $1/binds.json) $BASEREPO/$1

