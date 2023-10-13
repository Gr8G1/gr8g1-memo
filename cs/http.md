## HTTP(HyperText Transfer Protocol)

### 특징
#### 비연결성(Connectionless)
클라이언트와 서버 간의 연결을 계속 유지하지 않고, 요청(request)과 응답(response)을 완료한 후에 연결을 끊다. 
이는 매번 새로운 연결을 맺어야 한다는 단점이 있지만, 매번 새로운 연결을 맺음으로써 서버의 자원을 효율적으로 사용할 수 있도록 해준다.

#### 상태 정보 유지 불가능(Stateless)
클라이언트와 서버 간의 상태 정보를 유지하지 않는다. 
즉, 서버는 이전에 처리한 요청과 현재 처리 중인 요청을 구별하지 않는다. 
이러한 상태 정보의 미유지는 서버의 부하를 줄이는 장점이 있지만, 일부 상황에서는 상태 정보를 유지해야 하는 경우가 있다.

#### 무상태(Stateless)
각각의 요청(request)과 응답(response)이 서로 독립적이기 때문에, 
요청과 응답 간에 전달되는 데이터는 그 자체로 완전한 정보이며,다른 요청이나 응답에 영향을 받지 않는다.

#### 요청-응답(Request-Response)
클라이언트에서 요청을 보내면, 서버에서 해당 요청에 대한 응답을 보내는 구조로 되어 있다.

#### 텍스트 기반(Text-based)
텍스트 기반 프로토콜로, 요청과 응답 메시지는 ASCII 텍스트나 유니코드 문자열로 인코딩된다.

#### 단순성(Simplicity)
단순하고 이해하기 쉬운 구조를 가지고 있다. 이를 통해 다른 프로토콜과의 연동이나 기능 확장이 용이하다.

> Wiki HTTP: [https://ko.wikipedia.org/wiki/HTTP](https://ko.wikipedia.org/wiki/HTTP)
> MDN HTTP: [https://developer.mozilla.org/ko/docs/Web/HTTP](https://developer.mozilla.org/ko/docs/Web/HTTP)

### HTTPS(HyperText Transfer Protocol Secure)
HTTP의 보안 버전으로 SSL/TLS 프로토콜을 이용하여 데이터를 암호화하고 보호하여 데이터 무결성을 보장한다.

#### SSL(Secure Sockets Layer)과 TLS(Transport Layer Security)이란?
SSL/TLS는 네트워크 상에서 데이터를 암호화하고 안전하게 전송하기 위한 프로토콜이다.
SSL은 넷스케이프에서 처음 개발되었으며, 이후 TLS가 SSL의 후속 버전으로 개발되어 현재는 TLS가 더 많이 사용된다.
SSL/TLS는 공개키 암호화 방식과 대칭키 암호화 방식을 혼용하여 사용한다.
일반적으로 공개키 암호화 방식은 대칭키 암호화 방식보다 느리지만, 보안성이 더욱 높아 중요한 정보를 암호화할 때 주로 사용된다.
반면 대칭키 암호화 방식은 빠르고 간단하게 암호화할 수 있어 대용량 데이터의 암호화에 주로 사용한다.

##### 암호화 전송 과정
1. 클라이언트는 SSL/TLS를 사용하여 서버와 연결
   - 이 때 클라이언트와 서버는 SSL/TLS 핸드셰이크를 수행합니다.
     핸드셰이크가 완료되면, 클라이언트와 서버는 서로 암호화된 키를 생성, 암호화된 키는 데이터를 암호화하는 데 사용된다.
   > 핸드셰이크(handshake): 양쪽에서 데이터를 안전하게 전송하기 위한 공통 암호화 방식과 인증서 등의 정보를 교환하는 과정
2. 클라이언트와 서버는 이후 데이터를 암호화하여 전송
   - 데이터는 대칭키 암호화 방식을 사용하여 암호화된다.
3. 클라이언트와 서버는 데이터를 주고받으면서 암호화된 키를 변경합니다.
   - 이를 통해, 중간에 누군가가 키를 가로채더라도, 그 키는 일회성으로 사용되기 때문에, 데이터의 무결성(안전성)이 보장된다.
4. 데이터 전송이 끝나면, 클라이언트와 서버는 연결을 종료
   - 이 때, 연결 종료 메시지도 SSL/TLS 프로토콜을 이용하여 안전하게 전송된다.

### HTTP(HyperText Transfer Protocol)와 HTTPS(HyperText Transfer Protocol Secure)의 차이
웹에서 데이터를 주고받기 위한 프로토콜이지만, 다음과 같은 차이점이 있다

#### 보안(Security)
HTTP는 암호화되지 않은 평문 통신을 하기 때문에, 요청과 응답 데이터가 악의적인 공격자에게 노출될 가능성이 있다.
반면, HTTPS는 SSL(Secure Sockets Layer)이나 TLS(Transport Layer Security) 프로토콜을 이용하여 데이터를 암호화하고 보호하기 때문에, 보안성이 높다.

#### 포트(Port)
HTTP는 80번 포트를 사용하고, HTTPS는 443번 포트를 사용한다.

#### 인증서(Certificate)
HTTPS는 SSL/TLS 인증서를 사용하여 서버의 신원을 인증하고, 클라이언트와 서버 간의 안전한 통신을 보장한다.

#### 속도(Speed)
HTTPS는 암호화와 복호화를 위한 추가 작업이 필요하기 때문에, HTTP에 비해 속도가 다소 느릴 수 있다.
그러나 최근에는 암호화 방식의 개선과 하드웨어 지원 등으로 인해 HTTPS의 성능이 대부분의 경우 HTTP와 비슷하거나 더 빠른 속도를 낸다.

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
|:---------:|:---------------------------------------------:|:---------------------------------------:|
|  저장 위치  |              클라이언트(=접속자 PC)                |                 웹 서버                   |
|  저장 형식  |                text(key-value)                |                 Object                  |
|  만료 시점  |             저장시 설정(만료 시간 기준)              |    브라우저 종료시 삭제(기간 지정 가능)           |
|    자원    |                 클라이언트 리소스                  |             웹 서버 리소스                  |
|  용량 제한  |   총 300개 (도메인 당 20개) 개별 4KB(=4096byte)     |     서버의 허용 범위(용량제한 없음)             |
|    속도    |                     빠름                       |                  느림                     |
|    보안    |                     낮음                       |                  높음                     |

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
CORS(Cross-Origin Resource Sharing)는 웹 애플리케이션에서 다른 도메인의 리소스에 접근하는 것을 허용하기 위한 보안 기술 및 정책입니다. 
웹 페이지는 보안상의 이유로 동일 출처 원칙(Same-Origin Policy)에 따라 다른 도메인의 리소스에 직접 접근하지 못하도록 제한됩니다. 
그러나 CORS를 사용하면 웹 애플리케이션은 동일 출처 원칙을 우회하여 다른 도메인의 리소스에 안전하게 접근할 수 있습니다.

Origin (출처)
웹 페이지가 로드되는 도메인을 나타냅니다. 
이것은 프로토콜 (HTTP 또는 HTTPS), 호스트 (도메인 또는 IP 주소), 포트 (포트 번호)의 조합으로 구성됩니다. 
예를 들어, https://www.example.com은 하나의 출처입니다.

CORS 요청 (CORS Request)
웹 페이지에서 다른 도메인의 리소스를 요청하는 HTTP 요청입니다. 
이러한 요청은 브라우저에 의해 보내집니다.
주요한 CORS 요청은 다음과 같습니다.

- Simple Request (간단한 요청): HTTP 메서드 중 GET, HEAD, POST 중 하나를 사용하고, 특정 헤더를 제외하고 표준 헤더만을 사용하는 요청입니다. 브라우저는 이러한 요청을 자동으로 처리하고 승인합니다.
- Preflight Request (사전 요청): 복잡한 요청에 해당하며, 브라우저는 사전 요청을 보내서 서버에서 이 요청을 허용하는지 확인합니다.

CORS 응답 (CORS Response)
서버에서 브라우저로 보내지는 응답입니다. 
이 응답은 다른 도메인의 리소스에 대한 접근 권한 및 허용되는 도메인을 명시합니다. 
CORS 응답은 다음과 같은 헤더를 포함할 수 있습니다.

- Access-Control-Allow-Origin: 요청을 허용하는 출처(도메인) 목록을 지정합니다. 이 헤더가 없거나 출처에 포함되지 않으면 브라우저는 접근을 거부합니다.
- Access-Control-Allow-Methods: 허용된 HTTP 메서드를 지정합니다.
- Access-Control-Allow-Headers: 허용된 요청 헤더를 지정합니다.
- Access-Control-Allow-Credentials: 인증 정보 (쿠키, HTTP 기본 인증 등)를 요청에 포함시킬 수 있는지 여부를 나타냅니다.

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
- 304: (Not Modified) 클라이언트에게 리소스가 수정되지 않았음 알림. 클라이언트는 로컬 PC에 저장된 캐시를 재사용하도록 유도 (캐시로 리다이트)
  > **304 응답값 본문을 포함할 수 없다.**
- 307: (Temporary Redirect) 요청 메소드 및 본문 내용 유지
- 308: (Permanent Redirect) 요청 메소드 및 본문 내용 유지

- 영구 리다이렉션: 301, 308
  - 목적: 리소스의 URL이 영구적으로 변경되었을 때 사용된다. 클라이언트는 이후에도 항상 새로운 URL로 요청을 보내야 한다.
  - 캐시: 브라우저와 프록시 서버 등은 영구 리다이렉션 응답을 캐시하고, 다음에 같은 URL에 대한 요청이 오면 자동으로 새 URL로 리다이렉션한다.
- 일시 리다이렉션: 302, 303, 307
  - 목적: 리소스의 URL이 일시적으로 변경되었을 때 사용된다. 클라이언트는 임시 리다이렉션 응답을 받으면 즉시 새 URL로 이동하며, 이후 요청은 원래 URL로 보낼 수 있다.
  - 캐시: 브라우저와 프록시 서버는 일시 리다이렉션 응답을 캐시하지 않고, 다음에 같은 URL에 대한 요청이 오면 리다이렉션을 따르지 않습니다.

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
