FROM fedora:21
RUN yum -y update
RUN yum -y install java-1.8.0-openjdk supervisor git screen lsof openssh-server nano telnet ssh sudo zsh
RUN useradd -m -G wheel user
RUN mkdir -p /home/user/.ssh
RUN chown user:user /home/user/.ssh
RUN chmod 700 /home/user/.ssh
ADD sudoers /etc/sudoers
RUN yum -y install curl unzip 
ENV JAVA_HOME /etc/alternatives/jre_1.8.0_openjdk/
ENV HOME /root
EXPOSE 6666 
RUN yum -y install perl-JSON perl jq curl hostname
WORKDIR /opt
ENV PWD /opt
RUN curl -o /root/.zshrc "http://git.grml.org/?p=grml-etc-core.git;a=blob_plain;f=etc/zsh/zshrc;hb=HEAD"
ADD id.rsa /root/.ssh/id_rsa
RUN chmod 600 /root/.ssh/id_rsa
RUN echo -e "Host github.com\n\tStrictHostKeyChecking no\n" >> ~/.ssh/config
ADD supervisord.conf /etc/supervisord.conf
CMD supervisord -n
