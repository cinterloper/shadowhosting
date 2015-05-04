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


{% for vpnuser in salt['pillar.get']('openvpn_clients') %}
/etc/openvpn/ccd/{{ vpnuser }}:
  file.managed:
    - template: jinja
    - source: salt://openvpn/ccdtemplate
    - defaults:
        vpnuser: {{ vpnuser }}
{% endfor %}

#systemctl restart openvpn@udp:
#   cmd.run
