FROM cinterloper/lash
ARG http_proxy
ENV http_proxy ${http_proxy}
RUN apt-get update; apt -y upgrade; apt-get -y install wget unzip python python-pip
RUN pip install CherryPy salt
RUN mkdir /etc/salt
RUN echo baseimage > /etc/salt/minion_id
ADD _salt /srv/salt
RUN mkdir /var/log/salt
RUN salt-call --local state.highstate #pillar='{"sys_packages":'$(cat /srv/salt/pkgs.json)'}'
ENV CORNERSTONE_HOST='172.17.0.1'
ENV KVDN_START_TIME="5"
ENV MY_START_TIME="1"
ENV FAILURE_HOOK="echo FAILURE"
ADD default-startup.sh /startup.sh
ADD init.sh /init.sh
ENV STARTUP_HOOKS="/startup.sh"
CMD /init.sh
