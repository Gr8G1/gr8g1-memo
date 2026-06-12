## IoC, DI, 그리고 스프링 컨테이너

### IoC(Inversion of Control, 제어의 역전)
객체의 생성, 생명주기 관리, 의존 관계 설정 등 프로그램의 제어 흐름을 개발자가 직접 관리하지 않고 외부(프레임워크/컨테이너)에 위임하는 설계 원칙

기존에는 객체가 필요한 의존 객체를 스스로 생성(`new`)하고 제어했지만, IoC에서는 컨테이너가 객체를 생성·주입·관리한다. 즉 제어의 주체가 객체 자신에서 컨테이너로 "역전"된다.

> IoC는 특정 기술이 아닌 설계 원칙(개념)이며, 이를 구현하는 대표적인 방법이 DI다.

### DI(Dependency Injection, 의존성 주입)
객체가 의존하는 다른 객체를 외부에서 주입받는 방식

객체 내부에서 직접 생성하지 않고 컨테이너가 주입해 주므로, 구현체 간 결합도(Coupling)가 낮아지고 테스트와 교체가 쉬워진다.

#### 의존성 주입 3가지 방법
- 생성자 주입(Constructor Injection)
  - 생성자를 통해 의존 객체를 주입받는다. **권장 방식**
- 수정자 주입(Setter Injection)
  - setter 메소드를 통해 주입받는다. 선택적(optional) 의존성에 사용
- 필드 주입(Field Injection)
  - 필드에 `@Autowired`를 직접 붙여 주입받는다. 간결하지만 테스트와 불변성 측면에서 권장되지 않는다

#### 생성자 주입을 권장하는 이유
- **불변성 보장**: `final` 키워드를 사용해 주입 이후 의존성이 바뀌지 않도록 강제할 수 있다.
- **필수 의존성 명시**: 객체 생성 시점에 의존성이 모두 주입되므로 누락 시 컴파일/생성 단계에서 바로 드러난다.
- **순환 참조 조기 발견**: 순환 참조가 있을 경우 애플리케이션 구동 시점에 예외로 감지된다.
- **테스트 용이성**: 컨테이너 없이도 생성자에 직접 객체를 전달해 단위 테스트가 가능하다.

```java
@Service
public class TestService {
    private final TestRepository testRepository;

    // 생성자가 하나면 @Autowired 생략 가능
    public TestService(TestRepository testRepository) {
        this.testRepository = testRepository;
    }
}
```

> 롬복의 `@RequiredArgsConstructor`를 사용하면 `final` 필드에 대한 생성자를 자동 생성해 보일러플레이트를 줄일 수 있다.

### 스프링 컨테이너(Spring Container)
빈(Bean)의 생성, 의존 관계 주입, 생명주기 관리를 담당하는 IoC 컨테이너

#### BeanFactory
- 스프링 컨테이너의 최상위 인터페이스
- 빈을 등록·생성·조회하는 기본 기능을 제공
- 빈을 **지연 로딩(Lazy Loading)** 방식으로 요청 시점에 생성

#### ApplicationContext
- `BeanFactory`를 상속하여 부가 기능을 추가한 인터페이스 (실무에서 사용하는 컨테이너)
- 빈을 **즉시 로딩(Eager Loading)** 방식으로 구동 시점에 생성
- 추가 제공 기능
  - 메시지 소스를 활용한 국제화(i18n)
  - 환경 변수(local, dev, prod 등 프로파일) 처리
  - 애플리케이션 이벤트 발행·구독
  - 리소스 로딩 편의 기능

> `BeanFactory`와 `ApplicationContext`를 묶어 스프링 컨테이너라 부르며, 일반적으로 `ApplicationContext`를 사용한다.

#### 스프링 컨테이너 생성 과정
1. 스프링 컨테이너 생성 (설정 정보 = `@Configuration`, 컴포넌트 스캔 등 참고)
2. 빈 등록 (빈 이름과 빈 객체 정보를 컨테이너에 등록)
3. 의존 관계 설정 (등록된 빈들의 의존성 주입)

> 빈 등록과 의존 관계 주입은 단계가 나뉘어 동작한다. 이렇게 분리되어 있기 때문에 생성자 주입에서도 순환 참조 등의 문제를 구동 시점에 감지할 수 있다.
