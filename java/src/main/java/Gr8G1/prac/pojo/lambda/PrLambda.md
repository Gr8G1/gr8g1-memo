## 람다(Lambda)
함수형 프로그래밍 기법이 도입된 Java 문법요소 

> Documents: https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html

- 일반적인 형태
  ```
    (parameter1, parameter2, ...) -> { statement1; statement2; ... }
  ```

### 함수형 내장 인터페이스
- Runnable
  - void run(): 매개변수를 받지않고, 반환값도 없다.
- Supplier<T>
  - T get(): 매개변수는 없지만, T로 매핑 후 반환한다.
- Function<T, R>
  - R apply(T t): T 매개변수를 받아 R로 매핑 후 반환한다.
- BiFunction<T, U, R>
  - R apply(T t, U u): T, U 매개변수를 받아 R로 매핑 후 반환한다.
- Consumer<T>
  - void accept(T t): T 매개변수를 받지만 반환값은 없다.
- BiConsumer<T, U>
  - void accept(T t, U u): T, U 매개변수를 받지만 반환값은 없다
- Predicate<T>
  - boolean test(T t): T 매개변수를 받아 boolean(조건식 결과) 반환한다
- BiPredicate<T, U>
  - boolean test(T t, U u): T, U 매개변수를 받아 boolean(조건식 결과) 반환한다
- UnaryOperator<T>: Function의 자손 (*단항*)
  - T apply(T t): T를 배개변수로 받아 T로 매핑 후 반환한다.
- BinaryOperator<T>: BiFunction의 자손 (*이항*)
  - T apply(T t, T t): T, T를 매개변수로 받아 T로 매핑 후 반환한다. 

### 사용자 정의 람다 표현식
@FunctionalInterface 1:1 매핑을 통해 구현
```java
@FunctionalInterface
public interface TriFunction<A, B, C, R> {
  R apply(A a, B b, C c);

  default <V> TriFunction<A, B, C, V> andThen(Function<? super R, ? extends V> after) {
    Objects.requireNonNull(after);
    return (A a, B b, C c) -> after.apply(apply(a, b, c));
  }
}
```

### 메서드 또는 생성자 참조
 - 클래스::메서드 || 참조변수::메서드

### 람다 표현식 사용시 주의사항
1. 함수형 인터페이스의 메서드 시그니처를 반드시 확인
   - **IDE support 기능으로 확인 가능** 
2. 변수의 타입 추론이 자동으로 이루어지므로 람다 표현식의 매개변수 타입이 정확히 맞아야 한다.
3. 람다 표현식 내에서 외부 변수를 참조할 때는 final(값을 변경할 수 없는 변수) 또는 effectively final(한 번 초기화된 이후 값이 변경되지 않는 변수) 이어야 한다.
   - 외부 변수를 final 또는 effectively final로 선언해야 하는 이유
      - 람다 표현식은 클로저(closure)라는 개념을 기반으로 동작하기때문에 람다 표현식 내에서 외부 변수를 참조할 때 해당 변수가 변경되지 않도록 보장하기 위함  
        람다 표현식이 실행되는 동안 외부 변수가 변경되면, 람다 표현식 내에서 참조한 변수의 값이 예측할 수 없게된다.
        이를 방지하기 위해 final 또는 effectively final 변수만을 람다 표현식 내에서 참조할 수 있도록 제한하고있다. 
        > 클로저(closure)란 함수가 자신의 블록(scope) 밖에서 정의된 변수를 참조할 수 있는 기능을 의미
4. 람다 표현식은 익명 클래스와 달리 이름이 없기 때문에 디버깅이 어려울 수 있다.
