## 서비스 디스커버리 (Service Discovery)

### 정의
서비스 디스커버리는 IP·포트가 수시로 바뀌는 동적 환경(컨테이너, 오토스케일링 등)에서 "지금 이 순간 어떤 서비스가 어디(어떤 주소:포트)에 살아 있는가"를 자동으로 찾아주는 메커니즘이다.
서비스 인스턴스들이 자신의 위치를 어딘가에 등록해 두고, 다른 서비스는 그 위치를 직접 외우는 대신 매번 "지금 어디 있어?"라고 물어보는 방식으로 동작한다.

- Ex: 친구 집에 처음 방문할 때 집 주소를 종이에 적어 외워 두었다고 하자(하드코딩). 그런데 친구가 이사를 다니는 사람이라면, 외워 둔 주소는 금방 쓸모없어진다. 이때 필요한 것이 항상 최신 연락처를 알려주는 "전화번호부"다.
- 서비스 디스커버리는 마이크로서비스 세계의 전화번호부이자 안내데스크다. 손님(클라이언트)이 "결제 담당자 어디 계세요?"라고 물으면, 안내데스크(서비스 레지스트리)가 "지금 3층 302호(10.0.2.7:8080)에 계십니다"라고 실시간으로 알려준다. 담당자가 자리를 옮기면 안내데스크의 기록도 즉시 갱신된다.

### 등장 배경 / 왜 필요한가
전통적인 온프레미스 환경에서는 서버의 IP·포트가 거의 고정되어 있었다. 그래서 코드나 설정 파일에 `http://192.168.0.10:8080` 처럼 호스트:포트를 직접 박아 넣어도(하드코딩) 큰 문제가 없었다.

그러나 클라우드/MSA 환경으로 넘어오면서 이 전제가 무너졌다.

- **인스턴스가 동적으로 생기고 사라진다**: 오토스케일링이 트래픽에 따라 인스턴스를 늘렸다 줄였다 하고, 컨테이너는 재배포될 때마다 새 IP를 받는다. 어제 유효했던 주소가 오늘은 죽은 주소일 수 있다.
- **인스턴스 개수가 가변적이다**: 결제 서비스가 1대일 수도, 50대일 수도 있다. 하드코딩으로는 "지금 살아 있는 N대"를 표현할 수 없다.
- **수동 관리가 불가능하다**: 서비스 수십 개 × 인스턴스 수십 대 × 수시로 변하는 주소를 사람이 설정 파일로 따라잡는 것은 비현실적이다.

즉, "주소를 외우는" 정적 방식 대신 "주소를 물어보는" 동적 방식이 필요해졌고, 그 답이 서비스 디스커버리다. 이는 [클라우드의 신속한 탄력성(Rapid elasticity)](../on-premise-vs-cloud.md)이라는 특성과 [MSA](./msa.md)의 서비스 분리라는 흐름이 맞물리면서 사실상 필수 인프라가 되었다.

### 핵심 개념 / 구성요소
서비스 디스커버리는 **서비스 레지스트리(Service Registry)** 라는 중앙 저장소를 중심으로, 다음 3가지 동작으로 굴러간다.

- **등록(Registration)**: 서비스 인스턴스가 기동되면 자신의 위치(서비스 이름, IP, 포트, 메타데이터)를 레지스트리에 스스로 등록한다. 종료될 때는 등록을 해제한다.
- **조회(Lookup / Discovery)**: 다른 서비스가 특정 서비스 이름으로 "지금 살아 있는 인스턴스 목록"을 레지스트리에 질의해 받아온다.
- **헬스 체크(Health Check)**: 레지스트리는 주기적으로 각 인스턴스의 생존 여부를 확인한다. 응답이 없는(죽은) 인스턴스는 목록에서 제거해, 조회 결과에 포함되지 않게 한다.

```
                         [ 서비스 레지스트리 (전화번호부) ]
                         ┌─────────────────────────────────┐
                         │ payment-service                 │
                         │   - 10.0.2.7:8080  (healthy)     │
                         │   - 10.0.2.8:8080  (healthy)     │
                         │ order-service                   │
                         │   - 10.0.3.1:9090  (healthy)     │
                         └─────────────────────────────────┘
                              ▲   ▲             ▲
                ① 등록/해제    │   │ ③ 헬스체크   │ ② 조회
              (Registration)  │   │ (Health)    │ (Lookup)
                              │   │             │
                  ┌───────────┘   └──────┐   ┌──┴──────────┐
                  │                      │   │             │
          [ payment 인스턴스들 ]              [ order-service ]
        (기동 시 자기 주소를 등록)         (payment 위치를 물어봄)
```

### 장점 / 단점(트레이드오프)
- **장점**
  - 인스턴스가 동적으로 늘고 줄어도 호출자가 코드/설정 변경 없이 자동으로 최신 위치를 따라간다(오토스케일링 친화적).
  - 죽은 인스턴스를 헬스 체크로 걸러내 장애 인스턴스로의 라우팅을 줄인다.
  - 클라이언트 사이드 디스커버리와 결합하면 별도 로드밸런서 장비 없이 클라이언트가 직접 부하를 분산할 수 있다.
- **단점(트레이드오프)**
  - 레지스트리 자체가 새로운 핵심 인프라가 되어, 이것이 죽으면 전체 디스커버리가 마비되는 **단일 장애점(SPOF)** 위험이 생긴다(그래서 보통 클러스터로 다중화한다).
  - 등록 정보와 실제 상태 사이에 **시간차(eventually consistent)** 가 존재한다. 방금 죽은 인스턴스가 잠깐 동안 목록에 남아 있을 수 있다.
  - 헬스 체크, 캐시 갱신, 클라이언트 라이브러리 등 **운영 복잡도**가 늘어난다.

### 핵심 비교: Client-side vs Server-side Discovery
서비스 위치를 "누가 조회하고 누가 부하를 분산하느냐"에 따라 두 방식으로 나뉜다.

**Client-side Discovery** — 클라이언트가 직접 레지스트리에 묻고, 받아온 목록 중 하나를 직접 골라(클라이언트 사이드 로드밸런싱) 호출한다.

```
                         [ 서비스 레지스트리 ]
                              ▲
                ① 인스턴스 목록 조회 │
                              │
        [ 클라이언트 ] ─────────┘
             │
             │ ② 받아온 목록 중 하나를 직접 선택(LB) 후 호출
             ▼
        [ payment 인스턴스 #2 ]  (#1 #2 #3 중 클라이언트가 선택)
```

**Server-side Discovery** — 클라이언트는 그냥 단일 진입점(로드밸런서/게이트웨이)으로 요청하고, 그 중간 계층이 레지스트리를 조회해 적절한 인스턴스로 전달한다. 클라이언트는 디스커버리의 존재를 모른다.

```
                                        [ 서비스 레지스트리 ]
                                             ▲
                              ② 인스턴스 목록 조회 │
                                             │
        [ 클라이언트 ] ──① 요청──▶ [ 로드밸런서 / 게이트웨이 ] ──③ 전달──▶ [ payment 인스턴스 #2 ]
                                  (LB가 인스턴스 선택)
```

| 구분 | Client-side Discovery | Server-side Discovery |
| --- | --- | --- |
| 레지스트리 조회 주체 | 클라이언트 | 중간 계층(로드밸런서/게이트웨이) |
| 부하 분산 위치 | 클라이언트 내부(클라이언트 사이드 LB) | 중간 계층(서버 사이드 LB) |
| 네트워크 홉 | 적음(클라이언트 → 인스턴스 직접) | 한 단계 더 거침(클라이언트 → LB → 인스턴스) |
| 클라이언트 복잡도 | 높음(디스커버리/LB 로직 내장) | 낮음(LB만 알면 됨) |
| 클라이언트 언어 의존성 | 큼(언어별 라이브러리 필요) | 작음(어떤 언어든 LB 주소만 호출) |
| 대표 예시 | Eureka + Spring Cloud LoadBalancer/Ribbon | 쿠버네티스 Service, AWS ELB + 디스커버리 |

### 대표 구현
- **Eureka (Netflix / Spring Cloud Netflix)**: 자바·스프링 생태계의 대표적인 클라이언트 사이드 디스커버리. 클라이언트가 Eureka 서버에 등록하고, 인스턴스 목록을 받아 로컬에 캐싱한 뒤 클라이언트 사이드 LB로 호출한다.
- **Consul (HashiCorp)**: 서비스 레지스트리 + 강력한 헬스 체크 + Key/Value 저장소 + DNS 인터페이스를 제공한다. 언어 중립적이고, DNS로 조회할 수 있어 클라이언트·서버 양쪽 방식 모두에 쓰기 좋다.
- **쿠버네티스 Service / DNS 기반 디스커버리**: 쿠버네티스는 Pod가 뜨고 죽을 때 `Service` 오브젝트가 이를 추적하고, 클러스터 내부 DNS(`payment-service.default.svc.cluster.local`)로 조회되게 한다. 클라이언트는 그냥 서비스 이름(DNS)으로 호출하면 kube-proxy/엔드포인트가 살아 있는 Pod로 분산해 준다. 별도 클라이언트 라이브러리 없이 동작하는 사실상의 서버 사이드 디스커버리다.

### 로드밸런싱과의 연계
서비스 디스커버리와 [로드밸런싱](../server.md)은 따로 노는 개념이 아니라 한 몸처럼 맞물린다. 디스커버리가 "살아 있는 인스턴스 목록"을 제공하면, 로드밸런싱은 "그 목록 중 어디로 보낼지"를 결정하기 때문이다.

- [로드밸런싱 문서](../server.md)의 Scale-Out은 "서버 대수를 늘려 부하를 나눈다"는 것인데, 늘어난 서버들의 주소를 어떻게 알 것인가가 바로 디스커버리가 푸는 문제다.
- **서버 사이드 디스커버리**는 [로드밸런싱 문서](../server.md)에 나온 전통적인 L4/L7 로드밸런서·리버스 프록시 구조와 그대로 대응한다. 클라이언트 → 로드밸런서 → 서버 흐름에서 로드밸런서가 디스커버리 결과를 보고 대상을 고른다.
- **클라이언트 사이드 디스커버리**는 별도 LB 장비 없이 클라이언트가 직접 라운드로빈/최소 연결 등(로드밸런싱 문서의 알고리즘들)을 적용해 인스턴스를 고른다. 이를 **클라이언트 사이드 로드밸런싱**이라 한다.
- 헬스 체크는 로드밸런싱 알고리즘의 입력이다. 죽은 인스턴스가 목록에서 빠져야, 라운드로빈이든 최소 연결이든 살아 있는 대상에게만 트래픽을 분배할 수 있다.

### 실무 예제
Ex: Eureka를 쓰는 스프링 클라우드 환경에서 결제 서비스(`payment-service`)가 등록되고, 주문 서비스가 이를 조회해 호출하기까지의 흐름.

흐름 요약:
1. Eureka 서버(레지스트리)가 기동된다.
2. `payment-service` 인스턴스가 기동되면서 자신의 주소를 Eureka에 **등록**한다.
3. Eureka는 주기적으로 `payment-service`에 **헬스 체크(heartbeat)** 를 보내 생존을 확인한다.
4. `order-service`가 `payment-service`라는 **이름으로 조회**해 인스턴스 목록을 받아온다.
5. `order-service`가 받아온 목록 중 하나를 클라이언트 사이드 LB로 골라 호출한다.

```
order-service ──"payment-service 어디 있어?"──▶ Eureka 서버
order-service ◀──[10.0.2.7:8080, 10.0.2.8:8080]── Eureka 서버
order-service ──HTTP 호출──▶ 10.0.2.8:8080 (LB로 선택된 인스턴스)
```

Eureka 서버 설정 (`application.yml`)

```yaml
# Eureka 서버 (레지스트리) 측 설정
server:
  port: 8761

eureka:
  client:
    # 서버 자신은 레지스트리에 등록/조회하지 않음
    register-with-eureka: false
    fetch-registry: false
  server:
    # 헬스 체크 실패 인스턴스를 솎아내는 자기보호 모드 비활성(개발 환경 기준)
    enable-self-preservation: false
```

서비스(클라이언트) 측 설정 (`application.yml`)

```yaml
# payment-service / order-service 공통 클라이언트 설정
spring:
  application:
    name: payment-service        # 이 이름으로 레지스트리에 등록·조회됨

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/   # Eureka 서버 주소
    register-with-eureka: true   # 등록(Registration)
    fetch-registry: true         # 조회(Lookup)용 목록 가져오기
  instance:
    prefer-ip-address: true      # 호스트명 대신 IP로 등록
    lease-renewal-interval-in-seconds: 10   # 헬스 체크(heartbeat) 주기
```

조회·호출 코드 (스프링, 서비스 이름으로 호출)

```java
// 하드코딩된 IP:PORT 대신 "서비스 이름"으로 호출한다.
// @LoadBalanced 가 붙으면 디스커버리 결과를 받아 클라이언트 사이드 LB로 인스턴스를 선택한다.
@Configuration
public class HttpConfig {
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

@Service
public class OrderService {
    private final RestTemplate restTemplate;

    public OrderService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Payment requestPayment(Long orderId) {
        // 호스트 자리에 IP가 아니라 서비스 이름(payment-service)이 들어간다.
        return restTemplate.getForObject(
            "http://payment-service/payments/{orderId}",
            Payment.class,
            orderId
        );
    }
}
```

### 주의점 / 안티패턴
- **레지스트리의 단일 장애점(SPOF)을 방치하지 마라**: 레지스트리 1대만 띄워 두면 그것이 죽는 순간 모든 디스커버리가 멈춘다. 운영 환경에서는 Eureka 피어 클러스터, Consul 클러스터처럼 **다중 노드로 이중화**해야 한다.
- **헬스 체크를 형식적으로 두지 마라**: 헬스 체크가 부실하거나 주기가 너무 길면, 이미 죽은 인스턴스가 한참 동안 목록에 남아 거기로 트래픽이 흘러가 요청이 실패한다. 단순 포트 오픈 확인이 아니라 실제 의존성(DB 연결 등)까지 보는 의미 있는 헬스 체크를 두는 것이 좋다. (이 실패를 견디는 회복 전략은 [resilience](./resilience.md) 참고)
- **클라이언트 캐시의 갱신 지연(stale cache)을 잊지 마라**: 클라이언트 사이드 디스커버리는 인스턴스 목록을 로컬에 캐싱한다. 갱신 주기가 길면 방금 사라진 인스턴스로 계속 호출할 수 있다. 갱신 주기와 헬스 체크 주기를 운영 상황에 맞게 조정해야 한다.
- **디스커버리를 버리고 다시 하드코딩하지 마라**: "그냥 IP 박는 게 편하다"며 호스트:포트를 코드에 박으면, 등장 배경에서 설명한 동적 환경의 문제가 그대로 재발한다.

> document: https://learn.microsoft.com/ko-kr/azure/architecture/microservices/design/api-gateway
> document: https://spring.io/projects/spring-cloud-netflix
> document: https://developer.hashicorp.com/consul/docs/concepts/service-discovery

### 관련 문서
- [MSA(마이크로서비스 아키텍처)](./msa.md)
- [API Gateway](./api-gateway.md)
- [서버 / 로드밸런싱](../server.md)
