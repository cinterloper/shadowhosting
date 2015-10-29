#!/bin/bash
echo -e "o\nn\np\n1\n\n\nw" | fdisk /dev/$1
mkfs.ext4 /dev/"$1"1
mkdir /mnt/install
mount /dev/"$1"1 /mnt/install
