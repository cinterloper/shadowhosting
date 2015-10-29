mkdir buildir
pacman -S --noconfirm haveged
haveged
mkdir -p buildir/var/lib/pacman
mkdir -p buildir/tmp/build
pacman --root buildir -Sy base --noconfirm --noprogressbar
cp prepinit.sh buildir/
cp initcpio.sh buildir/
cp /etc/pacman.d/mirrorlist buildir/etc/pacman.d/mirrorlist
systemd-nspawn --capability=CAP_MKNOD --register=no -M $(uname -m) -D buildir /bin/bash /prepinit.sh
cp additives/mkinitcpio.conf buildir/etc/
cp additives/hook/build/* buildir/lib/initcpio/install/
mkdir -p buildir/etc/openvpn/
cp additives/vpnpak/* buildir/etc/openvpn/
systemd-nspawn --capability=CAP_MKNOD --register=no -M $(uname -m) -D buildir /bin/bash /initcpio.sh

