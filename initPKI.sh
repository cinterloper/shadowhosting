#!/bin/bash
#init the PKI for a new Shadow System
# - clean old key material
# - build root key for 1st shadowmaster
# - build root ca for vpn system
# - build 1st shadowmaster cert

initkey () {
 cd controllerContainer/master/srv/easyrsa
 echo yes | ./easyrsa init-pki
 ./easyrsa build-ca nopass
 ./easyrsa gen-dh
 cd ../../../
}
