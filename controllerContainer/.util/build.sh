#!/bin/bash
BASEREPO='shadowhosting'
COMPONENT=$1

cp $HOME/.ssh/id_rsa $COMPONENT/id.rsa
cd $COMPONENT
docker build -t $BASEREPO/$(cat meta.json | jq -r .tag) .
bash init.sh
