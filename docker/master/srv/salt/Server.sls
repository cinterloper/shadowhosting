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

jdk8-openjdk:
  pkg:
    - installed

{% for edgesvr, data in  salt['pillar.get']('gcfg:edges', {}).iteritems() %}
/etc/openvpn/{{ edgesvr }}.conf:
  file.managed:
    - source: salt://server/openvpn/client.conf.jinja
    - template: jinja
    - context:
        edgeport: 1194
        edgehost: {{ edgesvr }}
{% endfor %}


/etc/openvpn/dh.pem:
  file.managed:
    - source: salt://server/openvpn/dh.pem.jinja
    - template: jinja
/etc/openvpn/client.key:
  file.managed:
    - source: salt://server/openvpn/client.key.jinja
    - template: jinja
/etc/openvpn/client.crt:
  file.managed:
    - source: salt://server/openvpn/client.crt.jinja
    - template: jinja
/etc/openvpn/ca.crt:
  file.managed:
    - source: salt://server/openvpn/ca.crt.jinja
    - template: jinja


/tmp/shizzle:
  file.managed:
    - template: jinja
    - source: salt://server/test.jinja

