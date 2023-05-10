## Docker Nginx & Certbot

EC2 인스턴스에 배포 완료된 상태에서 최초 1회 .init-letsencrypt.sh 실행 인증서 발급 절차가 필요하다.

### 배포 환경(EC2 인스턴스 내부) SSL/TLS 인증서 발급 자동화 스크립트 다운로드 및 실행

```bash
# init-letsencrypt.sh 다운로드
$ curl -L https://raw.githubusercontent.com/wmnnd/nginx-certbot/master/init-letsencrypt.sh > init-letsencrypt.sh

# 실행 권한 추가
$ sudo chmod +x ./init-letsencrypt.sh
```

#### init-letsencrypt.sh 주요 항목
- domains: 개인 도메인 (**A 레코드 필수**)
- email: 인증 가능한 이메일 주소 작성
- stage: '0' = 1시간 5회 벨리데이션 검사 제약
  - '1' 변경시 테스트 진행 가능
- "### Starting nginx ..." 하위
  - 이미지 설정 변경에 의한 수정이 필요한 경우 --build 옵션 추가 
    > docker-compose up --force-recreate -d nginx > docker-compose up --build --force-recreate -d nginx


### Docker-compose.yml 구성
> 참고: [nginx-and-lets-encrypt-with-docker-in-less-than-5-minutes](https://pentacent.medium.com/nginx-and-lets-encrypt-with-docker-in-less-than-5-minutes-b4b8a60d3a71)

```yaml
# Nginx
# 신규 인증서가 발급됐다면 인증서를 다시 로드하는지 확인
command: "/bin/sh -c 'while :; do sleep 6h & wait $${!}; nginx -s reload; done & nginx -g \"daemon off;\"'"

# Certbot
# Let's Encrypt에서 권장사항 12시간마다 인증서 갱신 확인
entrypoint: "/bin/sh -c 'trap exit TERM; while :; do certbot renew; sleep 12h & wait $${!}; done;'"
```

```bash
# Docker 실행중인 컨테이너의 적용된 내용 확인 
$ sudo docker container inspect <컨테이너 이름>                 # :JSON 출력
```
