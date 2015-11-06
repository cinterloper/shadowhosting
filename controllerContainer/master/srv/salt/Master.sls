{% for edgesvr, data in  salt['pillar.get']('gcfg:edges', {}).iteritems() %}
/etc/openvpn/{{ edgesvr }}.conf:
  file.managed:
    - source: salt://server/openvpn/client.conf.jinja
    - template: jinja
    - context:
        edgeport: 1195
        edgehost: {{ edgesvr }}
{% endfor %}


#/root/.ssh/authorized_keys:
#  file.managed:
#    - template: jinja
#    - source: salt://server/ssh/authorized_keys.jinja
#    - user: root
#    - group: root
#    - mode: 600

/etc/salt/cloud.profiles.d/:
  file.directory:
    - user: root
    - group: root
/etc/salt/cloud.providers.d/:
  file.directory:
    - user: root
    - group: root

/etc/salt/cloud.profiles.d/linode.conf:
  file.managed:
    - source: salt://master/salt/cloud.profiles.d/linode.jinja
    - template: jinja
/etc/salt/cloud.providers.d/linode.conf:
  file.managed:
    - source: salt://master/salt/cloud.providers.d/linode.jinja
    - template: jinja


