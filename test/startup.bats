#!/usr/bin/env bats



@test "zfs loaded" {
  lsmod | grep zfs
  result=$?
  [ "$result" -eq 0 ]
}
@test "installerbuild mounted" {
  mount | grep installerbuild
  result=$?
  [ "$result" -eq 0 ]
}
