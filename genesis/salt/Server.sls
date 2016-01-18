python2-docker-py:
  pkg:
    - installed

screen:
  pkg:
    - installed
bc:
  pkg:
    - installed
util-linux:
  pkg:
    - installed
parted:
  pkg:
    - installed
openssh:
  pkg:
    - installed
haproxy:
  pkg:
    - installed 
docker:
  pkg:
    - installed
postfix:
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
powerdns-recursor:
  pkg:
    - installed
nmon:
  pkg:
    - installed
#mbuffer:
#  pkg:
#    - installed
htop:
  pkg:
    - installed
pigz:
  pkg:
    - installed
nano:
  pkg:
    - installed
openvpn:
  pkg:
    - installed
python-pip:
  pkg:
    - installed
supervisor:
  pkg:
    - installed

jdk8-openjdk:
  pkg:
    - installed

pacman-key -r 5E1ABF240EE7A126:
  cmd.run
pacman-key -f 5E1ABF240EE7A126:
  cmd.run
pacman-key --lsign-key 5E1ABF240EE7A126:
  cmd.run

/etc/pacman.conf:
  file.managed:
    - source: salt://server/etc/pacman.conf
pacman -Syu --noconfirm:
  cmd.run

spl-git:
  pkg:
    - installed
zfs-git:
  pkg:
    - installed

sshd:
  service.running:
    - enable: True

modprobe spl:
  cmd.run
modprobe zfs:
  cmd.run


create_zpool:
  module.run:
    - unless:
      -  salt-call --local zpool.list | grep core
    - name: zpool.create
    - pool_name: core
    - vdevs:
      - /dev/sda3

create_docker_zfs:
  module.run:
    - unless:
      -  salt-call --local zfs.list | grep docker
    - name: zfs.create
    - m_name: core/docker
    - kwargs: {
      properties: {mountpoint: '/var/lib/docker'} }
dockersvc:
  service.running:
    - name: docker
    - enable: True
    - reload: True
    - watch:
      - pkg: docker


docker pull tonistiigi/dnsdock:latest : 
  cmd.run


cd /vagrant/controllerContainer; bash build baseimage; bash build master:
  cmd.run
cd /vagrant/controllerContainer; bash launch.sh master:
  cmd.run:
   - unless: 
     - docker ps | grep master
salt:
  host.present:
    - ip: 127.1.1.1
salt-minion:
  service.running:
    - enable: True
