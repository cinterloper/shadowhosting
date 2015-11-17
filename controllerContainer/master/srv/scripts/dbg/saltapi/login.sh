#!/bin/bash
read password
curl -ksi https://localhost:8000/login \
> -H "Accept: application/json" \
> -d username="$1" \
> -d password="$password" \
> -d eauth='pam'
