## AOP(Aspect-Oriented Programming)

객체 지향 프로그래밍(OOP)을 보완하는 프로그래밍 패러다임
OOP의 다형성, 상속, 캡슐화와 같은 개념 외에도, 메소드 실행 시점의 횡단 관심사(Cross-cutting Concern)를 처리할 수 있는 기능을 제공

### 용어

#### 애스팩트(Aspect)
어드바이스 + 포인트컷의 모듈화 (여러 어드바이스와 포인트컷이 함께 존재한다.)
> 실제 동작 방식은 bytecode 조작을 통해 이뤄진다. (Spring instrumentation 참조)

#### 조인포인트(Join point)
클래스 초기화, 객체 인스턴스화, 메소드 호출, 필드 접근, 예외 발생과 같은 애플리케이션 실행 흐름에서의 특정 포인트를 의미
> 스프링 AOP 는 프록시 방식을 사용하므로 조인 포인트는 항상 메소드 실행 지점으로 제한된다.

#### 포인트컷(Pointcut)
조인 포인트 중에서 어드바이스가 적용될 위치를 선별하는 기능
> AspectJ 표현식을 사용해 식별 부분을 지정한다.
> 프록시를 사용하는 스프링 AOP 는 메서드 실행 지점만 포인트컷으로 선별 가능하다.

#### 타겟(Target)
어드바이스를 받는 객체이며 포인트컷에 의해 결정된다.

#### 어드바이스(Advice)
조인포인트에서 수행되는 코드

#### 어드바이저(Advisor)
하나의 어드바이스와 하나의 포인트 컷으로 구성된다.

#### 위빙(Weaving)
AOP에서 프록시 객체를 생성하고, 어드바이스를 적용하는 과정을 말한다. 

> 위빙은 런타임 이전에 수행되며, 컴파일 타임에 수행될 수도 있고, 클래스 로딩 타임에 수행될 수도 있다. 
> 스프링 AOP에서는 런타임에 프록시 객체를 생성하고 어드바이스를 적용한다.

### 스프링 AOP에서 사용할 수 있는 조인포인트 관련 어노테이션
- @Before: 대상 메소드 실행 전에 Advice를 실행
- @After: 대상 메소드 실행 후에 Advice를 실행
- @AfterReturning: 대상 메소드 실행 후 정상적으로 반환된 경우에 Advice를 실행
- @AfterThrowing: 대상 메소드 실행 중 예외가 발생한 경우에 Advice를 실행 
- @Around: 대상 메소드 실행 전/후 및 예외 발생 시점에 Advice를 실행
> 이 외에도 @Pointcut 어노테이션을 사용하여 조인포인트를 정의할 수 있다.

### @Pointcut 지시자
- execution: 메서드 실행 조인트 포인트를 매칭한다.
- within: 특정 타입 내의 조인 포인트를 매칭한다.
- args: 인자가 주어진 타입의 인스턴스인 조인 포인트
- this: 스프링 빈 객체(스프링 AOP 프록시)를 대상으로 하는 조인 포인트
- target: Target 객체(스프링 AOP 프록시가 가르키는 실제 대상)를 대상으로 하는 조인 포인트
- @target: 실행 객체의 클래스에 주어진 타입의 애너테이션이 있는 조인 포인트
- @within: 주어진 애너테이션이 있는 타입 내 조인 포인트
- @annotation: 메서드가 주어니 애너테이션을 가지고 있는 조인 포인트를 매칭
- @args: 전달된 실제 인수의 런타임 타입이 주어진 타입의 애너테이션을 갖는 조인 포인트
- bean: 스프링 전용 포인트컷 지시자이고 빈의 이름으로 포인트컷을 지정한다.

### Pointcut 표현식 문법
**execution(modifiers-pattern? ret-type-pattern declaring-type-pattern?name-pattern(param-pattern) throws-pattern?)**
> execution(접근제어자? 반환타입 선언타입?메서드이름(파라미터) 예외?)

#### 메소드 패턴: 메소드 이름 및 매개변수 타입, 반환 타입 등을 기준으로 메소드를 선택
- *: 모든 값
- ..: 0개 이상의 인자를 의미
- +: 상위 클래스 또는 인터페이스의 메소드 포함

#### 타입 패턴: 클래스나 인터페이스를 선택
- *: 모든 값
- ..: 하위 패키지를 의미

#### 패키지 패턴: 패키지 이름을 선택
- *: 모든 값
- ..: 하위 패키지를 의미

- 선언타입은 최상위 패키지명부터 시작한다.
- 메소드 실행 조인포인트를 매칭한다.
- ?는 생략할 수 있다.

### execution 표현식을 사용한 포인트컷 예시
```java
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TimingAspect {

    @Pointcut("execution(* com.example.Service.doSomething())")
    public void doSomethingPointcut() {}

    @After("doSomethingPointcut()")
    public void logExecutionTime(JoinPoint joinPoint) {
        long startTime = (long) joinPoint.getArgs()[0];
        long executionTime = System.currentTimeMillis() - startTime;
        System.out.println("Execution time: " + executionTime + "ms");
    }
}
```
1. @Pointcut 어노테이션을 사용하여 execution(* com.example.Service.doSomething()) 표현식을 정의한 doSomethingPointcut() 메소드 생성
    이 표현식은 com.example.Service 클래스의 doSomething() 메소드에 대한 포인트컷을 정의한다.
2. @After 어노테이션을 사용하여 Advice를 정의
3. Service 클래스에서 doSomething() 메소드를 정의하면, 해당 메소드가 실행될 때 호출 시간이 로그에 출력

```java
@Service
public class MyService {

    public void doSomething() {
        long startTime = System.currentTimeMillis();
        // do something
        // ...
        // ...
        applicationContext.getBean(TimingAspect.class).logExecutionTime(startTime);
    }
}
```
