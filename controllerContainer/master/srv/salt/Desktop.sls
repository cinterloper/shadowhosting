winswitch:
  pkgrepo.managed:
    - humanname: winswitch
    - baseurl: https://winswitch.org/dists/Fedora/$releasever/$basearch/
    - gpgcheck: 1
    - gpgkey: https://winswitch.org/gpg.asc

  pkg.latest:
    - name: winswitch
    - refresh: true

_copr_jstribny-vagrant-f21:
  pkgrepo.managed:
    - humanname: vagrant
    - baseurl: https://copr-be.cloud.fedoraproject.org/results/jstribny/vagrant-f21/fedora-$releasever-$basearch/
    - gpgcheck: 0



yum localinstall --nogpgcheck http://archive.zfsonlinux.org/fedora/zfs-release$(rpm -E %dist).noarch.rpm:
  cmd.run

yum localinstall --nogpgcheck http://download1.rpmfusion.org/nonfree/fedora/rpmfusion-nonfree-release-$(rpm -E %fedora).noarch.rpm:
  cmd.run

yum localinstall --nogpgcheck http://download1.rpmfusion.org/free/fedora/rpmfusion-free-release-$(rpm -E %fedora).noarch.rpm:
  cmd.run

kernel-devel:
  pkg:
    - installed
chromium:
  pkg:
    - installed
zfs:
  pkg:
    - installed
screen:
  pkg:
    - installed
 
pidgin:
  pkg:
    - installed
jq:
  pkg:
    - installed

git:
  pkg:
    - installed
zsh:
  pkg:
    - installed
pv:
  pkg:
    - installed
nmon:
  pkg:
    - installed
htop:
  pkg:
    - installed

pigz:
  pkg:
    - installed

firefox:
  pkg:
    - installed

nano:
  pkg:
    - installed

libvirt-devel:
  pkg:
    - installed

vlc:
  pkg:
    - installed
nmap:
  pkg:
    - installed
jython:
  pkg:
    - installed
openvpn:
  pkg:
    - installed
vagrant:
  pkg:
    - installed
vagrant-libvirt:
  pkg:
    - installed

mixxx:
  pkg:
    - installed
ncdu:
  pkg:
    - installed
mariadb:
  pkg:
    - installed
mariadb-server:
  pkg:
    - installed
python-pip:
  pkg:
    - installed

virt-manager:
  pkg:
    - installed
pki-ca:
  pkg:
    - installed
ruby-devel:
  pkg:
    - installed

java-1.8.0-openjdk-headless:
  pkg:
    - installed

pip install git+http://github.com/systemd/journal2gelf.git#egg=journal2gelf:
  cmd.run
