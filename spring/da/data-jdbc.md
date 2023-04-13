## Spring Data JDBC

### interface CrudRepository<T, ID>
Spring Data에서 제공하는 CRUD 편의 제공 인터페이스

```java
public interface Repository extends CrudRepository<T, ID> {
  ...
}
```
- @Id 대응값이 0 또는 null이면 신규 데이터라고 판단 insert 쿼리를 전송
- @Id 대응값이 0 또는 null아니면 기존 데이터라고 판단 update 쿼리를 전송

### interface PagingAndSortingRepository<T, ID>
Spring Data에서 제공하는 Paging & Sorting CrudRepository 확장 인터페이스
```java
public interface Repository extends PagingAndSortingRepository<T, ID> {
  ...
}
```
- findAll(page, size, sort)
- 특정 필드명 기준 정렬시: Sort.by("filedsName").ascending() / descending

### 쿼리 메서드(Query Method)
find + By + SQL 쿼리문에서 WHERE 절의 컬럼명 + (WHERE 절 컬럼의 조건이 되는 데이터)
WHERE 절 여러 컬럼 지정시 지정시 And를 붙인다.

- Ex: find[:All]By[:OrderBy](Target)[:ASC, Desc](Pageable pagaeble)
