FROM shadowhosting/baseimage
RUN sshd-keygen -y
RUN yum -y --enablerepo=updates-testing install salt-master salt-api salt-minion salt python-libcloud pyOpenSSL python-tornado salt-cloud
RUN yum -y install openssl

EXPOSE 4505 4506 8000

CMD ["/usr/bin/supervisord"]

