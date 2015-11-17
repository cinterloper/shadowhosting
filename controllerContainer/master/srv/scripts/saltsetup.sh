export TERM=vt100
cp /etc/resolv.conf /mnt/install/etc/resolv.conf
mount -o bind /dev /mnt/install/dev
mount -o bind /proc /mnt/install/proc
mount -o bind /sys /mnt/install/sys
chroot /mnt/install /bin/bash /installSalt.sh

