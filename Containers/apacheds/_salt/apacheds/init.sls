{% set packages = ["openjdk-8-jre","htop"] %}
{% for pkg in packages %}

{{ pkg }}:
  pkg:
    - installed
{% endfor %}

vault_package:
  archive.extracted:
    - name: /opt/apacheds/
    - source: http://www.webhostingjams.com/mirror/apache//directory/apacheds/dist/2.0.0-M23/apacheds-2.0.0-M23.zip
    - source_hash: md5=a98fff7887a66d016b0484c076789128
    - archive_format: zip
