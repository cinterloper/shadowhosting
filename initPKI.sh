#!/bin/bash
#init the PKI for a new Shadow System
# - clean old key material
# - build root key for 1st shadowmaster
# - build root ca for vpn system
# - build 1st shadowmaster cert
WD=$(pwd)
initpki () {
 haveged & 
 cd controllerContainer/master/srv/easyrsa
 echo yes | ./easyrsa init-pki
 ./easyrsa build-ca nopass
 ./easyrsa gen-dh
 cd $WD
}
#args: $1 - svr name
genserver () {
   cd controllerContainer/master/srv/easyrsa
  ./easyrsa build-server-full $1 nopass
  cd $WD
  mkdir -p controllerContainer/master/srv/pillar/tree/hosts/$1/files/pki/openvpn/
  cp -a controllerContainer/master/srv/easyrsa/pki/{ca.crt,dh.pem} controllerContainer/master/srv/pillar/tree/hosts/edge-alpha/files/pki/openvpn/
  cp -a controllerContainer/master/srv/easyrsa/pki/{issued/$1.crt,private/$1.key} controllerContainer/master/srv/pillar/tree/hosts/$1/files/pki/openvpn/  
  cd controllerContainer/master/srv/pillar/tree/hosts/$1/files/pki/openvpn/ 
  for fl in $( ls $1*); do mv $1.$(echo $fl | cut -d '.' -f 2) server.$(echo $fl | cut -d '.' -f 2); done
  cd $WD

}
#args: $1 - client name
genclient () {
   cd controllerContainer/master/srv/easyrsa
  ./easyrsa build-client-full $1 nopass
  cd $WD
  mkdir -p controllerContainer/master/srv/pillar/tree/hosts/$1/files/pki/openvpn/
  cp -a controllerContainer/master/srv/easyrsa/pki/{ca.crt,dh.pem} controllerContainer/master/srv/pillar/tree/hosts/$1/files/pki/openvpn/
  cp -a controllerContainer/master/srv/easyrsa/pki/{issued/$1.crt,private/$1.key} controllerContainer/master/srv/pillar/tree/hosts/$1/files/pki/openvpn/  
  cd controllerContainer/master/srv/pillar/tree/hosts/$1/files/pki/openvpn/ 
  for fl in $( ls $1*); do mv $1.$(echo $fl | cut -d '.' -f 2) client.$(echo $fl | cut -d '.' -f 2); done
  cd $WD

}
#args: $1 - crt name
cleanfailed () {
  cd $WD
  find  controllerContainer/master/srv/easyrsa/ -type f| grep $1 | xargs rm
}
#args: $1 - crt name
copyinstaller () { # copy the installer certificate into the files to be added to the installer iso build
  cp -a controllerContainer/master/srv/easyrsa/pki/{ca.crt,dh.pem} utils/installerbuild/additives/vpnpak
  cp -a controllerContainer/master/srv/easyrsa/pki/issued/$1.crt utils/installerbuild/additives/vpnpak/client.crt  
  cp -a controllerContainer/master/srv/easyrsa/pki/private/$1.key utils/installerbuild/additives/vpnpak/client.key
}
