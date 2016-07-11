FROM shadowsystem/baseimage
RUN echo vault > /etc/salt/minion_id
ADD _salt/vault /srv/salt/vault
RUN salt-call --local state.highstate #pillar='{"sys_packages":'$(cat /srv/salt/pkgs.json)'}'
RUN chmod +x /opt/vault/vault
ENV CAPABILITIES='["VAULT_SERVER"]'
ENV STARTUP_HOOK='/opt/vault/vault server -config=/opt/vault/config.hcl'
ADD shellservice.json /opt/lash/lib/chain/config.json