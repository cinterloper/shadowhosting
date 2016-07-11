#!/bin/bash
#init the PKI for a new Shadow System
# - clean old key material
# - build root key for 1st shadowmaster
# - build root ca for vpn system
# - build 1st shadowmaster cert
WD=$(pwd)
initpki () {
 echo $1
 PKICN=$1
 if [[ "$ARGBLK" == "" ]]
 then
  export ARGBLK="--req-cn=$PKICN --req-c=US --req-st=AZ --req-city=Tucson --req-org=shadow --req-email=admin@iowntheinter.net --req-ou=shadow"
  echo export ARGBLK="--req-cn=$PKICN --req-c=US --req-st=AZ --req-city=Tucson --req-org=shadow --req-email=admin@iowntheinter.net --req-ou=shadow"
 fi
 WD=$PWD
 export EASYRSA_BATCH=True
 cd easyrsa
 echo yes | ./easyrsa init-pki
 ./easyrsa $ARGBLK build-ca nopass
 ./easyrsa  gen-dh
 unset ARGBLK
 cd $WD
}
#args: $1 - svr name
genserver () {
   cd easyrsa
  ./easyrsa build-server-full $1 nopass
  cd $WD
  mkdir -p $RESULTDIR
  cp -a easyrsa/pki/{ca.crt,dh.pem} $RESULTDIR
  cp -a easyrsa/pki/{issued/$1.crt,private/$1.key} $RESULTDIR  

}
#args: $1 - client name
genclient () {
   cd easyrsa
  ./easyrsa build-client-full $1 nopass
  cd $WD
  mkdir -p $RESULTDIR
  cp -a easyrsa/pki/{ca.crt,dh.pem} $RESULTDIR
  cp -a easyrsa/pki/{issued/$1.crt,private/$1.key} $RESULTDIR  


}
#args: $1 - crt name
cleanfailed () {
  cd $WD
  find  easyrsa/ -type f| grep $1 | xargs rm
}
#args: $1 - crt name
copyinstaller () { # copy the installer certificate into the files to be added to the installer iso build
  mkdir -p $INSTALLER_VPN_DIR
  cp -a easyrsa/pki/{ca.crt,dh.pem} $INSTALLER_VPN_DIR
  cp -a easyrsa/pki/issued/$1.crt $INSTALLER_VPN_DIR/client.crt  
  cp -a easyrsa/pki/private/$1.key $INSTALLER_VPN_DIR/client.key
}
convertjks () {
  WD=$PWD
  cd $RESULTDIR
  openssl pkcs12 -passout pass:$KEYPASS -export -name $CLIENT -in $CLIENT.crt -inkey $CLIENT.key -out $CLIENT.p12
  keytool -importkeystore -destkeystore $CLIENT.jks -srckeystore $CLIENT.p12 -storepass $KEYPASS -srcstoretype pkcs12  -srcstorepass:env KEYPASS
  cd $WD
}
