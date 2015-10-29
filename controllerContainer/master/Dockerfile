FROM shadowhosting/baseimage
RUN pacman -S --noconfirm python2-pyopenssl python2-tornado python2-pip openssl salt-zmq python2 gcc make openvpn grep procps-ng
RUN pip2 install -U ws4py cherrypy
EXPOSE 4505 4506 8000
RUN ssh-keygen -A
