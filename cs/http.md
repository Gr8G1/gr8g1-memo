## HTTP
#### 특징
- Connectionless 프로토콜 (비연결지향)
  - 클라이언트가 서버에 요청을 했을 때, 그 요청에 맞는 응답을 보낸 후 연결을 끊는 처리방식이다.
  - HTTP 1.1 버전에서 연결을 유지하고, 재활용 하는 기능이 Default 로 추가되었다. (keep-alive 값으로 변경 가능)
- Stateless 프로토콜 (상태정보 유지 안함)
  - 클라이언트의 상태 정보를 가지지 않는 서버 처리 방식이다.
  - 클라이언트와 첫번째 통신에서 데이터를 주고 받았다 해도, 두번째 통신에서 이전 데이터를 유지하지 않는다.

> Wiki HTTP: [https://ko.wikipedia.org/wiki/HTTP](https://ko.wikipedia.org/wiki/HTTP)
> MDN HTTP: [https://developer.mozilla.org/ko/docs/Web/HTTP](https://developer.mozilla.org/ko/docs/Web/HTTP)

### HTTP Method 특징
#### GET(Request has body: N)
- 특정 리소스의 표시를 요청합니다. GET을 사용하는 요청은 오직 데이터를 받기만 한다.
- GET 요청에 본문이나 페이로드가 담겨있으면 명세에는 금지되어있지 않지만, 의미가 정의되지 않아 기존에 존재하는
  구현체에게 요청을 거부당할수 있다. 이러한 이유로 GET 요청에는 본문이나 페이로드를 담지 않는 것을 권장한다.
- 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/GET](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/GET)

#### HEAD(Request has body: N)
- GET 메서드의 요청과 동일한 응답을 요구하지만, 응답 본문을 포함하지 않는다.
- 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/HEAD](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/HEAD)

#### POST(Request has body: Y)
- POST 메서드는 특정 리소스에 엔티티를 제출할 때 쓰입니다. 이는 종종 서버의 상태의 변화나 부작용을 일으킨다.
- 보통 HTML 양식을 통해 서버에 전송 이떄 콘텐츠 유형(Content-Type)은 *<form> 요소의 enctype 특성이나 <input>, <button> *요소의 formenctype 특성 안에 적당한 문자열을 넣어 결정
- 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/POST](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/POST)

#### PUT(Request has body: Y)
- 요청 페이로드를 사용해 새로운 리소스를 생성하거나, 대상 리소스를 나타내는 데이터를 대체한다.
- 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/PUT](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/PUT)

#### PATCH(Request has body: Y)
- 리소스의 부분만을 수정하는 데 쓰인다.
- 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/PATCH](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/PATCH)

#### DELETE(Request has body: May)
- DELETE 메서드는 특정 리소스를 삭제한다.
- 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/DELETE](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/DELETE)

#### CONNECT(Request has body: N)
- CONNECT 메서드는 목적 리소스로 식별되는 서버로의 터널을 맺습니다.
- 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/CONNECT](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/CONNECT)

#### OPTIONS(Request has body: N)
- 목적 리소스의 통신을 설정하는 데 쓰입니다.
- 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/OPTIONS](https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/OPTIONS)

#### TRACE (en-US) (Request has body: N)
- 목적 리소스의 경로를 따라 메시지 loop-back 테스트를 합니다.
- 정의: [https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/TRACE](https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods/TRACE)

#### **GET과 POST의 차이**
1. 특징:
   - GET:
     - 캐시가 가능하다.
       - 웹 캐시가 요청을 가로채 서버로부터 리소스를 다시 다운로드하는 대신 리소스의 복사본을 반환한다.
       - HTTP 헤더에서 cache-control 헤더를 통해 캐시 옵션을 지정할 수 있다.
     - 브라우저 히스토리에 남는다.
     - 북마크 될 수 있다.
     - HTTP 자체는 GET 방식의 URL 길이에 제약을 두고 있지 않지만, 브라우저에서 최대 길이를 제한하고 있다. (브라우저별 상이)
       - 전송 데이터(쿼리 스트링)의 제한된 길이를 초과할경우 절삭되며 해당 데이터는 전송되지 않는다.
     - 중요한 정보(보안 요소)를 포함 시키지 않아야한다.
       - 쿼리스트링은 브라우저상에 직접적으로 노출되어 보안정보 유출에 우려가있고
         공용 컴퓨터 사용시 브라우저 캐시와 히스토리에 정보들이 지속적으로 남아있을 수 있기때문이다.
       - 데이터 조회, 검색에 유리하다.
   - POST:
     - 캐시되지 않는다.
     - 브라우저 히스토리에 남지 않는다.
     - 북마크 되지 않는다.
     - 전송 데이터(Http body)의 길이에 제한이 없다. (비교적 대용량이라 표현할 수 있다.)
2. 사용목적:
   - GET: 데이터 요청
   - POST: 데이터 생성 | 업데이트
3. 리소스 전달 방식:
   - GET: 쿼리스트링
   - POST: HTTP Body
4. 멱등성 (idempotent):
   - GET: 멱등 O
     - GET: 한 번 조회하든, 두 번 조회하든 같은 결과가 조회된다.
     - PUT: 결과를 대체한다. 따라서 같은 요청을 여러번 해도 최종 결과는 같다.
     - DELETE: 같은 요청을 여러번 해도 삭제된 결과는 똑같다.
       - 자동 복구 메커니즘
         - "서버가 Timeout 등으로 기타 이슈로 인해 정상 응답값을 생성하지 못했을때, 클라이언트가 같은 요청을 다시 해 도 되는가?" 의 판단의 근거가 되는것이 멱등이다.
  - POST: 멱등 X
    - 멱등 사전적 정의: 연산을 여러 번 적용하더라도 결과가 달라지지 않는 성질을 의미 (f(f(x)) = f(x))

#### **PUT과 POST의 차이**
멱등성으로, PUT은 멱등성을 가진다. 즉, 부수 효과(side effect)가 없다.

### HTTP Header
> HTTP 헤더 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Headers](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers)

> HTTP 헤더 정의: [https://developer.mozilla.org/ko/docs/Web/HTTP/Headers](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers)

#### 커스텀 헤더(Custom Header)
HTTP Request에 기본적으로 포함되어 있는 헤더 정보는 개발자가 컨트롤 해야 될 경우가 생각보다 많지 않다.
하지만 특정 상황(클라이언트 요구조건)에 의해 조종 추가해야될 부가적인 정보를 전달하는 경우가 발생한다.

#### 커스텀 헤더 네이밍(Naming)
2012년 이 전에는 커스텀 헤더에 ‘X-’라는 **Prefix**를 추가하는 것이 관례였으나, 이 관례는 문제점이 발생할 가능성이 높아서 더 이상 사용하지 않다. (Deprecated)
다만, 커스텀 헤더의 이름을 지을 때, 이 헤더를 사용하는 측에서 헤더의 목적을 쉽게 이용할 수 있도록 대시(-)를 기준으로 의미가 명확한 용어 사용을 권장한다.
대시(-)를 기준으로 각 단어의 첫 글자를 대문자로 작성하는 것이 관례이나 **Spring**에서 Request 헤더 정보를 확인할 때, 대/소문자를 구분하지는 않는다.

### Cookie & Session
#### 사용 이유
Stateless 한 HTTP 프로토콜의 특징과 대비되는 Stateful 한 상태를 위해 사용.

#### 특징
|           |                    Cookie                     |                 Session                 |
|-----------|:---------------------------------------------:|:---------------------------------------:|
| 저장 위치 |            클라이언트(=접속자 PC)             |                 웹 서버                 |
| 저장 형식 |                text(key-value)                |                 Object                  |
| 만료 시점 |          저장시 설정(만료 시간 기준)          |  브라우저 종료시 삭제(기간 지정 가능)   |
| 자원      |               클라이언트 리소스               |             웹 서버 리소스              |
| 용량 제한	| 총 300개 (도메인 당 20개) 개별 4KB(=4096byte) |     서버의 허용 범위(용량제한 없음)     |
| 속도      |                     빠름                      |                  느림                   |
| 보안      |                     낮음                      |                  높음                   |

- HTTP 쿠키(웹 쿠키, 브라우저 쿠키)는 서버가 사용자의 웹 브라우저에 전송하는 작은 데이터 조각으로,
브라우저는 그 데이터 조각들을 저장해 놓았다가, 동일한 서버에 재 요청 시 저장된 데이터를 함께 전송한다.
- <Header>Set-Cookie: <cookie-name> = <cookie-value> Options= ..; , ..;
- 옵션
  - Domain & Path
    - Domain: 쿠키의 도메인 옵션과 서버의 도메인이 일치해야만 쿠키를 전송할 수 있도록 한다.
    - Path: 세부 경로를 설정하여 일치하면 쿠키를 전송 할 수 있도록 한다.
       > 세부 경로 미 설정시 기본값인 '/' 적용된다.
  - Secure
    - 프로토콜에 따른 쿠키전송 여부를 결정합니다.
      만약 해당 옵션이 true로 설정된 경우, 'HTTPS' 프로토콜을 이용하여 통신하는 경우에만 쿠키를 전송 할 수 있다.
  - MaxAge or Expires
    - MaxAge: 초 단위 형식으로 쿠키의 유효시간을 설정하는 옵션
    - Expires: Date 형식으로 쿠키의 유효시간을 설정하는 옵션
      - 세션 쿠키: MaxAge 또는 Expires 옵션이 없는 쿠키로, 브라우저가 실행 중일 때 사용할 수 있는 임시 쿠키 (브라우저 종료시 삭제됨)
      - 영속성 쿠키: 브라우저의 종료 여부와 상관없이 MaxAge 또는 Expires에 지정된 유효시간만큼 사용가능한 쿠키
  - HttpOnly
    - 자바스크립트를 통해 쿠키 값에 접근하는 것을 막는다.
  - SameSite (실험적)
    - cross-site 요청과 함께 전송되지 않았음을 요구하게 만들어, cross-site 요청 위조 공격(CSRF)에 대한 보호 방법을 제공한다.
      - Lax: 사이트가 서로 달라도, GET 요청이라면 쿠키 전송이 가능하다.
      - Strict: 사이트가 서로 다르면, 쿠키 전송을 할 수 없다.
      - None: 사이트가 달라도, 모든(GET, POST, PUT 등등) 요청에 대해 쿠키 전송이 가능하다.

> Cookie : [https://ko.wikipedia.org/wiki/HTTP_%EC%BF%A0%ED%82%A4](https://ko.wikipedia.org/wiki/HTTP_%EC%BF%A0%ED%82%A4)
> Session: [https://ko.wikipedia.org/wiki/%EC%84%B8%EC%85%98_(%EC%BB%B4%ED%93%A8%ED%84%B0_%EA%B3%BC%ED%95%99)](https://ko.wikipedia.org/wiki/%EC%84%B8%EC%85%98_(%EC%BB%B4%ED%93%A8%ED%84%B0_%EA%B3%BC%ED%95%99))
> Set-Cookie: [https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Set-Cookie](https://developer.mozilla.org/ko/docs/Web/HTTP/Headers/Set-Cookie)

### CORS
브라우저의 자발적 보안조치 (특정 확장 프로그램으로 CORS 사용을 중지할 수 있다)
> Cors: [https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)

### HTML Form 제약
  - Form 전송 메소드는 GET, POST 메소드만 지원
    - 위 제약 사항으로 인해 API 설계시 GET, POST 만으로 작업이 이루어진다.

### 참고 할 만한 URI 설계 개념: [https://restfulapi.net/resource-naming/](https://restfulapi.net/resource-naming/)
1. Document
   - 단일 개념(파일, 객체 인스턴스, 데이터베이스 Row)
     - Ex: /members/100, /files/favicon.png
2. Collection
   - 서버가 관리하는 리소스 (서버가 Resource URI 생성 및 관리)
      - Ex: /members
3. Store
   - 클라이언트가 관리하는 저장소 (클라이언트가 Resource URI를 알며 직접 관리)
     - Ex: /files
4. Controller URI
   - 위 개념들로 해결하기 어려운 추가 프로세스 (동사를 사용)
     - Ex: /members/{id}/edit, /members/{id}/delete

### 상태 코드
#### 2xx (Successful)
- 200: (OK) 요청을 성공적으로 처리
- 201: (Created) 요청을 성공적으로 처리 및 새로운 리소스가 생성됨 (Header - Location)
- 202: (Accepted) 요청을 접수 처리는 완료되지 않음 (배치와 같은 상황에서 발생 특정 시간 이후 처리 프로세스를 진행)
- 204: (No Content) 요청을 성공적으로 처리 했지만 요청 반환 본문에 반환할 컨텐츠 없음

#### 3xx (Redirection - 응답의 결과에 Location 헤더가 포함되어있다면, Location 으로 리다이렉션 처리)
- 300: (Multiple Choices) 사용 X
- 301: (Moved Permanently) 요청 메소드 GET으로 변경, 본문 내용이 삭제될 수 있음
- 302: (Found) 요청 메소드 GET으로 변경, 본문 내용이 삭제될 수 있음
- 303: (See Other) 302 동일 한 기능
- 304: (Not Modified) 클라이언트에게 리소스가 수정되지 않았음 알림. 클라이언트는 로컬 PC에 저장된 캐시를 재사용 유도 (캐시로 리다이트)
  > **304 응답값 본문을 포함 하면 안된다.**
- 307: (Temporary Redirect) 요청 메소드 및 본문 내용 유지
- 308: (Permanent Redirect) 요청 메소드 및 본문 내용 유지
  > 영구 리다이렉션: 301, 308
  > 일시 리다이렉션: 302, 303, 307
  > PRG: Post / Redirect / Get
  > Post 메소드의 중복 요청에 의한 오류 방시를 위한 리다이렉션 전략
  
#### 4xx (Client Error)
- 400: (Bad Request) 요청을 처리할 수 없음
- 401: (Unauthorized) 인증되지 않음 (인증 실패)
- 403: (Forbidden) 요청은 정보는 이해, 승인은 거절 (인증 자격 증명은 있지만, 접근 권한이 불충분한 경우)
- 404: (Not Found) 요청 리소스를 찾을 수 없음
- 405: (Method Not Allowed) 지원하지 않는 요청 메소드

#### 5xx (Server Error)
    - 500: (Internal Server Error) 서버 내부 오류
    - 503: (Service Unavailable) 서비스 이용 불가 (일시적인 과부하 또는 예정된 작업으로 잠시 요청을 처리할 수 없음)
      > Retry-After 헤더 필드에 복구 시간을 알릴 수 있음 (현실적으로 예측 불가)
      > 응답 코드 참조: [https://developer.mozilla.org/ko/docs/Web/HTTP/Status](https://developer.mozilla.org/ko/docs/Web/HTTP/Status)


### REST API
#### REST(Representational State Transfer) API란?
REST API는 서로 다른 시스템에서 인터넷을 통해 정보(자원)를 안전하게 교환하기 위해 사용하는 인터페이스이다.

> 로이 필딩(Roy Thomas Fielding: HTTP 사양의 주요 작성자 중 한 명이며 REST(Representational State Transfer) 아키텍처 스타일 의 창시자)에 의해 제시되었다.
  논문에 서술된 균일한 인터페이스는 REST의 필수 요소로 모든 단계를 충족해야 REST API 라고 부를 수 있다.
  REST Blog: https://www.ics.uci.edu/~fielding/pubs/dissertation/rest_arch_style.htm
  REST Design Guide: https://blog.restcase.com/5-basic-rest-api-design-guidelines/
  
> REST 란?: API 작동 방식에 대한 조건을 부과하는 소프트웨어 아키텍처

#### **RESTful이란?**
REST(아키텍처) 원리를 따르는 시스템 또는 제공하는 웹 서비스를 ‘RESTful’하다 표현 또는 지칭(비공식)

#### RESTful의 목적
RESTful한 API 구현 목적은 성능 향상이 아닌 일관적인 컨벤션을 통한 API의 이해도 및 호환성을 높이는 것이 주 동기
> 이해하기 쉽고 사용하기 쉬운 REST API를 만드는 것

#### RMM(Richardson Maturity Model): REST 성숙도 모델
레오나르드 리차드슨(Leonard Richardson)에 의해 정의되었고 총 4단계(0~3단계)로 구성되어있다.
  - 3단계까지 지키기 어렵기 때문에 2단계까지 지켜진다면 좋은 API라 볼 수 있고 이를 HTTP API라고 부른다.
  - HTTP API : 2단계까지 준수
  - REST API : 모든 단계 준수

#### 단계별 설명
- 0단계(The Swamp of POX(Plain of XML)): HTTP 사용
  - 단일 URI 를 가지며 HTTP 프로토콜 사용한다.  
    이는 REST API 작성의 기본단계로 해당 API는 REST API라 부를 수 없다.

- 1단계(Resources): URI Resource
  - 요청을 특정 서비스 End-point로 보내는 것이 아닌, 개별 리소스로 통신 가능한 고유 End-point를 제공해야한다.
    - Ex: 뉴스 리소스를 예를 들어 /news 로 모든 요청을 전달하는 것이 아닌, /news/(topic, 1~100) 과 같은 분류(계층) 또는 식별자를 포함하는 개별 리소스를 만들어
      topic, 1~100번 리소스를 이해할 수 있도록 URI를 설계한다.
    - **End-point 작성시 주의사항**
      - 리소스에 집중한 명사 단어 선택 / 동사, HTTP 메서드, 특정 행위에 대한 단어 선택은 지양한다.
      - 모든 엔드포인트 작성시에 동사나 특정 행위에 대한 단어를 배제 할 수 없다.  
        따라서 이를 포함하는 URI는 컨트롤 URI 라 칭하며 적당한 사유와 함께 사용되어야한다.

- 2단계(HTTP Verbs): URI Resources + HTTP Method(CRUD에 적합하게 작성)
  - GET /news/topic/(topic, 1~100)/comments?from=20220101&to=20220201 처럼 리소스와 HTTP Method로 요청대상과 목적을 파악할 수 있다.
    - Status Code로 Client-Server간 커뮤니케이션 진행
      - [WN - 상태 코드 참조](#wn---상태-코드)

- 3단계(Hypermedia Controls): URI Resource + HTTP Method + HATEOAS
  - 호출 가능한 API 정보를 자원의 상태에 반영하여 표현하는것
   - Ex: GET 요청으로 반환된 리소스 표현에 그 리소스에 대한 액션의 링크도 함께 포함하여 보내는 것.
     - HATEOAS 제약 조건은 REST의 "균일한 인터페이스"기능의 필수 부분이다.
       - HATEOAS(Hypermedia As The Engine Of Application State)
         하이퍼미디어를 애플리케이션의 상태를 관리하기 위한 하나의 메커니즘으로 사용

#### REST 아키텍처 스타일과 원칙들
- 균일한 인터페이스(4가지 아키텍처 제약 조건 = REST 성숙도 참조)
  1. 균일한 리소스 식별자를 사용
  2. 리소스 수정/삭제 정보 수신
     - 서버는 리소스를 자세히 설명하는 메타데이터를 전송하여 이 조건을 충족한다.
  3. 추가 처리하는 방법에 대한 정보를 수신
     - 서버는 클라이언트가 리소스를 적절히 사용하는 방법에 대한 메타데이터가 포함된 명확한 메시지를 전송한다.
  4. 작업 완료 정보 수신
     - 서버는 클라이언트가 더 많은 리소스를 동적으로 검색할 수 있도록 표현에 하이퍼링크를 넣어 전송한다.
- 무상태
  - 서버가 이전의 모든 요청과 독립적으로 모든 클라이언트 요청을 완료하는 통신 방법을 나타낸다.
    클라이언트는 임의의 순서로 리소스를 요청할 수 있으며 모든 요청은 무상태이거나 다른 요청과 분리된다.
    이 REST API 설계 제약 조건은 서버가 매번 요청을 완전히 이해해서 이행할 수 있음을 의미한다.
- 계층화 시스템
  - 클라이언트 <=> 서버 사이의 다른 승인된 중개자에게 연결할 수 있으며 여전히 서버로부터도 응답을 받고 서버는 요청을 다른 서버로 전달할 수도 있다.
    클라이언트 요청을 이행하기 위해 함께 작동하는 보안, 애플리케이션 및 비즈니스 로직과 같은 여러 계층으로 여러 서버에서 실행되도록 RESTful 웹 서비스를 설계할 수 있다.
    이러한 계층은 클라이언트에 보이지 않는 상태로 유지된다.
- 캐시 가능성
  - RESTful 웹 서비스는 서버 응답 시간을 개선하기 위해 클라이언트 또는 중개자에 일부 응답을 저장하는 프로세스인 캐싱을 지원한다.
- 온디맨드 코드
  - REST 아키텍처 스타일에서 서버는 소프트웨어 프로그래밍 코드를 클라이언트에 전송하여 클라이언트 기능을 일시적으로 확장하거나 사용자 지정할 수 있다.

#### REST API 사용시 이점
- 확장성
   1. 클라이언트-서버 상호 작용: 최적화 및 효율적으로 크기 조정
   2. 무상태: 서버가 과거 클라이언트 요청 정보를 유지할 필요가 없기 때문에 서버 로드를 제거
   3. 캐싱: 일부 클라이언트-서버 상호 작용을 부분적으로 또는 완전히 제거
- 유연성
  - RESTful 웹 서비스는 완전한 클라이언트-서버 분리를 지원
- 독립성
  - REST API는 사용되는 기술과 독립적이다.
    API 설계에 영향을 주지 않고 다양한 프로그래밍 언어로 클라이언트 및 서버 애플리케이션을 모두 작성할 수 있다.
    또한 통신에 영향을 주지 않고 양쪽의 기본 기술을 변경할 수 있다.

#### REST 요청에 포함되는것
- 고유 리소스 식별자
  - 일반적으로 URL(Uniform Resource Locator)을 사용하여 리소스 식별을 수행한다.
- 메서드
  1. GET
     - 서버의 지정된 URL에 있는 리소스에 액세스
       요청을 캐싱하고 파라미터를 넣어 전송하여 전송 전에 데이터를 필터링하도록 서버에 지시 가능
  2. POST
     - 서버에 데이터를 전송
  3. PUT
     - 서버의 기존 리소스를 업데이트
        > POST와 달리, 동일한 PUT 요청을 여러 번 전송해도 결과는 동일
  4. DELETE
     - 리소스를 제거
  - HTTP 헤더
    - 요청 헤더는 클라이언트와 서버 간에 교환되는 메타데이터
  - 데이터
    - POST, PUT 및 기타 HTTP 메서드가 성공적으로 작동하기 위한 데이터가 포함될 수 있다.
  - 파라미터
    - 수행해야 할 작업에 대한 자세한 정보를 서버에 제공하는 파라미터가 포함될 수 있다.
      1. URL 세부정보를 지정하는 경로 파라미터 (/resource/1/10234)
      2. 리소스에 대한 추가 정보를 요청하는 쿼리 파라미터 (?name=john)
      3. 클라이언트를 빠르게 인증하는 쿠키 파라미터
