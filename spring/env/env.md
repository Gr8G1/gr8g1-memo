## Environment

### 개발 환경별 Profiles 설정

#### 폴더 및 application.yml 추가 및 변경
- src/main/resources
  - application.yml -> application-common.yml

- src/main/resources-env/{환경별 폴더}
  - application.yml
      running: 
      domain:

      spring:
          profiles:
              include: common
              active: {환경}
      ...


> 버전 주의(Spring Boot 2.4+): 멀티 문서/프로파일 처리 방식이 바뀌었다.
> - 프로파일 묶음은 `spring.profiles.include` 대신 **`spring.profiles.group`** 으로 정의하는 것이 권장된다(예: `spring.profiles.group.dev: common,db`).
> - 프로파일 한정 설정 문서는 `spring.profiles: <name>` 대신 **`spring.config.activate.on-profile: <name>`** 을 사용한다.
> - `spring.profiles.active`는 프로파일 한정 문서 안에서는 지정할 수 없다(전역 위치에서만 설정).

#### build.gradle 설정 추가
```groovy
ext {
    set('profile', (!project.hasProperty('profile') || !profile) ? 'local' : profile)
}

sourceSets {
    main {
        resources {
            srcDirs "src/main/resources-env/${profile}"
        }
    }
}
```

#### build & boot
```bash
$ ./gradlew clean bootJar || build -Pprofile={실행환경}
$ java -jar build/libs/{buildFile}.jar
```
