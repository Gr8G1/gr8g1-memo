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
