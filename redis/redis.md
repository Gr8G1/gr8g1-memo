## Redis

### Redis란?
오픈 소스 기반의 인메모리 데이터 구조 스토어이며, NoSQL 데이터베이스 중 하나이다. 
Redis는 key-value 데이터 구조를 지원하며, 디스크나 SSD에 저장하는 대신 인메모리에 데이터를 저장, 이로 인해 매우 빠르고 성능이 뛰어나다.

### 특징
- Key-value 데이터 모델
  - key-value 데이터 모델을 사용한다. 
  - 모든 데이터는 key-value 쌍으로 저장되며, key는 문자열 또는 바이너리 데이터일 수 있다.
- 데이터 타입
  - 다양한 데이터 타입을 지원한다. 
  - 가장 기본적인 데이터 타입은 문자열이고 리스트, 셋, 해시, 정렬된 셋 등의 데이터 타입을 지원한다. 
  - 리스트, 셋, 해시, 정렬된 셋 등의 데이터 타입을 지원합니다.
- 인메모리 데이터 저장
  - 인메모리 데이터 저장 방식을 사용한다. 
  - 데이터를 디스크나 SSD에 저장하는 대신, 메모리에 데이터를 저장하여 매우 빠른 데이터 접근을 가능하게 한다. 
    - 이로 인해 레디스는 데이터를 저장하고 검색하는 데 뛰어난 성능을 보인다.
- 영속성
  - 영속성(persistence)을 지원한다. (데이터를 디스크에 저장하여 데이터의 지속성을 보장한다.) 
  - 백업과 재부팅 후 데이터 복구를 지원한다.
- Pub/sub 메시징
  - pub/sub 메시징 시스템을 지원, 다수의 클라이언트가 메시지를 구독하고 발행할 수 있는 기능을 제공한다. 
  - 메시지 전송, 채팅, 게임 등의 다양한 용도로 활용할 수 있다.
- 클러스터링
  - 여러 대의 서버를 클러스터링하여 사용할 수 있다. (높은 가용성과 확장성을 제공)
- 클라이언트 라이브러리
  - 레디스는 대부분의 프로그래밍 언어에서 사용할 수 있는 클라이언트 라이브러리를 제공한다. (레디스를 쉽게 사용할 수 있다) 

### 기본 명령어
```bash
# SET 명령어: key-value 쌍 저장
SET key value

# GET 명령어: key에 저장된 값 조회
GET key

# DEL 명령어: key에 저장된 값 삭제
DEL key

# INCR 명령어: key에 저장된 값을 1 증가
INCR key

# LPUSH 명령어: 리스트의 왼쪽에 값 추가
LPUSH key value [value ...]

# RPUSH 명령어: 리스트의 오른쪽에 값 추가
RPUSH key value [value ...]

# LPOP 명령어: 리스트의 가장 왼쪽 값 삭제
LPOP key

# RPOP 명령어: 리스트의 가장 오른쪽 값 삭제
RPOP key

# SADD 명령어: 집합(set)에 값 추가
SADD key member [member ...]

# SMEMBERS 명령어: 집합에 있는 모든 멤버를 반환
SMEMBERS key

# HSET 명령어: 해시(hash)에 값 저장
HSET key field value

# HGET 명령어: 해시에서 key와 field에 해당하는 값 조회
HGET key field

# ZADD 명령어: 정렬된 집합(sorted set)에 값 추가
ZADD key score member [score member ...]

# ZRANGE 명령어: 정렬된 집합에서 지정한 범위의 값들을 반환
ZRANGE key start stop [WITHSCORES]
```
> 이 외에도 레디스에는 다양한 명령어와 문법이있다.
