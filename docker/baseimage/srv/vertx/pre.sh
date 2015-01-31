export UUID=$(date -Ins | md5sum | cut -d ' ' -f 1)
export CONFIG='./Config.json' 
export LOGPATH='./logs'
mkdir $LOGPATH 2>/dev/null
CALLBACK( )
{
 curl -s -XGET http://localhost:8090/$1
}

encodeJson( )
{
  perl -e 'use JSON; @in=grep(s/\n$//, <>); print encode_json(\@in)."\n";'
}

cleanmemory( )
{
  unset HTTP_USER_AGENT
  unset REQUEST_METHOD
  unset POST_DATA
}

