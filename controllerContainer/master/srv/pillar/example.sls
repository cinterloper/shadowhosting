haproxy:
  enabled: True
  config_file_path: /etc/haproxy/haproxy.cfg
  global:

    user: haproxy
    group: haproxy
    chroot:
      enable: True
      path: /var/lib/haproxy

    daemon: True


  userlists:
    userlist1:
      users:
        john: insecure-password doe
        sam: insecure-password frodo
#      groups:
#        admins: users john sam
#        guests: users jekyll hyde jane

  defaults:
    log: global
    mode: http
    retries: 3
    options:
      - httplog
      - dontlognull
      - forwardfor
      - http-server-close
    logformat: "%ci:%cp\\ [%t]\\ %ft\\ %b/%s\\ %Tq/%Tw/%Tc/%Tr/%Tt\\ %ST\\ %B\\ %CC\\ %CS\\ %tsc\\ %ac/%fc/%bc/%sc/%rc\\ %sq/%bq\\ %hr\\ %hs\\ %{+Q}r"
    timeouts:
      - http-request    10s
      - queue           10m
      - connect         10s
      - client          1m
      - server          1m
      - http-keep-alive 10s
      - check           10s
    stats:
      - enable
      - uri: '/admin?stats'
      - realm: 'Haproxy\ Statistics'
      - auth: 'admin1:AdMiN123'


  {# Suported by HAProxy 1.6 #}
  resolvers:
    local_dns:
      options:
        - nameserver resolvconf 8.8.8.8:53
        - resolve_retries 3
        - timeout retry 1s
        - hold valid 10s


  frontends:
    frontend1:
      name: www-http
      bind: "*:80"
      reqadds:
        - "X-Forwarded-Proto:\\ http"
      default_backend: www-backend
  backends:
    backend1:
      name: www-backend
      balance: roundrobin
      servers:
        server1:
          name: net
          host: icanhazip.com
          port: 80
          check: check
