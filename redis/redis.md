## Redis

### Redis란?
Redis(REmote DIctionary Server)는 데이터를 디스크가 아닌 **메모리(in-memory)에 저장**하는 **key-value 기반 NoSQL 데이터 스토어**다.
모든 데이터를 RAM에 두기 때문에 디스크 기반 DB와 비교해 읽기/쓰기가 수십~수백 배 빠르며, 단순 캐시를 넘어 다양한 **자료구조(String·List·Set·Sorted Set·Hash 등)** 를 서버 차원에서 제공하는 것이 특징이다.

- Ex: 디스크 기반 RDB가 **창고 깊숙이 보관된 서류를 매번 꺼내 오는 것**이라면, Redis는 **책상 위에 자주 쓰는 서류를 펼쳐 둔 것**과 같다. 손만 뻗으면 닿으니(메모리 접근) 압도적으로 빠르지만, 책상 공간(메모리)은 창고보다 좁고 비싸며, 정전이 나면 책상 위 서류는 날아갈 수 있다(휘발성). 그래서 "전부"가 아니라 "자주 쓰는 것"을 책상에 올려 두는 캐시 전략이 핵심이다.

### 등장 배경 / 왜 필요한가
서비스 규모가 커지면 **같은 데이터를 반복 조회**하는 부하가 디스크 기반 DB에 집중된다.

- **반복 조회 부하**: 인기 상품·공통 코드처럼 거의 바뀌지 않는데 수없이 읽히는 데이터를 매번 DB에서 디스크 I/O로 읽으면 DB가 병목이 된다.
- **세션·임시 상태 보관**: 로그인 세션, 장바구니, 인증 토큰처럼 빠른 접근이 필요하고 만료(TTL)가 있는 데이터를 RDB에 넣는 것은 무겁다.
- **실시간 집계·순위**: 실시간 랭킹, 조회수, 동시 접속자처럼 매우 빈번하게 갱신·정렬되는 값은 RDB로 처리하기 비효율적이다.

해법은 DB 앞단에 **인메모리 캐시 계층**을 두어, 자주 읽히는 데이터를 메모리에서 즉시 돌려주고 DB 부하를 덜어내는 것이다. Redis는 여기에 더해 풍부한 자료구조와 원자적 연산을 제공해, 캐시를 넘어 **세션 저장소·분산 락·랭킹·속도 제한·메시지 큐**까지 폭넓게 쓰인다.

### 핵심 개념 / 자료구조
Redis의 강점은 단순 문자열 저장을 넘어 **용도에 맞는 자료구조를 서버가 직접 제공**한다는 점이다.

| 타입 | 특징 | 대표 용도 |
| --- | --- | --- |
| **String** | 가장 기본. 바이너리 안전, 값 하나 최대 512MB | 캐시 값, 카운터(`INCR`), 분산 락 |
| **List** | 순서가 있는 양방향 리스트(연결 리스트) | 큐/스택, 최근 본 목록, 작업 대기열 |
| **Set** | 중복 없는 비순서 집합. 합/교/차집합 연산 | 태그, 좋아요 유저, 중복 제거 |
| **Sorted Set(ZSet)** | score로 자동 정렬되는 집합 | **랭킹·리더보드**, 우선순위 큐, 시간순 인덱스 |
| **Hash** | 필드-값 맵(객체 한 덩어리 표현) | 객체 캐싱(사용자 프로필 등) |
| **Bitmap** | 비트 단위 배열 | 출석 체크, on/off 플래그 대량 저장 |
| **HyperLogLog** | 적은 메모리로 **근사 카디널리티** 계산 | 순 방문자수(UV) 집계 |
| **Stream** | append-only 로그 + 소비자 그룹 | 이벤트 스트림, 메시지 큐(Kafka 유사) |

- **단일 스레드(single-thread) 모델**: Redis는 명령을 **하나의 메인 스레드가 순차 처리**한다. 그래서 한 명령이 실행되는 동안 다른 명령은 끼어들 수 없어, 별도 락 없이도 **각 명령이 원자적(atomic)** 으로 동작한다(`INCR`, `SETNX` 등). 반대로 **무거운 단일 명령 하나가 전체를 블로킹**하는 약점이 된다(아래 주의점 참고).
- **TTL(Time To Live, 만료 시간)**: 키마다 만료 시간을 줘서 일정 시간 뒤 자동 삭제할 수 있다. 캐시·세션의 수명 관리에 필수다.

### 영속성(Persistence)
메모리는 휘발성이라 재시작·장애 시 데이터가 사라진다. Redis는 이를 보완하기 위해 두 가지 디스크 영속화 방식을 제공한다.

| 구분 | RDB(스냅샷) | AOF(Append Only File) |
| --- | --- | --- |
| 방식 | 특정 시점의 메모리 전체를 **덤프**해 파일로 저장 | 수행된 **쓰기 명령을 순차적으로 기록** |
| 복구 속도 | 빠름(스냅샷을 통째로 로드) | 느림(기록된 명령을 재실행) |
| 데이터 유실 | 스냅샷 간격 사이 변경분 **유실 가능** | fsync 정책에 따라 **유실 거의 없음** |
| 파일 크기 | 작음(압축된 스냅샷) | 큼(명령 누적 → rewrite로 압축 관리) |
| 주 용도 | 백업·재해 복구 | 유실 최소화가 중요한 경우 |

- 실무에서는 **RDB + AOF를 함께 사용**해, RDB로 빠른 복구 지점을 확보하고 AOF로 마지막 변경까지 보존하는 **하이브리드**가 권장된다.
- 단, 영속성을 켜도 Redis는 **주 저장소(System of Record)가 아니라 캐시·보조 저장소**로 쓰는 것이 일반적이다. 원본의 신뢰 보관은 RDB가 맡는다.

### 활용 사례
Redis는 자료구조와 원자성·TTL을 조합해 다양한 패턴에 쓰인다.

- **캐시(Cache-aside / Look-aside)**: 가장 흔한 용도. 앱이 먼저 Redis를 조회하고, 없으면(miss) DB에서 읽어 Redis에 채운다.

```
[Cache-aside 읽기]
  앱 ──① GET key──▶ Redis ──(hit)─────────────▶ 값 즉시 반환
                       └──(miss)──▶ ② DB 조회 ──▶ ③ SET key value EX ttl ──▶ 값 반환
```

- **세션 저장소(Session Store)**: 여러 WAS 인스턴스가 세션을 공유해야 할 때, 세션을 Redis에 두어 어느 인스턴스로 요청이 가도 같은 세션을 본다(무상태 서버 + 중앙 세션).
- **분산 락(Distributed Lock)**: `SET key value NX EX ttl`(원래 `SETNX`)로 "한 번에 하나의 작업만" 실행되도록 보장한다. 여러 노드가 동시에 같은 자원을 건드리는 것을 막는다(다중 노드 환경은 Redlock 알고리즘).
- **랭킹·리더보드(Leaderboard)**: Sorted Set의 score 정렬을 이용해 실시간 순위를 `ZADD`/`ZRANGE`로 즉시 계산한다.
- **속도 제한(Rate Limiting)**: `INCR` + TTL로 "1분당 N회"를 원자적으로 카운트해 API 호출량을 제한한다.
- **메시지 큐 / Pub/Sub**: List(`LPUSH`/`BRPOP`)로 간단한 작업 큐를, Pub/Sub으로 실시간 브로드캐스트를, Stream으로 소비자 그룹 기반 이벤트 처리를 구현한다. 다만 내구성·재처리가 중요하면 [Kafka](../kafka/kafka.md)가 더 적합하다.

### 고가용성 / 확장
단일 Redis는 단일 장애점(SPOF)이 되므로, 운영 환경에서는 다음 구성으로 가용성과 확장성을 확보한다.

- **복제(Replication)**: Primary(쓰기) - Replica(읽기) 구조로 데이터를 복제한다. 읽기 부하를 Replica로 분산하고 데이터 사본을 확보한다.
- **Sentinel(센티넬)**: Primary를 감시하다가 장애가 나면 **Replica를 자동으로 Primary로 승격(failover)** 한다. 자동 장애 조치가 핵심.
- **Cluster(클러스터)**: 데이터를 **16384개 해시 슬롯**으로 나눠 여러 노드에 **샤딩(분산 저장)** 한다. 단일 노드 메모리 한계를 넘어 수평 확장하면서 가용성도 함께 얻는다.

### 장점 / 단점(트레이드오프)
- 장점
  - **압도적 속도**: 인메모리 처리로 마이크로초 단위 응답. 캐시·실시간 처리에 최적.
  - **풍부한 자료구조**: String을 넘어 List·Set·ZSet·Hash·Stream 등을 서버가 직접 제공.
  - **원자적 연산**: 단일 스레드 모델 덕에 락 없이도 `INCR`·`SETNX` 등이 원자적. 분산 락·카운터에 유리.
  - **TTL·만료**: 키 단위 수명 관리로 캐시·세션에 자연스럽다.
- 단점(트레이드오프)
  - **메모리 비용·용량 한계**: 데이터를 RAM에 두므로 디스크보다 비싸고, 다룰 수 있는 총량이 제한된다.
  - **휘발성 위험**: 본질이 인메모리라, 영속성을 적절히 구성하지 않으면 재시작·장애 시 데이터를 잃는다.
  - **단일 스레드 블로킹**: 무거운 명령 하나가 전체 처리를 멈춘다(`KEYS *`, 큰 키 삭제 등).

### 주요 명령어
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

# EXPIRE 명령어: key에 만료 시간(TTL, 초) 설정
EXPIRE key seconds

# SETNX(SET ... NX) : key가 없을 때만 저장(분산 락의 기본)
SET key value NX EX seconds
```
> 이 외에도 레디스에는 다양한 명령어와 문법이 있다.

### 실무 예제
Java/Spring 진영에서는 **Spring Data Redis** 가 캐시 추상화(`@Cacheable`)와 직접 접근(`RedisTemplate`)을 함께 제공한다.

Ex: `application.yml` — 접속 정보 설정.

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      timeout: 2s
  cache:
    type: redis
    redis:
      time-to-live: 600000      # 캐시 기본 TTL 10분(ms)
```

Ex: `@Cacheable` 로 메서드 결과를 캐싱한다. 첫 호출은 DB를 조회해 Redis에 저장하고, 이후 같은 키 호출은 Redis에서 즉시 반환한다(Cache-aside를 프레임워크가 대행).

```java
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // value=캐시 이름, key=캐시 키 → "product::{id}" 로 저장됨
    @Cacheable(value = "product", key = "#id")
    public Product findById(Long id) {
        // 캐시 미스일 때만 이 본문(DB 조회)이 실행된다
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    // 데이터 변경 시 해당 캐시를 무효화해 정합성을 맞춘다
    @CacheEvict(value = "product", key = "#product.id")
    public Product update(Product product) {
        return productRepository.save(product);
    }
}
```

Ex: `RedisTemplate` 으로 직접 다루기(랭킹을 Sorted Set으로 관리).

```java
@Service
@RequiredArgsConstructor
public class RankingService {

    private final RedisTemplate<String, String> redisTemplate;

    public void addScore(String userId, double score) {
        // ZADD: 점수 기준 자동 정렬
        redisTemplate.opsForZSet().add("ranking", userId, score);
    }

    public Set<String> top10() {
        // 점수 높은 순 상위 10명(ZREVRANGE)
        return redisTemplate.opsForZSet().reverseRange("ranking", 0, 9);
    }
}
```

### 주의점 / 안티패턴
- **캐시 스탬피드(Cache Stampede)**: 인기 키의 TTL이 만료되는 순간 다수 요청이 동시에 캐시 미스를 내고 한꺼번에 DB로 몰린다(thundering herd). TTL에 약간의 무작위 분산(jitter)을 주거나, 재계산을 하나의 요청만 하도록 **뮤텍스(분산 락)** 로 막아야 한다.
- **빅키(Big Key)**: 하나의 키에 수십만 개 원소를 가진 거대한 List/Hash/Set을 두면, 단일 스레드 모델에서 그 키를 다루는 명령이 전체를 오래 블로킹한다. 큰 컬렉션은 키를 쪼개 분산한다.
- **TTL 없는 키의 무한 증식**: 세션·캐시성 키에 만료를 주지 않으면 메모리가 계속 차올라 결국 한계에 도달한다(`maxmemory` 정책과 함께 TTL 설계 필수).
- **운영 환경에서 블로킹 명령 금지**: `KEYS *`(전체 키 스캔), `FLUSHALL`(전체 삭제), 큰 키의 일괄 삭제 등은 단일 스레드를 멈춰 전체 지연을 유발한다. 키 탐색은 `SCAN`을 커서 방식으로 사용한다.
- **캐시-DB 정합성**: 캐시는 원본(DB)의 사본이므로, 데이터 변경 시 캐시를 갱신/무효화하지 않으면 **낡은 값(stale)** 을 반환한다. 쓰기 시 캐시를 지우는 무효화(`@CacheEvict`) 전략 등으로 정합성을 관리한다. → [분산 데이터 일관성](../cs/architecture/data-consistency.md)

> document: https://redis.io/docs/latest/
> document: https://redis.io/docs/latest/develop/data-types/
> document: https://docs.spring.io/spring-data/redis/reference/

### 관련 문서
- [Kafka](../kafka/kafka.md)
- [이벤트 기반 아키텍처](../cs/architecture/event-driven.md)
- [MSA](../cs/architecture/msa.md)
- [분산 데이터 일관성](../cs/architecture/data-consistency.md)
