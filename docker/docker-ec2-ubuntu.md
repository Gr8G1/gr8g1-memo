## AWS EC2(Ubuntu)에 Docker 설치

### On EC2
```bash
$ apt update & apt upgrade

# Docker 설치에 필요한 필수 패키지 설치
$ sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common

# Docker GPC Key 인증
$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -
# 'OK' 출력시 정상 설치

# Docker Repository 등록
$ sudo add-apt-repository \
"deb [arch=amd64] https://download.docker.com/linux/ubuntu \
$(lsb_release -cs) \
stable"

$ sudo apt-get update && sudo apt-get install docker-ce docker-ce-cli containerd.io 

# Docker 설치 확인
$ docker -v

# Docker 활성화 & 실행
$ sudo systemctl enable docker
$ sudo service docker start
$ service docker status 
```

### Docker-compose 설치
```bash
$ sudo curl -L https://github.com/docker/compose/releases/download/1.25.0\
-rc2/docker-compose-`uname -s`-`uname -m` -o \
/usr/local/bin/docker-compose

# 실행권한 부여
$ sudo chmod +x /usr/local/bin/docker-compose
```
