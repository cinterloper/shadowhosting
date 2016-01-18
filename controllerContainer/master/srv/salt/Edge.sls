screen:
  pkg:
    - installed
haproxy:
  pkg:
    - installed
 
docker:
  pkg:
    - installed
  service:
    - running
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
powerdns:
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


/etc/openvpn/install.conf:
  file.managed:
    - source: salt://edge/openvpn/svr.conf.jinja
    - template: jinja
    - context:
        subnet: 254
        installer: 1

/etc/openvpn/udp.conf:
  file.managed:
    - source: salt://edge/openvpn/svr.conf.jinja
    - template: jinja
    - context:
        subnet: 13
        installer: 0

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


/root/.ssh:
  file.directory:
    - user: root
    - group: root
    - mode: 700
    - makedirs: True

/root/.ssh/authorized_keys:
  file.managed:
    - template: jinja
    - source: salt://edge/ssh/authorized_keys.jinja
    - user: root
    - group: root
    - mode: 600


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
openvpn@udp:
  service.running:
    - enable: True
    - reload: True
    - watch:
      - pkg: openvpn
openvpn@install:
  service.running:
    - enable: True
    - reload: True
    - watch:
      - pkg: openvpn
salt:
  host.present:
    - ip:  10.{{salt['pillar.get']('files:conf:openvpn:subnet')}}.1.10

