Vagrant.configure("2") do |config|
 
  config.vm.synced_folder './', '/vagrant'
  config.vm.synced_folder './genesis/salt', '/srv/salt'
  config.vm.synced_folder './genesis/pillar', '/srv/pillar'
  config.ssh.username = 'oper'
  config.ssh.password = 'changeme!'
  config.vm.provision "shell", inline: 
    "pacman -Syu --noconfirm"
  config.vm.provision "shell", inline: 
    "pacman -S --noconfirm salt-zmq"
  config.vm.provision "shell", inline: 
    "groupadd  -f docker"
  config.vm.provision "shell", inline: 
    " dirmngr < /dev/null"

  config.vm.define "shadow_controller"
  config.vm.box = "base.box"
  config.vm.box_download_checksum = "e352659eb1e9020e66474424c918dc20ef8763413a57cd4bc5986e45a9b91897"
  config.vm.box_download_checksum_type = "sha256"
  config.vm.provider "virtualbox" do |v|
   v.memory = 8192
   v.cpus = 8
  end
  config.vm.provision :salt do |salt|

      salt.masterless = "true"
      salt.run_highstate = true

    end
end

