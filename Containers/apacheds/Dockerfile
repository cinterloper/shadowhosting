FROM shadowsystem/baseimage
RUN echo apacheds > /etc/salt/minion_id
ADD _salt /srv/salt
RUN salt-call --local state.highstate # pillar='{"sys_packages":'$(cat /srv/salt/pkgs.json)'}'
RUN chmod +x /opt/apacheds/apacheds-2.0.0-M21/bin/apacheds.sh
ENV CAPABILITIES='["LDAP_SERVER"]'
ENV STARTUP_HOOK='/opt/apacheds/apacheds-2.0.0-M21/bin/apacheds.sh run'
