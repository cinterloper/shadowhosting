#!/bin/bash
PROFILE=linode_arch
NAME=edge-alpha
salt-cloud -p $PROFILE --script-args "-A localhost " $NAME
