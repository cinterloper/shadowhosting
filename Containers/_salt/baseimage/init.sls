{% set packages = ["git","htop","jq","mbuffer","nano","nmon","ntp","pigz","pv","python","screen","ssh","supervisor", "httpie", "libssl-dev", "luarocks"] %}

{% for pkg in packages %}

{{ pkg }}:
  pkg:
    - installed

{% endfor %}


