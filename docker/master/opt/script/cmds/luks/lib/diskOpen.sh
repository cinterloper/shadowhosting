#!/bin/bash
#auto-mount your 

#************************************************************************************************
#this implements getting a key from a file 
#in reality this should not be from a file, 
#this should be calling an external process to retrieve the config from a KDC, over cyphertext
getKey ( )
{
  CONFIG=$(<../config.json)
  echo $( cat config.json | jq -r '.["'$disk'"]') 
}
#implement this yourself by replaceing this section with a 'source' statement,
# and define your getKey function in another file
#*************************************************************************************************


#when we move a luks volume from one physical disk to another, does it retain its uuid?
findLuksDisks ( )
{
for id in $(find /dev/disk/by-uuid/) 
do 
   luksUUID=$(cryptsetup luksUUID $id 2>/dev/null)
	if [ "$luksUUID" != "" ]
	then
		echo $luksUUID  
	fi
done
}



for disk in $(findLuksDisks)
do 
  getKey $disk | cryptsetup luksOpen /dev/disk/by-uuid/$disk $disk
done
