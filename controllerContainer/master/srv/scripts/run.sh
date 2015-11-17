#!/bin/bash

DSTIP=$1
NAME=$2
DISK=$3
RMTCMD="ssh -oStrictHostKeyChecking=no"
RMTCP="scp -oStrictHostKeyChecking=no"
echo $NAME > /tmp/$$.name

$RMTCP diskSetup.sh $DSTIP:/tmp/
$RMTCMD $DSTIP "chmod +x /tmp/diskSetup.sh; /tmp/diskSetup.sh $DISK"
cat sysInstall.sh | $RMTCMD $DSTIP
$RMTCP installSalt.sh  $DSTIP:/mnt/install/
$RMTCP installGrub.sh  $DSTIP:/mnt/install/
$RMTCP /tmp/$$.name $DSTIP:/mnt/install/etc/hostname
cat saltsetup.sh | $RMTCMD $DSTIP
$RMTCP bootloader.sh  $DSTIP:/tmp/
$RMTCMD $DSTIP "chmod +x /tmp/bootloader.sh; /tmp/bootloader.sh $DISK"

