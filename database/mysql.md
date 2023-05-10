## Mysql

Mysql Github: [https://gist.github.com/nrollr/3f57fc15ded7dddddcc4e82fe137b58e](https://gist.github.com/nrollr/3f57fc15ded7dddddcc4e82fe137b58e)

### Mac 설치 (:8.0.28 기준)

#### MySql 공식 홈페이지 DMG 다운로드
Community: [https://dev.mysql.com/downloads/mysql/](https://dev.mysql.com/downloads/mysql/)

#### HomeBrew 사용
```bash
$ brew install mysql

# 서버 실행/종료
$ mysql.server start
$ mysql.server stop

# 데몬 확인 / 실행 / 재시작 / 종료
$ brew services list
$ brew services start mysql
$ brew services restart mysql
$ brew services stop mysql

# 서버 실행 후 초기 비밀번호 설정
$ mysql_secure_installation
# 비밀번호 적용 확인
$ mysql -u root -p ''
```

#### brew services
```bash
# 서비스 확인
$ brew tap homebrew/services
$ brew services start mysql
$ brew services list

$ mysql -V
```
> Homebrew Services Github: https://github.com/Homebrew/homebrew-services


### User 생성 및 확인
데이터베이스(스키마) 생성
```mysql
-- 데이터베이스 사용자 확인
USE mysql;
SELECT user, host FROM user;

-- 사용자 권한 확인
SHOW GRANTS FOR 계정명@'%';
    
-- 사용자 권한 설정
GRANT ALL PRIVILEGES ON dbname.* TO 계정명@'%';
-- 주의: '%' = IP 지정 or localhost 설정 
-- 특정 주소(IP) 없이 '%' 단일 설정은 전체 공개와 동일하다.

-- 사용자 생성
CREATE USER 계정명@'%' identified BY 'password';
    
-- 사용자 삭제
DROP USER 계정명@'%';
    
-- 사용자 비밀번호 변경
ALTER USER 계정명@'%' IDENTIFIED WITH mysql_native_password BY 'password';
```

### Mysql Spring boot 연동 (:8.0.28 기준)

#### build.gradle
```groovy
dependencies {
    runtimeOnly 'com.mysql:mysql-connector-j'
}
```

#### application.yaml
```yml
spring:
  datasource:
    url: "jdbc:mysql://${location}:${port}/${dbname}?useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Seoul"
    username:
    password:
    driver-class-name: "com.mysql.jdbc.Driver"
 jpa:
   database: mysql
   database-platform: org.hibernate.dialect.MySQL8Dialect           # ! DB 버전별 상이 mysql -V 확인
   hibernate:
     ddl-auto: update
   show-sql: true
```
