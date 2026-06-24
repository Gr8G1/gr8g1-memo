## SQL(Structured Query Language)

### 데이터 정의 언어(DDL: Data Definition Language)

SCHEMA, DOMAIN, TABLE, VIEW, INDEX를 정의 또는 변경 삭제 시 사용되는 언어

- CREATE: SCHEMA, DOMAIN, TABLE, VIEW, INDEX 정의
- ALTER: TABLE에 대한 정의 변경
- DROP: SCHEMA, DOMAIN, TABLE, VIEW, INDEX 삭제

### 데이터 조작 언어(DML: Data Manipulation Language)

질의어를 통해 실직적으로 데이터를 처리하는데 사용하는 언어

- SELECT: 데이터 조회
- INSERT: 데이터 삽입
- UPDATE: 데이터 수정
- DELETE: 데이터 삭제

### 데이터 제어 언어(DCL: Data Control Language)

데이터의 무결성, 보안, 회복, 병행 수행 제어 등을 정의하는데 사용하는 언어
 
- GRANT: 특정 데이터베이스 사용자에게 특정 작업수행 권한 부여
- REVOKE: 특정 데이터베이스 사용자에게 특정 작업수행 권한 회수

### 트랜잭션 제어어(TCL: Transaction Control Language)

- COMMIT: 트랜잭션 처리가 정상 종료시 수행 내용을 데이터베이스에 반영하는 연산을 수행
- ROLLBACK: 트랜잭션 처리가 비정상적으로 종료되어 일관성이 깨졌을 때, 모든 변경 작업을 취소하고 이전 상태로 되돌리는 연산을 수행
- SAVEPOINT: 트랜잭션 작은 단위로 분활하여 특정 지점을 생성하는 연산을 수행

#### 단일 라인 주석 (Single-line comment)
- '--'

#### 블록 주석 (Multi-line Comment)
- 일반적인 언어에서 제공하는 블록 주석과 동일 : /* ... */

### SQL 구문(실행순서)
```
SELECT DISTINCT 속성명 ----------- (7)
  FROM 테이블명 ------------------ (1)
    [FULL] [LEFT, RIGHT, OUTER, INNER] JOIN 테이블명 -----------------(2)
    ON 조건 -----------------------(3)
  WHERE 조건 --------------------- (4)
  GROUP BY 속성명 ---------------- (5)
  HAVING 그룹 조건 --------------- (6)
  ORDER BY 속성명 ---------------- (8)
    LIMIT 개수 -------------------- (9)
```

> 작성 순서(위 코드)와 실행 순서(괄호 번호)가 다르다.
> 실행: FROM(1) → JOIN/ON(2,3) → WHERE(4) → GROUP BY(5) → HAVING(6) → SELECT/DISTINCT(7) → ORDER BY(8) → LIMIT(9)


### 조인 특성
- INNER JOIN(내부 조인)은 두 테이블을 조인할 때, 두 테이블에 모두 지정한 열의 데이터가 있어야 한다.
- OUTER JOIN(외부 조인)은 두 테이블을 조인할 때, 1개의 테이블에만 데이터가 있어도 결과가 나온다.
  - 결과값이 없는 빈 컬럼이 생성될 수 있다.
- CROSS JOIN(상호 조인)은 한쪽 테이블의 모든 행과 다른 쪽 테이블의 모든 행을 조인하는 기능이다.
- SELF JOIN(자체 조인)은 자신이 자신과 조인한다는 의미로, 1개의 테이블을 사용한다.

#### ~ KEYWORDS
```sql
-- =====================================================================
-- [DDL] 구조를 만들고 바꾸고 지운다 (CREATE / ALTER / DROP)
--   * 한 번 실행하면 자동 커밋(auto-commit), 롤백 불가 → 운영 DB에서 신중히
-- =====================================================================

-- 데이터베이스 생성, 한글 깨짐 방지 위해 문자셋 utf8(권장: utf8mb4) 지정
CREATE DATABASE shop CHARACTER SET utf8;

-- 사용할 DB 선택, 이후 쿼리는 이 DB 기준으로 실행
USE shop;

-- 테이블 생성, 컬럼 정의와 제약조건을 함께 선언
CREATE TABLE member
(
  id        INT          AUTO_INCREMENT,   -- 자동 증가(보통 PK와 함께 사용)
  name      VARCHAR(50)  NOT NULL,         -- NULL 금지(필수 입력)
  grade     VARCHAR(10)  DEFAULT 'BRONZE', -- 값 미입력 시 기본값 적용
  age       INT,

  -- 입력값 자체를 검증하는 제약, 위반 시 INSERT/UPDATE 거부
  CONSTRAINT chk_member_age CHECK (age >= 0 AND grade = 'BRONZE'),
  PRIMARY KEY (id)
);

-- 데이터베이스 삭제, 내부 테이블·데이터 전부 소멸 (복구 불가)
DROP DATABASE shop;

-- 테이블 삭제
DROP TABLE IF EXISTS member;   -- 없어도 에러 안 나게 (배포 스크립트 안전장치)
DROP TABLE member, orders;     -- 여러 개 동시 삭제

-- 기존 테이블에 제약조건 추가 (PK / FK)
ALTER TABLE orders
    ADD CONSTRAINT pk_orders PRIMARY KEY (id),
    ADD CONSTRAINT fk_orders_member
        FOREIGN KEY (member_id) REFERENCES member (id);  -- 부모 테이블 참조

-- 제약조건/컬럼/PK/인덱스 등을 삭제
ALTER TABLE orders
    DROP CONSTRAINT fk_orders_member;
    -- DROP COLUMN, DROP PRIMARY KEY, DROP INDEX 도 동일 패턴

-- 컬럼 속성(타입 등) 변경, MySQL은 MODIFY, 표준/일부 DB는 ALTER COLUMN
ALTER TABLE member
    MODIFY name VARCHAR(100) NOT NULL;   -- (표준: ALTER COLUMN name VARCHAR(100))

-- 데이터베이스 백업 (SQL Server 계열 문법)
BACKUP DATABASE shop
   TO DISK = 'C:/backup/shop.bak';


-- =====================================================================
-- [DML] 데이터를 읽고 쓰고 고치고 지운다 (SELECT / INSERT / UPDATE / DELETE)
-- =====================================================================

-- ── SELECT: 조회 ─────────────────────────────────────────────
-- 작성 순서와 실제 실행 순서가 다름 (실행: FROM→WHERE→GROUP BY→HAVING→SELECT→ORDER BY→LIMIT)
SELECT DISTINCT m.name AS member_name, COUNT(o.id) AS order_count
  FROM member AS m
  LEFT JOIN orders AS o ON m.id = o.member_id  -- LEFT: member는 다 나오고 주문 없으면 NULL
 WHERE m.grade IS NOT NULL                      -- NULL 비교는 =가 아니라 IS / IS NOT
 GROUP BY m.name                                -- 집계(COUNT 등) 기준 그룹
HAVING COUNT(o.id) >= 1                          -- 그룹에 거는 조건 (WHERE는 그룹 전 행에 적용)
 ORDER BY order_count DESC                       -- 정렬 ASC(기본)/DESC
 LIMIT 10;                                       -- 상위 N건만 (SQL Server는 TOP)

-- 조회 결과를 새 테이블로 복사
SELECT * INTO member_backup FROM member;

-- 두 SELECT 결과 합치기, UNION은 중복 제거 / UNION ALL은 중복 포함(더 빠름)
SELECT name FROM member
UNION ALL
SELECT name FROM member_backup;

-- ── WHERE: 조건 필터 ─────────────────────────────────────────
WHERE name LIKE '김%';          -- 패턴: _(글자 1개) / %(0개 이상) → '김%' = 김씨
WHERE grade = 'VIP' OR grade = 'GOLD';
WHERE age BETWEEN 20 AND 29;    -- 양 끝값 포함 (20, 29 포함)
WHERE grade IN ('VIP', 'GOLD'); -- 여러 값 중 하나라도 일치
WHERE age = ANY (SELECT age FROM member WHERE grade = 'VIP');    -- 하나라도 일치 시 true
WHERE age > ALL (SELECT age FROM member WHERE grade = 'BRONZE'); -- 전부 만족 시 true
-- 대부분 NOT 붙여 부정 가능: NOT LIKE / NOT BETWEEN / NOT IN

-- ── INSERT: 추가 ─────────────────────────────────────────────
INSERT INTO member (name, grade, age) VALUES ('홍길동', 'VIP', 30);

-- ── UPDATE: 수정 (WHERE 없으면 전체 행이 바뀜! 반드시 조건 확인) ──
UPDATE member SET grade = 'GOLD' WHERE id = 1;

-- ── DELETE / TRUNCATE: 삭제 ──────────────────────────────────
DELETE FROM member WHERE id = 1;  -- 조건부 삭제, 롤백 가능
TRUNCATE TABLE member;            -- 전체 삭제(빠름), 구조는 유지·롤백 어려움·AUTO_INCREMENT 초기화


-- =====================================================================
-- [DCL] 권한을 주고 뺏는다 (GRANT / REVOKE)
-- =====================================================================
GRANT SELECT, INSERT ON shop.member TO 'app_user'@'%';  -- 권한 부여
REVOKE INSERT ON shop.member FROM 'app_user'@'%';       -- 권한 회수


-- =====================================================================
-- [TCL] 트랜잭션을 확정/취소한다 (COMMIT / ROLLBACK / SAVEPOINT)
--   * DML 묶음을 "전부 성공 or 전부 취소"로 처리할 때 사용
-- =====================================================================
START TRANSACTION;
  UPDATE member SET age = age + 1 WHERE id = 1;
  SAVEPOINT sp1;                 -- 중간 저장점, 여기까지만 되돌릴 수도 있음
  DELETE FROM orders WHERE member_id = 1;
ROLLBACK TO sp1;                 -- sp1 이후 작업만 취소 (UPDATE는 유지)
COMMIT;                          -- 최종 확정 → DB 영구 반영
-- 문제 발생 시: ROLLBACK;       -- 트랜잭션 전체 취소
```


### Operators
다른 언어에서 제공되는 연산자들 대부분 사용 가능

#### ~ Type
- 문자
  - CHAR(size): 고정길이 문자 - 0 ~ 255 characters
  - VARCHAR(size): 가변길이 문자 - 0 ~ 65535 characters
  - BINARY(size): CHAR(size) 동일 / 저장 방식 차이 - 2진 문자 (8,000 bytes)
  - VARBINARY(size): VARCHAR(size) 동일 / 저장 방식 차기 - 2진 문자 (8,000 bytes)

- 문자열
  - TINYTEXT: 255 characters
  - TEXT(size): 65,535 bytes
  - MEDIUMTEXT: 16,777,215 characters
  - LONGTEXT: 4,294,967,295 characters

- 블롭(BLOBs(Binary Large Objects))
   - TINYBLOB: 255 bytes
   - BLOB(size): 65,535 bytes
   - MEDIUMBLOB: 16,777,215 bytes
   - LONGBLOB: 4,294,967,295 bytes

- ENUM(val, ...)

- SET(val, ...)

- 비트
  - BIT(1~64 - 기본값: 1): 비트값 저장

- 불리언
  - BOOL: 불리언 저장 - 0: false, !0 = true
  - BOOLEAN: BOOL과 동일

- 정수
  - TINYINT(size): Signed: -128 ~ 127 / Unsigned: 0 ~ 255
  - SMALLINT(size): Signed: -32768 ~ 32767 / Unsigned: 0 ~ 65535.
  - MEDIUMINT(size): Signed: -8388608 ~ 8388607 / Unsigned: 0 ~ 16777215
  - INT(size): Signed: -2147483648 ~ 2147483647 / Unsigned: 0 ~ 4294967295
  - INTEGER(size): INT()와 동일
  - BIGINT(size): Signed: -9223372036854775808 ~ 9223372036854775807 / Unsigned: 0 ~ 18446744073709551615- 

- 실수
  - FLOAT(size, d): deprecated in MySQL 8.0.17
  - FLOAT(p): p: 0 ~ 24 = FLOAT() / p: 25 ~ 53 = DOUBLE()
  - DOUBLE(size, d)
  - DOUBLE PRECISION(size, d)
  - DECIMAL(size, d)
  - DEC(size, d): DECIMAL(size,d)과 동일

- 시간(날짜)
  - DATE: Format: YYYY-MM-DD
  - DATETIME(fsp): Format: YYYY-MM-DD hh:mm:ss
  - TIMESTAMP(fsp): Format: YYYY-MM-DD hh:mm:ss / the Unix epoch('1970-01-01 00:00:00' UTC)
  - TIME(fsp): Format: hh:mm:ss

#### ~ 내장 함수들
- 숫자 관련
  - COUNT(): 특정 열의 레코드 수를 반환합니다.
  - SUM(): 특정 열의 값의 합을 반환합니다.
  - AVG(): 특정 열의 값의 평균을 반환합니다.
  - MAX(): 특정 열의 최대값을 반환합니다.
  - MIN(): 특정 열의 최소값을 반환합니다.
  - ROUND(): 숫자를 반올림합니다.
  - CEILING(): 숫자를 올림합니다.
  - FLOOR(): 숫자를 내림합니다.
  - POWER(): 숫자의 거듭제곱 값을 계산합니다.
  - SQRT(): 숫자의 제곱근 값을 계산합니다.
  - LOG(): 숫자의 로그 값을 계산합니다.
  - EXP(): 숫자의 지수 값을 계산합니다.
  - MOD(): 나눗셈의 나머지 값을 반환합니다.
  - ABS(): 숫자의 절댓값을 계산합니다.
- 문자열 관련
  - UPPER(): 문자열을 대문자로 변환합니다.
  - LOWER(): 문자열을 소문자로 변환합니다.
  - SUBSTRING(): 문자열의 일부를 반환합니다.
  - TRIM(): 문자열의 앞뒤 공백을 제거합니다.
  - CONCAT(): 문자열을 결합합니다.
  - CONCAT_WS(): 문자열을 결합하며, 구분자를 사용하여 각 문자열을 구분합니다.
  - CHARINDEX(): 문자열 내에서 특정 문자열이 위치한 인덱스를 반환합니다.
  - REPLACE(): 문자열의 일부분을 다른 문자열로 대체합니다.
  - LENGTH(): 문자열의 길이를 반환합니다.
- 시간 관련
  - DATE(): 날짜를 생성합니다.
  - YEAR(): 날짜에서 년도를 추출합니다.
  - MONTH(): 날짜에서 월을 추출합니다.
  - DAY(): 날짜에서 일을 추출합니다.
  - GETDATE(): 현재 날짜와 시간을 반환합니다.
  - DATEPART(): 날짜/시간에서 지정된 부분(년, 월, 일, 시간 등)을 반환합니다.
  - DATEDIFF(): 날짜/시간 간의 차이를 반환합니다.
  - CAST(): 데이터 형식을 변환합니다.
- NULL 처리 관련 (값이 비었을 때 대체값 채우기)
  - COALESCE(a, b, c, ...): 왼쪽부터 보다가 처음으로 NULL이 아닌 값을 반환 (인자 여러 개, 표준 SQL)
  - IFNULL(a, b): a가 NULL이면 b 반환 (MySQL 전용, 인자 2개)
  - NVL(a, b): IFNULL과 동일 (Oracle)
  - ISNULL(a, b): IFNULL과 동일 (SQL Server)
  - NULLIF(a, b): a와 b가 같으면 NULL, 다르면 a 반환 (0으로 나누기 방지 등에 활용)
  - 비교 시 주의: NULL은 `= NULL`로 못 찾음 → 반드시 `IS NULL` / `IS NOT NULL` 사용

- 분기(조건) 관련 (값에 따라 다른 결과 반환)
  - CASE WHEN 조건 THEN 값 [WHEN ...] ELSE 기본값 END: if-else 처럼 동작
  - IF(조건, 참값, 거짓값): 단순 2분기 (MySQL 전용)

### 중첩 쿼리(서브쿼리) 단계별 이해

> 실무 쿼리는 컬럼명이 어렵고 깊게 중첩돼 읽기 힘들다.
> 아래는 같은 패턴을 "쇼핑몰" 예시로 쉽게 푼 버전, 단계별로 한 겹씩 쌓아 올린다.

#### 예시용 테이블 (3개만 기억하면 됨)

```sql
-- 회원: 누가 가입했는가
member   (id, name, grade, country_code)   -- grade: 'VIP' / 'NORMAL', country_code: 'KR' 등

-- 주문: 누가 무엇을 얼마에 샀는가
orders   (id, member_id, product_name, price)

-- 국가 코드표(코드 → 사람이 읽는 이름): 'KR' → '대한민국'
country  (code, name_kr)
```

#### 1단계 — 서브쿼리 기본: SELECT 안에 SELECT 넣기

```sql
-- 회원 이름과 함께, 그 회원의 주문 건수를 한 컬럼으로 가져온다
SELECT
    m.name,
    (SELECT COUNT(*) FROM orders o WHERE o.member_id = m.id) AS order_count  -- 스칼라 서브쿼리(값 1개 반환)
FROM member m;
```
- 괄호 안 `SELECT`는 **값 하나(스칼라)** 만 반환해야 컬럼 자리에 들어갈 수 있다.
- `o.member_id = m.id` 처럼 바깥(m)을 참조하면 **상관 서브쿼리**, 회원 행마다 다시 실행된다.

#### 2단계 — COALESCE로 NULL을 기본값으로 바꾸기

```sql
-- 코드값(KR)을 국가명(대한민국)으로 바꾸되, 매칭 실패 시 '미지정'으로 채운다
SELECT
    m.name,
    COALESCE(
        (SELECT c.name_kr FROM country c WHERE c.code = m.country_code),  -- 코드 → 이름 변환
        '미지정'                                                          -- 위가 NULL이면 대체
    ) AS country_name
FROM member m;
```
- `country`에 해당 코드가 없으면 서브쿼리는 NULL → `COALESCE`가 `'미지정'`으로 안전하게 fallback.

#### 3단계 — 우선순위 fallback: COALESCE 인자 여러 개

```sql
-- 1순위 국가명 → 없으면 2순위 원본 코드 → 그래도 없으면 '미지정'
SELECT
    m.name,
    COALESCE(
        (SELECT c.name_kr FROM country c WHERE c.code = m.country_code),  -- 1순위
        m.country_code,                                                   -- 2순위
        '미지정'                                                          -- 최후
    ) AS country_label
FROM member m;
```
- `COALESCE`는 왼쪽부터 **처음 만난 비-NULL 값**을 쓴다, 우선순위 fallback에 자주 쓰는 패턴.

#### 4단계 — 2중 중첩: 서브쿼리 안의 서브쿼리

```sql
-- "가장 비싼 주문을 한 회원"의 국가명을 구한다 (안쪽부터 읽는 게 핵심)
SELECT
    COALESCE(
        (SELECT c.name_kr
           FROM country c
          WHERE c.code = (
                SELECT m.country_code                       -- (3) 그 회원의 국가 코드
                  FROM member m
                 WHERE m.id = (
                       SELECT o.member_id                   -- (2) 그 주문을 한 회원 id
                         FROM orders o
                        ORDER BY o.price DESC                -- (1) 가장 비싼 주문
                        LIMIT 1)
          )),
        '미지정'
    ) AS top_spender_country;
```
- 읽는 순서는 **가장 안쪽(1) → 바깥(3)**, 안쪽 결과가 바깥 조건의 입력값이 된다.
- 안쪽이 한 단계씩 "id → 회원 → 국가코드 → 국가명"으로 변환을 이어가는 구조.

#### 5단계 — CASE WHEN: 값에 따라 분기

```sql
-- 주문 금액 구간별로 등급 라벨을 붙인다
SELECT
    o.product_name,
    o.price,
    CASE
        WHEN o.price >= 100000 THEN '고가'
        WHEN o.price >= 10000  THEN '중가'
        ELSE '저가'                       -- 위 조건 모두 불일치 시
    END AS price_level
FROM orders o;
```
- `CASE`는 위에서부터 조건을 검사, **처음 참인 THEN** 값을 반환, `ELSE`는 기본값.

#### 6단계 — 종합: 위 패턴을 한 쿼리에 모으기 (실무 쿼리의 축소판)

```sql
-- 회원 1명에 대해: 이름 / 국가명 / 주문수 / VIP여부를 한 행으로 모은다
SELECT
    m.name,

    -- 코드 → 이름 변환 + fallback
    COALESCE(
        (SELECT c.name_kr FROM country c WHERE c.code = m.country_code),
        '미지정'
    ) AS country_name,

    -- 상관 서브쿼리로 집계값 가져오기
    COALESCE(
        (SELECT COUNT(*) FROM orders o WHERE o.member_id = m.id),
        0
    ) AS order_count,

    -- 분기 라벨
    CASE WHEN m.grade = 'VIP' THEN '우수회원' ELSE '일반회원' END AS grade_label
FROM member m
WHERE 1=1
  AND m.id = 1   -- 실무에선 이 줄이 <if>로 동적으로 붙는 자리
LIMIT 1;
```

> 이 6단계 구조가 곧 실무의 복잡한 쿼리와 동일한 골격이다.
> 어려워 보이는 실무 쿼리도 결국 (1) 스칼라 서브쿼리 + (2) COALESCE fallback + (3) 코드→이름 변환 + (4) 다중 중첩 + (5) CASE 분기 의 조합일 뿐이다.


### Join 쿼리와 Select 쿼리 중 어떤 것이 더 효율적일까?
- Join 쿼리는 여러 테이블을 합치는 작업을 수행할 때 사용하며, 일반 쿼리는 하나의 테이블에서 데이터를 가져오는 작업을 수행할 때 사용한다.
- Join 쿼리는 여러 테이블에서 필요한 데이터를 한 번에 가져오는 경우 효율적일 수 있지만, 테이블이 많고 복잡할수록 성능상의 문제가 발생할 수 있다.
- 이러한 경우에는 성능을 향상시키기 위해 필요한 테이블만 join 하는 것을 고려하는 것이 좋다.
- 반면에 일반 쿼리는 하나의 테이블만 조작하는 경우 성능상 이슈가 없을 수 있다.

### Join 쿼리를 사용할 때 성능상의 문제
- Cartesian product : 두 테이블을 전부 결합하면 행의 수가 곱해지므로, 테이블의 크기가 커질수록 결과로 반환되는 행의 수도 기하급수적으로 증가하여 성능이 저하될 수 있다.
- Table Scan : 테이블에 인덱스가 없거나 잘못된 인덱스가 사용되면 테이블 전체를 스캔해야 하므로, 성능이 저하될 수 있다.
- Data Type Mismatch : Join 조건에서 데이터 타입이 맞지 않으면 성능이 저하될 수 있다.
- Over-Join : 필요한 테이블만 Join 하지 않고 필요없는 테이블도 Join 하면 불필요한 행이 많이 생성되어 성능이 저하될 수 있다.
- Join 에 사용되는 테이블의 크기가 매우 크면 메모리를 많이 사용하여 메모리가 부족할 수 있고 또는 속도가 느려질 수 있다.

### Join 쿼리를 사용할 때의 성능상 문제에 대해 해결하는 방법
- 적절한 인덱스 사용 : Join 조건에 해당하는 컬럼에 인덱스를 생성해서 사용하면 성능을 향상시킬 수 있다.
- 적절한 Join 조건 사용 : 필요한 테이블만 join 하는 것을 고려하고, join 조건에서 가능한 경우 unique 키를 사용하면 성능을 향상시킬 수 있다.
- Join 테이블 순서 : 크기가 작은 테이블을 앞에 join 하면 성능을 향상시킬 수 있다.
- Limit : 필요한 결과만 가져오도록 limit을 사용하면 성능을 향상시킬 수 있다.
- Sub-query : join 이 너무 복잡하거나 큰 테이블에서 필요한 데이터만 가져오려면 sub-query를 사용하면 성능을 향상시킬 수 있다.
- 추가적으로 복잡한 join 을 사용할 경우, Join 테이블을 미리 생성해두거나 Materialized View를 사용하면 성능을 향상시킬 수 있다.
- 마지막으로, Join 쿼리를 실행할 때 서버의 리소스가 부족하지 않는지 확인하는 것도 중요하다. 메모리, CPU, 디스크 I/O 등 서버 자원을 확인하고 최적화할 필요가 있을 수 있다.
