/etc/salt/cloud.providers.d/aws-provider:
  file.managed:
    - source: salt://saltmaster/resources/aws-provider.conf.jinja
    - template: jinja
/etc/salt/cloud.profiles.d/aws-profile:
  file.managed:
    - source: salt://saltmaster/resources/aws-profile.conf.jinja
    - template: jinja