## Web

### Client Server Architecture

#### 2-Tier: (서버(Tier 1) / 클라이언트(Tier 2))
- 1980년대 LAN 환경에서 단순 분산 처리의 초기형태 (Fat Client 라고도 함)
- 각종 비지니스 로직 (Business Logic)을 전부 클라이언트에서 담당하고 서버는 단지 데이터 만을 공유하는 형태
  - 클라이언트에게 부하가 많아져서 클라이언트 수가 많아지면 유지보수 비용이 증가

#### 3-Tier: (데이터베이스관리시스템(Tier 1) - 웹 서버(Tier 2) - 클라이언트(Tier 3))
- 각각 서로 다른 플랫폼에서 동작하는 3개의 잘 정의된 독립 프로세스로 구성된 분산 클라이언트/서버 구조

#### n-Tier
- 클라이언트 부분에서는 프리젠테이션만 맡고, 대부분의 로직을 여러 종류의 다수의 서버 등에서 분산 처리하는 구조

### 웹 애플리케이션 개발시 고려사항
  1. 신뢰성(reliability)
  2. 확장성(scalability)
  3. 보안성(security)
  4. 견고성(robustness)
  
### 웹 애플리케이션의 3단계 계층 구조
↑↓ Client 👦🏻
  => Cross-cutting
   1. Presentation Layer
    - Web Server, UI 요소들이 포함한다
   2. Application Layer => 3rd party integrations
    - Business Layer, Business Logic 혹은 Domain Logic 이라고 불리며
    Application Server, 데이터 접근을 위한 경로 규격화등의 과정이 포함한다.
   3. Data access Layer
    - Persistence layer 불리는 이 계층은 서비스의 데이터 저장소에 접근/저장을 담당한다.
    Application Layer 는 위 계층과 밀접한 관계 형성하고 저장소의 접근/저장 최적화를 로직을 포함한다.
 ↑↓ Database 🔐

#### Cross-cutting
- 보안, 통신, 운영 관리등을 위한 요소들을 포함한다.

#### 3rd party integrations
- 제 3의 API 서비스를 이용하는 것을 의미 (OAuth 2.0을 이용한 소셜 로그인, PG 결재 기능등)


### 웹 캐쉬 (WEB-CACHE (TIME-SPACE TRADEOFF))

웹 캐쉬란 Client 가 요청하는 Resource(html, image, js, css 등)에 대해 최초 요청 시 파일을 내려받아 특정 위치에 복사본을 저장하는 행위이다.

- 동일한 URL 의 Resource 요청은 내부에 저장한 파일(캐시)을 사용하여 정보를 전달 된다.
  - 동일한 URL 의 Resource 의 경우 서버를 통하지 않아 응답 시간이 감소하고 네트워크 트레픽이 감소되는 이점이 있다.
  - 잘못된 캐시 정책을 사용한다면 실시간으로 제공 되야 할 정보를 올바르게 전달하지 못하고, 오히려 이슈를 발생하는 계기가된다.

#### Header 지시문을 통한 캐싱
- 모든 브라우저에는 HTTP 캐시 구현이 포함되어 있기 때문에 서버 응답이 브라우저에 응답을 캐시할 수 있는 시점,  
  그 기간을 지시하기 위한 올바른 HTTP 헤더 지시문을 제공하는지 확인하면 캐시 적용이 가능하다.
- 일반 적으로 캐싱은 GET 요청 시 처리, 아래 응답 코드를 기준으로 처리 할 수 있다.
  - 2xx (Successful)
  - 3xx (Redirection)
  - 4xx (Client Error)
- 구문
  1. Cache-Control
     - 일반적으로 캐싱은 GET Method 에 대한 응답을 캐싱하는 것으로 제한한다.
     - Cache-Control HTTP 헤더를 통해 캐싱 정책을 정의할 수 있다.
     - Cache-Control 지시문은 응답을 캐시할 수 있는 사용자, 해당 조건 및 기간을 제어한다.
     - Cache-Control 헤더는 HTTP/1.1 사양의 일부로 정의되었고, 응답 캐싱 정책을 정의하는 데 사용된 이전 헤더(ex) Expires)를 대체한다.
     - 모든 최신 브라우저는 Cache-Control 을 지원한다.
     - Cache-Control 는 응답 헤더 뿐만아니라, 요청 헤더로도 사용할 수 있다.
  2. 'no-cache' vs 'no-store'
     - 'no-cache': 캐시된 정보를 사용하기전 전 원(Origin) 서버에 해당 캐시 사용 여부 질의
       ! 프록시 서버(Proxy Server) 정책에 의해 원 서버와의 통신 실패(장애) 발생시 200 OK 응답한다.
     - 'no-store': 민감한 정보를 포함한 내용에 대한 캐시 미사용(최소한의 시간을 보장) 요청
  3. 'public' 및 'private'
     - 'public': 공유(Proxy)에 캐시를 보관해도 된다는 의미
       - max-age: 캐시 유효시간 설정 (초단위)
     - 'private': 브라우저같은 특정 사용자 환경에만 저장하라는 의미
       - 일반적으로 단일 사용자를 대상으로 하므로 Proxy 서버가 응답을 캐시하는 것은 허용되지 않는다.
       - 비공개 사용자 정보가 포함된 HTML 페이지는 사용자의 브라우저가 캐시할 수 있지만, CDN 은 이 페이지를 캐시할 수 없다.
  4. must-revalidate
     - 만료된 캐시만 서버에 확인을 받도록 한다.
       - 프록시 서버(Proxy Server) 정책과 무관 원 서버와의 통신 실패(장애) 발생시 504 Gateway Timeout 응답한다.
     - max-age= <seconds>
       - Ex: Cache-Control: max-age=86400
  5. max-age
     - 캐시 유효시간, 즉 리소스가 유효하다고 판단되는 최대 시간 이다.
     - 초단위로 설정하며, 해당 시간이 지나면 만료된것으로 여겨진다.
       - Ex: Cache-Control: max-age=31536000
  6. Age
     - 캐시 응답 때 나타나는데, max-age 시간 내에서 얼마나 흘렀는지 초 단위로 알려준다.
  7. Expires (하위호환 HTTP.1.0)
     - Cache-Control 과 동시에 동작하진 않지만, Expires 라는 헤더를 지정하여 응답 컨텐츠 만료 시간을 나타낼 수 있다.
       - Ex: Expires: Wed, 23 Jan 2019 03:44:00 GMT
     - Cache-Control 의 max-age 가 있는 경우 이 무시된다.
     - 유효 수명: Expires 헤더에서 Date 헤더의 값을 뺀 결과
  8. Pragma: no-cache (HTTP 1.0 하위 호환)
     - no-cache와 동일
  9. ETag
     - ETag로 캐시된 응답에 대한 유효성 검사 수행한다. (HTTP 컨텐츠가 바뀌었는지를 검사할 수 있는 태그)
     - 서버는 ETag HTTP 헤더를 사용하여 유효성 검사 토큰을 전달한다. 유효성 검사 토큰을 사용하여 리소스 업데이트 검사하며 리소스가 변경되지 않은 경우 데이터가 전송되지 않는다.
     - Expires | max-age 등을 사용하여 특정 시간동안만 캐시를 사용한다고 했을때 특정 시간, 시점이 지나야 브라우저가 새 요청을 발송하고 전체 새 응답을 가져온다.
       그러나, 리소스가 변경되지 않은 경우엔 이미 캐시에 있는 동일한 정보를 다운로드할 이유가 없으므로 이렇게 시간을 지정하여 캐쉬를 갱신하는 작업은 비효율적이다.
     - ETag 헤더에 지정된 유효성 검사 토큰은 바로 이 문제를 해결하기 위해 고안되었다. 서버는 일반적으로 파일 콘텐츠의 해시나 기타 일부 지문인 임의 토큰을 생성하고 반환한다.
       클라이언트는 다음 요청 시 지문을 서버에 전송하기만 하면 된다. 지문이 여전히 동일한 경우 리소스가 변경되지 않고 이 다운로드를 건너뛸 수 있다.
       - Ex: 클라이언트에 최대 120초 동안 캐시하도록 지시하고, 응답이 만료된 후 리소스가 수정되었는지 확인하는 데 사용할 수 있는 유효성 검사 토큰('x456dff')을 제공한다.

#### 캐시 무효화
  - Cache-Control: no-cache, no-store, must-revalidate  
    Pragma: no-cache
