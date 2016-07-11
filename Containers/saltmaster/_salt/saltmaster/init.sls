{% set packages = ["python-pip"] %}

{% for pkg in packages %}

{{ pkg }}:
  pkg:
    - installed

{% endfor %}


salt:
  host.present:
    - ip: 127.0.0.1
  pip.installed:
    - name: salt
ws4py:
  pip.installed:
    - name: ws4py
/etc/supervisord.conf:
  file.managed:
    - source: salt://saltmaster/resources/supervisord.conf
/etc/salt/master:
  file.managed:
    - source: salt://saltmaster/resources/master
/etc/salt/cloud.providers.d/:
  file.directory
/etc/salt/cloud.profiles.d/:
  file.directory
/etc/salt/reactor.d/:
  file.directory