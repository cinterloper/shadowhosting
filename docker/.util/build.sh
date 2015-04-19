#!/bin/bash
BASEREPO="shadowhosting"
cd $1
docker build -t $BASEREPO/$(cat meta.json | jq -r .tag) .
