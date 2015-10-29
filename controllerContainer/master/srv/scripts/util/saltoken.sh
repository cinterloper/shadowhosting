curl -sSk http://localhost:8000/login \
    -H 'Accept: application/json' \
    -d username=user \
    -d password=changeme \
    -d eauth=pam
