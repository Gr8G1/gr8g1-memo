## Spring Boot
스프링(Spring) 프레임워크를 기반으로 한 자바 어플리케이션 개발을 더욱 빠르고 쉽게 할 수 있게 해주는 프레임워크 
스프링 부트는 **자동설정(auto-configuration)**과 **내장형 서버(embedded server)**를 제공하여 설정이나 배포에 대한 복잡한 작업을 줄여준다. 
이러한 특징으로 인해 스프링 부트는 개발자가 의존성 관리나 환경설정에 더욱 집중할 수 있게 하여 생산성을 높여주는 프레임워크로 평가받고 있다.
**org.springframework.boot:spring-boot-starter-XXX** 와 같은 라이브러리는 설정 파일과 라이브러리 의존성을 자동으로 구성하여 사용자가 간단하게 애플리케이션을 구성할 수 있도록 도와준다.

### 버전
- Spring Boot 1.0: 2014년 4월 1일
- Spring Boot 1.1: 2014년 9월 4일
- Spring Boot 1.2: 2014년 12월 18일
- Spring Boot 1.3: 2015년 10월 29일
- Spring Boot 1.4: 2016년 8월 3일
- Spring Boot 1.5: 2017년 1월 12일
- Spring Boot 2.0: 2018년 3월 1일
- Spring Boot 2.1: 2018년 11월 12일
- Spring Boot 2.2: 2019년 10월 9일
- Spring Boot 2.3: 2020년 5월 15일
- Spring Boot 2.4: 2020년 11월 12일
- Spring Boot 2.5: 2021년 5월 20일

### @SpringBootApplication 내부 동작
@Configuration, @EnableAutoConfiguration, @ComponentScan 을 조합한 것

1. @Configuration 어노테이션이 선언된 클래스를 로드
2. @EnableAutoConfiguration 자동 구성을 활성화
  - @EnableAutoConfiguration 실행될때 Spring Boot는 META-INF/spring.factories 파일에서 AutoConfigurationImportSelector를 찾아서 AutoConfigurationImportSelector의 selectImports() 메서드를 호출한다.
  - AutoConfigurationImportSelector를 사용하여 classpath 상의 설정 정보를 찾아서 @Configuration 어노테이션이 붙은 클래스를 로드하고, @Bean 어노테이션을 사용하여 Spring Bean을 등록한다.
  - 또한, @Conditional 어노테이션을 사용하여 필요한 구성 요소만 활성화한다.
  - 'org.springframework.boot.autoconfigure.AutoConfiguration.imports'는 Spring Boot에서 자동 구성(auto-configuration)을 수행할 때, @EnableAutoConfiguration 어노테이션으로 설정된 클래스에서 참조되는 클래스들을 지정하는 META-INF/spring.factories 파일의 프로퍼티이다.
3. @ComponentScan 스프링 Bean 스캐닝


### 부트없이 application.properties 또는 application.yml 적용 할 수 있을까?
- PropertySourcesPlaceholderConfigurer 
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AppConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new ClassPathResource("application.properties"));
        return configurer;
    }

}
```

- @Value 
```java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyComponent {

  @Value("${my.property}")
  private String myProperty;

  public void doSomething() {
    // myProperty 값을 사용하여 로직 처리
  }

}
```
> 부트 사용시 PropertySourcesPlaceholderConfigurer Boot 자동구성으로 구현된다.


### Build 특징
스프링 부트는 Maven이나 Gradle과 같은 빌드 도구를 사용하여 애플리케이션을 패키징하고 실행 가능한 JAR 파일 또는 WAR 파일을 만들때
스프링 부트의 빌드 도구 플러그인을 사용해 애플리케이션 실행에 필요한 모든 종속성이 패키징된 JAR(Spring Boot Executable JAR(Spring Boot Runnable JAR))을 생성한다.
따라서 애플리케이션 실행에 필요한 의존성을 미리 다운로드하거나 설치하지 않아도 되고, JAR 파일 하나로 간단하게 애플리케이션을 배포할 수 있다.

**Fat jar(uber jar)**
애플리케이션을 실행하는 데 필요한 모든 의존성 JAR 파일을 하나의 JAR 파일에 압축하여 패키징
애플리케이션을 실행하기 위해 별도의 라이브러리를 제공할 필요가 없으므로 매우 편리하나 용량이 크고 모든 의존성을 포함하므로 실행 속도가 느리고
동일한 이름을 갖는 모듈이 존재할 수 있으므로 의존성 모듈간 충돌이 발생할 가능성이 높다.
> 동일 이름을 갖는 모듈이 1개 이상 존재할 경우 의존성 클래스 로더는 첫 번째로 로드된 모듈만 로드하고 나머지는 무시한다.

#### Spring Boot Executable JAR (Spring Boot Runnable JAR) 내부 예시
```
// Ex: boot-0.0.1-SNAPSHOT.jar 
META-INF
  MANIFEST.MF
    org/springframework/boot/loader
    JarLauncher.class : 스프링 부트 main() 실행 클래스
BOOT-INF
  classes : 개발한 class 파일과 리소스 파일
    hello/boot/BootApplication.class
  ...
  lib : 외부 라이브러리
    spring-webmvc-6.0.4.jar
    tomcat-embed-core-10.1.5.jar
  ...
  classpath.idx : 외부 라이브러리 경로 
  layers.idx : 스프링 부트 구조 경로
```
