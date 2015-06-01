FROM shadowhosting/baseimage
RUN yum -y --enablerepo=updates-testing install python-libcloud pyOpenSSL python-tornado
RUN yum -y install openssl
RUN sshd-keygen -y -A
RUN curl -o install_salt.sh -L https://bootstrap.saltstack.com
RUN sudo sh install_salt.sh -X -M git v2015.2.0rc2
RUN yum -y install python-pip gcc python-devel
RUN pip install -U tornado
EXPOSE 4505 4506 8000


