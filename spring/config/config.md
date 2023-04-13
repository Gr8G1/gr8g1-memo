## Config

### application.properties
key-value 속성 정의

Ex:
  - spring.datasource.url=jdbc:h2:mem:test
  - spring.datasource.username=sa
  - spring.datasource.password=

#### Placeholders
app.name=MyApp
app.description=${app.name} is a Spring Boot application
> 위와 같이 ${..} 사용해 app.name 참조 할 수 있다.

#### List
application.servers[0].ip=127.0.0.1
application.servers[0].path=/

application.servers[1].ip=127.0.0.2
application.servers[1].path=/

application.servers[2].ip=127.0.0.3
application.servers[2].path=/
> 위와 같이 각기 다른 값의 설정을 List 형식으로 선언 후 사용할 수 있다.

#### Multiple Profiles
logging.file.name=myapp.log
bael.property=default
#---
spring.config.activate.on-profile=dev
spring.datasource.url=jdbc:h2:dev
spring.datasource.username=SA
spring.datasource.password=
bael.property=dev
#---
spring.config.activate.on-profile=prod
spring.datasource.url=jdbc:h2:prod
spring.datasource.username=prodSA
spring.datasource.password=
bael.property=prod

> 위와 같이 하나의 .properties 파일에서 '#---'을 사용하여 여러 개의 document로 구분하여 사용할 수 있다.
> 해당 기능은 Spring Boot 2.4.0^ 지원한다.

# YAML
계층적 속성 정의

Ex:
  spring:
    datasource:
      url: jdbc:h2:mem:test
      username: sa
      password: password

> .properties와 비교시 중복 선언된 내용들이 제거되고 가독성의 향상된것을 볼 수 있다.

#### List
application:
  servers:
    - ip: '127.0.0.1'
  path: '/'
    - ip: '127.0.0.2'
    path: '/'
    - ip: '127.0.0.3'
    path: '/'

#### Multiple Profiles
  logging:
    file:
      name: myapplication.log
  ---
  spring:
    config:
      activate:
        on-profile: stag
    datasource:
      url: jdbc:h2:stag
      username: stagSA
      password:
  > '---' 사용하여 분기를 표기한다.

### Spring Boot 정보 접근
- @ConfigurationProperties
  클래스 레벨 애너테이션을 사용 정의된 설정을 필드 변수에 주입 받을 수 있다.

- @Value("${..}")
  필드 레벨 애너테이션 사용 정의된 필드 변수에 할당 받을 수 있다.

- Environment Abstraction
  @Autowired private Environment env;
    - Environment API를 이용한 정보 접근이 가능하다.
      - env.get()
