## Docker

### Docker란?
컨테이너를 사용하여 응용프로그램을 더 쉽게 만들고 배포하고 실행할 수 있도록 설계된 도구이며, 컨테이너 기반의 오픈소스 가상화 플랫폼 생태계

### 컨테이너란?
- 컨테이너는 코드와 모든 종속성을 패키지화하여 응용 프로그램이 한 컴퓨팅 환경에서 다른 컴퓨팅 환경으로
  빠르고 안정적으로 실행되도록 하는 소프트웨어의 표준 단위
- 간단하고 편리하게 프로그램을 실행 시켜주는 것
- 컨테이너는 컨테이너 안의 다양한 프로그램, 실행환경을 컨테이너로 추상화하고 동일한 인터페이스를 제공하여
  프로그램의 배포 및 관리를 단순하게 해준다.

#### 컨테이너의 생명주기
1. 생성(Create)
   - 도커 이미지를 기반으로 컨테이너를 생성하는 명령어: **docker create <이미지 이름>**
2. 준비(Pending)
   - 컨테이너 실행 전 준비 단계를 거치는 명령어는 따로 존재하지 않는다.
3. 실행(Running)
   - 컨테이너를 실행하는 명령어: **docker start <컨테이너 이름 또는 ID>**
   - 실행 중인 컨테이너의 상태를 확인하는 명령어: **docker ps**
4. 일시 중지(Paused)
   - 실행 중인 컨테이너를 일시 중지하는 명령어: **docker pause <컨테이너 이름 또는 ID>**
   - 일시 중지된 컨테이너를 다시 실행하는 명령어: **docker unpause <컨테이너 이름 또는 ID>**
5. 중지(Stopped)
   - 실행 중인 컨테이너를 중지하는 명령어: **docker stop <컨테이너 이름 또는 ID>**
     -  stop 명령어는 컨테이너에 SIGTERM 신호를 보내서 그 컨테이너를 graceful하게 종료시키려고 시도
   - 실행 중인 컨테이너를 강제종료하는 명령어: **docker kill <컨테이너 이름 또는 ID>**
     - kill 명령어는 바로 SIGKILL 신호를 보내 컨테이너를 강제로 종료
   - 중지된 컨테이너를 다시 시작하는 명령어: **docker start <컨테이너 이름 또는 ID>**
6. 삭제(Removed)
   - 중지된 컨테이너를 삭제하는 명령어: **docker rm <컨테이너 이름 또는 ID>**
   - 도커 이미지를 삭제하는 명령어: **docker rmi <이미지 이름>**

### 이미지란?
- 코드, 런타임, 시스템 도구, 시스템 라이브러리 및 설정과 같은 응용 프로그램을 실행하는 데 필요한
  모든 것을 포함하는 가볍고 독립적이며 실행 가능한 소프트웨어 패키지
- 이미지를 이용해서 컨테이너를 생성하고, 컨테이너가 실행되면 어플리케이션이 컨테이너 안에서 돌아간다.
  따라서 컨테이너를 이미지의 인스턴스라고 볼 수 있다.

### Dockerfile이란?
이미지를 만들기 위한 설정파일, 컨테이너가 어떻게 행동해야 하는지에 대한 설정들을 정의하는것

#### 파일 생성 순서
   1. 베이스 이미지 명시 (파일 스냅샷에 해당)
   2. 추가적으로 필요한 파일을 다운 받기 위한 몇가지 명령어 명시(파일 스냅샷에 해당)
   3. 컨테이너 시작시 실행 될 명령어 명시

### 베이스 이미지란?
   - 도커의 이미지는 여러개의 레이어로 구성되어 있는데 그중에서 이미지의 기반의 되는 부분이 베이스 이미지이다.

#### Dockerfile
> Document: https://docs.docker.com/engine/reference/builder/
```dockerfile
# Docker 이미지를 빌드시 빌드 타임에 전달되는 변수를 정의하는 데 사용
# <VAR NAME>: ARG로 정의할 변수의 이름
# <DEALUT>: 선택적으로 정의할 수 있으며, 변수에 기본값을 할당합
# ! ARG 명령어는 FROM 명령어 이전에 선언되어댜 한다.
ARG <VAR NAME>=<DEALUT>

# FROM: 새로운 이미지를 생성하기 위해 기반 이미지를 가져오는 데 사용한다.
# image: 기반 이미지의 이름
# tag: 명시하지 않을시 가장 최신(latest)형으로 자동 적용
FROM <image>:<tag>

# RUN: 이미지를 빌드할 때 컨테이너 내부에서 실행할 명령어를 지정한다.
# 명령에는 소프트웨어 설치, 파일 및 디렉터리 만들기, 환경 구성 만들기와 같은 항목이 포함될 수 있다.
RUN <command>

# COPY: 호스트 파일 시스템에서 파일을 복사하여 Docker 이미지 내부로 복사한다.
COPY <source> <destination>

# ! 원본 또는 대상에 공백이 포함된 경우 다음 예제처럼 경로를 대괄호와 큰따옴표를 묶는다.
COPY ["<source>", "<destination>"]

# ADD: 호스트 파일 시스템에서 파일을 복사하여 Docker 이미지 내부로 복사한다.
# - COPY와 유사하게 동작하지만, ADD는 COPY보다 더 다양한 기능을 제공한다.
# - <source>가 파일 뿐 아니라 URL이나 압축 파일도 가능
#   - URL(원격저장소) 경로 파일 다운로드 
#   - 압축파일의 경우 자동으로 압축 해제 후 복사
# * <source>가 디렉토리일 경우 해당 디렉토리 전체가 <destination> 경로에 복사된다.
# ! 주의: URL에서 파일을 다운로드할 때, 보안상의 이유로 ADD 대신 curl 등의 명령을 사용하는 것이 권장
# ! 주의: 압축 파일을 자동으로 해제하는 기능이 있어서 의도치 않게 이미지 크기가 커질 수 있다.
ADD <source> <destination>

# ! 원본 또는 대상에 공백이 포함된 경우 다음 예제처럼 경로를 대괄호와 큰따옴표를 묶는다.
ADD ["<source>", "<destination>"]

# 이미지 내부의 작업 디렉토리를 설정한다.
# RUN, CMD 등 다른 Dockerfile 명령에 대한 작업 디렉터리를 설정하고 컨테이너 이미지의 실행 중인 인스턴스에 대한 작업 디렉터리도 설정
# - Working Directory를 설정하는 이유
#   - 베이스 이미지에 이미 같은 폴더명 혹은 파일명이 있을 가능성
#   - 모든 파일이 한 디렉토리에 있는 경우 정리 X
#   - 모든 어플리케이션을 위한 소스들은 Working Directory를 따로 만들어 보관
WORKDIR <path to working directory>

# 빌드 과정에서 사용될 환경 변수를 설정
ENV <key>=<value>
 
# Dockerfile 이스케이프 문자 : '\', '`' 
# 여러줄에서 계속되는 RUN 명령어 예시
RUN powershell.exe -Command \
     $ErrorActionPreference = 'Stop'; 

# Docker 이미지에서 실행되는 기본 실행 파일을 지정하는 데 사용한다.
# CMD와 유사하게 동작하지만, CMD가 컨테이너가 실행될 때 실행되는 기본 명령어의 인수를 정의하는 데 사용되는 반면, 
# ENTRYPOINT는 이미지에서 실행될 기본 실행 파일 자체를 정의한다.
# ! 첫 번째 요소는 실행할 명령어이고, 나머지 요소는 해당 명령어에 전달할 매개 변수이다.
ENTRYPOINT ["executable", "param1", "param2"]

# CMD: 컨테이너가 시작될 때 실행할 명령어를 지정
# Dockerfile에서 CMD 명령을 여러 개 지정하는 경우 마지막 명령만 평가된다.
CMD ["executable","param1","param2"]
```

### Dockerfile을 이용해 도커 이미지 만들기
```bash
$ docker build ./ 
$ docker build .

# 위의 명령어는 해당 디렉토리 내에서 Dockerfile을 찾아서 도커 클라이언트에게 전달
# ~ '.', './' 경로 표현식은 모두 현재 디렉토리를 의미한다.
# 정의된 내용을 도커 클라이언트에 전달하여 도커 서버가 인식 이미지가 생성된다.
```

### 자주 사용되는 명령어
```bash
# * 현재 실행 중인 컨테이너의 목록 출력
$ docker ps [옵션]
# [옵션]: 컨테이너 목록 출력 시 필요한 추가적인 옵션을 지정 : 자주 사용되는 옵션은 -a, -q, -f 등이 있다.
#   -a: 중지된 컨테이너를 포함하여 모든 컨테이너의 목록을 출력
#   -q: 컨테이너 ID만 출력
#   -f: 지정한 조건에 해당하는 컨테이너만 출력

# * Docker 호스트에 저장된 이미지 목록 출력
$ docker images [옵션] [저장소[:태그]]
# [옵션]: 옵션을 추가 : -a 옵션을 사용하면 모든 이미지를 출력한다.
# 저장소: 이미지의 리포지토리 이름
# 태그: 이미지의 태그 이름

# * 도커 이미지를 기반으로 새로운 컨테이너를 생성(create)하고, 해당 컨테이너를 실행(start)
$ docker run [옵션] <이미지 이름> [명령어]
# [옵션]: 컨테이너 생성과 실행 시 필요한 추가적인 옵션을 지정 : 자주 사용되는 옵션은 -d, -p, -v, -e 등이 있다.
#   -d: 컨테이너를 백그라운드에서 실행
#   -p: 호스트와 컨테이너 간의 포트 포워딩을 지정
#   -v: 호스트와 컨테이너 간의 디렉토리 공유를 지정
#   -e: 컨테이너 내부에 환경 변수를 설정
# <이미지 이름>: 컨테이너를 생성할 때 사용할 도커 이미지 이름
# [명령어]: 컨테이너를 실행할 때 실행할 명령어를 지정 (기본값: 이미지 내부에 설정된 명령어가 실행된다.)

# * Docker 이미지를 기반으로 새로운 컨테이너를 생성 (아직 시작되지 않은 상태)
$ docker create [옵션] <이미지 이름> [명령어] [ARG...]
# [옵션]: 옵션을 설정 : 대표적으로 -t, -i, -v 등이 있다.
#   -a, --attach: 컨테이너에서 출력되는 로그를 터미널에 출력
#   -i, --interactive: 컨테이너와 터미널 간의 입력 상호 작용을 가능하게 한다.
#   -t, --tty: 가상 터미널 환경을 생성하여 컨테이너 내부에 쉘을 실행할 수 있도록 한다.
#   -v, --volume: 호스트와 컨테이너 간의 디렉토리 공유를 설정
#   -p, --publish: 호스트와 컨테이너 간의 포트 매핑을 설정
#   -e, --env: 컨테이너 내부의 환경 변수를 설정
#   --name: 컨테이너의 이름을 설정
#   --network: 컨테이너가 속할 네트워크를 설정
#   --restart: 컨테이너가 종료되었을 때 자동으로 재시작 여부를 설정
# <이미지 이름>: 컨테이너를 생성할 Docker 이미지의 이름입니다.
# [명령어]: 컨테이너를 시작할 때 실행할 명령어입니다. 이미지에 CMD 명령어가 지정되어 있으면 생략할 수 있습니다.
# [ARG...]: 명령어 실행 시 전달할 인자

# * 컨테이너 시작 또는 중지된 컨테이너를 다시 시작
$ docker start [옵션] <컨테이너 이름 또는 ID> [컨테이너...]
# [옵션]: 옵션을 설정 : 대표적으로 -a, -i, -d 등이 있다.
#   -a, --attach: 컨테이너에서 출력되는 로그를 터미널에 출력
#   -i, --interactive: 컨테이너와 터미널 간의 입력 상호 작용을 가능하게 한다.
#   -d, --detach: 컨테이너를 백그라운드에서 실행
#   -p, --publish: 호스트와 컨테이너 간의 포트 매핑을 설정
#   -e, --env: 컨테이너 내부의 환경 변수를 설정
# <컨테이너 이름 또는 ID>: 시작할 컨테이너의 이름이나 ID

# * 실행 중인 컨테이너를 중지
$ docker stop [옵션] <컨테이너 이름 또는 ID> [컨테이너...]
# [옵션]: 옵션을 설정 : 대표적으로 -t 등이 있다.
#   -t, --time: 컨테이너가 종료될 때까지 대기하는 시간을 설정 : 기본값 10초
# <컨테이너 이름 또는 ID>: 시작할 컨테이너의 이름이나 ID

# * 컨테이너를 강제로 중지
$ docker kill [옵션] <컨테이너 이름 또는 ID> [컨테이너...]
# [옵션]: 옵션을 설정 : 대표적으로 -s 등이 있다.
#   -s, --signal: 컨테이너에 보낼 시그널을 설정 : 기본값 SIGKILL
# <컨테이너 이름 또는 ID>: 시작할 컨테이너의 이름이나 ID

# * Docker 이미지를 생성
$ docker build [옵션] <경로>
# [옵션]: 이미지 빌드에 필요한 추가적인 옵션을 지정 : 자주 사용되는 옵션으로는 -t, -f 등이 있다.
#   -t <이미지명:태그>: 빌드한 이미지에 이름과 태그를 지정
#   -f <Dockerfile 경로>: 빌드에 사용할 Dockerfile 경로를 지정 기본값은 ./Dockerfile 이다.
# <경로>: Dockerfile이 위치한 경로를 지정

# * 로컬 환경에서 빌드한 Docker 이미지를 Docker Hub나 다른 레지스트리에 업로드
$ docker push <이미지명:태그>
# <이미지명:태그>: 업로드할 Docker 이미지의 이름과 태그를 지정

# * Docker Hub나 다른 레지스트리에서 Docker 이미지를 다운로드
$ docker pull <이미지명:태그>
# <이미지명:태그>: 다운로드할 Docker 이미지의 이름과 태그를 지정

# * 실행 중인 Docker 컨테이너 내에서 명령을 실행
$ docker exec [옵션] <컨테이너 이름 또는 ID> [명령어] [ARG...]
# [옵션]: 옵션 추가 -it 옵션을 사용하면 실행 중인 컨테이너에 대화형으로 연결할 수 있다.
# <컨테이너 이름 또는 ID>: 명령을 실행할 Docker 컨테이너의 이름 또는 ID
# [명령어]: 컨테이너에서 실행할 명령
# [ARG...]: 명령에 전달할 추가 인수

# * 실행된 컨테이너를 삭제하거나, 종료된 컨테이너를 완전히 제거
$ docker rm [옵션] <컨테이너 이름 또는 ID> [컨테이너...]
# [옵션]: 옵션을 설정 : 대표적으로 -f, -v 등이 있다.
#   -f: 실행 중인 컨테이너를 강제로 종료하고 삭제
#   -v: 컨테이너와 연결된 볼륨도 함께 삭제
# <컨테이너 이름 또는 ID>: 명령을 실행할 Docker 컨테이너의 이름 또는 ID

# * 이미지를 삭제
# ! 이미지를 삭제할 때는 해당 이미지를 사용하는 모든 컨테이너를 먼저 삭제해야된다.
$ docker rmi [옵션] <이미지명> [이미지명...]
# [옵션]: 옵션을 설정 : 대표적으로 -f, -a 등이 있다.
#   -f, --force: 이미지를 강제로 삭제 
#       이미지를 사용하는 모든 컨테이너를 자동으로 중지하고 삭제한다.
#   --no-prune: 태그 없는 이미지만 삭제 
#       이 옵션을 사용하면 이미지를 삭제하기 전에 해당 이미지가 사용 중인지 확인하지 않는다.
#   -a, --all: 모든 이미지를 삭제 
#       docker rmi 명령어를 실행할 때 IMAGE 인수를 지정하지 않으면 기본적으로 모든 이미지를 삭제한다.
#   --filter: 지정된 조건에 따라 삭제할 이미지를 필터링한다.
# <이미지명>: 이미지의 이름 또는 ID
```

### 컨테이너 > 이미지 전체 삭제
```bash
# 모든 컨테이너를 중지
$ sudo docker stop $(sudo docker ps -a -q)

# 모든 컨테이너를 삭제
$ sudo docker rm $(sudo docker ps -a -q)

# 모든 이미지 삭제
$ sudo docker rmi $(sudo docker images -q)

# <none> 생성된 이미지 삭제 (실행중인 이미지 포함)
$ docker image prune -a
```

#### 이미지에 이름 설정
```bash
$ docker build -t new/image:latest ./
# ~ t 저장소 ID(docker hub ID) / 이미지명 : 버전
$ docker run -it new/image
```

#### 실행 중인 컨테이너에 명령 전달
```bash
$ docker exec [옵션] <컨테이너 이름> <실행할 명령어>

# [옵션]: 명령어 실행에 필요한 추가적인 옵션을 지정 (자주 사용되는 옵션은 -i와 -t)
#   -i: 명령어 실행 시 표준 입력(stdin)을 유지
#   -t: 명령어 실행 시 TTY(터미널)을 사용
# <컨테이너 이름>: 명령어를 실행할 대상이 되는 컨테이너의 이름이나 ID
# <실행할 명령어>: 실행할 명령어를 입력.
```

### Volume
컨테이너 안의 파일 변경 사항을 UnionFS을 통해 관리
- COPY는 로컬 리소스를 컨테이너에 복사하는 반면 Volume은 리소스 맵핑을 통해 참조한다.
```bash
# * Docker Volume을 생성
$ docker volume create <volume-name>

# * 컨테이너에서 생성된 볼륨을 사용하려면 --mount 또는 -v 옵션을 사용하여 볼륨을 마운트
$ docker run <option> -v <volume-name>:<container-route> <image-name>

# 예시
$ docker run --name my-redis -d -p 8080:8080 -v my-redis:/data redis
```

#### bind-mount 방식
특정 Volume을 생성하지않고 호스트의 특정 경로의 폴더를 공유하는것
```bash
$ docker run <option> -v <host-route>:<container-route> <image-name>

# 예시
docker run --name my-node -d -p 8080:8080 -v $(pwd):/app/src node:10

# - ubuntu 20.04 terminal 현재경로: $(pwd)
# - window 10 cmd 현재경로: %cd%
```

### compose
다중 컨테이너 애플리케이션을 정의하고 공유하는 목적으로 사용된다.

- 프로젝트 루트에 docker-compose.yml 파일 생성
  - Compose 파일에서 먼저 스키마 버전을 정의시 최신 버전을 사용하는것이 가장 안정적이다.
    > version: https://docs.docker.com/compose/compose-file/compose-versioning/

#### Sample docker.compose.yml
```
version: <version>
services:
  <service-name>:
    image: <image-name>
    command: <command>
    ports:
      - 3000:3000
    working_dir: /dir
    volumes:
      - ./:/dir
```

#### docker-compose 자주 사용되는 명령어
```bash
# * 정의된 내용을 기반으로 이미지를 빌드한다.
$ docker-compose build

# * 이미지가 존재하지 않을 경우 빌드를 진행하고(이미지 존재시 빌드 생략), 컨테이너를 시작한다.
$ docker-compose up

# * 강제로 이미지 빌드하며, 컨테이너 시작한다.
$ docker-compose up --build

# * 이미지 빌드 없이, 컨테이너 시작한다. (이미지가 존재하지 않을시 실패한다.)
$ docker-compose up --no-build

# * detached 모드, 백그라운드 실행한다. (애플리케이션 실행 결과 output 출력 X)
$ docker-compose up -d

# * docker compose 종료한다. 
$ docker-compose down {:container_name}

# down 절차 
# 실행 중인 Docker 컨테이너를 모두 정지
# 정지된 Docker 컨테이너를 모두 삭제
# Docker 네트워크를 삭제
# Docker 볼륨을 삭제
```

### Docker Network
도커 네트워크(Docker Network)는 도커 컨테이너들 간의 네트워크 통신을 위한 가상 네트워크를 말한다.

도커에서는 기본적으로 3가지 종류의 네트워크를 제공한다.

1. bridge network: 
   - 기본적으로 제공되는 네트워크
   - 컨테이너들이 동일한 가상 네트워크에 속하며, 기본적으로 IP 주소가 할당된다.
2. host network 
   - 호스트의 네트워크 인터페이스를 사용하는 네트워크
   - 컨테이너가 호스트와 동일한 네트워크 인터페이스를 사용하기 때문에 IP 주소를 할당하지 않는다.
3. overlay network
   - 여러 호스트 간에 컨테이너를 연결하는 데 사용된다. 
   - 여러 호스트 간에 컨테이너 간에 통신을 위해 오버레이 네트워크를 설정하고, 컨테이너들이 이 네트워크에 속하게 된다.

```bash
# 네트워크 목록
$ docker network ls

# 네트워크 정보 확인
$ docker network inspect <NAME>

# 네트워크 생성
$ docker network create <NETWORK_NAME>

# 컨테이너를 생성할 때 --network 옵션을 사용하여 컨테이너가 속할 네트워크를 지정할 수 있다.
$ docker run --name my-container --network my-network my-image

# 컨테이너끼리 통신할 때는 컨테이너 이름으로 통신한다. 
# Ex) my-container라는 이름의 컨테이너에서 my-other-container라는 이름의 컨테이너로 통신하려면 다음과 같이 입력한다.
$ curl http://my-other-container:8080
```
