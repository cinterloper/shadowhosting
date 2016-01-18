#!/bin/bash 
#@TAGS: psudeocode unchecked
#initpki
#build app
#build container
#start container/vm
#configure api keys
#register your ssh key?
#
source initPKI.sh
initpki
genserver edge-alpha
genclient basevbox
genclient installer
copyinstaller installer
echo -e  'y\n'|ssh-keygen -q -t ed25519 -N "" -f controllerContainer/master/config/ssh/
cp -a controllerContainer/master/config/ssh/id_* /root/.ssh/
chown root:root /root/.ssh/* ; chmod 600 /root/.ssh/*
cd utils
bash buildinstaller.sh


