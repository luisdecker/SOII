set -x
echo I am provisioning...
USER="$(whoami)"
date > /etc/vagrant_provisioned_at

# Make Ubuntu use mirrors based on localization
# sudo sed -i 's/http:\/\/archive.ubuntu.com\/ubuntu/mirror:\/\/mirrors.ubuntu.com\/mirrors.txt/g' /etc/apt/sources.list
sudo apt-get update

sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password password projeto'
sudo debconf-set-selections <<< 'mysql-server mysql-server/root_password_again password projeto'
sudo apt-get install -y mysql-server
/sbin/service mysqld start
mysql -uroot -pprojeto < /vagrant/dump.sql

echo "Installing Java 8.."
sudo apt-get install -y software-properties-common python-software-properties
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | sudo /usr/bin/debconf-set-selections
sudo add-apt-repository ppa:webupd8team/java -y
sudo apt-get update
sudo apt-get install oracle-java8-installer

echo "Installing tomcat8.."
sudo apt-get -y install tomcat8
sudo /etc/init.d/tomcat8 stop
sudo /etc/init.d/tomcat8 start

echo "Installing socat.."
sudo apt-get -y install socat

echo "Setting environment variables for Java 8.."
sudo apt-get install -y oracle-java8-set-default

sudo cp /vagrant/simulateUsb.sh /usr/local/bin
sudo chmod +x /vagrant/simulateUsb.sh
sudo chmod +x /usr/local/bin/simulateUsb.sh
if [ "$USER" == "packer" ]; then
    # This makes sense just for packer
    sudo chmod -Rvf 0777 /var/lib/tomcat8/webapps
else
    sudo apt-get install -y expect # Needed to acccept licenses
    curl --location https://dl.google.com/android/android-sdk_r24.4.1-linux.tgz | tar -x -z -C /opt
    COMMAND="export ANDROID_HOME=/opt/android-sdk-linux"
    echo $COMMAND >> /etc/bash.bashrc
    echo "export SLAVE_AAPT_TIMEOUT=60" >> /etc/bash.bashrc
    sudo chmod +x /vagrant/accept-android-licenses
    /vagrant/accept-android-licenses "/opt/android-sdk-linux/tools/android update sdk --no-ui --all --filter platform-tools"
    /vagrant/accept-android-licenses "/opt/android-sdk-linux/tools/android update sdk --no-ui --all --filter tools"
    /vagrant/accept-android-licenses "/opt/android-sdk-linux/tools/android update sdk --no-ui --all --filter build-tools-24.0.3"
    /vagrant/accept-android-licenses "/opt/android-sdk-linux/tools/android update sdk --no-ui --all --filter android-24"
    /vagrant/accept-android-licenses "/opt/android-sdk-linux/tools/android update sdk --no-ui --all --filter extra-android-m2repository"
    sudo fallocate -l 2G /swapfile
    sudo chown root:root /swapfile
    sudo chmod 0600 /swapfile
    sudo mkswap /swapfile
    sudo swapon /swapfile
    sudo sh -c 'echo "/swapfile none            swap    sw              0       0" >> /etc/fstab'
fi
