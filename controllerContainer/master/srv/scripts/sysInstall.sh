#!/bin/bash
echo 'Server = http://mirrors.kernel.org/archlinux/$repo/os/$arch' > /etc/pacman.d/mirrorlist
pacstrap /mnt/install base base-devel
