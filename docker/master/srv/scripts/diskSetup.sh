#!/bin/bash
echo -e "o\nn\np\n1\n\n\nw" | fdisk /dev/vda
mkfs.ext4 /dev/vda1
mkdir /mnt/install
mount /dev/vda1 /mnt/install
