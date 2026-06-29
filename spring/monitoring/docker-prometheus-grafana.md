## Docker + Prometheus + Grafana

### docker-compose.yml
**네트워크 구성은 서비스 환경에 따라 다르게 설정**

```yaml
# Compose V2에서는 최상단 version 키가 obsolete 이므로 생략한다.
services:
  prometheus:
    image: prom/prometheus
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./data/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    networks:
      - project_network
  grafana:
    image: grafana/grafana
    container_name: grafana
    ports:
      - "3000:3000"
    volumes:
      # 데이터소스(프로메테우스)를 자동 연결하도록 provisioning 디렉터리 마운트
      - ./data/grafana/provisioning:/etc/grafana/provisioning
      - grafana_data:/var/lib/grafana   # 대시보드·설정 영속화
    depends_on:
      - prometheus
    networks:
      - project_network

networks:
  project_network:

volumes:
  grafana_data:
```

### Grafana 데이터소스 자동 연결(provisioning)
Grafana UI에서 매번 손으로 데이터소스를 추가하지 않도록, 아래 파일을 `./data/grafana/provisioning/datasources/datasource.yml`에 두면 컨테이너 기동 시 프로메테우스가 자동 등록된다.

```yaml
apiVersion: 1
datasources:
  - name: Prometheus
    type: prometheus
    access: proxy
    url: http://prometheus:9090   # 같은 compose 네트워크의 서비스명으로 접근
    isDefault: true
```

### 실행 & 접속
```bash
$ docker compose up -d
# Prometheus: http://localhost:9090   /   Grafana: http://localhost:3000 (초기 계정 admin/admin)
```
