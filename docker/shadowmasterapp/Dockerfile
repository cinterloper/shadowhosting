FROM shadowhosting/baseimage

ADD .tmpkey/id_rsa /root/.ssh/user_rsa
ADD config.ssh /root/.ssh/config
RUN git clone git@github.com:raJFS/VXUI.git
RUN cd /VXUI; ./gradlew clean; ./gradlew shadowJar


EXPOSE 8085

