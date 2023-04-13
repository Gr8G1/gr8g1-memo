## MVC
사용자 인터페이스, 데이터 및 논리 제어를 구현하는데 널리 사용되는 소프트웨어 디자인 패턴
  - MVC 패턴 사용 의의: 역할과 채임에 따른 분리로 인해 유지보수, 확장, 유연성의 증가와, 코드의 중복도 제거의 효과를 보장하기 위해
  > document: [mvc](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc)

### Model
클라이언트에게 응답으로 돌려주는 작업의 처리 결과 데이터를 Model 이라고 한다.

### View
Model 데이터를 이용해서 클라이언트 애플리케이션의 화면에 보여지는 리소스(Resource)를 제공하는 역할을 한다.

- 처리
  - HTML 페이지의 출력
    - Spring MVC에서 지원하는 HTML 페이지 출력 기술에는 Thymeleaf, FreeMarker, JSP + JSTL, Tiles 등이 있다.
  - PDF, Excel 등의 문서 형태로 출력
  - XML, JSON 등 특정 형식의 포맷으로의 변환

### Controller
클라이언트 요청을 직접적으로 전달 받는 엔드포인트(Endpoint)로써 Model과 View의 중간에서 상호 작용을 해주는 역할을 한다.

- Spring MVC 개략적 흐름
  1. Client Request ->
  2. Controller 요청 데이터 수신 및 비즈니스 로직 처리(Model 데이터 생성) 및 전달
  3. View Model 데이터 수신 및 응답 데이터 생성 및 전달
  4. -> Client Response

- Spring MVC 동작 흐름
```
Client
    1. Client Request
    14. Client Response
  dispatcherServlet
    2. Controller 검색
    4. Contorller 호출 위임
    9. view 검색 요청
    11. view 정보 수신
    12. view 응답 생성 요청
  handleMapping
    3. Controller 정보 반환
  handleAdapter
    5. Contorller 호출 위임 수신
    6. Controller 호출
    8. Model 데이터 수신
    9. Model 데이터와 View 이름 반환
  Controller
    7. Model 데이터 반환
  viewResolver
    10. view 정보 반환
  View
    13. view 응답 생성 및 반환
```

### DispatcherServlet의 역할
Spring MVC의 요청 처리 흐름을 가만히 살펴보면 DispatcherServlet이 굉장히 많은 일을 하는 것처럼 보인다.
클라이언트로부터 요청을 전달 받으면 HandlerMapping, HandlerAdapter, ViewResolver, View 등 대부분의 Spring MVC 구성 요소들과 상호 작용을 하고 있는 것을 볼 수 있다.
그런데 DispatcherServlet이 굉장히 바빠보이지만 실제로 요청에 대한 처리는 다른 구성 요소들에게 위임(Delegate)하고 있다.
이처럼 DispatcherServlet이 애플리케이션의 가장 앞단에 배치되어 다른 구성요소들과 상호작용하면서 클라이언트의 요청을 처리하는 패턴을 Front Controller Pattern이라고 한다.

### DTO(Data Transfer Object)란?
마틴 파울러(Martin Fowler)가 ‘Patterns of Enterprise Application Architecture’ 라는 책에서 처음 소개한 엔터프라이즈 애플리케이션 아키텍처 패턴의 하나이다.
Transfer라는 의미에서 알 수 있듯이 데이터를 전송하기 위한 용도의 객체 정도로 생각할 수 있는데 클라이언트 <-> 서버 요청/응답 데이터의 형태를 객체로 관리하는것으로 볼 수 있다.

### HttpMessageConverter

#### JSON 직렬화(Serialization)와 역직렬화(Deserialization)
- JSON 직렬화(Serialization): Java 객체 → JSON
  - 서버 쪽에서 클라이언트에게 응답 데이터를 전송하기 위해서 DTO 같은 Java의 객체를 JSON 형식으로 변환하는 것
- JSON 역직렬화(Deserialization): JSON → Java 객체
  - 클라이언트 쪽에서 JSON 형식의 데이터를 서버 쪽으로 전송하면 서버 쪽의 웹 애플리케이션은 전달 받은 JSON 형식의 데이터를 DTO 같은 Java의 객체로 변환하는것



### Annotations
@RequestMapping
클래스 레벨 Annotation 으로 핸들러의 역할을 하며 URL을 맵핑한다. 특정 HTTP Request Method("GET", "POST" 등)에 대한 기본 매핑을 좁히는 추가 메서드 레벨 Annotation과 함께 사용된다.
- URI 템플릿 패턴 적용 가능
  - Ex: http://www.example.com/{valiable} : valiable == 변수
- URI 템플릿 패턴에는 정규식도 포함될 수 있다.
  - Ex:
    ```java
    @RequestMapping("/spring-web/{symbolicName:[az-]+}-{version:\\d\\.\\d\\.\\d}{extension:\\.[az]+}")
    public String accessURITepl(@PathVariable String version, @PathVariable String extension) { .. }
    ```

@RequestHeader
Request Header에 포함된 정보 접근하여 인수로 인수(Parameter - 바인딩 된 정보)로 받을 수 있다.
```java
public String accessURL(@RequestHeader Map<String, String> headers) { 
  ... 
}

public String accessURL(@RequestHeader("user-agent") String userAgent) { 
  ... 
}
```

응답값 Header 내용 변경
```java
HttpHeaders headers = new HttpHeaders();
headers.set("Client-Geo-Location", "Korea, Seoul");
return new ResponseEntity<>(XXX, headers, HttpStatus.CREATED)
```

@RequestBody
JSON 형식의 Request Body를 DTO 클래스(정의된) 객체로 변환을 시켜주는 역할을 한다.
내부적으로 HttpMessageConverter 를 통해 JSON 형식으로 변경된다.

@ResponseBody
JSON 형식의 Response Body를 클라이언트에게 전달하기 위해 DTO 클래스의 객체를 Response Body로 변환하는 역할을 한다.
내부적으로 HttpMessageConverter 를 통해 Response Body(JSON) 형식으로 변경된다.

@PathVariable
@RequestMapping URI 템플릿 패턴 형식으로 지정된 변수의 접근하여 인수(Parameter - 바인딩 된 정보)로 받을 수 있다.
```java
@RequestMapping(value="/path/{var}", method = RequestMethod.GET)
public String accessURL(@PathVariable String var) { 
  ... 
}
```

@RequestParam
쿼리 파라미터(Query Parmeter 또는 Query string), 폼 데이터(form-data), x-www-form-urlencoded 형식의 데이터를 파라미터로 전달 받을 수 있다.

@GetMapping, @PostMapping
URI 생략시 클래스 레벨의 URI 경로로 Request URI를 구성한다.

@RestController, @Controller, @Service, @Repository
@Component를 포함한 애너테이션 (역할과 책임 분리에 따른 정형화된 네이밍룰 스프링 계층 구조와 밀접한 연관이 있다)

@ExceptionHandler
  - 전략에 따라 사용 (지역범위, 전역범위) 예외 처리를 지정할 수 있다.
  - @[Rest]Controller, @[Rest]ControllerAdvice 함께 사용 가능
  - BusinessLogic 핸들링 @ResponseStatus와 함께 사용

### Spring MVC HTTP Request 헤더(Header) 정보 조회 및 추가
- @RequestHeader 개별 헤더 정보 또는 전체 헤더 정보 조회 및 추가 가능
- HttpServletRequest | HttpEntity 객체로 헤더 정보 조회 및 추가가 가능하다.


### Rest Client란?
Rest API 서버에 HTTP 요청을 보낼 수 있는 클라이언트 툴 또는 라이브러리를 의미
Java에서 사용할 수 있는 HTTP Client 라이브러리로는 java.net.HttpURLConnection, Apache HttpComponents, OkHttp 3, Netty 등이 있다.

#### RestTemplate
원격지에 있는 다른 Backend 서버에 HTTP 요청을 전송할 수 있는 Rest Client API (Spring에서 제공되는 API)
기본적으로 RestTemplate의 객체를 생성하기 위해서는 RestTemplate의 생성자 파라미터로 HTTP Client 라이브러리의 구현 객체를 전달해야한다.
> org.springframework.boot:spring-boot-starter-web 의존성에 RestTemplate을 위한 HttpClient가 포함되어 있다.

### 활용 예시
```java
// 공공데이터 포탈 정보 조회
void getData() {
    final String END_POINT = "http://apis.data.go.kr/1471000/FoodNtrIrdntInfoService1/getFoodNtrItdntList1";
    final String SERVICE_KEY_ENCODE = "";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("Content-Type", "application/json;charset=utf-8");

    UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(END_POINT)
            .queryParam("serviceKey", SERVICE_KEY_ENCODE)
            .queryParam("type", "json");

    HttpEntity<?> entity = new HttpEntity<>(headers);
    
    try {
      ResponseEntity<String> response = new RestTemplate().exchange(
              builder.build().encode().toUri(),
              HttpMethod.GET,
              entity,
              String.class
      );

    } catch (RestClientException e) {
      // handle exception
    }

    System.out.println("Response code: " + response.getStatusCodeValue());
    System.out.println(response.getBody());
}
```

### Apache httpclient VS Apache HttpComponents
Apache HttpClient는 현재 공식적으로 개발이 중단된 상태이고, Apache HttpComponents 프로젝트로 대체되었다.

#### Apache HttpComponents 의존성 추가
```groovy
dependencies {
  ..
  implementation 'org.apache.httpcomponents:httpclient'
}
```

#### Apache HttpComponents RestTemplate 사용 예시 
Apache HttpComponents를 RestTemplate에 적용하는 방법은 다음과 같습니다.

```java
@Bean
public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
    return restTemplateBuilder
            .requestFactory(HttpComponentsClientHttpRequestFactory.class)
            .build();
}
```

### WebClient
Spring Framework 5부터 지원하는 비동기적인 HTTP 클라이언트
RestTemplate과 비교시 더욱 성능이 좋으며, 비동기적인 방식으로 HTTP 요청 및 응답 처리가 가능하다.
WebClient는 Reactor Core 프로젝트를 기반으로 만들어졌기 때문에, Reactive Streams의 Publisher/Subscriber 패턴에 기반하여 작동
를 통해 비동기적인 데이터 스트림 처리가 가능하다.

#### WebClient 제공 기능
1. 비동기적인 HTTP 요청 및 응답 처리
2. 기본적인 HTTP 메서드(GET, POST, PUT, DELETE, ...) 지원
3. 비동기적인 데이터 스트림 처리를 위한 Flux, Mono 지원
4. HTTP 요청 및 응답 데이터의 캐싱
5. HTTP 요청 및 응답 데이터의 로깅
6. HTTP 요청 및 응답 데이터의 필터링

#### 의존성 추가
Spring Boot 2.4.x 버전 이상
```groovy
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-webflux'
}
```

#### 사용 예시
```java
WebClient client = WebClient.create("http://example.org");

// Get
Mono<String> result = WebClient.create()
  .get()
  .uri("http://example.org")
  .retrieve()
  .bodyToMono(String.class);

  result.subscribe(System.out::println);
  
// Post
Mono<String> result = WebClient.create()
  .post()
  .uri("http://example.org")
  .body(BodyInserters.fromValue("Hello, world!"))
  .retrieve()
  .bodyToMono(String.class);

  result.subscribe(System.out::println);
```
