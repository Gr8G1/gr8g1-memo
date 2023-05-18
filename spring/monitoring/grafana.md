## Grafana
그라파나(Grafana)는 실시간 데이터 시각화 및 모니터링 도구로 오픈 소스로 개발되었으며, 
다양한 데이터 소스에서 수집한 데이터를 실시간으로 모니터링하고 분석할 수 있다.
특히, 그라파나는 시계열 데이터를 다루는 데 특화되어 있으며, 대규모 환경에서 사용되는 메트릭, 로그 및 이벤트 데이터를 쉽게 시각화할 수 있다.
사용자가 다양한 데이터 소스를 연결하고, 원하는 대시보드를 생성하여 데이터를 시각화할 수 있도록 도와주며 대시보드는 다양한 차트, 그래프, 테이블 등을 포함할 수 있다. 

그라파나는 데이터 소스로서 프로메테우스(Prometheus), 그래파이트(Graphite), 엘라스틱서치(Elasticsearch), 인플럭스DB(InfluxDB) 등 다양한 시계열 데이터베이스 및 모니터링 도구와 통합이 가능하다. 
또한, 다양한 플러그인과 확장 기능을 제공하여 사용자가 필요에 맞게 확장할 수 있다.

### 데이터소스 설정
1. 그라파나 접속 -> 설정 -> 데이터 소스 -> 데이터 소스 추가 선택
<div style="text-align: center">
  <p><img width="550"  alt="" src="https://github.com/Gr8G1/gr8g1-memo/assets/29472238/9500e785-926c-474a-99f6-24534aea30f1"></p>
</div>
2.-> Prometheus 선택 -> 프로메테우스 활성화 경로 입력 (localhost or docker conatiner name)
<div style="text-align: center">
  <p><img width="550"  alt="" src="https://github.com/Gr8G1/gr8g1-memo/assets/29472238/8f247d25-4aee-4e9c-a1c0-93fe9fbb70d3"></p>
</div>
3. 저장 및 테스트로 연결 확인
<div style="text-align: center">
  <p><img width="550"  alt="" src="https://github.com/Gr8G1/gr8g1-memo/assets/29472238/7939479f-81d5-4b37-92b7-a13f096652c0"></p>
</div>

### 공유 대시보드
개인이 직접 매트릭을 활용하여 대시보드를 생성하고 관리할 수 있지만 공유 대시보드를 활용하는것도 하나의 방법이다.

공유 dashboards: [https://grafana.com/grafana/dashboards/](https://grafana.com/grafana/dashboards/)

1. 공유 대시보드 사이트에 접속한 다음에 Spring 검색
    <div style="text-align: center">
      <p><img width="550"  alt="" src="https://github.com/Gr8G1/gr8g1-memo/assets/29472238/68f84ce5-0682-4f61-a0b2-7bcea63b4c45"></p>
    </div>

2. 공개된 다양한 대시보드중 선택 -> Copy Id to clipboard
    <div style="text-align: center">
      <p><img width="550"  alt="" src="https://github.com/Gr8G1/gr8g1-memo/assets/29472238/73e09284-f667-44df-90b9-8d6855ddda2e"></p>
    </div>

3. 그라파나 접속 -> 대시보드 메뉴 -> import
    <div style="text-align: center">
      <p><img width="550"  alt="" src="https://github.com/Gr8G1/gr8g1-memo/assets/29472238/b4459e0e-c391-405e-9435-ef26c02a9303"></p>
    </div>

4. 복사한 ID 입력
    <div style="text-align: center">
      <p><img width="550"  alt="" src="https://github.com/Gr8G1/gr8g1-memo/assets/29472238/1f1b8098-7d16-4dac-b8c6-39017986699b"></p>
    </div>

5. 공유 데이터 소스 선택 후 활성화
    <div style="text-align: center">
      <p><img width="550"  alt="" src="https://github.com/Gr8G1/gr8g1-memo/assets/29472238/9be3a534-8b29-4edc-a569-dbd30024904d"></p>
    </div>
