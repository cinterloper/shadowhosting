#!/bin/bash
BASEREPO='shadowhosting'
COMPONENT=$1
BASEI=$(grep FROM $1/Dockerfile | cut -d ' ' -f 2-)
if [[ $(echo $BASEI | grep -c $BASEREPO) -lt 1 ]]; then
 docker pull $BASEI
fi
cp $HOME/.ssh/id_rsa $COMPONENT/id.rsa
cd $COMPONENT
docker build -t $BASEREPO/$(cat meta.json | jq -r .tag) .
if [[ -a init.sh ]]; then
  bash init.sh
fi
