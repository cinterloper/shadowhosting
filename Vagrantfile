Vagrant.configure("2") do |config|
  config.vbguest.auto_update = true
  config.vm.provision "shell", inline:
    "apt update; apt install -y virtualbox-guest-dkms"
# config.vm.provision "file", source: "salt", destination: "/"
 config.vm.provision "shell", inline:
    "apt update; apt-get -y install python-pip; pip install salt; mkdir -p /etc/salt; echo 'controller' > /etc/salt/minion_id"
  config.vm.define "shadow-controller-vm"
  config.vm.box = "https://cloud-images.ubuntu.com/xenial/current/xenial-server-cloudimg-amd64-vagrant.box"
  config.vm.provider "virtualbox" do |v|
#   v.customize ['createhd', '--filename', 'shadow_data', '--size', 20 * 1024]
#   v.customize ['storageattach', :id, '--storagectl', "SCSI Controller", '--port', 1, '--device', 0, '--type', 'hdd', '--medium', 'shadow_data.vdi' ]
   v.memory = 4096
   v.cpus = 4
   v.name = "shadow-controller-vm"
  end
  config.vm.provision :salt do |salt|
      salt.masterless = "true"
      salt.run_highstate = true
  end

  config.vm.synced_folder "salt", "/srv/salt"
  config.vm.synced_folder "./", "/vagrant"
  config.vm.network "forwarded_port", guest: 2224, host: 3224
end
