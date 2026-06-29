## Kafka (Apache Kafka)

### 정의
Kafka는 대용량 이벤트(event)를 실시간으로 흘려보내고 저장·재처리할 수 있는 **분산 이벤트 스트리밍 플랫폼(distributed event streaming platform)** 이자, 발행-구독(pub-sub) 모델 기반의 **메시지 브로커(message broker)** 다. 발행자(Producer)가 보낸 메시지를 **디스크에 추가 기록(append-only log, 덧붙이기만 하는 로그)** 형태로 저장하고, 소비(consume)된 뒤에도 **일정 기간(retention) 동안 지우지 않고 보관**한다. 덕분에 메시지가 한 번 소비되면 사라지는 전통 큐와 달리, **여러 소비자가 같은 데이터를 각자 읽고**, 필요하면 **과거 시점부터 다시 읽어(replay, 재처리)** 처리할 수 있다.

- Ex: Kafka는 **신문 가판대의 신문 묶음**과 같다. 신문(이벤트)은 발행 순서대로 묶여(로그) 쌓이고, 가판대(브로커)는 정해진 기간 동안 그 묶음을 치우지 않는다. 독자(소비자)는 각자 "내가 어디까지 읽었는지(offset, 읽은 위치 표시)"를 기억해 두고, 자기 속도로 다음 신문을 집어 든다. 한 독자가 5번 신문을 읽어도 신문이 사라지지 않으므로, 다른 독자도 1번부터 다시 읽을 수 있다. 반면 전통 큐는 **읽으면 가져가 버리는 우편함**이라, 한 번 꺼내면 그 편지는 더 이상 거기에 없다.

### 등장 배경 / 왜 필요한가
서비스가 서로를 **직접(동기) 호출**하는 구조는 규모가 커질수록 한계가 명확하다. 주문 서비스가 알림·재고·정산·검색 색인을 직접 호출하면 다음 문제가 생긴다.

- **강한 결합(tight coupling)**: 호출 대상의 주소·API 형식·존재 여부를 전부 알아야 하고, 소비처가 하나 늘 때마다 호출하는 쪽 코드를 고쳐야 한다.
- **장애 전파(cascading failure)**: 동기 호출은 응답을 기다리며 블로킹된다. 하위 서비스 하나가 느려지면 호출 사슬을 타고 상위까지 함께 느려지거나 멈춘다.
- **대용량 실시간 처리의 한계**: 사용자 행동 로그·클릭 스트림·IoT 센서처럼 초당 수십만 건씩 쏟아지는 이벤트를, 동기 요청-응답으로 받아 처리하면 트래픽 급증 시 그대로 하위로 부하가 전가되어 무너진다.
- **데이터 파이프라인의 폭발적 결합**: 데이터 소스 M개와 목적지 N개를 직접 연결하면 M×N개의 연동이 생기고, 하나 바뀔 때마다 연쇄적으로 깨진다.

해법은 서비스 사이에 **메시지 브로커라는 완충 지대(buffer)** 를 두고 **비동기 메시징(asynchronous messaging)** 으로 결합도를 낮추는 것이다. 발행자는 이벤트만 던지고 즉시 자기 일을 끝내고, 소비자는 자기 속도로 나중에 처리한다. 한쪽이 잠시 죽어도 메시지는 브로커에 쌓여(버퍼링) 있다가 회복되면 처리되므로 장애가 격리된다. Kafka는 여기에 더해 **로그를 보존**하므로, M×N 연동을 "모두가 Kafka에 쓰고 모두가 Kafka에서 읽는" **M+N 구조**로 단순화하고, 새 소비처가 과거 데이터까지 거슬러 처리할 수 있게 한다. 이 맥락은 [MSA](../cs/architecture/msa.md)의 비동기 통신·데이터 파이프라인 논의와 직접 맞닿아 있다.

### 핵심 개념 / 구성요소
Kafka는 "누가 보내고(Producer), 어디에 어떻게 저장되며(Broker·Topic·Partition), 누가 어떻게 나눠 읽는가(Consumer Group·Offset)"의 축으로 구성된다.

- **Producer(프로듀서, 발행자)**: 메시지를 특정 토픽으로 전송하는 주체. 메시지의 **키(key)** 에 따라 어느 파티션에 들어갈지가 결정된다.
- **Consumer(컨슈머, 소비자)**: 토픽을 구독해 메시지를 읽는 주체. 자신이 어디까지 읽었는지(offset)를 관리한다.
- **Broker(브로커)**: Kafka 서버 한 대(노드). 여러 브로커가 모여 **클러스터(cluster)** 를 이루고, 파티션을 나눠 보관한다.
- **Topic(토픽)**: 메시지가 분류되어 담기는 논리적 카테고리(채널). `order-created`처럼 의미 단위로 이름 짓는다.
- **Partition(파티션)**: 토픽을 물리적으로 쪼갠 단위. 각 파티션은 **순서가 보장되는 독립 로그**이며, 병렬 처리·확장의 기본 단위다.
- **Offset(오프셋)**: 파티션 안에서 각 메시지에 매겨지는 **순번(0, 1, 2 …)**. 소비자는 "내가 어디까지 읽었다"는 위치를 offset으로 기록한다.
- **Consumer Group(컨슈머 그룹)**: 같은 `group.id`를 공유하는 소비자들의 묶음. 그룹 내에서 **파티션을 나눠 가져** 병렬로 처리하고, 그룹 단위로 offset을 관리한다.
- **Replication(복제, Leader/Follower)**: 각 파티션을 여러 브로커에 복제해 내구성을 확보한다. 한 복제본이 **리더(Leader)** 로서 읽기/쓰기를 받고, 나머지 **팔로워(Follower)** 가 리더를 따라 복제한다. 리더 장애 시 팔로워 하나가 새 리더로 승격된다.
- **ISR(In-Sync Replicas, 동기화된 복제본 집합)**: 리더와 충분히 동기화된 복제본의 목록. 리더가 죽으면 ISR 안에서만 새 리더를 뽑아 **데이터 유실 없이** 장애를 복구한다.
- **Retention(보존 기간)**: 메시지를 디스크에 유지하는 정책. 시간 기준(예: 7일)이나 크기 기준으로 설정하며, 이 기간 안에서는 재처리가 가능하다.
- **ZooKeeper → KRaft 전환**: 과거 Kafka는 메타데이터·리더 선출을 외부 코디네이터인 **ZooKeeper**에 의존했다. 최신 버전은 Kafka 자체에 합의 프로토콜(Raft)을 내장한 **KRaft(KafkaRaft) 모드**로 전환해, ZooKeeper 없이 Kafka만으로 운영하며 구성과 확장을 단순화했다.

```
                   ┌──────────────────────── Topic: order-created ────────────────────────┐
                   │                                                                       │
   ┌────────────┐  │   Partition 0 :  [off0][off1][off2][off3] ... ──▶ append(추가 기록)   │
   │  Producer  │  │   Partition 1 :  [off0][off1][off2] ...                               │
   │ (발행자)    │──┼─▶ Partition 2 :  [off0][off1][off2][off3][off4] ...                  │
   │ key→partition │   (각 파티션 = 순서가 보장되는 독립 append-only 로그)                  │
   └────────────┘  └───────────────────────────────────────────────────────────────────┘
                          │  P0          │  P1                 │  P2
                          ▼              ▼                     ▼
                   ┌──────────────────── Consumer Group: stock-service ───────────────────┐
                   │   Consumer A ◀─ P0      Consumer B ◀─ P1      Consumer C ◀─ P2        │
                   │   (그룹 내에서 파티션을 나눠 가져 병렬 소비 / offset은 그룹이 관리)     │
                   └───────────────────────────────────────────────────────────────────────┘

   * 같은 토픽을 "다른 그룹"(예: search-indexer)이 또 구독하면, 그 그룹은 처음(off0)부터
     독립적으로 같은 데이터를 다시 읽는다 → 다중 소비자 + 재처리.
   * Replication: 위 각 파티션은 다른 브로커에 Leader/Follower 복제본으로 분산 저장된다.
```

#### 파티션과 순서 보장
Kafka의 순서 보장은 **토픽 전체가 아니라 파티션 단위로만** 이루어진다. 하나의 파티션은 append-only 로그이므로 그 안에서는 들어온 순서대로 offset이 매겨지고 그대로 읽힌다. 하지만 **서로 다른 파티션 사이에는 순서가 보장되지 않는다.** 따라서 "같은 주문의 이벤트는 반드시 순서대로 처리돼야 한다"면, **파티션 키(partition key)** 를 그 주문 ID로 지정해 **같은 키는 항상 같은 파티션으로 라우팅**되게 해야 한다.

```
key=order:1001 ──▶ hash(key) % 파티션수 ──▶ 항상 Partition 1 로 고정
key=order:2002 ──▶ hash(key) % 파티션수 ──▶ 항상 Partition 0 로 고정

   같은 주문(order:1001)의 Created → Paid → Shipped 이벤트는
   모두 Partition 1 에 차례로 쌓이므로 순서가 보장된다.
   서로 다른 주문은 다른 파티션에 흩어져도 무방하다(상호 순서 무의미).
```

- 키를 지정하지 않으면 라운드로빈/스티키 방식으로 분산되어 **순서가 흐트러질 수 있다.**
- "전역(global) 순서"가 정말 필요하면 파티션을 1개로 두는 수밖에 없는데, 이는 병렬도를 1로 떨어뜨리므로 **처리량과 순서는 트레이드오프** 관계다. 대부분은 "키 단위 순서"로 충분하다.

#### 컨슈머 그룹과 병렬 처리
하나의 컨슈머 그룹 안에서는 **파티션 하나를 그룹 내 컨슈머 하나가 전담**한다. 따라서 **파티션 수가 그 그룹의 병렬 처리 상한**이 된다. 파티션이 3개면 컨슈머를 3개까지 늘려 동시에 처리할 수 있고, 4번째 컨슈머를 추가해도 할당받을 파티션이 없어 **놀게(idle)** 된다.

```
파티션 3개 + 컨슈머 2개   →  C1:[P0,P1]  C2:[P2]            (불균형하지만 모두 일함)
파티션 3개 + 컨슈머 3개   →  C1:[P0]  C2:[P1]  C3:[P2]      (이상적: 1:1)
파티션 3개 + 컨슈머 4개   →  C1:[P0]  C2:[P1]  C3:[P2]  C4:(idle, 놀고 있음)
```

- **리밸런싱(rebalancing)**: 그룹에 컨슈머가 추가/제거되거나 장애가 나면, Kafka가 **파티션 할당을 다시 분배**한다. 이 과정에서 잠깐 소비가 멈출 수 있고(stop-the-world), 빈번하면 처리가 출렁인다.
- 처리량을 늘리고 싶다면 **파티션 수를 먼저 충분히 잡아 두는** 것이 중요하다. 운영 중 파티션 수를 늘릴 수는 있으나, 키 기반 파티셔닝의 분배가 바뀌어 순서 보장이 깨질 수 있으므로 신중해야 한다.

#### Pull 기반 / 로그 보존 / 재처리
Kafka는 브로커가 소비자에게 메시지를 밀어 넣는(push) 방식이 아니라, **소비자가 직접 가져가는(pull) 방식**이다. 소비자는 자신이 어디까지 읽었는지를 **offset으로 직접 관리**하며, 자기 처리 속도에 맞춰 다음 메시지를 당겨 온다.

- **Pull 기반의 이점**: 소비자가 감당할 수 있는 만큼만 가져가므로, 브로커가 소비자를 과부하시키지 않는다(소비자 주도의 흐름 제어, back-pressure).
- **로그 보존(retention)**: 메시지는 소비된 뒤에도 보존 기간 동안 디스크에 남는다. offset은 "어디까지 읽었다"는 **읽기 위치 표시일 뿐**, 메시지를 삭제하는 행위가 아니다.
- **재처리(replay)**: 소비자가 offset을 과거로 되돌리면(seek), 보존 기간 안의 데이터를 **처음부터 다시 읽어** 재처리할 수 있다. 새 소비 로직을 배포하고 과거 데이터로 검증하거나, 버그로 잘못 처리한 구간을 다시 돌릴 때 핵심이다. 이벤트 소싱(Event Sourcing)·데이터 재적재의 토대가 된다.

### 전달 보장(Delivery Semantics)
메시지가 "몇 번 전달·처리되는가"에 대한 보장 수준은 세 가지로 나뉜다. 강한 보장일수록 중복·유실은 줄지만 비용(지연·복잡도)이 커진다.

| 구분 | 의미 | 중복 | 유실 | 비용 | 적합한 경우 |
| --- | --- | --- | --- | --- | --- |
| at-most-once(최대 한 번) | 보내고 확인하지 않음 | 없음 | **있을 수 있음** | 가장 낮음 | 일부 유실이 허용되는 메트릭·로그 샘플링 |
| at-least-once(최소 한 번) | 확인될 때까지 재전송 | **있을 수 있음** | 없음 | 중간 | 대부분의 업무(소비자를 멱등하게 설계) |
| exactly-once(정확히 한 번) | 중복도 유실도 없음 | 없음 | 없음 | 가장 높음 | 정산·금액 합산 등 중복이 치명적인 처리 |

- **기본은 at-least-once**다. 브로커는 ack를 받지 못하면 재전송하므로, **같은 메시지가 두 번 이상 도착할 수 있다.** 따라서 소비자는 처리 여부를 이벤트 ID로 기록해 **멱등(idempotent)** 하게 짜는 것이 사실상 필수다.
- **멱등 프로듀서(idempotent producer)**: 프로듀서가 보낸 메시지에 시퀀스 번호를 붙여, 재전송으로 인한 **브로커 측 중복 저장**을 막는다(`enable.idempotence=true`). 같은 파티션 내 중복 쓰기를 제거한다.
- **트랜잭션(transaction)**: 여러 파티션·여러 토픽에 대한 쓰기와 소비 offset 커밋을 **하나의 원자 단위**로 묶는다. 멱등 프로듀서 + 트랜잭션 + 트랜잭션 인지 소비자(`read_committed`)를 결합하면, "소비 → 처리 → 발행"의 한 사이클에서 **exactly-once 처리(EOS, Exactly-Once Semantics)** 를 달성할 수 있다. 다만 처리량 저하와 운영 복잡도라는 대가가 따른다.

### Kafka vs 전통 메시지 큐(RabbitMQ)
같은 "비동기 메시징"이라도 Kafka(로그 기반)와 RabbitMQ(전통 큐)는 설계 철학이 다르다.

| 항목 | Apache Kafka (로그 기반) | RabbitMQ (전통 메시지 큐) |
| --- | --- | --- |
| 저장 모델 | 토픽 파티션에 **추가 기록(append-only log)**, 일정 기간 보존 | 큐에 적재, **소비(ack) 시 제거가 기본** |
| 소비 후 데이터 | 보존 기간 동안 **유지** → 재처리·다중 소비 가능 | **사라짐** → 재처리는 별도 구성 필요 |
| 다중 소비자 | 여러 컨슈머 그룹이 같은 토픽을 **각자 처음부터** 읽음 | 기본은 한 큐의 메시지를 소비자들이 **나눠** 가짐(경쟁 소비) |
| 순서 보장 | **파티션 단위**(같은 키 → 같은 파티션) | 단일 큐 내 순서(소비자 여럿이면 흐트러질 수 있음) |
| 처리량 | 초당 수십만~수백만 건 규모의 대용량에 강함 | Kafka보다 낮으나 일반 업무에는 충분 |
| 라우팅 | 단순(토픽/파티션 중심) | 풍부(direct/topic/fanout/headers 익스체인지로 유연) |
| 위치 추적 | **소비자가 offset 관리**(되돌려 재처리 가능) | 브로커가 미확인 메시지 관리(되감기 개념 없음) |
| 주 용도 | 이벤트 스트리밍, 로그 수집, 대규모 파이프라인, 이벤트 소싱 | 작업 분배(task queue), RPC, 복잡한 라우팅 업무 메시징 |

- 정리하면, **대용량 스트림을 흘려보내고 나중에 다시 읽어야 하면 Kafka**, **세밀한 라우팅으로 작업을 나눠 주고 처리되면 지우는 큐가 맞으면 RabbitMQ**가 어울린다.
- 단순 실시간 알림(broadcast)만 필요하고 보존·재처리가 불필요하다면 [Redis](../redis/redis.md)의 Pub/Sub도 선택지다. 다만 Redis Pub/Sub은 **메시지를 저장하지 않아**(구독자가 없을 때 보낸 메시지는 사라짐) 내구성·재처리가 필요한 곳에는 부적합하다.

### 장점 / 단점(트레이드오프)
- 장점
  - **고처리량(high throughput)**: 순차 디스크 쓰기·배치·제로카피(zero-copy)로 초당 수십만~수백만 건을 처리한다.
  - **내구성(durability)**: 디스크에 기록하고 복제(replication)하므로 브로커 장애에도 데이터를 잃지 않는다.
  - **재처리(replay)**: 보존 기간 내 과거 데이터를 다시 읽어 검증·재적재·버그 복구가 가능하다.
  - **확장성(scalability)**: 파티션을 늘리고 브로커를 추가해 수평 확장한다. 컨슈머 그룹으로 소비도 수평 확장된다.
  - **느슨한 결합·다중 소비**: 발행자는 소비자를 모르고, 여러 그룹이 같은 데이터를 독립적으로 소비한다. → [이벤트 기반 아키텍처](../cs/architecture/event-driven.md)
- 단점(트레이드오프)
  - **운영 복잡도**: 클러스터·복제·파티션·리밸런싱·모니터링을 다뤄야 하며, 학습·운영 비용이 크다.
  - **순서 보장의 한계**: 전역 순서가 아닌 **파티션 단위 순서**만 보장된다(아래 주의점 참고).
  - **지연 vs 처리량 트레이드오프**: 배치를 키우면 처리량은 오르지만 메시지당 지연(latency)이 늘고, 반대도 마찬가지다. 워크로드에 맞춘 튜닝이 필요하다.
  - **exactly-once 비용**: 트랜잭션·멱등 프로듀서로 정확히 한 번을 달성하면 처리량이 떨어지고 구성이 복잡해진다. 가능하면 **멱등 소비자 + at-least-once**로 단순하게 푸는 편이 낫다.

### 실무 예제
Java/Spring 진영에서는 **Spring Kafka**가 `KafkaTemplate`(발행)과 `@KafkaListener`(구독)로 Kafka 연동을 표준화한다.

Ex: `application.yml` — 접속 정보와 직렬화(serializer)·컨슈머 그룹 설정.

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092        # 접속할 브로커 주소(클러스터의 진입점)
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      acks: all                              # 모든 ISR 복제 확인까지 기다림(유실 최소화)
      properties:
        enable.idempotence: true             # 멱등 프로듀서: 브로커 측 중복 쓰기 방지
    consumer:
      group-id: stock-service                # 컨슈머 그룹 ID(offset 관리 단위)
      auto-offset-reset: earliest            # offset 없을 때 가장 처음부터 읽기
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "com.example.event"  # 역직렬화 허용 패키지
```

Ex: Producer — 주문 저장 후 `KafkaTemplate`으로 이벤트 발행. 키를 주문 ID로 주어 같은 주문은 같은 파티션으로 보낸다(순서 보장).

```java
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Transactional
    public Long createOrder(OrderRequest request) {
        Order order = orderRepository.save(Order.from(request)); // 본 처리

        // key = orderId → 같은 주문의 이벤트는 항상 같은 파티션 → 파티션 단위 순서 보장
        kafkaTemplate.send(
            "order-created",                       // 토픽
            String.valueOf(order.getId()),         // 파티션 키
            new OrderCreatedEvent(order.getId(), order.getProductId(), order.getQuantity())
        );
        return order.getId();
    }
}
```

Ex: Consumer — `@KafkaListener`로 토픽을 구독. **중복 수신(at-least-once)에 대비해 멱등하게** 처리한다.

```java
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final StockService stockService;

    // groupId 가 같은 인스턴스끼리 파티션을 나눠 병렬 소비
    @KafkaListener(topics = "order-created", groupId = "stock-service")
    public void consume(OrderCreatedEvent event) {
        // 멱등성: 이미 처리한 주문이면 건너뛴다(같은 이벤트가 두 번 올 수 있으므로)
        if (stockService.alreadyProcessed(event.orderId())) {
            return;
        }
        stockService.decrease(event.productId(), event.quantity());
    }
}
```

해당 빌드 의존성(Gradle)은 다음과 같이 구성한다.

```groovy
dependencies {
    implementation 'org.springframework.kafka:spring-kafka'
}
```

### 주의점 / 안티패턴
- **전역 순서 보장 오해**: Kafka는 토픽 전체 순서를 보장하지 않는다. **순서는 파티션 단위로만** 지켜지므로, 순서가 중요한 단위(주문 ID 등)를 파티션 키로 지정해 같은 파티션에 묶어야 한다. "Kafka에 넣었으니 순서대로 처리되겠지"라는 가정은 틀린다.
- **컨슈머 랙(consumer lag) 방치**: 컨슈머 랙은 "최신 offset과 소비한 offset의 격차", 즉 **밀린 메시지 양**이다. 이를 모니터링하지 않으면 소비가 생산을 못 따라가 지연이 무한정 쌓이다 보존 기간을 넘겨 데이터가 삭제될 수 있다. 랙을 지표로 띄우고 임계치 초과 시 컨슈머를 늘리거나 처리를 최적화해야 한다.
- **멱등성(idempotency) 부재**: 기본 전달 보장은 at-least-once라 **같은 메시지가 두 번 이상 도착**한다. 재고를 그냥 빼면 두 번 차감되는 버그가 난다. 처리 여부를 이벤트 ID로 기록해 이미 처리한 것은 건너뛰도록 **멱등하게** 설계해야 한다.
- **파티션 수 과소/과다 설정**: 너무 적으면 병렬도가 막혀 처리량이 안 나오고, 너무 많으면 브로커의 파일 핸들·메타데이터·리밸런싱 부담이 커지고 종단 지연이 늘어난다. 목표 처리량과 컨슈머 수를 추정해 적정 값을 잡고, 늘리기는 쉬워도 줄이기는 어렵다는 점을 고려한다.
- **리밸런싱 폭풍(rebalancing storm)**: 컨슈머가 잦게 죽고 살아나거나 `max.poll.interval` 안에 처리를 못 끝내면 리밸런싱이 반복되며 소비가 계속 멈춘다. 처리 시간을 짧게 유지하고, 무거운 작업은 분리하며, 세션/폴 타임아웃을 워크로드에 맞게 조정한다.
- **거대한 메시지 페이로드**: 큰 파일·이미지·전체 엔티티를 메시지 본문에 그대로 실으면 브로커 처리량이 떨어지고 메시지 크기 한계에 걸린다. 큰 데이터는 객체 스토리지에 두고 **참조(키·URL)만 이벤트로 전달**하는 클레임 체크(claim-check) 패턴을 쓴다.

> document: https://kafka.apache.org/documentation/
> document: https://docs.spring.io/spring-kafka/reference/
> document: https://kafka.apache.org/intro

### 관련 문서
- [이벤트 기반 아키텍처](../cs/architecture/event-driven.md)
- [MSA](../cs/architecture/msa.md)
- [분산 데이터 일관성](../cs/architecture/data-consistency.md)
- [Redis](../redis/redis.md)
