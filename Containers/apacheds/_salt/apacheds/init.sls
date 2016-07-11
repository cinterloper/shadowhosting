{% set packages = ["openjdk-8-jre","htop"] %}
{% for pkg in packages %}

{{ pkg }}:
  pkg:
    - installed
{% endfor %}

vault_package:
  archive.extracted:
    - name: /opt/apacheds/
    - source: http://mirror.olnevhost.net/pub/apache//directory/apacheds/dist/2.0.0-M21/apacheds-2.0.0-M21.zip
    - source_hash: sha256=3fee567483231133db0d9f10331d97f7ed42fbe133524c77facea50aaa7ae86e
    - archive_format: zip
