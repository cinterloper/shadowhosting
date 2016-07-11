/etc/salt/cloud.providers.d/linode-provider:
  file.managed:
    - source: salt://saltmaster/resources/linode-provider.jinja
    - template: jinja
/etc/salt/cloud.profiles.d/linode-profile:
  file.managed:
    - source: salt://saltmaster/resources/linode-profile.jinja
    - template: jinja
