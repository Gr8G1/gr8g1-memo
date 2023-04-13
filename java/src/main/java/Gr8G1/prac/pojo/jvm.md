## JVM (Java Virtual Machine)

Java 는 OS에 종속적이지 않는 특징을 갖고, OS 위에서 Java 를 실행하기 위해 사용되는것이 JVM 이다.

### JVM 동작방식
<div style="text-align: center">
  <p><img width="554" alt="JVM 동작방식" src="https://user-images.githubusercontent.com/29472238/230361614-fc1dcfc9-9a65-47ee-98b9-ccfa9594bb97.png"></p>
  <p>출처: <a href="https://techvidvan.com/tutorials/java-virtual-machine/">techvidvan</a></p>
</div>

### JAVAC
<div style="text-align: center">
  <p><img width="424"  alt="JAVAC" src="https://user-images.githubusercontent.com/29472238/230361604-e29efd92-895b-4388-8396-6edf6157bf5e.png"></p>
  <p>출처: <a href="https://techvidvan.com/tutorials/java-virtual-machine/">techvidvan</a></p>
</div>

*.java 클래스 파일은 CPU 가 인식을 하지 못하므로 기계어로 컴파일이 필요하다.

- 처리 과정
  1. Java compiler 가 Java 원시코드(*.java)를 클래스파일(*.class)로 변환
  2. 전달된 클래스파일(*.class)을 JIT(just-in-time compilation) 컴파일러를 이용해 OS가 인식 가능한 기계어로 전환한다.

### JVM 구성 요소
<div style="text-align: center">
  <p><img width="581" alt="JVM 전체 모델" src="https://user-images.githubusercontent.com/29472238/230361636-027871da-f507-4ae9-a152-66a0da691097.png"></p>
  <p>출처: <a href="https://techvidvan.com/tutorials/java-virtual-machine/">techvidvan</a></p>
</div>

1. 클래스 로더(Class Loader)
   JVM 내로 클래스 파일(*.class)을 로드하고, 링크를 통해 배치하는 작업을 수행하는 모듈
   런타임시 동적으로 클래스를 로드하고 jar 파일 내 저장된 클래스들을 JVM 위에 탑재

   - 부트스트랩 클래스 로더(Bootstrap Class Loader)
     - JVM이 시작될 때 가장 먼저 실행되며, JDK 내의 라이브러리들을 로드
   - 확장 클래스 로더(Extension Class Loader)
     - JDK의 확장 라이브러리를 로드
   - 시스템 클래스 로더(System Class Loader)
     - 애플리케이션 클래스를 로드
2. 실행 엔진(Execution Engine)
   클래스 로더에 의해 메모리에 적재된 클래스(Bytecodes) 코드들을 기계어로 변경해 명령어 단위로 실행하는 역할을 하며, 아래 1, 2 방식으로 실행된다.

   1. 인터프리터(Interpreter)
      명령어 단위로 읽어서 실행 (한줄씩 처리) :> 실행 속도가 느리다는 단점이있다.
   2. JIT 컴파일러(Just-in-Time)
      인터프리터 방식으로 실행되다 적절한 시점에 바이트 코드 전체를 컴파일하여 기계어로 변경,
      이후 더 이상 인터프리팅 하지 않고 기계어로 직접 실행하는 방식이다.
   3. 가비지 콜렉터(Garbage collector)
      더이상 사용되지 않는 인스턴스를 찾아 메모리에서 삭제하는 역할을 수행한다.
      자바에서 가비지 컬렉팅은 JVM 이 자동으로 처리해주므로, 일반적으로 개발자가 직접 가비지 컬렉션을 수행할 필요는 없다.
      하지만 때로는 메모리 누수(memory leak)나 성능 이슈(performance issue)를 처리하기 위해 가비지 컬렉션을 직접 수행해야 하는 경우있는데
      이 경우, System 클래스의 gc() 메소드를 호출하여 가비지 컬렉션을 수행할 수 있다.

      ```java
      public class GarbageCollectionExample {
          public static void main(String[] args) {
              Object obj1 = new Object();
              Object obj2 = new Object();

              // obj1과 obj2를 사용한 후에 null로 초기화하여 가비지 컬렉션 대상으로 만든다.
              obj1 = null;
              obj2 = null;

              // 가비지 컬렉션을 명시적으로 요청한다.
              System.gc();
          }
      }
      ```
3. PC Register
   쓰레드가 생성될 때마다 생성되는 영역으로 현재 쓰레드가 실행되는 부분의 주소와 명령을 저장하고 있는 영역이다.
   이것을 이용해 쓰레드를 돌아가며 수행할 수 있게 한다.
4. 런타임 데이터 영역 (Runtime Data Area)
   프로그램을 수행하기 위해 OS에서 할당받은 메모리 공간으로 이렇게 할당받은 메모리를 JVM 은 용도에 따라 아래와 같이 구분하여 관리한다.

```
  낮은 주소(low memory) - 메모리
   
                         클래스/클래스 변수       (메소드 영역)
                         객체 인스턴스            (힙 영역)
                         메소드(지역/매개)변수     (스택 영역)

  높은 주소(high memory) -
```

- 네이티브 메서드 스택 영역(Native Method Stack Area)
  - 자바 외부의 언어(C, C++ 등)로 작성된 네이티브 코드를 실행할 때 사용하는 메모리 영역

- 메소드(method) 영역 : 클래스에 대한 정보와 함께 클래스 변수(static variable)가 저장되는 영역
  - 특정 클래스가 사용되면 해당 클래스의 클래스 파일(*.class)를 읽어들여, 해당 클래스에 대한 정보를 메소드 영역에 저장한다.

- 힙(heap) 영역 : 모든 인스턴스 변수가 저장되는 영역
  - new 키워드를 사용하여 인스턴스가 생성되면, 해당 인스턴스의 정보를 힙 영역에 저장한다.
    - 힙 영역은 메모리의 낮은 주소에서 높은 주소의 방향으로 할당된다.

- 스택(stack) 영역
  - 메소드가 호출될 때 메소드의 스택 프레임이 저장되는 영역
  - 메소드가 호출되면, 메소드의 호출과 관계되는 지역 변수와 매개변수를 스택 영역에 저장한다.
  - 스택 영역은 메소드의 호출과 함께 할당되며, 메소드의 호출이 완료되면 소멸한다.
  - 스택 영역에 저장되는 메소드의 호출 정보를 스택 프레임(stack frame)이라 하고,
  - 스택은 데이터 저장(push), 데이터를 인출(pop) 후입선출(LIFO, Last-In First-Out) 방식에 따라 동작한다.
  - 메모리의 높은(아래) 주소에서 낮은(위) 주소의 방향으로 할당한다.

- 런타임 상수 풀(Runtime Constant Pool)
  - 자바 클래스 파일(.class 파일) 내에 존재하는 상수들을 저장하는 테이블
  - 클래스 파일은 바이트 코드 형식으로 되어있으며, 바이트 코드 내에는 상수 풀 참조(Constant Pool Reference) 명령어가 존재한다.
  - 이 명령어는 클래스 파일 내의 상수 풀에 있는 상수를 참조하도록 지시하는 역할을 하고 상수 풀은 클래스 파일 로드 시 JVM의 메모리에 로드되며, 클래스의 런타임 동안 사용된다.
  - 상수 풀에는 문자열, 정수, 부동 소수점 수, 클래스와 인터페이스의 전체 이름, 메소드와 필드의 이름과 타입, 메소드 타입 등 다양한 상수들이 저장된다.
  - **런타임 상수 풀의 주요 특징**
    - 클래스 로딩 시 로드되며, JVM 메모리에 상주한다.
    - 클래스의 모든 인스턴스에 대해 공유
    - 런타임 상수 풀의 각 항목은 고유한 인덱스 값을 가진다.
    - 런타임 상수 풀의 각 항목은 참조되는 상수의 타입과 상수값을 저장한다.

### JVM Options
Ex: -Xms2048m -Xmx2048m -XX:PermSize=256m -XX:MaxPermSize=512m

- Xms: JVM 시작시의 Heap size
- Xmx: 최대 Heap size
  - 32bit 운영체제인 경우 최대 Heap Size : 2^32 = 4GB
  - 64bit 운영체제인 경우 최대 Heap Size : 32GB **(최대 HeapSize 32GB 이하 권장)**
- -XX:MaxPermSize= & -XX:PermSize=
  - 해당 기능은 Java 8 이후로 :fire: Deprecated 되었다.
    Native Memory 영역으로 이동하여 Metaspace 영역으로 바뀌게 되었다.
- Java 8 에서 사라진 maxPermSize, PermSize 대체 옵션
  - -XX:UseContainerSupport
    JVM 이 사용 가능한 CPU 와 RAM 을 즉 cgroup limits 를 읽어들일 수 있게 하는 옵션이다.

Ex: -Dproperty=value main-class
- -D 옵션은 시스템 프로퍼티(System Property)를 설정하는 옵션이다.

Ex: -Pproperty=value
- -P 옵션은 프로젝트 프로퍼티를 설정하는 옵션이다.

### JVM DNS Cache
- 유형
  - Positive DNS Cache:
    호스트 이름을 IP 주소로 성공적으로 변환한 경우, 그 결과를 캐시에 저장,
    이후 같은 호스트 이름에 대한 조회 요청이 들어오면, 캐시에서 바로 응답하므로 DNS 조회 시간을 단축할 수 있다.
  - Negative DNS Cache:
    호스트 이름을 IP 주소로 변환할 수 없는 경우, 실패한 결과를 캐시에 저장,
    이후 같은 호스트 이름에 대한 조회 요청이 들어오면,
    DNS 서버로부터 다시 조회하는 대신 캐시에서 실패한 결과를 반환

- 환경 구성
  - JVM 의 DNS 캐시는 시스템 프로퍼티 또는 환경 변수를 통해 구성할 수 있다.
  ```
     -Dnetworkaddress.cache.ttl=60
     -Dnetworkaddress.cache.negative.ttl=0
     -Dsun.net.inetaddr.ttl=60
     -Dsun.net.inetaddr.negative.ttl=0
  ```

- IP 변동 가능성 문제와 해결 방안
  - JVM 재가동
  - networkaddress.cache.negative.ttl 옵션 설정
