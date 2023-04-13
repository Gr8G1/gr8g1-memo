## OAuth
OAuth는 인터넷 사용자들이 비밀번호를 제공하지 않고 다른 서버상의 자신들의 정보에 대해 웹사이트나 애플리케이션의 접근 권한을 부여할 수 있는 공통적인 수단으로서 사용되는, 접근 위임을 위한 개방형 표준이다. (위키백과)
사용자의 인증을 직접 처리하는 것이 아니라 사용자 정보를 보유하고 있는 신뢰할 만한 써드 파티 애플리케이션(GitHub, Google, Facebook 등)에서 사용자의 인증을 대신 처리해 주고 Resource에 대한 자격 증명용 토큰을 발급한 후, Client가 해당 토큰을 이용해 써드 파티 애플리케이션의 서비스를 사용하게 해주는 방식입니다.

### OAuth 인증 구성요소(Component) : 참여자

#### Resource Owner
Google 로그인 -> Google 서비스(Resource) 이용자 = Resource Owner;
  - 이용중인 서비스의 Resource를 소비하는 이용자를 말한다.

#### Client
Resoure Server에 접속해서 정보를 가져오고자 하는 클라이언트(웹 애플리케이션)를 말한다.

#### Resource Server
Client가 제어하고자 하는 자원을 보유하고 있는 서버를 말한다.
  - Google, Facebook, Instagram 등이 이에 속한다.

#### Authorization Server
Client가 Resource Server에 접근할 수 있는 권한을 부여하는 서버

### Oauth 용어
#### Authorization Grant
Client 애플리케이션이 Access Token을 얻기 위한 Resource Owner의 권한을 표현하는 크리덴셜(Credential)을 의미한다. 

#### 종류
1. Authorization Code Grant(권한 부여 승인 코드 방식)
   - 권한 부여 승인을 위해 자체 생성한 Authorization Code를 전달하는 방식으로 가장 보편화된 방식으로, Refresh Token을 사용할 수 있다.
   - 권한 부여 승인 요청시 응답 타입(response_type)을 code 로 지정하여 요청한다.
2. Implicit Grant(암묵적 승인 방식)
   - 별도의 Authorization Code 없이 바로 Access Token을 발급하는 방식으로, 자격증명을 안전하게 저장하기 힘든 Client(자바스크립트 등 스크립트 언어를 사용하는 브라우저)에게 최적화된 방식이다.
   - Refresh Token 을 사용할 수 없고 이 방식에서 Authorization Server는 Client Secret을 통해 클라이언트 인증 과정을 생략한다.
   - 권한 부여 승인 요청시 응답 타입(response_type)을 token 으로 지정하여 요청한다.
3. Client Credentials(클라이언트 자격 증명 승인 방식)
   - Client 스스로 관리하는 Resource 혹은 Authorization Server에 해당 Client를 위한 제한된 Resource 접근 권한이 설정되어 있는 경우 사용 가능한 방식으로, Refresh Token을 사용할 수 없다.
4. Resource Owner Password Credentials(자원 소유자 자격 증명 승인 방식)
   - 로그인 시 필요한 정보(username, password)로 Access Token을 발급받는 방식 자신의 서비스에서 제공하는 애플리케이션의 경우에만 사용되는 인증 방식으로, Refresh Token의 사용도 가능하다.
   - Authorization Server, Resource Server, Client가 모두 같은 시스템에 속해 있을 때만 사용이 가능하다.

#### Access Token
  - Resource Server의 보호된 Resource에 접근하기 위해 사용되는 자격 증명용 토큰을 말한다.
    - Authorization Code & Client Secret 를 이용한 자격증명을 통해 접근 권한을 획득한다.

#### Scope
Access Token 의 Resource 접근 범위를 의미한다.
