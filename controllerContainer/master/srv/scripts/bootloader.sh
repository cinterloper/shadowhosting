mount -o bind /proc /mnt/install/proc
mount -o bind /sys /mnt/install/sys
mount -o bind /dev /mnt/install/dev
cp -a /etc/resolv.conf /mnt/install/etc/resolv.conf
chroot /mnt/install /bin/bash /installGrub.sh "$1"
#systemd-nspawn --capability=CAP_MKNOD --register=no -M $(uname -m) -D /mnt/install /installGrub.sh
