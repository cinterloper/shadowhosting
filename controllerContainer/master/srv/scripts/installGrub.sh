umount /mnt/install/proc
umount /mnt/install/dev
umount /mnt/install/sys
pacman-key --init
pacman-key --populate archlinux
pacman --noconfirm -Syu
pacman --noconfirm -S grub
ln -sf /proc/self/mounts /etc/mtab
grub-install /dev/$1
grub-mkconfig -o /boot/grub/grub.cfg
systemctl enable dhcpcd
