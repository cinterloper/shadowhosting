
pacman -S --noconfirm haveged
haveged
mkdir 864ch
mkdir -p 864ch/var/lib/pacman
pacman --root 864ch -Sy base --noconfirm --noprogressbar
cp build.sh 864ch/
cp prep.sh 864ch/
cp /etc/pacman.d/mirrorlist 864ch/etc/pacman.d/mirrorlist
systemd-nspawn --capability=CAP_MKNOD --register=no -M $(uname -m) -D 864ch /prep.sh
cp additives/archboot/allinone.conf 864ch/etc/archboot/
cp additives/hook/build/* 864ch/usr/lib/initcpio/install/
mkdir -p 864ch/etc/openvpn/
cp additives/vpnpak/* 864ch/etc/openvpn/
systemd-nspawn --capability=CAP_MKNOD --register=no -M $(uname -m) -D 864ch /build.sh
ln 864ch/core-x86_64.tar 864ch/core-i686.tar

