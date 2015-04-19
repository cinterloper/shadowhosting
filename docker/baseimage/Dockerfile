FROM fedora
RUN yum -y update
RUN yum -y install java-1.8.0-openjdk supervisor git screen lsof openssh-server nano telnet ssh sudo 
RUN useradd -m -G wheel user
RUN mkdir -p /home/user/.ssh
RUN chown user:user /home/user/.ssh
RUN chmod 700 /home/user/.ssh
ADD sudoers /etc/sudoers
RUN yum -y install curl unzip 
ENV JAVA_HOME /etc/alternatives/jre_1.8.0_openjdk/
RUN export HOME=/root/; curl -s get.gvmtool.net | bash
RUN export HOME=/root/; source /root/.gvm/bin/gvm-init.sh;  gvm i vertx
RUN echo "source /root/.gvm/bin/gvm-init.sh" >> /etc/bash.bashrc
ENV HOME /root
EXPOSE 6666 
RUN yum -y install perl-JSON perl jq curl hostname
RUN /root/.gvm/vertx/current/bin/vertx run stub.groovy 2>&1
WORKDIR /opt
ENV PWD /opt
CMD java -jar /opt/Vert.Xecutor-0.1-fat.jar -conf /opt/conf.json > /srv/vx.log 2> /srv/vx.err

