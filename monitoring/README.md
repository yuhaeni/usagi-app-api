# monitoring/

Prometheus + Grafana 메트릭 수집 스택. dev(AWS Lightsail Container Service)에서는 app/nginx/redis/prometheus/grafana 가 같은 task 의 sibling 컨테이너로 떠 네트워크 네임스페이스를 공유하므로 `localhost:8080`, `localhost:9090` 으로 서로 직접 통신한다.

## 디렉토리 구조

- `prometheus/` — Prometheus 컨테이너 빌드 자산 (Dockerfile + scrape config)
- `grafana/` — Grafana 컨테이너 빌드 자산 (Dockerfile + provisioning + dashboards)

## 로컬 단독 검증

Lightsail 의 sibling localhost 가정은 로컬 `docker run` 에서 깨진다 — 컨테이너마다 독립 loopback 을 가지므로 컨테이너 안의 `localhost` 는 자기 자신을 가리킨다. macOS/Windows Docker Desktop 에서는 `host.docker.internal` 가 호스트(현재 머신)를 가리키므로 그것으로 **임시 치환**하면 로컬 검증이 가능하다.

### Prometheus 단독

전제: Spring Boot 앱이 호스트 8080 에서 떠 있어야 함 (`./gradlew bootRun`).

```bash
# 1. scrape target 임시 치환 (커밋 X)
sed -i '' "s/'localhost:8080'/'host.docker.internal:8080'/" \
    monitoring/prometheus/prometheus.yml

# 2. 컨테이너 띄움 (커스텀 빌드 없이 upstream 이미지 + 마운트)
docker run -d --name usagi-prometheus-local -p 9090:9090 \
    -v "$(pwd)/monitoring/prometheus/prometheus.yml:/etc/prometheus/prometheus.yml" \
    prom/prometheus:v2.55.0

# 3. 브라우저
open http://localhost:9090/targets       # usagi-app-api State=UP 기대
open http://localhost:9090/graph         # PromQL: up{job="usagi-app-api"} → 1

# 4. 검증 끝 — ★ 반드시 되돌리기
docker rm -f usagi-prometheus-local
git checkout monitoring/prometheus/prometheus.yml
```

### Grafana 단독 (provisioning load 검증)

```bash
# 0. dashboards/ 빈 디렉토리 확보 (Dockerfile 의 COPY dashboards/ 통과용)
mkdir -p monitoring/grafana/dashboards
touch monitoring/grafana/dashboards/.gitkeep

# 1. datasource URL 임시 치환 (Grafana 컨테이너 → 호스트 9090 도달용)
sed -i '' 's|http://localhost:9090|http://host.docker.internal:9090|' \
    monitoring/grafana/provisioning/datasources/prometheus.yaml

# 2. 빌드 + 띄움
docker build -t usagi-grafana-dev:local monitoring/grafana
docker run -d --name usagi-grafana-local -p 3001:3000 \
    -e GF_SECURITY_ADMIN_PASSWORD=admin \
    usagi-grafana-dev:local

# 3. 브라우저 → admin / admin 로그인
open http://localhost:3001
# Data sources → Prometheus → Save & test → "Data source is working"

# 4. 검증 끝 — ★ 되돌리기 필수
docker rm -f usagi-grafana-local
git checkout monitoring/grafana/provisioning/datasources/prometheus.yaml
```

## ⚠️ 주의

`host.docker.internal` 은 **macOS/Windows Docker Desktop 전용** 특수 호스트 (Linux 는 별도 옵션 필요). dev(Lightsail)에서는 sibling localhost 가정이라 **동작하지 않는다** — 임시 치환이 커밋되면 dev 배포에서 메트릭 수집 0건 + Grafana datasource 연결 실패. 반드시 검증 후 `git checkout` 으로 되돌릴 것.

로컬 풀스택(앱 + Prometheus + Grafana 한 번에)은 셋업 비용 큼 → 인프라 셋업 검증은 **dev 사이클 한 번 돌리는 게 효율적**이다.
