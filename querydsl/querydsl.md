## QueryDsl

정적 타입을 이용해서 SQL 과 같은 쿼리를 생성할 수 있도록 해주는 프레임워크

### Q 타입 생성 위치

build -> generated -> querydsl

### JPAQueryFactory 동시성

JPAQueryFactory 를 생성할 때 제공하는 EntityManager 에 달려있는데, 스프링 프레임워크는 여러 쓰레드에서 동시에 같은 EntityManager 에 접근해도,
트랜잭션 마다 별도의 영속성 컨텍스트를 제공하기 때문에, 동시성 문제는 걱정하지 않아도 된다.

### QueryDsl 적용
#### build.gradle
```groovy
plugins {
    id "com.ewerk.gradle.plugins.querydsl" version "1.0.10"
}

configurations {
    querydsl.extendsFrom compileClasspath
}

ext {
    set('queryDslVersion', "5.0.0")
}

dependencies {
    implementation "com.querydsl:querydsl-jpa:${queryDslVersion}"
    implementation "com.querydsl:querydsl-apt:${queryDslVersion}"
}

def querydslDir = "$buildDir/generated/querydsl"

querydsl {
    jpa = true
    querydslSourcesDir = querydslDir
}

sourceSets {
    main {
        java {
            srcDir querydslDir
        }
    }
}

compileQuerydsl {
    options.annotationProcessorPath = configurations.querydsl
}
```

### 사용자 정의 리포지토리
1. 사용자 정의 인터페이스 작성
```java
public interface TestRepositoryCustom {
     List<Entity> search(Condition condition);
}
```
2. 사용자 정의 인터페이스 구현체 작성
인터페이스 구현체 작성시 파일명 **Impl** 필수
```java
public class TestRepositoryImpl implements TestRepositoryCustom {
     private final JPAQueryFactory queryFactory;

     public MemberRepositoryImpl(EntityManager em) {
         this.queryFactory = new JPAQueryFactory(em);
     }

     @Override
     public List<Entity>search() {
         ...
     }
}
```
3. 스프링 데이터 리포지토리에 사용자 정의 인터페이스 상속
```java
public interface TestRepository extends JpaRepository<Entity, Long>, TestRepositoryCustom {
     ...
}
```

### @QueryProjection
- DTO 를 컴파일러로 타입 체크할 수 있으는 가장 안전한 방법이다.
  다만 DTO 에 QueryDSL 어노테이션을 유지해야 하는 점과 DTO 까지 Q 파일을 생성해야 하는 단점이 있다.
  > @QueryProjection 사용시 QueryDsl에 의존적이게 된다. 사용시 주의하자

### 동적 쿼리 생성 2가지 방법
- BooleanBuilder || BooleanExpression
- Where 다중 파라미터 사용

### 수정/삭제 벌크 연산시 주의
JPQL 배치와 마찬가지로, 영속성 컨텍스트에 있는 엔티티를 무시하고 실행되기 때문에 배치 쿼리를 실행하고 나면 영속성 컨텍스트를 초기화 하는 것이 안전하다.

### QueryDsl Web 지원
> Document: https://docs.spring.io/spring-data/jpa/docs/2.2.3.RELEASE/reference/html/#core.web.type-safe

> 페이징 및 정렬 기능 구현시 내부 서포트 기능 사용 또는 서포트 기능 Querydsl5RepositorySupport 상속 후 사용 추천

```java
@Repository
public abstract class Querydsl5RepositorySupport {
    private final Class<?> domainClass;
    private Querydsl querydsl;
    private EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    public Querydsl5RepositorySupport(Class<?> domainClass) {
        Assert.notNull(domainClass, "Domain class must not be null!");

        this.domainClass = domainClass;
    }

    @Autowired
    public void setEntityManager(EntityManager entityManager) {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        JpaEntityInformation<?, ?> entityInformation =
            JpaEntityInformationSupport.getEntityInformation(domainClass, entityManager);
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<?> path = resolver.createPath(entityInformation.getJavaType());

        this.entityManager = entityManager;
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @PostConstruct
    public void validate() {
        Assert.notNull(entityManager, "EntityManager must not be null!");
        Assert.notNull(querydsl, "Querydsl must not be null!");
        Assert.notNull(queryFactory, "QueryFactory must not be null!");
    }

    protected JPAQueryFactory getQueryFactory() {
        return queryFactory;
    }

    protected Querydsl getQuerydsl() {
        return querydsl;
    }

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    protected <T> JPAQuery<T> select(Expression<T> exp) {
        return getQueryFactory().select(exp);
    }

    protected <T> JPAQuery<T> selectFrom(EntityPath<T> ep) {
        return getQueryFactory().selectFrom(ep);
    }

    protected <T> Page<T> applyPagination(
        Pageable pageable,
        Function<JPAQueryFactory, JPAQuery<T>> contentQuery,
        Function<JPAQueryFactory, JPAQuery<?>> countQuery
    ) {
        JPAQuery<T> jpaContentQuery = contentQuery.apply(getQueryFactory());
        JPAQuery<?> jpaCountQuery = countQuery.apply(getQueryFactory());

        List<T> content = getQuerydsl().applyPagination(pageable, jpaContentQuery).fetch();

        return PageableExecutionUtils.getPage(content, pageable, () -> (long) jpaCountQuery.fetchOne());
    }
}
```
