## MapStruct
빠르고 안정적인 데이터 매핑을 지원하는 라이브러리 (Object Mapping 라이브러리 **(DTO <-> Entity <-> Response : Cycle)**)
> Java Bean Mapper로 자동화된 코드를 생성하여 복잡한 객체 간 매핑 문제 쉽게 해결할 수 있다.
> Controller <-> Service 역할과 책임 분리를 위해 사용

- Home: [https://mapstruct.org/](https://mapstruct.org/)
- Examples: [https://github.com/mapstruct/mapstruct-examples](https://github.com/mapstruct/mapstruct-examples)
> build시 generated 하위 폴더에 interface 구현체가 생성된다.

### 특징
1. 객체간 매핑 자동화
   - MapStruct는 인터페이스 기반의 매핑 코드 생성을 지원한다.
   - 인터페이스 내에 매핑할 메소드를 정의하면 MapStruct는 해당 메소드를 구현하는 코드를 자동으로 생성해줌으로 이를 통해 객체간 매핑 코드를 자동화할 수 있다.
2. 다양한 매핑 옵션 제공
   - 다양한 매핑 옵션을 제공 
   - 매핑할 필드 이름이 서로 다를 때는 @Mapping 어노테이션을 이용하여 소스 필드와 대상 필드 간의 매핑을 지정할 수 있다.
3. 복잡한 매핑 처리
   - 다른 객체를 내포하고 있는 객체를 매핑할 때는 @Mapping 어노테이션을 이용하여 내포된 객체의 필드를 매핑할 수 있다.
   ```java
   // 내포된 객체의 필드 매핑
   @Mapper(componentModel = "spring")
   public interface OrderMapper {
   OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
   
       @Mapping(source = "customer.name", target = "customerName")
       OrderDto orderToOrderDto(Order order);
   }
   ```
4. 매핑 라이프사이클 관리
   - 매핑 라이프사이클 관리를 지원한다. 
   - 매핑 작업 중 발생하는 객체 생성, 초기화, 해제 등의 라이프사이클 관리를 제공하므로, 매핑 코드의 안정성과 퍼포먼스를 보장할 수 있다.
   ```java
   // 매핑 라이프사이클 관리
   @Mapper(componentModel = "spring")
   public interface OrderMapper {
   OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);
   
       @Mapping(source = "customer.name", target = "customerName")
       OrderDto orderToOrderDto(Order order);
   
       @AfterMapping
       default void setOrderNumber(@MappingTarget OrderDto orderDto, Order order) {
           orderDto.setOrderNumber(order.getNumber().toString());
       }
   }
   ```

### 커스텀 매핑
1. Mapper 인터페이스에 해당 메소드를 직접 정의하는 방법
```java
@Mapper
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mappings({
        @Mapping(target = "id", source = "carId"),
        @Mapping(target = "model", source = "carModel")
    })
    CarDto carToCarDto(Car car);

    default String map(CarType carType) {
        if (carType == null) {
            return null;
        }

        switch (carType) {
            case SEDAN:
                return "승용차";
            case SUV:
                return "SUV";
            case SPORTS_CAR:
                return "스포츠카";
            default:
                throw new IllegalArgumentException("Unsupported car type: " + carType);
        }
    }
}
```
> 메소드의 이름은 매핑 대상 필드의 이름과 일치해야 한다.

2. @Mapping 어노테이션의 expression 속성을 이용하여 매핑 로직을 지정하는 방법
```java
public class User {
    private Long id;
    private String name;
    private int age;
    private String email;
    // getter, setter, constructor 생략
}

public class UserDTO {
    private String name;
    private int age;
    private String emailAddress;
    // getter, setter, constructor 생략
}

@Mapper
public interface UserMapper {
   @Mapping(source = "email", target = "emailAddress", expression = "java(email.split(\"@\")[0])")
   UserDTO userToUserDTO(User user);
}
```
3. Mapping#constant를 이용하여 상수 매핑을 지정하는 방법
```java
@Mapper
public interface OrderMapper {
    @Mapping(target = "status", constant = "PAID")
    OrderDto orderToOrderDto(Order order);
}
```
> OrderStatus 열거형 타입을 String 타입으로 매핑, 이렇게 constant를 사용하면 모든 Order 객체가 PAID 상태로 매핑된다.
4. Mapping#qualifiedBy를 이용하여 매핑 후보들 중에서 특정 매핑을 지정하는 방법
5. @Named 어노테이션을 이용하여 매핑 후보를 지정하는 방법
```java
@Mapper
public interface MyMapper {

    @Named("toUpperCase")
    String toUpperCase(String value);

    @Mapping(source = "sourceField", target = "targetField", qualifiedByName = "toUpperCase")
    TargetObject sourceToTarget(SourceObject source);
}
```

### Annotations
@Mapper
 - componentModel = "spring"
   - Spring의 Bean으로 등록 (필수) - **DI 컨테이너와의 연동 방식 지정**
 - injectionStrategy
   - InjectionStrategy.(*) 주입 전략 설정
   - 생성자 주입 방식을 지정, 이 옵션은 생성자 인수가 하나인 경우에만 사용된다.
 - unmappedTargetPolicy
   - ReportingPolicy.(*) 매핑되지 않은 정보가 있을시 대응 방법을 규정
   - 기본값은 IGNORE 이다. 미매핑 필드는 무시된다.
 - ...

@Mappings
 - 여러개의 조건 매핑이 필요할 시 ({ @Mapping(..), ..) 전달하여 묶음으로 적용할 수 있다. (개별 매핑 또한 가능하다,)

@Mapping
- source
   - 소스 객체에서 매핑할 필드 이름(매핑값을 제공할 필드명)
- target
  - 대상 객체에서 매핑할 필드 이름(매핑값을 적용할 필드명)
- qualifiedBy
  - 매핑 후보들 중에서 특정 매핑을 지정할 때 사용되는 한정자
- constant
  - 상수 매핑을 지정할 때 사용되는 값
- expression
  - SpEL 표현식을 이용하여 매핑할 때 사용되는 식
- defaultExpression: 
  - SpEL 표현식을 이용하여 매핑할 때 사용되는 식. 
  - expression 속성과 유사하지만, 값이 없을 때 기본값으로 사용된다.
- defaultValue
  - 매핑할 필드의 값이 null일 때 사용되는 기본값
- ignore
  - 필드의 매핑값을 무시
