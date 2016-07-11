{% set packages = ["maven","git","htop","jq","mbuffer","nano","nmon","ntp","pigz","pv","python","screen","ssh","supervisor", "openjdk-8-jdk-headless", "openvpn"] %}

{% for pkg in packages %}

{{ pkg }}:
  pkg:
    - installed

{% endfor %}


system:
  network.system:
    - enabled: True
    - hostname: shadow-controller-vm

rprereq:
  pkg.installed:
    - pkgs:
      - apt-transport-https
      - ca-certificates
      - python-apt


apt-docker-repo:
  pkgrepo.managed:
    - humanname: Docker main repo
    - name: deb https://apt.dockerproject.org/repo ubuntu-xenial main
    - dist: ubuntu-xenial
    - keyid: 58118E89F3A912897C070ADBF76221572C52609D
    - keyserver: hkp://p80.pool.sks-keyservers.net:80


/lib/systemd/system/docker.service:
  file.managed:
    - source: salt://docker.service


{% if grains['osrelease'] == '16.04' %}
/etc/default/docker:
  file.managed:
    - contents:
      - 'DOCKER_OPTS=" -H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock  "'
{% endif %}

docker.io:
  pkg:
    - removed
docker-engine:
  pkg:
    - installed

docker-compose:
  pkg:
    - installed
docker:
  service:
    - running

