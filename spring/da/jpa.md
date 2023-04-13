## JPA(Java Persistence API)
Java 진영에서 사용하는 ORM(Object-Relational Mapping) 기술의 표준 사양(또는 명세, Specification)
> Jakarta Persistence라고도 불린다.

### Hibernate ORM
JPA 표준 사양을 구현한 구현체
> 기타 구현체로는 EclipseLink, DataNucleus 등이있다.

### 영속성 컨텍스트(Persistence Context)
JPA에서는 테이블과 매핑되는 엔티티(Entity) 객체 정보를 영속성 컨텍스트(Persistence Context)라는 곳에 보관, 애플리케이션 내에서 지속성을 보장하고
이렇게 보관된 엔티티 정보는 데이터베이스 테이블에 저장, 수정, 조회, 삭제하는데 사용한다.

- 1차 캐시
  - 영속성 컨텍스트에 저장(persist)하는 API 호출시 데이터베이스에 즉시 접근하는것이 아닌 1차 캐시에 저장 후 버퍼링 전략(Buffering strategy)에 따라 동작한다.
- 쓰기 지연 저장소
  - 영속성 컨텍스트에 저장(persist)하는 API 호출시 저장될 객체를 쿼리 형태로 저장한다.

#### 영속성 컨텍스트 이점
- 1차 캐시
- 동일성(identity) 보장
- 트랜잭션을 지원하는 쓰기 지연 (transactional write-behind)
- 변경 감지(Dirty Checking)
- 지연 로딩(Lazy Loading)

#### Flush(플러시)란?
영속성 컨텍스트의 변경 내용을 데이터베이스에 반영하는 행위

#### Flush(플러시) 발생 경로
- 변경 감지(Dirty Checking)
- 수정된 엔티티 쓰기 지연 SQL 저장소에 등록
- 쓰기 지연 SQL 저장소의 쿼리를 데이터베이스에 전송 (등록, 수정, 삭제 쿼리)

#### 플러시 모드 옵션
- em.setFlushMode()
  - FlushModeType.AUTO: 커밋이나 쿼리를 실행할 때 플러시 호출 (Default)
  - FlushModeType.COMMIT: 커밋할 때만 플러시 호출

#### 영속성 컨텍스트의 Flush 실행 순간
- em.flush() - 직접 호출
- 트랜잭션 커밋 - 플러시 자동 호출
- JPQL 쿼리 실행 - 플러시 자동 호출

 > 영속성 컨텍스트는 플러시 호출에 영향을 받지 않는다. (1차 캐시가 지워지지 않는다)

### 엔티티의 생명주기
- 비영속 (new/transient)
  - 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
- 영속 (managed)
  - 영속성 컨텍스트에 관리되는 상태
- 준영속 (detached)
  - 영속성 컨텍스트에 저장되었다가 분리된 상태
- 삭제 (removed)
  - 삭제된 상태


### **연관 관계 매핑**
엔티티 클래스 간의 관계를 맺어주는것
> 연관관계의 주인(Owner): 객체 양방향 연관관계는 관리 주인이 필요하다.
> 등록, 수정 접근 제어 (연관관계 주입 편의 메소드 제공 권장( 영속성 컨텍스트 관리 이슈 ))

#### ~ 단방향에서의 정의
```java
@[Direction]
@JoinColumn(..)
private T t;
```

#### ~ 양방향에서의 정의
```java
@[Direction](mappedBy = "t")
private List<T> ts = new ArrayList<>();
```

#### 1:1 (일대일 : @OneToOne)
주 테이블이나 대상 테이블중  외래키 위치를 선택 가능하다.
- 외래키에 유니크(UNI) 제약조건 추가
- 단 / 양방향
  - 외래키가 존재하는 객체가 연관관계의 주인이된다.
  - 단방향 관계는 JPA 지원 X (역방향 검색 불가)

#### 1:N (일대다 : @OneToMany)
일(1)에 해당하는 클래스가 다(N)에 해당하는 객체를 참조할 수 있는 관계
- 단방향
  - 연관관계의 주인
  -  @JoinColumn 함께 사용 필수 (미지정시 조인 테이블 형태로 테이블 자동 생성됨)
- 양방향
  - 공식적으로 지원되는 기능은 아니다 편법으로 사용가능하다.
  - @JoinColumn(insertable=false, updatable=false) 읽기 전용 필드로 지정해서 양방향 형태를 취한다.

- _테이블 기준_
  - 다 테이블에서 외래키(Forign key)를 갖는다.
  - 객체 기준
  - 일 기준 객체에서 참조 객체를 갖는다. (참조 객체는 외래키 역할을 수행)

#### N:1 (다대일: @ManyToOne)
(N)에 해당하는 클래스가 일(1)에 해당하는 객체를 참조할 수 있는 관계
- 단 / 양방향
  - 연관 관계의 주인

- 테이블 기준
  - 다 테이블에서 외래키(Forign key)를 갖는다.

- 객체 기준
  - 다 기준 객체에서 참조 객체를 갖는다. (참조 객체는 외래키 역할을 수행)

### N:M (다대다: @ManyToMany)
관계형 데이터베이스는 정규화된 테이블 2개로 다대다 관계를 표현할 수 없다
> 연결(중간) 테이블을 추가해서 1:N, N:1 관계로 풀어내야한다.

- 매핑: 단방향, 양방향 가능
  - @JoinTable로 연결 테이블 지정
    ```java
    @JoinTable(
      name = "JOIN_TABLE_NAME"
      joinColumns = @JoinColumn(name = "Join_PK")
      inversejoinColumns = @JoinColumn(name = "Inverse_Join_PK")
    )
    ```

### 영속성 전이(CascadeType)
- CascadeType.ALL
  - 상위 엔터티에서 하위 엔터티로 모든 작업을 전파
- CascadeType.PERSIST
  - 하위 엔티티까지 영속성 전달
- CascadeType.MERGE
  - 하위 엔티티까지 병합 작업을 지속
- CascadeType.REMOVE
  - 하위 엔티티까지 제거 작업을 지속
- CascadeType.REFRESH
  - 데이터베이스로부터 인스턴스 값을 다시 읽어 오기(새로고침) (연결된 하위 엔티티까지 전이)
- CascadeType.DETACH
  - 영속성 컨텍스트에서 엔티티 제거 (연결된 하위 엔티티까지)

  
### Annotations
@Table
위 애너테이션이 선언되지 않는다면 기본적으로 클래스(Entity)명을 테이블 이름과 매핑한다.
- name: 테이블명 직접 선언

@Entity
JPA 관리 대상 Entity 지정
- name: 엔티티 이름 직접 선언
  - 미 지정시 기본값으로 클래스 이름을 엔티티 이름으로 사용한다.

@Id
기본키(Primary key) 정의

@GeneratedValue
기본키(Primary key) 생성 전략 설정
- strategy
  - GenerationType.IDENTITY
    - 기본키 생성을 DB에게 위임 (MySQL: AUTO INCREMENT)
    - 특정 DB 벤더에 의존적이다.
  - GenerationType.TABLE
    - 별도의 테이블을 직접 생성해 데이터베이스 시퀀스를 흉내내는 방식
    - 특정 벤더에 의존적이지 않다.
      ```java
      // @TableGenerator() 함께 사용
      // DDL  
      // create table MY_SEQUENCES (
      //    sequence_name varchar(255) not null,
      //    next_val bigint,
      //    primary key (sequence_name)
      // )
            
       @TableGenerator(
          name = "TableStrategy_SEQ_GENERATOR"    // 식별자 생성기 이름 (필수)
          table = "MY_SEQUENCES",                 // 시퀀스 테이블 이름 (자동생성 | 사전정의)
          pkColumnValue = "MY_SEQUENCES_ID"       // 키로 사용할 값 이름
          allocationSize = 50                     // 시퀀스 한 번 호출에 증가하는 수 (성능 최적화에 사용됨 데이터베이스 시퀀스 값이 하나씩 증가하도록 설정되어 있으면 이 값 을 반드시 1로 설정해야 한다)
        )
            
        public class TableStrategy {
            @Id
            @GeneratedValue(strategy = GenerationType.TABLE, generator = "TableStrategy_SEQ_GENERATOR")
            private Long id;
            ...
        }
      ```
    - GenerationType.SEQUENCE
      - DB의 sequence 객체를 이용해 유일한 값을 순서대로 생성한다.
      - 특정 벤더에 의존적이다.
        ```java
        // @SequenceGenerator 함께 사용
        @Entity
        @SequenceGenerator(
          name = "SequenceStrategy_SEQ_GENERATOR"   // 식별자 생성기 이름 (필수)
          sequenceName = "SequenceStrategy_SEQ",    // 데이터베이스에 등록되어 매핑될 시퀀스 이름
          initialValue = 1,                         // DDL 생성 시에만 사용 (시퀀스 DDL을 생성할 때 처음 1 시작하는 수를 지정한다.)
          allocationSize = 50                       // 시퀀스 한 번 호출에 증가하는 수 (성능 최적화에 사용됨 데이터베이스 시퀀스 값이 하나씩 증가하도록 설정되어 있으면 이 값 을 반드시 1로 설정해야 한다)
        )
            
        public class SequenceStrategy {
              @Id
              @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SequenceStrategy_SEQ_GENERATOR")
              private Long id;
              ...
          }
        ```
    - GenerationType.AUTO
      - DB 벤더에 따라 자동으로 3가지 전략 중 하나를 선택한다.
        이때 DB 벤더가 변경되도 전략을 수정하지 않아도 되지만, SEQUENCE나 TABLE 전략을 선택된다면 sequence나 키 테이블을 생성이 선행되야함에 주의 필요

@Column
- name
  - 컬럼명 직접 정의
- nullable (Default: true)
  - 컬럼에 null 값 허용 여부 선언
- updatable (Default: true)
  - 컬럼 데이터를 수정 허용 여부 선언
- unique (Default: false)
  - unique 유니크 제약 조건 설정 폴트
- ... 

@Lob
BLOB, CLOB 타입 매핑

@Transient
임시 필드 정의
  - 데이터베이스와 매핑되지 않는다.

@Temporal
시간 맵핑에 사용되는 애너테이션
  - 구버전 java.util.Date, java.util.Calendar 타입과 대응하기 위해 사용한다.

@Enumerated (Default: ORDINAL)
enum 타입 필드 정의시 사용한다.
- EnumType.STRING
  - enum 타입 문자열로 저장된다. (사용 O)
    - enum 타입 필드 정의시 기본값 변경 필수
- EnumType.ORDINAL
  - enum의 순서를 정수형으로 저장한다. (사용 X)
    - enum 의 정의된 순서가 변경되어 테이블에 저장될 시 심각한 장애를 발생 시킬 수 있다.

@MappedSuperclass
- 엔티티별 공통 정보 상속에 사용한다.
- 데이터베이스 테이블에 영향을 미치지 않는다.
- 조회, 검색 불가(em.find(BaseEntity) 불가)
- 직접 생성해서 사용할 일이 없으므로 추상 클래스 권장

@Inheritance
상속 관계 매핑을 지정하기 위한 어노테이션
- InheritanceType.SINGLE_TABLE
  - 부모 클래스와 자식 클래스들의 필드들을 모두 하나의 테이블에 매핑
  - @DiscriminatorColumn 어노테이션을 이용해서 구분자 컬럼을 지정할 수 있다.
- InheritanceType.TABLE_PER_CLASS
  - 부모 클래스와 자식 클래스들이 각각 별도의 테이블에 매핑
  - 자식 클래스가 매핑될 때 부모 클래스의 필드들도 함께 매핑된다.
- InheritanceType.JOINED
  - 부모 클래스와 자식 클래스들이 각각 별도의 테이블에 매핑
  - 자식 클래스가 매핑될 때 부모 클래스의 기본키(PK)를 외래키(FK)로 참조한다. 이를 통해 자식 클래스와 부모 클래스를 조인해서 사용
  

@Query("select c from ClassType (as?) c where c.Id = :Id")
JPQL 또는 Native Query를 작성시 사용
- **ClassType**: 실제 객체를 의미한다.
  - Native Query를 작성시 **ClassType**는 **DB**를 기준으로 작성되어야한다.

#### JPQL 작성 예시
```java
@Query("SELECT e FROM Employee e WHERE e.department.id = :deptId")
List<Employee> findByDepartmentId(@Param("deptId") Long deptId);
```

#### Native Query 작성 예시
```java
@Query(value = "SELECT * FROM employee WHERE department_id = :deptId", nativeQuery = true)
List<Employee> findByDepartmentId(@Param("deptId") Long deptId);
```

> document: [jpa.query-methods.at-query](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query)

### 예외 처리
Entity 에서 전파된 예외 던지기는 API 계층까지 전파되므로 API 계층의 GlobalExceptionAdvice 에서 캐치(catch)한 후, 처리할 수 있다


### JPA 설정 - application(.yml?)
#### spring.jpa.hibernate.ddl-auto
데이터베이스 스키마 자동 생성 설정

- create:
  - 기존 테이블 삭제 후 재생성 (DROP + CREATE)
- create-drop
  - create와 같으나 종료시점에 테이블 (DROP)
- update
  - 변경된 내용만 반영 (UPDATE SET) (운영시점 사용 X)
- validate
  - 엔티티와 테이블이 정상 매핑 여부만 확인
- none
  - 스키마 자동 생성 사용하지 않음
