## AWS EC2(Ubuntu)에 Redis 설치

#### EC2 콘솔 SSH 접속 설정
- 인스턴스 생성시 발급된 .pem 키 확인(분실시 재발급)
- Terminal - Local
  ```bash
  $ chmod 400 ~~.pem                                      # root directory .pem 키 관리 또는 .pem 키 특정 저장 위치 권한 설정
  $ ssh -i ".~~.pem" ubuntu@[EC2 인스턴스의 IP or DNS]
      
  # EC2 연결 콘솔 연결 -> SSH 연결 -> 위 내용 확인
  ```

#### redis-server 설치 (EC2 SSH 접속)
```bash
$ sudo apt-get update
$ sudo apt-get upgrade
$ sudo apt-get install redis-server

# Redis 버전 확인
$ redis-server --version

# Redis 연결 확인
$ redis-cli                            
```

#### Redis 기본 설정값 변경 **(복사본 생성 후 설정 변경 추천)**
```bash
$ sudo vi /etc/redis/redis.conf

# 접속 비밀번호
# /requirepass 검색 후 비밀번호 설정

# 접속 가능 ip 주소 변경
# /bind 검색 ip 수정  Ex: 0:0:0:0

# max 메모리
# /maxmemory 검색 후 메모리 변경 
  # Ex: 500mb, 1gb, 2gb - EC2(프리티어: T2:micro) 500mb 이상 사용 자제

# 메모리 사용량 초과시 데이터 교체 알고리즘 선택
# /maxmemory-policy 검색 후 알고리즘 선택
  # ~ 가장 오래된 데이터 순서로 삭제하는 알고리즘 allkeys-lru 선택 (기타 알고리즘 설정 파일목록 참고)
```

#### redis 백그라운드 실행
```bash
# 시스템 실행
$ sudo systemctl start redis-server
# 설정 파일 변경후 서버 상태 확인 필수
$ sudo systemctl [명령어] redis-server

# 데몬 실행 (추천)
$ redis-server --daemonize yes
$ redis-cli
    # 백그라운드 실행 종료
    $ shutdown

# Error 로그 위치
$ vim /var/log/redis/redis-server.log
```

#### redis 외부접속 EC2 포트 개방(인바운드 규칙 추가)
- 사용자 포트 : 6379 개방 추가

#### redis 외부접속
```bash
$ redis-cli -h [EC2 인스턴스 IP or DNS] -p [개별 설정 포트 or 6379] -a [비밀번호]
  ```

#### redis 포트 이슈 발생시 열려있는 포트 확인
```bash
$ sudo netstat -tulpn | grep :6379
```
