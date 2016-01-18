if [ ! -e /core/installerbuild ]
then
  zfs create core/installerbuild
fi

cp -a installerbuild/* /core/installerbuild/
cd /core/installerbuild/
bash start.sh
bash end.sh
