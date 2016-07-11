#!/usr/bin/env bash
startup_func() {
  $STARTUP_HOOK
}
healthcheck_func() {
  true
}