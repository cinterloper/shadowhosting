#outputs a list of zfs filesystems in json fromat
# this is kind of a hack until https://github.com/zfsonlinux/zfs/pull/2905 gets merged
zfs list $1 | sed 's/  */ /g' | tr ' ' ',' |
  $JQPath --raw-input --raw-output 'split("\n")  | map(split(",")) | map({"NAME": .[0], "USED": .[1],
        "AVAIL": .[2],
        "REFER": .[3],
        "MOUNTPOINT": .[4]})'
