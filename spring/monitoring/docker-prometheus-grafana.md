## Docker + Prometheus + Grafana

### docker-compose.yml
**네트워크 구성은 서비스 환경에 따라 다르게 설정**

```bash

version: "3"
services:
  prometheus:
    image: prom/prometheus
    container_name: prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./data/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml
    networks:
      project_network:
  grafana:
    image: grafana/grafana
    container_name: grafana
    ports:
      - "3000:3000"
    networks:
      project_network:

networks:
  project_network:
```
