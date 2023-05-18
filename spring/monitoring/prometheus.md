## Prometheus
프로메테우스는 모니터링 및 애플리케이션 성능 분석 도구로 서버, 네트워크, 애플리케이션 등 
IT 인프라 및 시스템의 상태와 성능을 모니터링하고 분석하여 문제를 식별하고 해결하는 데 도움을 받을 수 있다.

클라우드 및 온프레미스 환경에서 활용할 수 있으며, 서버 상태, 리소스 사용, 애플리케이션 성능, 로그 등 다양한 데이터를 수집하는데 
이때 사용되는 데이터는 메트릭(metric)이라고 불리며, 프로메테우스는 이러한 메트릭을 수집, 저장, 분석하고, 사용자에게 시각화된 형태로 제공한다.

프로메테우스는 모니터링 시스템에서 사용되는 질의 언어(Query Language) PromQL 제공함으로써 쿼리를 활용해 수집된 데이터를 분석하고 원하는 정보를 추출 할 수 있다.

> document: [https://prometheus.io/](https://prometheus.io/)
> 로고 의미: 프로메테우스(프로메테우스, Prometheus)는 그리스 신화에서 등장하는 인물로, 인류에게 불을 가져와 지식과 문명을 선사한 신화적인 존재


### 데이터 수집
지정된 주기로 애플리케이션에서 제공하는 엔드포인트를 호출하여 메트릭 데이터를 가져온다. 이 데이터는 내부의 시계열 데이터베이스에 저장된다.
스크래핑(scraping) 메커니즘을 사용하여 여러 애플리케이션 및 서비스에서 메트릭을 수집할 수 있다. 
이를 통해 여러 서버 및 애플리케이션의 메트릭을 중앙 집중식으로 수집하고 모니터링할 수 있다.
수집된 메트릭은 PromQL을 사용하여 프로메테우스에서 쿼리하고 분석할 수 있으며, 필요에 따라 그래프나 경고 등의 기능을 활용하여 시각화하거나 실시간 모니터링을 수행할 수 있다.
프로메테우스는 스프링 액추에이터와 마이크로미터와 함께 사용되는 것이 일반적이며, 이들은 함께 시스템의 상태와 성능을 효과적으로 모니터링하고 분석하기 위한 강력한 조합으로 사용된다..


### 프로메테우스 아키텍처
<div style="text-align: center">
  <p><img width="424"  alt="prometheus-architecture" src="https://github.com/Gr8G1/gr8g1-memo/assets/29472238/7ac9fb36-16c9-4dab-94f1-d48f2e798998"></p>
  <p>출처: <a href="https://prometheus.io/docs/introduction/overview/">prometheus</a></p>
</div>


### 프로메테우스 연동
```gradle
dependencies {
  ...
  
  implementation 'io.micrometer:micrometer-registry-prometheus'
  
  ...
}
```

애플리케이션 실행 -> http://${application}${/base_path}/actuator/prometheus -> 변경된 매트릭 정보확인

### 프로메테우스 수집 설정
프로메테우스 설정 파일인 prometheus.yml은 메트릭 수집 대상, 스크래핑 주기, 알림 규칙 등을 정의하는 구성 파일이다.

```yml
# 전역 설정을 정의하는 섹션, 기본적인 프로메테우스 서버 동작에 영향을 준다.
global: 
  scrape_interval: 1m                            # 스크래핑 주기 : 기본값: 1m
  evaluation_interval: 1m                        # 평가 주기 : 기본값: 1m

# 메트릭을 수집할 대상을 정의하는 섹션
scrape_configs:
  - job_name: 'prometheus'  
    metrics_path: /actuator/prometheus           # 수집 엔드포인트를 지정합니다.
    static_configs:
      - targets: ['localhost:9090']

# 알림 규칙 파일을 정의하는 섹션
rule_files:
  - 'alert.rules.yml'                            # alertmanager 알림을 보내기 위해 사용. 

# 알림 전송을 구성하는 섹션
alerting:
  alertmanagers:                                 # alertmanager 엔드포인트 및 기타 관련 설정을 지정
    - static_configs:
        - targets: ['alertmanager:9093']

# 원격 스토리지에 대한 쓰기 및 읽기 연결을 구성하는 섹션
remote_write:
  - url: 'http://remote-write-endpoint:8080/write'
remote_read:
  - url: 'http://remote-read-endpoint:8081/read'
```

### 프로메테우스 연동 결과 확인
1. 프로메테우스 메뉴 -> Status -> Configuration 확인 
   - 앞서 작성된 prometheus.yml 정보 출력
2. 프로메테우스 메뉴 -> Status -> Targets
   - Job으로 설정한 타켓 상태 확인

### 게이지(Gauge) & 카운터(Counter)
매트릭 정보는 크게 게이지와 카운터라는 2가지로 분류된다.
- 게이지(Gauge)
  - 임의로 오르내일 수 있는 값
  - Ex: CPU 사용량, 메모리 사용량, 사용중인 커넥션
- 카운터(Counter)
  - 누적하여 증가하는값 
  - Ex: HTTP 요청 수, 로그 발생 수

### 지표 생성시 유용한 쿼리 명령어
1. increase()
   - 카운터 메트릭의 증가율을 계산하는 데 사용 
   - 주어진 시간 범위 내에서 카운터 메트릭의 증가량을 반환한다
     - Ex: increase(http_requests_total[1h])는 지난 1시간 동안 http_requests_total 카운터 메트릭의 증가량을 계산한다.
2. rate()
   - 게이지 메트릭의 변화율을 계산하는 데 사용 
   - 주어진 시간 범위 내에서 게이지 메트릭의 변화율을 반환 
     - Ex: rate(cpu_usage_percent[5m])는 최근 5분 동안 cpu_usage_percent 게이지 메트릭의 평균 변화율을 계산한다.
3. sum()
   - 여러 개의 메트릭 값을 합산하는 데 사용
   - Ex: sum(http_requests_total)은 http_requests_total 메트릭의 모든 인스턴스의 값을 합산한다.
4. avg()
   - 여러 개의 메트릭 값을 평균화하는 데 사용
   - Ex: avg(cpu_usage_percent)은 cpu_usage_percent 메트릭의 모든 인스턴스의 값을 평균화한다.
5. max()
   - 여러 개의 메트릭 값 중 최댓값을 반환하는 데 사용 
   - Ex: max(memory_usage_bytes)는 memory_usage_bytes 메트릭의 최댓값을 반환한다.
6. min()
   - 여러 개의 메트릭 값 중 최솟값을 반환하는 데 사용 
   - Ex: min(temperature_celsius)는 temperature_celsius 메트릭의 최솟값을 반환한다.
7. topk()
   - 주어진 숫자에 해당하는 최상위 메트릭 값을 반환하는 데 사용 
   - Ex: topk(5, http_requests_total)은 http_requests_total 메트릭에서 값이 가장 큰 5개의 결과를 반환한다.
