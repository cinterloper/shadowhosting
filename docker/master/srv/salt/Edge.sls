screen:
  pkg:
    - installed
haproxy:
  pkg:
    - installed
 
docker-io:
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
pdns:
  pkg:
    - installed
pdns-recursor:
  pkg:
    - installed
nmon:
  pkg:
    - installed
mbuffer:
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
    - source: salt://edge/openvpn/svr.conf.jinja
    - template: jinja
    - context:
        subnet: 13
/etc/openvpn/dh.pem:
  file.managed:
    - source: salt://edge/openvpn/dh.pem.jinja
    - template: jinja
/etc/openvpn/server.key:
  file.managed:
    - source: salt://edge/openvpn/server.key.jinja
    - template: jinja
/etc/openvpn/server.crt:
  file.managed:
    - source: salt://edge/openvpn/server.crt.jinja
    - template: jinja
/etc/openvpn/ca.crt:
  file.managed:
    - source: salt://edge/openvpn/ca.crt.jinja
    - template: jinja
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



#/etc/openvpn/keys/svr.crt:
#  file.managed:
#    - source: salt://edge/svr.crt.jinja
#/etc/openvpn/keys/ca.crt:
#  file.managed:
#    - source: salt://edge/ca.crt.jinja
#/etc/openvpn/keys/dh.pem:
#  file.managed:#
# ##   - source: salt://edge/dh.pem.jinja
#/etc/openvpn/keys/svr.key:
#  file.managed:
#    - source: salt://edge/svr.key.jinja

/tmp/shizzle:
  file.managed:
    - template: jinja
    - source: salt://edge/test.jinja


{% for shdwsvr, data in  salt['pillar.get']('gcfg:svrs', {}).iteritems() %}
/etc/openvpn/ccd/{{ shdwsvr }}:
  file.managed:
    - template: jinja
    - source: salt://edge/openvpn/ccd.jinja
    - context:
        ip: {{data['ip']}}
{% endfor %}

#systemctl restart openvpn@udp:
#   cmd.run
