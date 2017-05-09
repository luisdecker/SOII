sudo socat -d -d pty,raw,echo=0,link=/dev/ttyUSB0 pty,raw,echo=0 & disown
sleep 5
USER="$(whoami)"
sudo chown -Rvf $USER:$USER /dev/ttyUSB0
sudo chmod 0777 /dev/ttyUSB0
