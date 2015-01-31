Vert.Xecutor
============

Cluster / Grid job execution framework in Vert.X, Config compatible with REST.sh


This requires durbinlib
https://github.com/jdurbin/durbinlib
just check it out in this directory and build it

run like so:
 vertx run executor.groovy -cp durbinlib/target/jar/durbinlib.jar


example:

grant@localhost ~/projects/vertx/Vert.Xecutor (git)-[master] % curl -XGET http://localhost:8089/cgi-bin/SysMaint.sh?action=commandlist

[
  "commandlist",
  "env",
  "loglist",
  "mailresults",
  "methods",
  "runlog",
  "testinput"
]
