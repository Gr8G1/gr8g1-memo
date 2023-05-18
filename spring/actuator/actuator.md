## 스프링 액추에이터(Spring Actuator)
스프링 부트(Spring Boot) 프로젝트에서 제공하는 기능으로, 애플리케이션의 운영 환경에서 유용한 정보를 제공하고 모니터링을 지원한다. 
액추에이터는 RESTful 엔드포인트를 통해 다양한 관리 및 모니터링 기능에 접근할 수 있게한다.

### 액추에이터가 제공하는 기능
스프링 액추에이터는 엔드포인트(End-point)를 통해 애플리케이션의 다양한 관리 기능을 제공한다.

1. 헬스 체크(Health Check)
   - /health 엔드포인트를 통해 애플리케이션의 상태를 확인 
   - 이를 통해 애플리케이션의 정상 작동 여부를 모니터링하고, 서비스 장애를 예방할 수 있다.
2. 정보 엔드포인트(Info Endpoint)
   - /info 엔드포인트를 통해 애플리케이션의 정보를 확인
   - 애플리케이션의 버전, 개발자 정보, 빌드 정보 등이 포함되어있다.
3. 환경 구성(Env Endpoint)
   - /env 엔드포인트를 통해 애플리케이션의 환경 구성 정보를 확인
   - 애플리케이션의 설정 값, 시스템 변수 등을 제공하여 구성 관리를 용이하게 한다.
4. 메모리 사용량(Metrics Endpoint)
   - /metrics 엔드포인트를 통해 애플리케이션의 메모리 사용량, CPU 사용량 등의 메트릭 정보를 확인 
   - 애플리케이션의 성능을 모니터링하고 성능 튜닝에 활용할 수 있다.
5. 로그 관리(Logging Endpoint)
   - /loggers 엔드포인트를 통해 로그 관리 
   - 로그 레벨 설정, 로그 메시지 확인 등의 작업을 수행할 수 있다.
6. 스레드 상태(Thread Dump)
   - /threaddump 엔드포인트를 통해 애플리케이션의 스레드 상태 정보를 확인 
   - 애플리케이션의 스레드 블로킹, 데드락 등의 문제를 분석하고 조치할 수 있다.

### 액추에이터 활성화
1. 의존성 추가
   - build.gradle 또는 pom.xml 파일에 스프링 액추에이터 의존성을 추가
2. 스프링 부트의 기본 구성 파일
   - application.properties 또는 application.yml 에서 액추에이터 엔드포인트를 활성화
     > 필요한 경우, 액추에이터 엔드포인트를 명시적으로 활성화: management.endpoint.{엔드포인트명}.enabled=true
3. 보안 구성(Optional)
   - 엔드포인트에 대한 보안을 추가로 구성하려는 경우
   - application.properties 또는 application.yml 파일에서 보안 관련 설정을 추가한다. 
   ```
       Ex) 사용자 인증을 요구
       spring.security.user.name=admin
       spring.security.user.password=password
   ```

### 액추에이터 엔드포인트 목록
> document: [actuator.endpoints](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html#actuator.endpoints)

### 액추에이터와 마이크로미터가 제공하는 메트릭정보
1. JVM 메트릭
2. 시스템 메트릭
3. 애플리케이션 시작 메트릭
4. 스프링 MVC 메트릭
5. 톰캣 메트릭
6. 데이터 소스 메트릭
7. 로그 메트릭

> 이외 다양한 메트릭을 제공하고 사용자 정의 메트릭 생성도 가능하다.

### 액추에이터 보안 이슈
잘못된 설정으로 정보들로 인해 액추에이터 엔드포인트(Ex: env, heapdump, shutdown ...) 활성화시 API Key, Token 정보, 내부 서비스 도메인, IP Address와 같은 중요 정보들이 유출될 수 있으며 서비스를 강제로 중단될 수 있는 위험에 노출 될 수 있다.
따라서 엔드포인트의 외부 인터넷 접근을 막고, 내부망을 사용하는 것이 안전하다.

- 액추에이터 보안성 강화
  1. 내부망 사용 및 포트번호 변경
     - 외부 인터넷 망을 통해서 공개된 포트(Ex: 8080) 접근 -> 내부 액추에이터 설정 포트(Ex: 8888)로 접근
     - application.properties 또는 application.yml: management.server.port = 8888
  2.  모든 엔드포인트를 비활성화한 후 필요에따라 활성화(ex: management.endpoints.web.exposure.include: info, health ...) 후 화이트리스트 형태로 운영
      - **asterisk(*)를** 이용하여 include 설정은 하지않는다.
  3. **shutdown 엔드포인트**는 활성화 하지않는다.
     - 기본 설정값은 비활성화이고 노출도 되지 않지만 학습 또는 테스트 이외에 절대 활성화하지 않도록 주의한다.
  4. 액추에이터의 기본 경로를 사용하지 않고, 경로를 변경하여 운영
     - application.properties 또는 application.yml: management.endpoints.web.base-path = ${actuator.base_path}
  5. 액추에이터 활성화 경로 인증된 사용자 접근 제어(Optional)
     - 상황과 환경에 따라 스프링 시큐리티 연동 후 접근 제어
