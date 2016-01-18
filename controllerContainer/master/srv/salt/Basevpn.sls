{% for edgesvr, data in  salt['pillar.get']('gcfg:edges', {}).iteritems() %}
/etc/openvpn/{{ edgesvr }}-install.conf:
  file.managed:
    - source: salt://server/openvpn/client.conf.jinja
    - template: jinja
    - context:
        edgeport: 1195
        edgehost: {{ edgesvr }}
{% endfor %}

