## Bean
스프링에서 빈(Bean)은 스프링 컨테이너에 등록되어서 관리되는 객체를 일컫는다.
스프링 빈은 스프링 컨테이너에 등록될 때 이름(ID)과 타입(Class)을 갖게 되며, 스프링에서는 이 이름과 타입을 이용해 빈을 참조한다.
빈은 @Component 어노테이션을 사용하여 등록할 수 있으며, @Controller, @Service, @Repository 등과 같은 어노테이션을 사용하여 세부적인 용도에 맞는 빈을 등록할 수도 있다.

### 빈 생명주기(Lifecycle) 
스프링 컨테이너에 등록될 때 초기화되고, 스프링 컨테이너가 종료될 때 소멸된다.
이러한 라이프사이클은 @PostConstruct 어노테이션과 @PreDestroy 어노테이션을 사용하여 관리할 수 있다.
> 스프링 컨테이너 생성 -> 스프링 빈 생성 -> 의존관계 주입 -> 초기화 콜백 -> 소멸 전 콜백 -> 스프링 종료

### 빈 스코프(scope) 개념 
스코프는 빈이 생성되고 관리되는 범위를 의미 
스프링에서는 기본적으로 싱글톤(Singleton) 스코프를 사용한다. 
이외에도 프로토타입(Prototype), 세션(Session), 요청(Request) 등의 다양한 스코프가 있다.

### 빈 간의 의존성 관리
빈은 @Autowired 어노테이션을 사용하여 다른 빈을 자동으로 주입할 수 있다. 
또한 @Qualifier 어노테이션을 사용하여 동일한 타입의 빈 중에서 특정한 빈을 지정할 수 있다. 
이외에도 생성자 주입, 세터 주입 등의 방법으로 의존성을 주입할 수 있다.

> 스프링에서는 빈을 등록하고 관리함으로써 객체 지향의 다형성과 의존성 주입(Dependency Injection)을 구현하고, 
> 객체 간의 결합도를 낮추어 유지보수성과 확장성을 높일 수 있다.

### 스프링 빈 생명주기 콜백 3가지
1. 초기화 콜백(Initialization callback)
스프링 컨테이너는 빈(bean)을 생성하고, 프로퍼티(property) 값을 설정한 뒤 초기화 메서드를 호출하여 빈을 초기화한다. 
이러한 초기화 작업은 InitializingBean 인터페이스를 구현하거나 @PostConstruct 어노테이션을 사용하여 수행할 수 있다.

2. 소멸 콜백(Destruction callback)
스프링 컨테이너가 종료되기 전, 빈 소멸하기 전, 빈이 소멸되기 직전 정리 작업을 수행해야 하는 경우가 있다. 
이를 위해서는 DisposableBean 인터페이스를 구현하거나 @PreDestroy 어노테이션을 사용할 수 있다.

3. 후처리 콜백(Post-processing callback)
스프링 컨테이너는 빈(bean)을 생성하고, 프로퍼티(property) 값을 설정한 뒤, 초기화 메서드를 호출하기 전에 빈 객체에 대해 후처리 작업을 할 수 있다. 
이를 위해서는 BeanPostProcessor 인터페이스를 구현한다.

#### InitializingBean, DisposableBean 단점
  1. 해당 코드가 스프링 전용 인터페이스에 의존한다.
  2. 초기화, 소멸 메서드의 이름을 변경할 수 없다.
  3. 직접 코드를 고칠 수 없는 외부 라이브러리 사용시 인터페이스 콜백을 적용할 수 없다.

### Jakarta Bean Validation
- 유효성 검증에 필요한 표준 스펙 정의 참고
  - 대표: [https://beanvalidation.org/](https://beanvalidation.org/)
  - 내장 constraints 목록: [builtinconstraints](https://jakarta.ee/specifications/bean-validation/3.0/jakarta-bean-validation-spec-3.0.html#builtinconstraints)
- Java Bean 스펙을 준수하는 Java 클래스라면 Jakarta Bean Validation의 애너테이션을 사용해서 유효성 검증이 가능하다.
