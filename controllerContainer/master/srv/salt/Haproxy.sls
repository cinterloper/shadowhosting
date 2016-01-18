haproxy:
  pkg:
    - installed
nginx:
  pkg:
    - installed
nano:
  pkg:
    - installed
grep:
  pkg:
    - installed
jq:
  pkg:
    - installed
psmisc:
  pkg:
    - installed
procps-ng:
  pkg:
    - installed
diffutils:
  pkg:
    - installed
git:
  pkg:
    - installed
/etc/haproxy/errors/:
  file.directory:
     - user: haproxy
     - group: haproxy
     - dir_mode: 755
     - file_mode: 644
/var/lib/haproxy:
  file.directory:
     - user: haproxy
     - group: haproxy
     - dir_mode: 755
     - file_mode: 644

/etc/haproxy/haproxy.cfg.new:
  file.managed:
    - source: salt://haproxy/haproxy.conf.jinja
    - template: jinja
    - context:
        mode: local
/etc/haproxy/reload.sh:
  cmd.run
