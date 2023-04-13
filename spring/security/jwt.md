## JWT(Json Web Token)?
- 데이터를 안전하고 간결하게 전송하기 위해 고안된 인터넷 표준 인증 방식(RFC-7519) 이다
- Json 포맷을 이용하여 사용자에 대한 속성을 저장하는 Claim 기반의 Web Token이다.
- JWT는 토큰 자체를 정보로 사용하는 Self-Contained(자체적으로 정보를 포함) 방식으로 작성된다.
   
### JWT 2가지 토큰
- AccessToken
  - 보호된 정보들(사용자의 이메일, 연락처, 사진 등)에 접근할 수 있는 권한부여에 사용한다.
- RefreshToken
  - Access Token의 유효기간이 만료된다면 Refresh Token을 사용하여 새로운 Access Token을 발급을때 사용한다.
  - 추가 인증 절차 불필요
   
### JWT 구조
**Header.Payload.Signature**

#### Header
토큰의 헤더는 typ과 alg 두 가지 정보로 구성된다.
- typ: 토큰의 타입 지정
- alg: 알고리즘 방식 지정 (서명(Signature) 및 토큰 검증에 사용)
- HS256(SHA256) | RSA ..
   
#### Payload
토큰에서 사용할 정보의 조각들인 클레임(Claim)이 담겨 있다. 클레임은 총 3가지로 나누어지며, Json(Key/Value) 형태로 다수의 정보를 넣을 수 있다.
- 등록된 클레임(Registered Claim)
  - 등록된 클레임은 토큰 정보를 표현하기 위해 이미 정해진 종류의 데이터들로, 모두 선택적으로 작성이 가능하며 가급적 사용할 것을 권장한다.
  - key는 모두 길이 3의 String으로 되어있다.
    - subject로는 unique한 값을 사용하는데, 이때 주로 사용자 이메일이 사용된다.
    - sub: 제목 / (subject)
    - iss: 발급자 / (issuer)
    - aud: 대상자 / (audience)
    - iat: 발급 시간 / (issued at)
      - 토큰 발급 이후의 경과 시간을 확인 할 수 있다.
    - exp: 만료 시간 / (expiration)
      - NumericDate 형식 필수 -> 1234567890000
    - nbf: 활성 날짜 / (not before)
      - 해당 일자가 지나기 전의 토큰은 활성화되지 않는다.
    - jti: 식별자 / (JWT ID)
      - 중복 방지를 위해 사용되며, 일회용 토큰(Access Token) 등에 사용된다.
    - 공개 클레임(Public Claim)
      - 사용자 정의 클레임으로, 공개용 정보를 위해 사용된다. 충돌 방지를 위해 URI 포맷을 이용한다.
        - { "https://public.claim.sample": true }
    - 비공개 클레임(Private Claim)
      - 사용자 정의 클레임으로, 서버와 클라이언트 사이에 임의로 지정한 정보를 저장한다.
        - { "token_type": "access" }
   
#### Signature
Signature에서는 원하는 비밀 키(Secret Key)와 Header에서 지정한 알고리즘을 사용하여 Header와 Payload에 대해서 단방향 암호화를 수행한다.
- Ex: HMACSHA256 알고리즘을 사용한 Signature 생성 방식
  - HMACSHA256(base64UrlEncode(header) + '.' + base64UrlEncode(payload), secret);
   
   
### 생성 예시
HS256 생성 토큰
- eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
  - ~ Header: eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
  - ~ Payload: eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
  - ~ Signature: SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
   
생성된 토큰은 request header에 Authorization이라는 key의 value로 사용된다.
  - 일반적으로 value에는 Bearer을 붙여서 사용한다.
    - Ex: { "Authorization": "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c" }
   
#### JWT 장점
  - 상태를 유지하지 않고(Stateless), 확장에 용이한(Scalable) 애플리케이션을 구현하기 용이하다.
    - 서버는 클라이언트에 대한 정보를 저장할 필요 없이 오직 정상 토큰인지 검증만 처리한다.
    - 여러대의 서버를 이용한 서비스라면 하나의 토큰으로 여러 서버에서 인증이 가능하기 때문에 JWT를 사용하는 것이 효과적이다.
      - 만약에 여러 서버를 세션 방식으로 관리한다면 모든 서버가 해당 사용자의 세션 정보를 공유하고 있어야 한다.
  - 클라이언트가 request를 전송할 때 마다 자격 증명 정보를 전송할 필요가 없다.
    - 토큰이 만료되기 전까지는 한번의 인증만 수행하면 된다.
  - 인증을 담당하는 시스템을 다른 플랫폼으로 분리하는 것이 용이하다.
    - 사용자의 자격 증명 정보를 직접 관리하지 않고, Github, Google 등의 다른 플랫폼의 자격 증명 정보로 인증하는 것이 가능하다.
  - 토큰 생성용 서버를 만들거나, 다른 회사에서 토큰 관련 작업을 맡기는 것 등 다양한 활용이 가능하다.
  - 권한 부여에 용이하다
    - 토큰의 Payload(내용물) 안에 해당 사용자의 권한 정보를 포함하는 것이 용이하다.
   
#### JWT 단점
  - 토큰의 페이로드(Payload)에 3종류의 클레임을 저장하기 때문에, 정보가 많아질수록 토큰의 길이가 늘어나 네트워크에 부하를 줄 수 있다.
  - Payload 자체는 암호화 된 것이 아닌, Base64Url로 인코딩 된 것이므로 중간에 Payload를 탈취하여 디코딩하면 데이터를 볼 수 있다.
  - JWT는 상태를 저장하지 않기 때문에 한번 만들어지면 제어가 불가능하다.
  - 토큰은 클라이언트 측에서 관리해야 하기 때문에, 토큰을 저장해야 한다.
   
#### JWT 사용시 주의사항
  - 토큰 자체에 정보를 담고 있으므로 보안 취약성을 인지 해야한다.
  - Payload JWE로 암호화하거나 Payload에 중요 데이터를 넣지 않아야 한다.
  - 토큰을 임의로 삭제하는 것이 불가능하므로 토큰 만료 시간을 필수로 설정해야한다.
