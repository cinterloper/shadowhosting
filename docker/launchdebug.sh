#!/bin/bash
#pass in the name of the docker shard folder you want to run bash in to debug the filesystem

BASEREPO='shadowhosting'

docker run -t -i $(bash .util/ports.sh $1/ports.json)  $(bash .util/binds.sh $1/binds.json) "$BASEREPO/$1"  /bin/bash

