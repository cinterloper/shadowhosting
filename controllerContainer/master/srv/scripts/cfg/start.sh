#!/bin/bash
ROOTDIR=$1
pacman -S --noconfirm haveged
haveged
mkdir $ROOTDIR
mkdir -p $ROOTDIR/var/lib/pacman
pacman --root $ROOTDIR -Sy base --noconfirm --noprogressbar
cp build.sh $ROOTDIR/
cp prep.sh $ROOTDIR/
cp /etc/pacman.d/mirrorlist $ROOTDIR/etc/pacman.d/mirrorlist
systemd-nspawn --capability=CAP_MKNOD --register=no -M $(uname -m) -D $ROOTDIR /prep.sh
cp ../../docker/master/config/ssh/id_rsa.pub additives/authorized_keys
cp additives/archboot/allinone.conf $ROOTDIR/etc/archboot/
cp additives/hook/build/* $ROOTDIR/usr/lib/initcpio/install/
mkdir -p $ROOTDIR/etc/openvpn/
cp additives/vpnpak/* $ROOTDIR/etc/openvpn/
systemd-nspawn --capability=CAP_MKNOD --register=no -M $(uname -m) -D $ROOTDIR /build.sh

