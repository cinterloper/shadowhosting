FROM shadowsystem/baseimage
ADD _salt/saltmaster /srv/salt/saltmaster
RUN echo saltmaster > /etc/salt/minion_id
RUN salt-call --local state.highstate # pillar='{"sys_packages":'$(cat /srv/salt/pkgs.json)'}'
RUN useradd "user"
RUN echo "user:changeme" | chpasswd
ENV SALT_PASS=changeme
ENV CAPABILITIES='["SALT_MASTER"]'
ENV STARTUP_HOOK='supervisord'
ENV MY_START_TIME="10"
RUN echo "master: localhost" > /etc/salt/minion
ADD startup.sh /startup.sh
EXPOSE 8000 8200 4505 4506
