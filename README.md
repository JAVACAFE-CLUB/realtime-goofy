# 배운거 전부! (카프카 중심)

### 멀티모듈

프로젝트는 다음과 같이 5개의 모듈로 구성되어 있습니다:

- 수집 시스템 : realtime-goofy-collector (port: 8081)
- 정제 시스템 : realtime-goofy-cleansing (port: 8082)
- 색인 시스템 : realtime-goofy-indexer (port: 8083)
- 서빙 시스템 : realtime-goofy-api (port: 8080)

각 모듈은 독립적으로 배포 가능합니다.
