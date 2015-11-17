FROM fedora
RUN yum -y update
RUN yum -y install java-1.8.0-openjdk supervisor git screen lsof openssh-server nano telnet ssh sudo 
RUN useradd -m -G wheel user
RUN mkdir -p /home/user/.ssh
RUN chown user:user /home/user/.ssh
RUN chmod 700 /home/user/.ssh
ADD sudoers /etc/sudoers

