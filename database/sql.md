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
SELECT DISTINCT 속성명 ----------- (5, 7)
  FROM 테이블명 ------------------ (1)
    [FULL] [LEFT, RIGHT, OUTER, INNER] JOIN 테이블명 -----------------(2)
    ON 조건 -----------------------(3)
  WHERE 조건 --------------------- (4)
  GROUP BY 속성명 ---------------- (6)
  HAVING 그룹 조건 --------------- (7)
  ORDER BY 속성명 ---------------- (9)
```


### 조인 특성
- INNER JOIN(내부 조인)은 두 테이블을 조인할 때, 두 테이블에 모두 지정한 열의 데이터가 있어야 한다.
- OUTER JOIN(외부 조인)은 두 테이블을 조인할 때, 1개의 테이블에만 데이터가 있어도 결과가 나온다.
  - 결과값이 없는 빈 컬럼이 생성될 수 있다.
- CROSS JOIN(상호 조인)은 한쪽 테이블의 모든 행과 다른 쪽 테이블의 모든 행을 조인하는 기능이다.
- SELF JOIN(자체 조인)은 자신이 자신과 조인한다는 의미로, 1개의 테이블을 사용한다.

#### ~ KEYWORDS
```sql
-- 데이터베이스 생성
CREATE DATABASE 데이터베이스명 CHARACTER SET utf8;

-- 테이블 생성 및 제약조건 추가
CREATE TABLE 테이블명
(
  속성명1 속성1 AUTO_INCREMENT,
  속성명2 속성2 NOT NULL,
  속성명3 속성3 DEFAULT 기본값
  
  CONSTRAINT 제약조건명 CHECK (속성명1 >= 속성1 AND 속성명2 = '속성2')
);

-- 데이터베이스 사용설정
USE 데이터베이스명;

-- 데이터베이스 삭제
DROP DATABASE 데이터베이스명;

-- 테이블 삭제
DROP TABLE IF EXISTS 테이블명;
DROP TABLE 테이블명, ...;

-- 테이블의 제약 조건(constraint) 추가
ALTER TABLE 테이블명
    ADD CONSTRAINT 제약조건명
        PRIMARY KEY (속성명, ...),
     FOREIGN KEY (속성명, ...) REFERENCES 테이블명 (테이블 속석명, ...);

-- 테이블의 제약 조건(constraint), 열(column), 기본 키(primary key), 인덱스(index), 뷰(view), 기본값(default) 등을 삭제
ALTER TABLE 테이블명
    DROP [CONSTRAINT, COLUMN, PRIMARY KEY, INDEX, VIEW, DEFAULT ...] [...];

-- 테이블의 열(column) 속성(attribute)을 변경
ALTER COLUMN 속성명 속성;

-- 데이터베이스 백업(backup)
BACKUP DATABASE 데이터베이스명
   TO DISK = '경로';

-- SELECT 절
SELECT [DISTINCT, TOP 1~] 속성명 AS 별칭, ...
  INTO 테이블명
  FROM 테이블명
  UNION [ALL]
  [FULL] [LEFT, RIGHT, OUTER, INNER] JOIN 테이블명
  ON 조건
  GROUP BY 속성명
  HAVING 조건
  WHERE 조건 IS [NOT] NULL
  ORDER BY 속성명 [ASC, DESC]
    LIMIT 1~;
    
-- WHERE 절
WHERE 속성명 [NOT] LIKE 조건; (_: 공란 / %: 나머지)
WHERE 속성명 [NOT] OR 조건;
WHERE 속성명 [NOT] BETWEEN 조건 [AND] 조건;
WHERE 속성명 [NOT] IN (속성값, ...);
WHERE 속성명 = ANY(SELECT 구문) | ALL(SELECT 구문)
-- * ANY(SELECT 구문): 하나라도 일치하면 True 반환
-- * ALL(SELECT 구문): 전체가 일치하면 True 반환

-- 레코드(record) 추가
INSERT INTO 테이블명 (`속성명`, ...) VALUES (속성값, ...);

-- 레코드(record) 수정
UPDATE 테이블명 SET 속성명 = 속성값 WHERE 조건;

-- 레코드(record) 삭제
DELETE FROM 테이블명 | TRUNCATE 테이블명;
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
