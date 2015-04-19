FROM shadowhosting/baseimage
RUN yum -y --enablerepo=updates-testing install salt-master salt-api salt-minion salt python-libcloud pyOpenSSL python-tornado salt-cloud
RUN yum -y install openssl
RUN sshd-keygen -y -A

EXPOSE 4505 4506 8000


