pacman-key --init
pacman-key --populate archlinux
pacman --noconfirm -S mkinitcpio archboot
pacman --noconfirm -Syu
pacman --noconfirm -S extra/jre8-openjdk-headless
