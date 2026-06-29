## Spring Data JDBC

### interface CrudRepository<T, ID>
Spring Data에서 제공하는 CRUD 편의 제공 인터페이스

```java
public interface Repository extends CrudRepository<T, ID> {
  ...
}
```
- @Id 대응값이 0 또는 null이면 신규 데이터라고 판단해 insert 쿼리를 전송
- @Id 대응값이 0 또는 null이 아니면 기존 데이터라고 판단해 update 쿼리를 전송
- 단, 엔티티가 `Persistable<ID>`를 구현하거나 `@Version` 필드를 두면 신규 여부 판단 방식이 달라진다(직접 `isNew()` 제어 등).

### interface PagingAndSortingRepository<T, ID>
Spring Data에서 제공하는 Paging & Sorting CrudRepository 확장 인터페이스
```java
public interface Repository extends PagingAndSortingRepository<T, ID> {
  ...
}
```
- findAll(page, size, sort)
- 특정 필드명 기준 정렬시: Sort.by("fieldName").ascending() / descending

### 쿼리 메서드(Query Method)
find + By + SQL 쿼리문에서 WHERE 절의 컬럼명 + (WHERE 절 컬럼의 조건이 되는 데이터)
WHERE 절 여러 컬럼 지정시 지정시 And를 붙인다.

- Ex: find[:All]By[:OrderBy](Target)[:ASC, Desc](Pageable pageable)

### Spring Data JDBC의 핵심 (JPA와의 차이)
Spring Data JDBC는 JPA보다 **단순함**을 지향하며, JPA의 복잡한 기능을 의도적으로 덜어냈다.

- **애그리거트(Aggregate)와 애그리거트 루트(Aggregate Root)** 중심으로 설계한다. 루트 엔티티를 저장하면 그에 속한 자식들도 함께 저장·삭제된다(생명주기를 루트가 소유). 자식 컬렉션은 `@MappedCollection`으로 매핑한다.
- **영속성 컨텍스트·1차 캐시·더티 체킹이 없다.** 조회한 객체를 수정해도 자동으로 UPDATE되지 않으며, 변경 후 `save()`를 명시적으로 호출해야 한다.
- **지연 로딩(lazy loading)이 없다.** 연관은 조회 시점에 함께 적재되며 프록시를 쓰지 않는다.
- 동작이 SQL에 가깝게 예측 가능해 학습·디버깅이 쉬운 반면, JPA만큼의 자동화(변경 감지·지연 로딩)는 제공하지 않는다.
