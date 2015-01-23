screen:
  pkg:
    - installed
haproxy:
  pkg:
    - installed
 
docker-io:
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
nano:
  pkg:
    - installed
openvpn:
  pkg:
    - installed
python-pip:
  pkg:
    - installed

java-1.8.0-openjdk-headless:
  pkg:
    - installed

/etc/openvpn/udp.conf:
  file.managed:
    - source: salt://edge/openvpn.svr.conf.jinja
    - template: jinja
    - context:
        subnet: 13
/etc/openvpn/ccd:
  file.directory:
    - user: root
    - group: root
    - mode: 755
    - makedirs: True
/etc/openvpn/keys:
  file.directory:
    - user: root
    - group: root
    - mode: 755
    - makedirs: True
/etc/openvpn/keys/svr.crt:
  file.managed:
    - source: salt://edge/svr.crt
/etc/openvpn/keys/ca.crt:
  file.managed:
    - source: salt://edge/ca.crt
/etc/openvpn/keys/dh.pem:
  file.managed:
    - source: salt://edge/dh.pem
/etc/openvpn/keys/svr.key:
  file.managed:
    - source: salt://edge/svr.key
systemctl restart openvpn@udp:
   cmd.run
