## Protocol 
데이터의 전송과 처리를 이벤트를 기반으로 수행하는 프로토콜을 의미한다.
일반적으로 이벤트 기반 프로토콜은 비동기적인 특성을 가지며, 빠른 응답(오류 포함) 시간과 높은 확장성을 제공한다.

### Server-Sent Events(SSE)
클라이언트가 서버에게 "이벤트 스트림(Event Stream)"을 요청하면, 서버는 지속적으로 이벤트 데이터를 전송하게 된다. 
이때 이벤트는 "event", "data", "id", "retry"와 같은 필드를 포함한 텍스트 형식으로 전송된다.

> NOTE: 단방향 통신을 지원, 서버에서 클라이언트로만 데이터를 전송할 수 있다.

- 장점
  1. 실시간 업데이트
     - 서버에서 데이터를 보내고, 클라이언트에서 이를 실시간으로 업데이트할 수 있다. 
     - 이를 통해 실시간으로 변경되는 데이터에 대한 처리가 가능해지며, 빠른 응답 시간과 높은 확장성을 제공한다.
  2. 비교적 간단한 구현
      - HTTP 프로토콜을 기반으로 하기 때문에, 기존의 웹 애플리케이션 인프라를 그대로 사용할 수 있다.
  3. 클라이언트 호환성
     - 대부분의 최신 브라우저에서 SSE를 지원하며, 서버 측에서도 구현이 비교적 간단하여 쉽게 사용할 수 있다.
  4. 에러 핸들링
     - 에러 핸들링을 지원, 서버에서 에러가 발생하면 클라이언트로 에러 메시지를 전송하고, 클라이언트에서는 에러를 처리할 수 있습니다.

- 단점
  1. 단방향 통신
     - 서버에서 클라이언트로만 데이터를 전송, 따라서 클라이언트에서 서버로 데이터를 전송하는 양방향 통신은 지원하지 않는다.
  2. 연결 유지
     - 지속적인 연결을 유지, 이는 서버 측에서 부하를 일으킬 수 있으며, 대규모 애플리케이션에서는 스케일링 문제가 발생할 수 있다.
  3. 안정성
     - TCP 연결을 사용하므로, 인터넷 환경이 불안정한 경우 연결이 끊어질 수 있다. 이러한 경우, 클라이언트는 다시 연결을 시도해야 한다.
  4. 보안
     - 기본적으로 암호화되지 않은 HTTP 연결을 사용한다. 따라서 보안이 중요한 데이터를 전송할 경우, SSL/TLS와 같은 보안 프로토콜을 사용해야 한다.

### WebSocket
클라이언트와 서버 간에 일반적인 HTTP 요청-응답 흐름과 달리, 클라이언트와 서버 간의 지속적인 연결을 유지하고, 양방향 통신을 가능하게 하는 프로토콜이다.

> NOTE: 클라이언트와 서버의 양방향 통신을 지원, 이벤트(요청)는 서버나 클라이언트 측에서 생성되거나 수신될 수 있다.

- 장점
  1. 실시간 양방향 통신
     - 실시간 채팅, 온라인 게임, 주식 거래 등과 같이 실시간 데이터 처리가 필요한 애플리케이션 구현에 사용할 수 있다.
  2. 빠른 전송 속도
     - HTTP의 헤더를 최소화하고, 데이터 프레임의 압축과 복호화 등의 기술을 사용하여 빠른 전송 속도를 보장한다.
  3. 쉬운 구현
     - 웹 브라우저에서 기본적으로 지원하므로, 클라이언트 측에서 별도의 라이브러리를 설치하지 않아도 쉽게 구현할 수 있다.
  4. 상태 유지가 가능
     - 연결이 한 번 맺어지면, 연결이 유지되는 동안 상태를 유지할 수 있다. 이를 통해, 연결 정보나 인증 정보 등을 유지할 수 있다.
  5. 보안
     - SSL/TLS와 같은 보안 프로토콜을 사용하여 데이터 보안을 보장할 수 있다.
- 단점
  1. 지원되지 않는 브라우저 존재
     - 최신 웹 브라우저에서는 지원되지만, 일부 오래된 브라우저에서는 지원되지 않을 수 있다.
  2. 서버 리소스 소비
     - 연결은 상태 유지를 위해 서버의 리소스를 지속적으로 사용하게 된다. 따라서, 대규모 연결이 발생할 경우 서버 측에서 리소스를 관리해야 할 필요가 있다.
  3. 보안 취약점 존재
     - 연결은 서버와 클라이언트 간의 데이터 전송이 이루어지므로, 보안 취약점이 발생할 수 있다. 따라서, 적절한 보안 대책이 필요하다.
