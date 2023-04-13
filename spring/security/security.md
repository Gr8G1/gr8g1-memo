## Spring Security

### Spring Security란?
Spring MVC 기반 애플리케이션의 인증(Authentication)과 인가(Authorization or 권한 부여) 기능을 지원하는 보안 프레임워크로써, 
사실상 Spring MVC 기반 애플리케이션에 보안을 적용하기위한 표준이다.

#### 기능
- 다양한 유형(폼 로그인 인증, 토큰 기반 인증, OAuth 2 기반 인증, LDAP 인증)의 사용자 인증 기능 적용
- 애플리케이션 사용자의 역할(Role)에 따른 권한 레벨 적용
- 애플리케이션에서 제공하는 리소스에 대한 접근 제어
- 민감한 정보에 대한 데이터 암호화
- TSL/SSL 적용
- 일반적으로 알려진 웹 보안 공격 차단
- 이 외에도 SSO, 클라이언트 인증서 기반 인증, 메서드 보안, 접근 제어 목록(Access Control List) 같은 보안을 위한 대부분의 기능을 지원

#### 용어 정리
- Principal(주체)
  - 애플리케이션에서 작업을 수행할 수 있는 사용자, 디바이스 또는 시스템 등이 될 수 있으며,
    일반적으로 인증 프로세스가 성공적으로 수행된 사용자의 계정 정보를 의미한다.
- Authentication(인증)
  - 애플리케이션을 사용하는 사용자가 본인이 맞음을 증명하는 절차를 의미한다.
  - Authentication을 정상적으로 수행하기 위해서는 사용자를 식별하기 위한 정보가 필요한데 이를 Credential(신원 증명 정보)이라고 한다.
- Authorization(인가 또는 권한 부여)
  - 인증이 정상적으로 수행된 사용자에게 하나 이상의 권한(authority)을 부여하여 특정 애플리케이션의 특정 리소스에 접근할 수 있게 허가하는 과정을 의미한다.
  - 인가 또는 권한 부여는 반드시 인증 과정 이후 수행되어야 하며 권한은 일반적으로 역할(Role) 형태로 부여됩니다.
- Access Control(접근 제어)
  - 사용자가 애플리케이션의 리소스에 접근하는 행위를 제어하는 것을 의미한다.

#### 특징
  - Spring Security는 특정 보안 요구 사항을 만족하기 위한 커스터마이징이 용이하고, 유연한 확장이 가능하다.

#### 주의 사항
SimpleGrantedAuthority 권한 지정
  - Role 베이스 형태의 권한을 지정할 때 ‘Role_’ + 권한명 형태로 지정해 주어야 한다.
    - 참조: org/springframework/security/core/userdetails/User.java -> roles()
  - Spring Security 에서 User로 생성된 정보는 UserDetails로 관리한다.

### Ant Pattern VS mvcMatcher
> Ant Document: [https://ant.apache.org/manual/dirtasks.html#patterns](https://ant.apache.org/manual/dirtasks.html#patterns)

- antMatcher(String antPattern)
  - antPattern 과 일치 할시 HttpSecurity 가 호출(실행) 될 수 있도록 정의한다.
    - antMatchers("/secured"): 오직 /secured URL 에 국한되어 매칭된다.

- mvcMatcher(String mvcPattern)
  - mvcPattern 과 일치 할시 HttpSecurity 가 호출(실행) 될 수 있도록 정의한다.
    - mvcMatchers("/secured"): /secured, /secured/, /secured.html, /secured.xyz 과 매칭된다.

일반 적으로 두 기능중 mvcMatcher 가 보안상 우위에 있고 mvcMatcher 는 Spring MVC rules을 적용하여 매칭 할 수 있다.
> @RequestMapping annotation 사용시 매핑이 가능하다.
> 참고: It may be added that mvcMatchers API (since 4.1.1) is newer than the antMatchers API (since 3.1).

### 세션 기반 자격 증명
#### 방식
서버 측에 인증된 사용자의 정보를 세션 형태로 세션 저장소에 저장/관리 하는 방식이다.
서버 리소스를 요청하는 클라이언트가 세션 저장소에 등록된 사용자 정보와 일치하는지 확인하여 리소스 사용 여부를 결정한다.

#### 특징
  - 인증된 사용자 정보를 서버 측 세션 저장소에서 관리한다.
  - 생성된 사용자 세션의 고유 ID인 세션 ID는 클라이언트의 쿠키에 저장되어 request 전송 시, 인증된 사용자인지를 증명하는 수단으로 사용된다.
  - 세션 ID만 클라이언트 쪽에서 사용하므로 상대적으로 적은 네트워크 트래픽을 사용한다.
  - 서버 측에서 세션 정보를 관리하므로 보안성 측면에서 조금 더 유리하다.
  - 서버의 확장성 면에서는 세션 불일치 문제가 발생할 가능성이 높다.
  - 세션 데이터가 많아질수록 서버의 부담이 가중될 수 있다.
  - SSR(Server Side Rendering) 방식의 애플리케이션에 적합한 방식이다.

### 토큰 기반 자격 증명
#### 방식
인증된 사용자의 정보를 토큰에 저장하고, 접근 권한을 부여해 접근 권한이 부여된 특정 리소스에만 접근이 가능하게 하는 방식이다.
#### 특징
  - 토큰에 포함된 인증된 사용자 정보는 서버 측에서 별도의 관리를 하지 않는다.
  - 생성된 토큰을 헤더에 포함시켜 request 전송 시, 인증된 사용자인지를 증명하는 수단으로 사용된다.
  - 토큰내에 인증된 사용자 정보 등을 포함하고 있으므로 세션에 비해 상대적으로 많은 네트워크 트래픽을 사용한다.
  - 기본적으로 서버 측에서 토큰을 관리하지 않으므로 보안성 측면에서 조금 더 불리하다.
  - 인증된 사용자 request의 상태를 유지할 필요가 없기 때문에 서버의 확장성 면에서 유리하고, 세션 불일치 같은 문제가 발생하지 않는다.
  - 토큰에 포함되는 사용자 정보는 토큰의 특성상 암호화가 되지 않기때문에 공격자에게 토큰이 탈취될 경우, 사용자 정보를 그대로 제공하는 셈이된다. 따라서 민감한 정보는 토큰에 포함시키면 안된다.
  - 기본적으로 토큰이 만료되기 전까지는 토큰을 무효화 시킬 수 없다.
  - CSR(Client Side Rendering) 방식의 애플리케이션에 적합한 방식이다.
