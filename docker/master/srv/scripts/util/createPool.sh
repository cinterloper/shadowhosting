 salt 'bal*' zpool.create core raidz2 $(for dsk in $(bash disksExclude.sh balthazarServer sdf ); do echo -n /dev/$dsk\ ; done) force=True
