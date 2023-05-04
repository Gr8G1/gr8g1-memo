## AWS - Deploy

### S3
버킷 생성 후 하위 내용 스텝별 진행

#### 정적 웹 호스팅
  1. 버킷 -> 속성 -> 정적 웹 사이트 호스팅: 비활성화 -> 활성화
  2. 버킷 -> 권한 -> 퍼블릭 액세스 차단(버킷 설정): 활성화 -> 비활성화
  3. 버킷 -> 권한 -> 버킷 정책
     1. 편집 -> 정책 생성
     2. Select Type of Policy: S3 Bucket Policy
     3. Effect: Allow
     4. Principal: *
     5. AWS Service: AmazonS3
     6. Actions: GetObject
     7. Amazon Resource Name(ARN): ${arn}/*
     8. 정책 추가

### EC2
#### JAVA 설치
```bash
$ bash
$ cd ~
$ sudo apt update
$ sudo apt install openjdk-11-jre-headless
```

#### Github Project SSH 연결 설정
```bash
$ ssh-keygen || ssh-keygen -t ed25519 -C "your_email@example.com"
$ cat .ssh/id_rsa.pub             # git -> settings -> SSH and GPG keys -> SSH keys 등록
```
- **ssh-key issue**: [2023-03-23-we-updated-our-rsa-ssh-host-key](https://github.blog/2023-03-23-we-updated-our-rsa-ssh-host-key/)

#### AWS CLI 설치
```bash
$ curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
$ sudo apt install unzip
$ unzip awscliv2.zip
$ sudo ./aws/install
```

#### Code Deploy Agent 설치
```bash
$ sudo apt install ruby-full
$ sudo apt install wget

$ cd /home/ubuntu
$ sudo wget https://aws-codedeploy-ap-northeast-2.s3.ap-northeast-2.amazonaws.com/latest/install
$ sudo chmod +x ./install
$ sudo ./install auto > /tmp/logfile

# 정상 동작 확인
$ sudo service codedeploy-agent status                             // * active 내용 확인
```

### EC2 Timezone 변경
- EC2 OS 환경별 설정: [configure-amazon-time-service-amazon-linux](https://docs.aws.amazon.com/ko_kr/AWSEC2/latest/UserGuide/set-time.html#configure-amazon-time-service-amazon-linux)

### EC2 run.sh
개발 임시 배포 테스트 script/run.sh 사용시

**SERVICE_NAME 등록 후 사용**
  - *.sh 권한 변경 및 실행
  ```bash
  $ sudo chmod 755 run.sh
  $ ./run.sh
  $ ./run.sh %{func}
  ```

### RDS - MySql
- DB 생성 후 로컬 테스트
```bash
$ mysql -u [계정 ID] --host [엔드 포인트 주소] -P [포트 번호] -p

# 계정 ID: admin
# 엔드 포인트 주소: AWS RDS EndPoint
# 포트번호: (기본) 3306
# 포트번호 변경시 규칙 추가: EC2 보안 -> 인바운드 규칙 -> 사용자 지정 TCP, 포트 지정(오픈)
```

### AWS - CloudFront, Route53, ACM
- 

### AWS - CodeDeploy (Github Actions 사용시)
#### 환경변수 등록
```bash
$ sudo vi /etc/environment
```

#### /appspec.yml
- CodeDeploy 에서 지정한 각 단계에 맞춰 셸 스크립트를 실행 지정
- CodeDeploy(appspec)
   - hooks: https://docs.aws.amazon.com/ko_kr/codedeploy/latest/userguide/reference-appspec-file-structure-hooks.html

#### /scripts/~ 
내부 파일 확인
  - initialize.sh
  - server_clear.sh
  - server_start.sh
    - **${buildName}** 프로젝트 빌드명 변경 설정 필수
  - server_stop.sh

####  **IAM 권한 확인 (역할 생성)**
- EC2CodeDepl~~oyRole
  1. AmazonS3FullAccess
  2. AmazonEC2RoleforAWSCodeDeploy
  3. AWSCodeDeployFullAccess
  4. AmazonSSMFullAccess
- 신뢰관계 설정
  ```json
  {
    "Version": "2012-10-17",
    "Statement": [
      {
        "Effect": "Allow",
        "Principal": {
          "Service": [
            "ec2.amazonaws.com",
            "codedeploy.ap-northeast-2.amazonaws.com"
          ]
        },
        "Action": "sts:AssumeRole"
      }
    ]
  }
  ```

### **EC2 - ELB - Docker(Nginx + spring) 접근 순서**
1. 클라이언트는 ELB(DNS)로 접근한다.
2. ELB는 연결된 인스턴스(AWS EC2 인스턴스) 중 하나를 선택하여 연결한다.
3. 선택된 인스턴스 내부에서 Docker 컨테이너를 찾아 연결한다.
4. Docker 컨테이너 내부의 Nginx 서버는 예:) 80번 포트를 통해 클라이언트 요청을 수신한다.
5. Nginx 서버는 리버스 프록시 설정을 통해 예:) 8080 포트에 바인딩된 스프링 애플리케이션 서버로 요청을 전달한다.
6. 스프링 애플리케이션 서버는 해당 요청을 처리하고 결과를 Nginx 서버에 반환한다.
7. Nginx 서버는 반환된 결과를 ELB에게 전달합니다.
8. ELB는 반환된 결과를 클라이언트에게 전달합니다.

> 도커 컨테이너는 각각 독립된 환경에서 실행되는 가상 환경일 뿐, 외부로 나가는 데이터의 흐름을 제어하는 역할을 하지 않는다.
> 따라서 도커 컨테이너 내부에서 발생하는 데이터 흐름은 호스트와 마찬가지로 인터넷을 통해 외부로 나간다.
> document: [https://docs.docker.com/config/containers/container-networking/](https://docs.docker.com/config/containers/container-networking/)

### AWS - ParameterStore(**Region 주의**)

#### 생성 규칙
- /prefix/name/key
  - Ex:
    - 이름: /prefix/name/key
    - 값: value

#### 프로젝트 설정
- /src/main/resources/bootstrap.yml
  ```yaml
    aws:
      paramstore:
        enabled: true                        # 파라미터 스토어 설정 ON
        prefix: ${prefix}                    # 파라미터 스토어의 Key 구분: /a/b/c == /a
        name: ${name}                        # 파라미터 스토어의 Key 구분: /a/b/c == /b
        profileSeparator: _                  # profile 구분(name에서 사용): /a/b_local/c -> local (Spring Boot 애플리케이션 profile이 local 일 경우 값을 호출)
        failFast: true                       # 파라미터 스토어에서 값을 읽지 못할시 행동 결정: true(정지), false(실행)
  ```

#### /build.gradle
```groovy
  // # Boot v2.3 이하
  // Spring cloud 부트 호환 버전: https://spring.io/projects/spring-cloud#overview
  // aws parameter 버전 : https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-starter-aws-parameter-store-config

  ext {
    set('springCloudVersion', "부트 호환 버전")
  }

  dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
  }

  dependencies {
      implementation 'org.springframework.cloud:spring-cloud-starter-aws-parameter-store-config:${클라우드 호환 버전}'
  }
```

#### /src/main/resources/application.yml
```yaml
  # ParameterStore 변수 조회
    spring
    datasource
  url: ${/prefix/name/key}
  username: ${/prefix/name/key}
  password: ${/prefix/name/key}
    ...
```

### EC2 Swap Memory 할당

프리티어의 경우 메모리 부족 현상이 발생할 수 있다.  
메모리 부족 현상 해결을 위해 AWS 에서 공식적으로 Swap 메모리 설정 후 사용을 안내하고 있다.

> Swap 메모리 설정: https://aws.amazon.com/ko/premiumsupport/knowledge-center/ec2-memory-swap-file/

```bash
// Swap 설정 후 적용 내용 확인
$ free -h
``` 

### EC2 Security-group 설정 : 80 -> 8080 포트 포워딩
```bash
// # 포트 할당
$ sudo iptables -A PREROUTING -t nat -i eth0 -p tcp --dport 80 -j REDIRECT --to-port 8080

// 할당된 포트 확인
$ sudo iptables -t nat -L --line-numbers
```

> iptables 그림으로 보면서 이해하기: https://webterror.net/2015/02/11/1622/
