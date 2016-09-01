FROM cinterloper/cornerstone-base
RUN apt-get update; apt -y upgrade; apt-get -y install wget unzip python python-pip salt-master salt-minion salt-api
RUN echo baseimage > /etc/salt/minion_id
ADD Containers/_salt /srv/salt
RUN salt-call --local state.highstate #pillar='{"sys_packages":'$(cat /srv/salt/pkgs.json)'}'
ENV CORNERSTONE_HOST='172.17.0.1'
ENV KVDN_START_TIME="5"
ENV MY_START_TIME="1"
ENV FAILURE_HOOK="echo FAILURE"
ENV STARTUP_HOOKS="/startup.sh"
CMD /init.sh
