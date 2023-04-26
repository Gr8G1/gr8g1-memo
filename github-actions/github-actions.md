## GitHub Actions

Github 에서 공식적으로 제공하는 빌드, 테스트 및 배포 파이프라인을 자동화할 수 있는 CI/CD 플랫폼
- document: [actions](https://docs.github.com/ko/actions)

### 표현식
- document: [example-using-an-object-filter](https://docs.github.com/ko/actions/learn-github-actions/expressions#example-using-an-object-filter)

### 이벤트 트리거
- document: [events-that-trigger-workflows](https://docs.github.com/ko/actions/using-workflows/events-that-trigger-workflows)

### GitHub status
- github api 상태: [https://www.githubstatus.com/](https://www.githubstatus.com/)

#### front/back 동일 저장소 사용시 pull-request-target & labels 워크플로우 구성
```yaml
on:
  pull_request_target:
      types: [closed, labeled]
      branches: ["브랜치명"]

  ...

jobs:
  if_merged:
      if: github.event.pull_request.merged == true && contains(github.event.pull_request.labels.*.name, 'deploy-[구분자]')
      
...
```
