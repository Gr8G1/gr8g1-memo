## 어노테이션(Annotation)
자바 코드에 메타데이터를 추가하는 방법 중 하나로 어노테이션을 사용하면 코드에 대한 정보를 더욱 세밀하게 제어하고,  
컴파일러나 런타임 환경에서 특정한 작업을 수행하도록 지시할 수 있다. 
어노테이션은 **'@'** 기호를 이용하여 표시하며, 주로 클래스, 메소드, 변수 등의 선언부에 붙여서 사용한다.

### 표준 어노테이션
- @Override: 해당 메서드가 오버라이드된 메서드라는 것을 명시, 상위 클래스 또는 인터페이스에서 해당 메서드를 찾을 수 없다면 컴파일 에러 발생
- @Deprecated: 해당 메서드가 더 이상 사용되지 않음을 표시
- @FunctionalInterface: 함수형 인터페이스라는 것을 명시
  - 함수형 인터페이스는 오직 하나의 추상 메서드만을 포함해야 되는 제약이 있다.
- @SafeVarargs: 가변인자의 매개변수를 사용할 때의 경고 생략
- @SuppressWarnings: 컴파일 경고 생략
  - all: 모든 경고 생략
  - cast: 캐스트 경고 생략
  - dep-ann: 사용불가 어노테이션 사용 경고 생략
  - deprecation: @Deprecated 경고 생략
  - fallthrough: switch문 break 누락 경고 생략
  - finally: 반환하지 않는 finally 블럭 경고 생략
  - null: null 분석 경고 생략
  - rawtypes: Generic 사용 클래스의 매개 변수가 특정되지 않을때 경고 생략
  - unchecked: 검증되지 않은 연산자 경고 생략
  - unused: unused 코드 경고 생략

### 메타 어노테이션
- @Documented: 해당 어노테이션을 Javadoc 포함
- @Target: 어노테이션의 적용범위 지정
  - PACKAGE: 패키지
  - CONSTRUCTOR: 생성자
  - ANNOTATION_TYPE: 어노테이션
  - PARAMETER: 매개변수
  - METHOD: 메서드
  - TYPE: 타입(class, interface, enum)
  - TYPE_USE: 타입이 사용되는 모든 대상 (Java 8^)
  - TYPE_PARAMETER: 타입 매개변수
  - FIELD: 필드(멤버변수, 열거형 상수)
  - LOCAL_VARIABLE: 지역변수
  - MODULE: 모듈 (Java 9^)
- @Inherited: 어노테이션의 상속 지정
- @Retention: 어노테이션의 유지 기간(시간)을 결정
  - Retention policy
    - SOURCE: *.java 파일 존재, *.class 파일 미존재 (javac 의해 삭제)
    - CLASS: *.class 파일 존재, Runtime 미제공 (*기본값*)
    - RUNTIME: *.class 파일 존재, Runtime 제공. (자바 reflection 으로 선언한 어노테이션에 접근 가능)
- @Repeatable: 어노테이션의 연속적인 적용
- @Native: 어노테이션을 네이티브 코드로 선언

### 사용자 정의 어노테이션
개발자가 직접 어노테이션을 정의하여 사용할 수도 있다. 이러한 사용자 정의 어노테이션은 주로 프레임워크나 라이브러리에서 사용되며,  
코드에 대한 추가 정보를 제공하거나 특정 작업을 수행하기 위해 활용된다.  
사용자 정의 어노테이션을 만들 때는 @interface 키워드를 사용하여 어노테이션을 정의한다.  
예를 들어, 다음과 같은 사용자 정의 어노테이션을 만들 수 있다.

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, FIELD })
public @interface MyCustomAnno {
  String name() default "";
}
```
