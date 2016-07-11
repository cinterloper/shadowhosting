{% set packages = ["nano","supervisor"] %}

{% for pkg in packages %}

{{ pkg }}:
  pkg:
    - installed

{% endfor %}



vault_package:
  archive.extracted:
    - name: /opt/vault/
    - source: https://releases.hashicorp.com/vault/0.5.2/vault_0.5.2_linux_amd64.zip
    - source_hash: sha256=7517b21d2c709e661914fbae1f6bf3622d9347b0fe9fc3334d78a01d1e1b4ec2
    - archive_format: zip

/opt/vault/:
  file.directory:
    - mode: 0755
    - owner: root
    - group: root
    - recurse:
       - user
       - group
       - mode

/opt/vault/config.hcl:
  file.managed:
    - source: salt://vault/vault.hcl
/etc/supervisord.conf:
  file.managed:
    - source: salt://vault/supervisord.conf
