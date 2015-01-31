#!/bin/bash

cd /srv/vertx
/root/.gvm/vertx/current/bin/vertx run initVerticle.groovy -cp $PWD$(bash /srv/vertx/CPBuilder.sh)

