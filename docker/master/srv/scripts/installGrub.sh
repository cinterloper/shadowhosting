pacman-key --init
pacman-key --populate archlinux
pacman --noconfirm -Syu
pacman --noconfirm -S grub
grub-install /dev/vda
grub-mkconfig -o /boot/grub/grub.cfg
