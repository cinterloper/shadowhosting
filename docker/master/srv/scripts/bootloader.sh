mount -o bind /dev/ /mnt/install/dev
mount -o bind /proc /mnt/install/proc
mount -o bind /sys /mnt/install/sys
chroot /mnt/install /bin/bash /installGrub.sh
#systemd-nspawn --capability=CAP_MKNOD --register=no -M $(uname -m) -D /mnt/install /installGrub.sh
