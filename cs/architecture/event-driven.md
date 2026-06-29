## 이벤트 기반 아키텍처 (Event-Driven Architecture, EDA)

### 정의
이벤트 기반 아키텍처는 서비스끼리 서로를 **직접 호출**하지 않고, "무슨 일이 일어났다"는 사실을 **이벤트(Event)** 로 발행(publish)하고, 그 이벤트에 관심 있는 다른 서비스가 구독(subscribe)해서 알아서 처리하는 구조다. 발행자는 누가 듣는지 신경 쓰지 않고, 구독자는 누가 보냈는지 신경 쓰지 않는다. 둘 사이에는 **메시지 브로커(Broker)** 라는 중계자가 끼어 있어, 서로의 존재를 모른 채 메시지로만 느슨하게 연결(loose coupling)된다.

- Ex: 라디오 **방송국과 청취자**의 관계와 같다. 방송국(발행자)은 "지금 3번 채널에서 음악을 튼다"는 신호를 전파(브로커)로 쏠 뿐, 누가 몇 명이나 듣는지 알지 못한다. 청취자(구독자)는 자기가 듣고 싶은 채널(토픽)에 주파수를 맞춰 두고, 방송이 나오면 각자 알아서 듣는다. 방송국은 청취자가 0명이든 100만 명이든 똑같이 방송하고, 청취자가 한 명 더 늘어도 방송국 코드를 바꿀 필요가 없다.
- Ex: 회사 **게시판**과도 비슷하다. 누군가 "신규 입사자 환영회 공지"를 게시판(토픽)에 붙이면(발행), 관심 있는 사람들이 각자 와서 읽고(구독) 자기 일정에 반영한다. 글을 붙인 사람은 누가 읽을지 미리 알 필요가 없다.

### 등장 배경 / 왜 필요한가
전통적인 동기(synchronous) **요청-응답(Request-Response)** 방식에서는, 한 서비스가 다른 서비스를 직접 호출하고 응답이 올 때까지 기다린다. 서비스가 몇 개 안 될 때는 단순하고 직관적이지만, [MSA](./msa.md)처럼 서비스가 수십 개로 늘어나면 다음과 같은 문제가 드러난다.

- 강한 결합(tight coupling): 주문 서비스가 알림 서비스, 재고 서비스, 배송 서비스를 직접 호출하면, 호출 대상의 주소·API 형식·존재 여부를 전부 알고 있어야 한다. 구독 대상이 하나 늘어날 때마다 호출하는 쪽 코드를 고쳐야 한다.
- 연쇄 장애(cascading failure): 동기 호출은 응답을 기다리며 블로킹된다. 재고 서비스가 느려지거나 죽으면, 그것을 호출한 주문 서비스도 함께 느려지거나 멈춘다. 장애가 호출 사슬을 타고 전파된다.
- 확장성 한계: 주문 한 건마다 알림·재고·배송을 **순서대로** 동기 호출하면, 가장 느린 서비스의 속도에 전체 응답 시간이 묶인다. 트래픽이 몰리면 전 구간이 같이 느려진다.
- 트래픽 급증 취약: 순간적으로 요청이 폭증하면, 동기 호출은 그대로 하위 서비스로 부하를 떠넘겨 도미노처럼 무너뜨린다.

이 문제를 풀기 위해, 서비스 사이에 **메시지 브로커**라는 완충 지대를 두고 **비동기 메시징(asynchronous messaging)** 으로 결합도를 낮춘 것이 이벤트 기반 아키텍처다. 발행자는 이벤트만 던지고 즉시 자기 일을 끝내고, 구독자는 자기 속도에 맞춰 나중에 처리한다. 한쪽이 느리거나 잠시 죽어도 메시지가 브로커에 쌓여(버퍼링) 있다가 회복되면 처리되므로, 장애가 격리(isolation)된다.

### 핵심 개념 / 구성요소
이벤트 기반 아키텍처는 "누가 보내고(발행), 무엇을 거쳐(브로커), 누가 받는가(구독)"의 세 축으로 구성된다.

- 이벤트(Event): "이미 일어난 사실"을 표현한 메시지다. `OrderCreated`(주문 생성됨), `PaymentCompleted`(결제 완료됨)처럼 보통 **과거형**으로 이름 짓는다. 명령(Command, "결제하라")과 달리 이벤트는 "결제됐다"는 통보이며, 누가 어떻게 반응할지는 받는 쪽이 결정한다.
- 발행자(Producer / Publisher): 이벤트를 만들어 브로커로 보내는 주체. 누가 구독하는지 알지 못한다.
- 구독자(Consumer / Subscriber): 관심 있는 이벤트를 받아 처리하는 주체. 누가 발행했는지 알지 못한다.
- 메시지 브로커(Message Broker): 발행자와 구독자 사이의 중계자. 이벤트를 받아 보관(버퍼링)하고, 구독자에게 전달한다. 이 중계자 덕분에 양쪽이 시간적·공간적으로 분리된다.
- 토픽/큐(Topic / Queue): 이벤트가 담기는 논리적 통로(채널). 발행자는 특정 토픽에 쓰고, 구독자는 그 토픽을 구독한다.
- Pub-Sub(Publish-Subscribe): 하나의 이벤트를 여러 구독자가 **각각** 받는 발행-구독 모델. 방송처럼 한 번의 발행이 N명에게 전파된다.

```
                                  +------------------------+
                                  |   알림 서비스           |  (구독자 1)
                                  |   subscribe(order.*)   |
                                  +------------------------+
                                              ^
                                              | 전달(deliver)
   +----------------+   발행      +-----------------------------+
   |  주문 서비스    |  publish    |     메시지 브로커 (Broker)   |
   |  (발행자)       | ----------> |  토픽: order-created         |
   |  OrderCreated  |  이벤트     |  [evt][evt][evt] ... 버퍼링  |
   +----------------+             +-----------------------------+
                                              |
                                              | 전달(deliver)
                                              v
                                  +------------------------+
                                  |   재고 서비스           |  (구독자 2)
                                  |   subscribe(order.*)   |
                                  +------------------------+

   * 발행자는 구독자가 누군지 모른다. 구독자는 발행자가 누군지 모른다.
   * 하나의 OrderCreated 이벤트를 알림/재고 서비스가 "각각" 받는다 (Pub-Sub).
   * 구독자가 잠시 죽어도 이벤트는 브로커에 쌓였다가 회복 후 처리된다.
```

#### 메시지 브로커 비교: Kafka vs RabbitMQ
브로커는 크게 "로그 기반(log-based)"인 Kafka와 "전통 메시지 큐(message queue)"인 RabbitMQ로 나뉜다. 둘 다 비동기 메시징을 지원하지만 설계 철학이 다르다.

| 항목 | Apache Kafka | RabbitMQ |
| --- | --- | --- |
| 모델 | 분산 로그(distributed log). 이벤트를 토픽 파티션에 **추가 기록**하고 일정 기간 보관 | 메시지 큐 + 익스체인지(exchange) 라우팅. 소비되면 큐에서 사라지는 게 기본 |
| 처리량 | 초당 수십만~수백만 건 규모의 대용량에 강함 | Kafka보다는 낮으나 일반 업무에는 충분 |
| 순서 보장 | **파티션 단위**로 순서 보장(같은 키는 같은 파티션) | 단일 큐 안에서 순서 보장(소비자가 여럿이면 흐트러질 수 있음) |
| 메시지 보존 | 소비 후에도 보관 기간 동안 유지 → **재처리(replay)** 가능 | 소비(ack) 시 제거가 기본 → 재처리는 별도 구성 필요 |
| 라우팅 | 단순(토픽/파티션 중심) | 풍부함(direct, topic, fanout, headers 등 익스체인지로 유연한 라우팅) |
| 주 용도 | 이벤트 스트리밍, 로그 수집, 대규모 데이터 파이프라인, 이벤트 소싱 | 작업 분배(task queue), RPC, 복잡한 라우팅이 필요한 업무 메시징 |

정리하면, **대용량 스트림을 흘려보내고 나중에 다시 읽어야 하면 Kafka**, **세밀한 라우팅으로 작업을 나눠 주고 처리되면 지우는 전통 큐가 맞으면 RabbitMQ**가 어울린다.

#### 이벤트 기반(비동기) vs 요청-응답(동기)
| 구분 | 이벤트 기반(비동기) | 요청-응답(동기) |
| --- | --- | --- |
| 통신 방식 | 이벤트 발행 후 즉시 반환, 응답을 기다리지 않음 | 호출 후 응답이 올 때까지 대기(블로킹) |
| 결합도 | 느슨함(서로의 존재를 모름, 브로커 경유) | 강함(호출 대상의 주소·API를 직접 알아야 함) |
| 장애 전파 | 격리됨(브로커에 버퍼링되어 회복 후 처리) | 전파됨(하위 서비스 장애가 상위로 연쇄) |
| 일관성 | 최종 일관성(eventual consistency) | 즉시(강한) 일관성에 유리 |
| 흐름 추적 | 어려움(흐름이 이벤트로 흩어짐) | 쉬움(호출 스택을 따라가면 됨) |
| 어울리는 상황 | 부수 작업(알림·집계·로깅), 서비스 확장, 트래픽 완충 | 즉답이 필요한 조회·검증, 명확한 트랜잭션 경계 |

### 장점 / 단점(트레이드오프)
- 장점
  - 느슨한 결합(loose coupling): 발행자와 구독자가 서로를 모른 채 브로커로만 연결된다. 새 구독자(예: 분석 서비스)를 추가해도 발행자 코드는 그대로다.
  - 확장성(scalability): 구독자를 여러 인스턴스로 늘려 같은 토픽을 나눠 처리하면, 트래픽 증가에 수평 확장으로 대응할 수 있다.
  - 버퍼링·장애 격리: 트래픽이 급증해도 이벤트가 브로커에 쌓여 완충된다. 구독자가 잠시 죽어도 메시지가 보존되어 회복 후 이어서 처리하므로 연쇄 장애를 막는다.
  - 응답성: 발행자는 이벤트만 던지고 즉시 반환하므로, 부수 작업(알림·집계)이 본 처리(주문 생성)의 응답 시간을 늘리지 않는다.
- 단점(트레이드오프)
  - 흐름 추적 어려움: 로직이 여러 이벤트와 구독자로 흩어져, "주문이 어떤 경로로 처리됐는지"를 한눈에 보기 어렵다. 분산 추적(distributed tracing)과 상관관계 ID(correlation ID)가 사실상 필수다.
  - 최종 일관성(eventual consistency): 발행 직후 구독자가 곧바로 반영하지 않으므로, 잠깐 데이터가 어긋나 보이는 구간이 생긴다. 강한 일관성이 필요한 곳에는 부적합하다. 상세는 [분산 데이터 일관성](./data-consistency.md) 참고.
  - 디버깅 난이도: 동기 호출처럼 스택 트레이스를 따라갈 수 없어, 어디서 멈췄는지 찾기가 어렵다. 모니터링·로그 수집이 잘 갖춰져야 한다.
  - 운영 복잡도: 브로커라는 인프라가 하나 더 늘고, 중복·순서·실패 재시도(retry)·죽은 편지 큐(DLQ, Dead Letter Queue) 같은 처리 장치를 별도로 설계해야 한다.

### 실무 예제
Ex: 쇼핑몰에서 **주문이 생성되면**, 본 처리(주문 저장)와 별개로 **알림 서비스**는 고객에게 주문 확인 메일을, **재고 서비스**는 재고 차감을 처리해야 한다. 이를 동기로 직접 호출하면 주문 응답이 느려지고 강결합이 생긴다. 대신 주문 서비스는 `OrderCreatedEvent`만 발행하고, 알림·재고는 각자 구독해 처리한다.

먼저 같은 애플리케이션(모놀리식 또는 단일 서비스) 안에서는 Spring의 `ApplicationEventPublisher`로 구현할 수 있다. 트랜잭션이 **커밋된 뒤** 이벤트를 처리하도록 `@TransactionalEventListener`를 쓰는 것이 핵심이다.

```java
// 1) 이벤트 정의 (이미 일어난 사실 → 과거형 이름)
public record OrderCreatedEvent(Long orderId, Long productId, int quantity) {
}

// 2) 발행자: 주문 저장 후 이벤트 발행
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public Long createOrder(OrderRequest request) {
        Order order = orderRepository.save(Order.from(request)); // 본 처리
        // 누가 구독하는지 모른 채 "주문이 생성됐다"는 사실만 발행
        eventPublisher.publishEvent(
            new OrderCreatedEvent(order.getId(), order.getProductId(), order.getQuantity())
        );
        return order.getId();
    }
}

// 3) 구독자: 알림 서비스 (커밋 이후 비동기 처리)
@Component
@RequiredArgsConstructor
public class OrderNotificationListener {

    private final MailSender mailSender;

    @Async // 별도 스레드에서 비동기 처리 → 주문 응답을 막지 않음
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(OrderCreatedEvent event) {
        mailSender.sendOrderConfirmed(event.orderId());
    }
}

// 4) 구독자: 재고 서비스
@Component
@RequiredArgsConstructor
public class StockDecreaseListener {

    private final StockService stockService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(OrderCreatedEvent event) {
        stockService.decrease(event.productId(), event.quantity());
    }
}
```

서비스가 프로세스 경계를 넘어 분리되면(MSA), 같은 발상을 외부 브로커인 **Kafka**로 옮긴다. 발행자는 토픽에 쓰고, 다른 서비스가 `@KafkaListener`로 구독한다.

```java
// 발행자: Kafka 토픽으로 이벤트 전송
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Transactional
    public Long createOrder(OrderRequest request) {
        Order order = orderRepository.save(Order.from(request));
        // key 를 orderId 로 주면 같은 주문의 이벤트는 같은 파티션 → 순서 보장
        kafkaTemplate.send(
            "order-created",
            String.valueOf(order.getId()),
            new OrderCreatedEvent(order.getId(), order.getProductId(), order.getQuantity())
        );
        return order.getId();
    }
}

// 구독자: 재고 서비스 (별도 애플리케이션)
@Component
@RequiredArgsConstructor
public class OrderCreatedConsumer {

    private final StockService stockService;

    @KafkaListener(topics = "order-created", groupId = "stock-service")
    public void consume(OrderCreatedEvent event) {
        // 멱등성: 이미 처리한 주문이면 건너뛴다 (중복 수신 대비)
        if (stockService.alreadyProcessed(event.orderId())) {
            return;
        }
        stockService.decrease(event.productId(), event.quantity());
    }
}
```

여기서 `@Async`를 쓰는 비동기 처리의 상세 동작은 [Spring 비동기](../../spring/da/async.md), `ApplicationEventPublisher`와 리스너 구현의 기본기는 [Spring 이벤트](../../spring/da/event.md) 문서에서 다룬다.

### 주의점 / 안티패턴
- 멱등성(idempotency) 미보장 금지: 브로커는 보통 "최소 한 번 전달(at-least-once)"을 보장하므로, **같은 이벤트가 두 번 이상 도착할 수 있다.** 재고를 그냥 빼면 두 번 차감되는 버그가 난다. 처리 여부를 이벤트 ID로 기록해 두고, 이미 처리한 이벤트는 건너뛰도록 **멱등하게** 설계해야 한다.
- 순서 의존 방치 금지: 여러 파티션·여러 구독자로 처리하면 이벤트 도착 순서가 발행 순서와 달라질 수 있다. 순서가 중요하면 같은 키를 같은 파티션으로 보내거나(Kafka의 키 파티셔닝), 순서가 어긋나도 결과가 같도록 처리 로직을 짠다.
- 이벤트 스키마 무관리 금지: 발행자가 이벤트 필드를 마음대로 바꾸면 모든 구독자가 깨진다. 스키마 레지스트리(Schema Registry)로 버전을 관리하고, **하위 호환(backward compatible)** 을 지키며 진화시켜야 한다(필드는 추가만, 삭제·타입 변경은 신중히).
- 이벤트 남발(event soup) 금지: 사소한 모든 변화를 이벤트로 쏟아 내면, 어떤 이벤트가 어디로 흘러 무엇을 일으키는지 아무도 추적할 수 없는 "이벤트 수프" 상태가 된다. 도메인적으로 의미 있는 사실만 이벤트로 정의하고, 단순 조회·즉답이 필요한 곳은 동기 요청-응답으로 두는 편이 낫다.
- 실패 처리 누락 금지: 구독자가 처리에 계속 실패하는 이벤트(독성 메시지, poison message)를 무한 재시도하면 큐가 막힌다. 재시도 횟수를 제한하고, 끝내 실패한 이벤트는 **죽은 편지 큐(DLQ)** 로 보내 따로 분석·재처리한다.
- 강한 일관성 오용 금지: 이벤트 기반은 본질적으로 최종 일관성이다. 결제 승인처럼 "지금 즉시 확정"이 필요한 경계까지 이벤트로 비동기 처리하면 데이터가 어긋난다. 일관성 설계는 [분산 데이터 일관성](./data-consistency.md)의 사가(Saga)·아웃박스(Outbox) 패턴과 함께 검토한다.

> document: https://martinfowler.com/articles/201701-event-driven.html
> document: https://microservices.io/patterns/data/event-driven-architecture.html
> document: https://kafka.apache.org/documentation/

### 관련 문서
- [MSA (Microservices Architecture)](./msa.md)
- [분산 데이터 일관성 (Saga / Outbox)](./data-consistency.md)
- [Spring 이벤트 (ApplicationEvent)](../../spring/da/event.md)
- [Spring 비동기 (@Async)](../../spring/da/async.md)
