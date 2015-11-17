pigz:
  pkg:
    - installed
nodejs:
  pkg:
    - installed
npm:
  pkg:
    - installed
fakeroot:
  pkg:
    - installed

make:
  pkg:
    - installed

firefox:
  pkg:
    - installed

nano:
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
ncdu:
  pkg:
    - installed

python-pip:
  pkg:
    - installed

virt-manager:
  pkg:
    - installed


zshdb:  
  pkg:
    - installed
zsh-syntax-highlighting:
  pkg:
    - installed
zsh-completions:
  pkg:
    - installed
grml-zsh-config:
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


