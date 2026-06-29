## 트랜잭션(Transaction)
스프링 프레임워크에서 제공하는 트랜잭션 관리 기능은 데이터베이스 트랜잭션 처리에서 일어날 수 있는 문제를 해결하고 안정성을 보장하는 데 큰 역할을 한다.
스프링 트랜잭션은 선언적 트랜잭션 관리와 프로그래밍 방식의 트랜잭션 관리 두 가지 방식을 지원한다.

1. 선언적 트랜잭션 관리
   - AOP를 사용하여 트랜잭션을 관리하는 방식. 
   - 트랜잭션 처리를 위해 미리 정의한 트랜잭션 어드바이스를 사용하거나 어노테이션을 이용해 트랜잭션 경계설정을 할 수 있다.
2. 프로그래밍 방식의 트랜잭션 관리
   - 개발자가 직접 코드상에서 트랜잭션을 시작하고 종료하는 방식 
   - TransactionTemplate이라는 객체를 사용하여 트랜잭션을 처리할 수 있다.
   
스프링 트랜잭션은 기본적으로 JDBC와 JPA, Hibernate 등과 같은 ORM 프레임워크와 연동하여 사용된다. 
스프링 트랜잭션은 데이터베이스 커넥션을 가져와 커밋 또는 롤백을 처리하고, 트랜잭션 처리 중에 발생할 수 있는 예외를 처리하여 안전한 데이터베이스 작업을 보장한다.

### @Transactional
트랜잭션 관리 어노테이션으로 메소드나 클래스 레벨에서 사용할 수 있다. 
이 어노테이션은 트랜잭션을 시작하고, 커밋하거나 롤백하는 등의 트랜잭션 처리를 자동으로 처리해준다.

- readOnly (Default = false)
  - 읽기 전용 속성으로 변경 (1차 캐시(영속성 컨텍스트) 저장 X, 스냅샷 생성 X - 자동 성능 최적화 고려사항)
- propagation (Default = Propagation.REQUIRED)
  - 트랜잭션이 이미 존재할 시 (일련의 흐름안에서) 동일 트랜잭션에 참여 여부를 결정한다.
- Propagation.REQUIRED
  - 이미 진행중인 트랜잭션이 없으면 새로운 트랜잭션을 시작하고, 진행 중인 트랜잭션이 있으면 해당 트랜잭션에 참여한다.
- Propagation.REQUIRES_NEW
  - 이미 진행중인 트랜잭션과 무관하게 새로운 트랜잭션을 시작한다.
  - 기존에 진행중인 트랜잭션은 새로 시작된 트랜잭션이 종료할 때까지 중지된다.
- Propagation.MANDATORY
  - 진행 중인 트랜잭션이 없으면 예외를 발생시킨다.
- Propagation.NOT_SUPPORTED (메소드 레벨)
  - 트랜잭션을 필요로 하지 않음을 의미
  - 진행 중인 트랜잭션이 있으면 메서드 실행이 종료될 때 까지 진행중인 트랜잭션은 중지되며, 메서드 실행이 종료되면 트랜잭션을 계속 진행한다.
- Propagation.NEVER
  - 트랜잭션을 필요로 하지 않음을 의미, 진행 중인 트랜잭션이 존재할 경우에는 예외를 발생시킨다.
- isolation (Default = Isolation.DEFAULT)
    > 트랜잭션의 격리성 보장
  - Isolation.READ_UNCOMMITTED
    - 다른 트랜잭션에서 커밋하지 않은 데이터를 읽는 것을 허용
  - Isolation.READ_COMMITTED
    - 다른 트랜잭션에 의해 커밋된 데이터를 읽는 것을 허용
  - Isolation.REPEATABLE_READ
    - 트랜잭션 내에서 한 번 조회한 데이터를 반복해서 조회해도 같은 데이터가 조회되도록 한다.
  - Isolation.SERIALIZABLE
    - 동일한 데이터에 대해서 동시에 두 개 이상의 트랜잭션이 수행되지 못하도록 막는다.

#### 격리 수준이 막는 동시성 현상
격리 수준은 아래 세 가지 이상 현상을 어디까지 막는지로 구분된다.

| 격리 수준 | Dirty Read | Non-repeatable Read | Phantom Read |
| --- | --- | --- | --- |
| READ_UNCOMMITTED | 발생 | 발생 | 발생 |
| READ_COMMITTED | 방지 | 발생 | 발생 |
| REPEATABLE_READ | 방지 | 방지 | 발생(*) |
| SERIALIZABLE | 방지 | 방지 | 방지 |

- **Dirty Read(미커밋 읽기)**: 다른 트랜잭션이 아직 커밋하지 않은 변경을 읽는 것.
- **Non-repeatable Read(반복 불가 읽기)**: 같은 행을 두 번 읽었는데 그 사이 다른 트랜잭션의 수정·커밋으로 값이 달라지는 것.
- **Phantom Read(유령 읽기)**: 같은 조건으로 여러 행을 두 번 조회했는데 그 사이 삽입·삭제로 결과 행 집합이 달라지는 것.
- (*) 표준상 REPEATABLE_READ는 Phantom을 허용하지만, MySQL InnoDB는 갭 락(Gap Lock)으로 사실상 Phantom도 막는다.

#### @Transactional 롤백 규칙 (자주 틀리는 핵심)
- 기본적으로 **`RuntimeException`(언체크 예외)과 `Error`가 발생할 때만 롤백**된다.
- **체크 예외(checked exception, 예: `IOException`)는 기본적으로 롤백되지 않고 커밋된다.** 체크 예외에도 롤백하려면 `@Transactional(rollbackFor = Exception.class)`로 명시해야 한다.
- 특정 예외만 롤백에서 제외하려면 `noRollbackFor`를 사용한다.
- 같은 클래스 내부에서 메소드를 직접 호출(self-invocation)하면 프록시를 거치지 않아 `@Transactional`이 적용되지 않는다.
