## BFF (Backend for Frontend)

### 정의
BFF(Backend for Frontend)는 클라이언트(웹, 모바일, IoT 등)의 종류마다 그 클라이언트만을 위한 전용 백엔드 계층을 하나씩 두는 아키텍처 패턴이다.
하나의 공용 API가 모든 클라이언트를 떠받치는 대신, 각 클라이언트의 화면과 데이터 요구에 딱 맞춰진 백엔드를 따로 만들어 그 뒤에 있는 마이크로서비스들의 응답을 조합(aggregation)·변환해서 내려준다.

- Ex: 한 식당이 손님 유형마다 다른 응대를 한다고 생각해보자.
  - 정장을 입고 온 손님(웹)에게는 풀코스 메뉴판을 자세히 펼쳐 보여주고,
  - 점심시간에 바쁘게 들른 손님(모바일)에게는 핵심만 추린 간편 도시락 메뉴를 빠르게 내어주며,
  - 드라이브스루로 잠깐 들른 손님(IoT)에게는 음료 하나만 창문으로 건넨다.
  - 주방(마이크로서비스)은 동일하지만, 손님 유형별로 "전담 웨이터(BFF)"가 따로 붙어 그 손님에게 가장 알맞은 형태로 음식을 정리해서 내어주는 것과 같다.

웹은 큰 화면에 풍부한 정보를 한 번에 담을 수 있어 데이터를 많이 요구하지만, 모바일은 화면이 좁고 네트워크가 불안정해 꼭 필요한 데이터만 가볍게 받기를 원한다.
이렇게 클라이언트마다 데이터 양·응답 형태·통신 빈도의 요구가 다르기 때문에, 클라이언트마다 백엔드를 따로 두는 발상이 BFF다.

### 등장 배경 / 왜 필요한가
초기에는 모든 클라이언트가 단 하나의 공용 API(또는 단일 [API Gateway](./api-gateway.md))를 함께 사용했다.
하지만 클라이언트 종류가 늘어날수록 이 공용 API는 "웹도 만족시키고, 모바일도 만족시키고, IoT도 만족시켜야 하는" 만능 계층이 되어야 했고, 그 결과 다음 문제가 생겼다.

- 웹용 필드와 모바일용 필드가 한 응답에 뒤섞여 응답이 비대해진다(over-fetching).
- 모바일이 화면 하나를 그리려고 공용 API를 여러 번 호출해야 한다(under-fetching, N번 왕복).
- 한 클라이언트의 요구가 바뀔 때마다 공용 API를 건드려야 하므로, 변경이 다른 클라이언트에 영향을 준다(결합도 증가).
- 분기 처리(`if 웹 then ... else 모바일 ...`)가 공용 API 안에 누적되어 코드가 거대해지고 유지보수가 어려워진다.

즉, 공용 API 1개가 모든 클라이언트를 책임지려다 비대해지는 한계 때문에, "클라이언트별 전용 백엔드"인 BFF가 등장했다.

```
[공용 API 1개로 모든 클라이언트 처리]

  웹 ----+
         |
  모바일 -+---->  공용 API (만능, 비대)  ---->  마이크로서비스들
         |        - 웹/모바일/IoT 분기 누적
  IoT ---+        - 응답 비대, 변경 충돌


[클라이언트별 BFF]

  웹 ------>  웹 BFF    ----+
                            |
  모바일 -->  모바일 BFF ----+---->  마이크로서비스들 (User / Order / Product ...)
                            |
  IoT ----->  IoT BFF   ----+

  * 각 BFF는 자기 클라이언트 화면에 맞춰 응답을 조합·축약해서 내려준다.
```

### 핵심 개념 / 구성요소
- per-client BFF(클라이언트별 BFF)
  - 클라이언트 종류마다 하나씩 전용 백엔드를 둔다. 대표적으로 웹 BFF, 모바일 BFF로 나뉜다.
  - 각 BFF는 자신이 담당하는 클라이언트의 화면·데이터 요구만 책임지므로, 다른 클라이언트의 요구 변화에 영향을 받지 않는다.
- 응답 조합(aggregation)
  - BFF의 핵심 역할이다. 한 화면을 그리는 데 필요한 데이터가 여러 마이크로서비스에 흩어져 있을 때, BFF가 대신 여러 서비스를 호출해 그 결과를 하나의 응답으로 합쳐서 내려준다.
  - 클라이언트는 BFF에 한 번만 요청하면 화면에 필요한 데이터를 한 덩어리로 받는다(왕복 횟수 감소).
- 변환(transformation) / 축약
  - 마이크로서비스가 주는 원시 데이터를, 해당 클라이언트 화면에 맞는 형태로 가공한다(필드 추리기, 이름 바꾸기, 단위 변환 등).
- 조합 범위는 "클라이언트 대면 조회"에 한정된다
  - BFF의 조합은 주로 **클라이언트에게 화면 데이터를 내려주는 읽기(조회)** 흐름을 위한 것이다. 서비스 간 내부 호출(예: 주문 서비스가 결제 서비스를 부름)이나 주문 생성 같은 **쓰기 흐름은 BFF를 거치지 않고** 서비스끼리 직접 호출하거나 이벤트로 처리한다(→ [MSA](./msa.md)의 서비스 간 통신). 즉 모든 통신이 BFF/게이트웨이를 통과하는 것은 아니며, BFF는 어디까지나 stateless한 조합·변환 계층이라 **자기 비즈니스 DB를 갖지 않는다.**

```
[모바일 BFF의 응답 조합 흐름]

  모바일 앱
     |  GET /mobile/home   (요청 1번)
     v
  +------------------+
  |   모바일 BFF      |
  |  (조합 + 축약)    |
  +------------------+
     | 병렬 호출 (BFF가 대신 여러 서비스 호출)
     +--------> User Service     (사용자 이름)
     +--------> Order Service    (최근 주문 1건)
     +--------> Product Service  (추천 상품 목록)
     |
     v  세 응답을 모바일 화면용 단일 JSON으로 합쳐서 반환
  모바일 앱 (한 번의 응답으로 홈 화면 완성)
```

### 장점 / 단점(트레이드오프)
- 장점
  - 클라이언트별 최적화: 화면마다 꼭 필요한 데이터만 알맞은 형태로 내려줄 수 있어 over-fetching / under-fetching을 줄인다.
  - 왕복 횟수 감소: 여러 서비스 호출을 BFF가 대신 묶어주므로, 클라이언트는 한 번의 요청으로 화면을 그린다(특히 모바일 네트워크에서 유리).
  - 결합도 분리: 한 클라이언트의 요구 변경이 그 클라이언트 전용 BFF 안에서만 처리되어, 다른 클라이언트에 영향을 주지 않는다.
  - 팀 자율성: 웹 팀은 웹 BFF, 모바일 팀은 모바일 BFF를 각자 책임지고 독립적으로 배포할 수 있다.
- 단점(트레이드오프)
  - 구성요소 증가: 클라이언트 종류만큼 BFF가 늘어 배포·모니터링·운영 대상이 많아진다.
  - 코드 중복: 여러 BFF가 비슷한 조합 로직을 각자 들고 있게 되기 쉽다(중복 폭증 위험).
  - 책임 경계 흐려짐: BFF에 비즈니스 로직을 자꾸 넣다 보면, 본래 조합·변환 계층이어야 할 BFF가 또 다른 비대한 백엔드가 된다.

### 실무 예제
Ex: 모바일 홈 화면 하나를 그리는 데 사용자 정보, 최근 주문, 추천 상품이 모두 필요하다고 하자.
이 데이터는 각각 User Service / Order Service / Product Service에 흩어져 있다.
모바일 앱이 세 서비스를 직접 세 번 호출하는 대신, 모바일 BFF가 한 번의 요청을 받아 세 서비스를 대신 호출하고, 그 결과를 모바일 화면용 단일 응답으로 합쳐서 내려준다.

```java
// 모바일 전용 BFF 컨트롤러 (조합 + 축약 계층)
@RestController
@RequestMapping("/mobile")
public class MobileBffController {

    private final UserClient userClient;       // User Service 호출용
    private final OrderClient orderClient;      // Order Service 호출용
    private final ProductClient productClient;  // Product Service 호출용

    public MobileBffController(UserClient userClient,
                               OrderClient orderClient,
                               ProductClient productClient) {
        this.userClient = userClient;
        this.orderClient = orderClient;
        this.productClient = productClient;
    }

    // 모바일 홈 화면용 단일 엔드포인트: 앱은 이 한 번만 호출하면 된다
    @GetMapping("/home")
    public MobileHomeResponse home(@RequestParam Long userId) {
        // 1) 여러 마이크로서비스의 응답을 모은다 (aggregation)
        User user = userClient.findById(userId);
        Order recentOrder = orderClient.findLatestByUser(userId);
        List<Product> recommended = productClient.recommendFor(userId);

        // 2) 모바일 화면에 필요한 필드만 추려 단일 응답으로 변환한다 (transformation)
        return MobileHomeResponse.builder()
                .userName(user.getName())                       // 이름만 노출 (그 외 필드는 버림)
                .recentOrderSummary(recentOrder.toSummary())    // 요약 형태로 축약
                .recommendedTop3(recommended.stream()
                        .limit(3)                               // 모바일은 추천 3개만
                        .map(Product::toCard)
                        .toList())
                .build();
    }
}
```

위 흐름에서 BFF가 하는 일은 "여러 서비스를 대신 호출(조합) + 모바일 화면용으로 추리기(변환)"가 전부이며, 주문 생성 규칙이나 재고 차감 같은 핵심 비즈니스 로직은 각 마이크로서비스 안에 남겨둔다.
같은 화면을 웹에서 그린다면, 웹 BFF는 더 많은 필드와 더 긴 추천 목록을 내려주도록 별도로 구성한다.

### 주의점 / 안티패턴
- BFF에 핵심 비즈니스 로직 과적재
  - BFF는 어디까지나 "조합(aggregation) + 변환(transformation)" 계층이다. 주문 검증, 결제 정합성, 재고 차감 같은 도메인 규칙을 BFF에 넣으면 BFF가 또 다른 거대한 백엔드가 되어버린다.
  - 핵심 로직은 [MSA](./msa.md)의 각 도메인 서비스에 두고, BFF는 그 응답을 모아 클라이언트 화면용으로 다듬는 역할만 맡긴다.
- 클라이언트가 늘수록 BFF 중복 폭증
  - 웹 BFF, 모바일 BFF, TV BFF, IoT BFF... 클라이언트 종류가 늘수록 비슷한 조합 코드가 각 BFF에 복제되기 쉽다.
  - 공통 조합 로직은 공유 라이브러리/공통 모듈로 추출하거나, 클라이언트별 차이가 작다면 BFF를 무리하게 쪼개지 않는 식으로 균형을 잡는다.
- 단순한 단일 클라이언트에 과한 도입
  - 클라이언트가 사실상 하나뿐이라면 BFF는 불필요한 계층(오버엔지니어링)이 될 수 있다. 클라이언트 요구가 실제로 갈라질 때 도입하는 것이 좋다.

### BFF vs API Gateway vs GraphQL 비교
세 개념은 "클라이언트와 백엔드 사이에 끼는 계층"이라는 점에서 자주 헷갈리지만, 목적·위치·책임 경계가 다르다.

| 구분 | 목적 | 위치 | 책임 경계 |
| --- | --- | --- | --- |
| BFF | 특정 클라이언트(웹/모바일 등) 화면에 맞춘 응답 조합·변환 | 각 클라이언트와 백엔드 사이에 클라이언트별로 1개씩 | 화면 단위 응답 조합·축약(클라이언트 친화). 횡단 관심사는 보통 게이트웨이에 위임 |
| API Gateway | 모든 요청의 단일 진입점에서 라우팅 + 횡단 관심사 처리 | 시스템 가장 앞단(클라이언트 전체 ↔ 내부 서비스) 보통 1개(또는 소수) | 인증·인가, 라우팅, 속도 제한(rate limit), 로깅 등 공통 처리. 화면별 조합은 책임 아님 |
| GraphQL | 클라이언트가 필요한 필드를 직접 질의해 한 번에 받기 | 게이트웨이 또는 BFF 안에서 질의 처리 계층으로 사용 가능 | 스키마 기반 유연한 질의/응답(쿼리 언어·런타임). "패턴"이 아닌 "기술"이라는 점이 다름 |

- 핵심 구분
  - API Gateway는 "모든 클라이언트의 단일 진입점 + 공통 처리"를, BFF는 "특정 클라이언트 전용 응답 조합"을 맡는다. 둘은 경쟁 관계가 아니라 함께 쓰일 수 있다(게이트웨이 뒤에 클라이언트별 BFF 배치).
  - GraphQL은 BFF/게이트웨이를 대체하는 개념이 아니라, BFF가 "클라이언트가 원하는 필드만 골라 받게" 구현하는 한 가지 기술 수단이다. BFF는 패턴, GraphQL은 그 패턴을 실현할 수 있는 기술이다.

> document: https://learn.microsoft.com/ko-kr/azure/architecture/patterns/backends-for-frontends
> document: https://samnewman.io/patterns/architectural/bff/

### 관련 문서
- [API Gateway](./api-gateway.md)
- [MSA](./msa.md)
- [REST API](../rest-api.md)
