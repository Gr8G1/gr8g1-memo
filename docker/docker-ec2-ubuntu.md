## AWS EC2(Ubuntu)에 Docker 설치

### On EC2
```bash
$ sudo apt update && sudo apt upgrade

# Docker 설치에 필요한 필수 패키지 설치
$ sudo apt-get install apt-transport-https ca-certificates curl gnupg-agent software-properties-common

# Docker GPG Key 등록 (apt-key 는 Ubuntu 22.04+ 에서 제거되어 keyrings 방식 사용)
$ sudo install -m 0755 -d /etc/apt/keyrings
$ curl -fsSL https://download.docker.com/linux/ubuntu/gpg \
  | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
$ sudo chmod a+r /etc/apt/keyrings/docker.gpg

# Docker Repository 등록 (signed-by 로 위 키 지정)
$ echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" \
  | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

$ sudo apt-get update && sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# Docker 설치 확인
$ docker -v

# Docker 활성화 & 실행
$ sudo systemctl enable docker
$ sudo service docker start
$ sudo service docker status 
```

### Docker Compose (V2)
Compose V2는 위 `docker-compose-plugin` 설치로 함께 들어오며, `docker compose`(하이픈 없는 서브커맨드) 형태로 사용한다. (구 `docker-compose` 바이너리 직접 설치 방식은 더 이상 권장되지 않는다.)
```bash
# 설치 확인
$ docker compose version
```
