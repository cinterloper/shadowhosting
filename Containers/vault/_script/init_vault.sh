#!/usr/bin/env bash

init_error(){
  kill -9 $$
}


do_init() {
  {
    /opt/vault/bin/vault init
  } || {
    init_error
  }
}


RD=$(do_init | head -n 6)
