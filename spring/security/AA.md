## 인증(Authorization) & 권한(Authorization)

### HTTPS
  - HTTP Secure Socket layer의 약자로 HTTP over SSL(TLS), HTTP over Secure라고 부르기도 한다.
  - HTTP 요청을 SSL or TLS라는 알고리즘을 이용해, HTTP 통신을 하는 과정에서 내용을 암호화하여 데이터를 전송하는 방법

#### HTTPS 사용 목적
- 교환되는 데이터를 암호화하여 제 3자가 서버와 클라이언트가 주고받는 정보를 탈취할 수 없도록 안전하게 보호하기 위함
  - HTTP 요청 및 응답은 중간자 공격에 취약하다.
  > 중간자 공격: [https://en.wikipedia.org/wiki/Man-in-the-middle_attack](https://en.wikipedia.org/wiki/Man-in-the-middle_attack)
- 데이터 전송중 의도/비의도적으로 감지 및 탐지 되지 않고 수정되거나 손상될 수 없도록 데이터 무결성을 보장하기 위함
- 인증서를 통한 사용자가 의도한 웹 사이트와 통신하고 있음을 알리고 사용자의 정보를 보호하기 위함

#### Localhost HTTPS 적용 해보기
mkcert 설치 및 PKCS12 인증서 생성
```bash
$ brew install mkcert
$ mkcert -pkcs12 localhost
```
- Spring web proejct -> resources/localhost.p12 cmd + c ~ v
- Spring web proejct -> resources/application.properties
  - server.ssl.key-store=classpath:localhost.p12
  - server.ssl.key-store-type=PKCS12
  - server.ssl.key-store-password=changeit
- Run application

### SSL(Secure Socket layer)
  - 웹사이트와 브라우저(혹은 두 서버) 사이에 전송된 데이터를 암호화하여 인터넷 연결의 보안을 유지하는 표준 기술
  - CA로부터 SSL 인증서를 구매, 발급 받아야 한다.

### TLS(Transport Layer Security)
  - 서버와 클라이언트 간의 CA를 통해 서버를 인증하는 과정과 데이터를 암호화하는 과정을 아우른 프로토콜을 TLS 또는 SSL이라고 말한다.
    > SSL과 TLS는 사실상 동일한 규약을 뜻하며 SSL 이 표준화되며 바뀐 이름이 TLS 이다.

### CA(Certificate Authority)
  - 인증서를 발급/보증 할 수 있는 엄격하게 공인된 기관을 말한다.

### 대칭키와 공개키(비대칭키)
  - 대칭키
    - 암호화를 진핼할 때 사용되는 비밀번호와 같은 역할을 한다.
      이 키에 따라 암호화된 결과가 달라지기 때문에 키를 모르면 복호화 또한 할 수 없다.
      대칭키는 암호화에 사용되는 키와 복호화에 사용되는 키가 동일일 암호화 기법이다.
    - 대칭키 방식의 치명적 단점
      - 암호화된 메세지를 상대가 복호화 시킬 수 있게 하기위해서는 적어도 한번은 이 대칭키의 공유가 이루어져야 하는데,
        이 과정에서 대칭키가 유출되면 키를 획득한 사람은 누구나 복호화를 할 수 있기 때문에 암호화가 무용지물이 되기 때문이다.
  - 공개키(비대칭키)
    - 공개키 방식은 두개의 키를 생성 A키를 사용해 암호화를 하면 B키로 복호화를 할 수 있고, B키로 암호화를 하면 A로 복호화를 할 수 있는 방식이다.
      - 일반적으로 A키는 공개(Public)키, B키는 비공개(Private)키라 칭한다.
      - 공개키로 암호화한 내용을 공개키로 복호화를 진행 할 수 없다.
      - 비공개키로 암호화한 내용은 전자서명의 성격을 띈다.

### Hashing
  - 클라이언트가 서버로부터 인증 절차를 진행할때 설정한 사용자 비밀번호를 암호화해서 데이터베이스에 저장하는 행위를 Hashing이라고 한다.
    - 어떠한 문자열에 임의의 연산을 적용하여 다른 문자열로 변환하는 것

#### Hashing 3가지의 철칙
  - 모든 값에 대해 해시 값을 계산하는데 오래걸리지 않아야 한다.
  - 최대한 동일한 해시 값을 피해야 하며, 모든 값은 고유한 해시 값을 가져야 한다.
  - 아주 작은 단위의 변경이라도 완전히 다른 해시 값을 가져야 한다.

### Salt란?
- 암호화해야 하는 값에 어떤 '별도의 값'을 추가하여 결과를 변형하는 것.

#### 사용 이유
  - 암호화(hashing)만 할 경우 해시된 결과(데이터베이스에 저장된 결과)는 늘 동일하다.
    하여 해시된 값과 원래 값을 테이블로 만들어서 decoding 하는 경우도 있다.
    원본값에 임의로 약속된 '별도의 문자열'을 추가해서 해시를 진행한다면
    기존 해시값과 전혀 다른 해시값이 반환되어 알고리즘이 노출되더라도 원본값을 보호할 수 있다.
      - (암호화 진행 값) + (Salt 값) => (hash 값) == 결과 값

#### 주의 사항
  - Salt는 유저와 패스워드 별로 유일한 값을 가져야 한다.
  - 사용자 계정을 생성할 때와 비밀번호를 변경할 때 마다 새로운 임의의 Salt를 사용해서 해싱해야 한다.
  - Salt는 절대 재사용하지 않는다!
  - Salt는 DB의 유저 테이블에 같이 저장되어야 한다.
