## Filter Chain

### 서블릿 필터(Servlet Filter)란?
서블릿 기반 애플리케이션의 경우, 애플리케이션의 엔드포인트에 요청이 도달하기 전에 중간에서 요청을 가로챈 후 어떤 처리를 할 수 있는 적절한 포인트를 제공하는데 그것이 서블릿 필터의 역할이다.

- 서블릿 필터는 javax.servlet.Filter 인터페이스를 구현하고, 서블릿 컨테이너에 등록된다.
- 등록된 서블릿 필터는 필터 체인(filter chain)을 통해 다른 필터나 서블릿에게 요청과 응답을 전달할 수 있다.
- 서블릿 필터는 스프링 시큐리티에서도 활용되며, 스프링 시큐리티의 다양한 기능을 구현하는 시큐리티 필터들도 서블릿 필터의 일종이다.

### Security FilterChain이란?

#### 필터 체인 기본 구성
1. SecurityContextPersistenceFilter: 요청과 응답 사이클에서 보안 컨텍스트를 유지하도록 설계된 필터
2. WebAsyncManagerIntegrationFilter: 웹 비동기 요청 처리에 필요한 보안 관리자를 통합하는 필터
3. HeaderWriterFilter: 응답 헤더를 처리하는 필터
4. LogoutFilter: 로그아웃 요청을 처리하는 필터
5. UsernamePasswordAuthenticationFilter: 아이디와 비밀번호를 사용한 인증을 처리하는 필터
6. DefaultLoginPageGeneratingFilter: 기본 로그인 페이지를 생성하는 필터
7. DefaultLogoutPageGeneratingFilter: 기본 로그아웃 페이지를 생성하는 필터
8. BasicAuthenticationFilter: HTTP Basic 인증을 처리하는 필터
9. RequestCacheAwareFilter: 캐시를 통해 요청 처리를 했을 경우 처리 결과를 적용하는 필터
10. SecurityContextHolderAwareRequestFilter: 보안 컨텍스트를 Servlet API request에 바인딩하는 필터
11. AnonymousAuthenticationFilter: 인증되지 않은 사용자를 익명으로 처리하는 필터
12. SessionManagementFilter: 세션 관리를 처리하는 필터
13. ExceptionTranslationFilter: 인증 및 권한 예외를 처리하는 필터
14. FilterSecurityInterceptor: 보안 필터링을 처리하는 필터

> 위 필터 체인은 기본값이며 **'configure'** 메소드를 오버라이드하여 필터 체인에 직접 추가하거나 제거할 수 있다.

#### 보안관련 Annotations (자주 사용)
@EnableWebSecurity
  - 웹 보안을 활성화하는 어노테이션입니다. 이 어노테이션은 Spring Security의 기본 보안 설정을 사용합니다.

@EnableGlobalMethodSecurity
  - 메소드 보안을 활성화하는 어노테이션 @Secured, @RolesAllowed, @PreAuthorize, @PostAuthorize 등 사용할 수 있도록 설정한다.

@Secured
  - 메소드 실행 전 인가된 사용자만 해당 메소드를 실행할 수 있도록 제한하는 어노테이션입니다.

@RolesAllowed
  - 메소드 실행 전 인가된 역할(Role)을 가진 사용자만 해당 메소드를 실행할 수 있도록 제한하는 어노테이션입니다.

@PreAuthorize
  - 메소드 실행 전에 표현식(Expression)에 따라 인가된 사용자만 해당 메소드를 실행할 수 있도록 제한하는 어노테이션입니다.

@PostAuthorize
  - 메소드 실행 후에 표현식(Expression)에 따라 인가된 사용자만 해당 메소드의 결과를 볼 수 있도록 제한하는 어노테이션입니다.

@AuthenticationPrincipal
  - 현재 인증된 사용자를 가리키는 Principal 객체를 주입하는 어노테이션

스프링 시큐리티에서 제공하는 어노테이션들은 크게 4가지 카테고리로 나눌 있다.
1. 인증(Authentication)
2. 인가(Authorization)
3. OAuth 2.0
4. 기타

각각의 카테고리별 어노테이션은 공식문서를 참조하자.
> document: [https://docs.spring.io/spring-security/reference/](https://docs.spring.io/spring-security/reference/)
