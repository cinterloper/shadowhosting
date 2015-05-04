#!/bin/bash
BASEREPO=$1
mkdir $BASEREPO/.tmpkey/
cp $HOME/.ssh/id_* $BASEREPO/.tmpkey/
cd $1
docker build -t $BASEREPO/$(cat meta.json | jq -r .tag) .
rm $BASEREPO/.tmpkey/*
