## 저장소 전략 (Repository Strategy: 모노레포 vs 멀티레포 vs 서브모듈)

### 정의
저장소 전략은 **여러 서비스(또는 모듈)의 소스 코드를 Git에서 어떤 단위로 묶거나 나눌 것인가**를 결정하는 설계 선택이다.
MSA처럼 서비스가 여러 개로 나뉘는 순간, "이 코드들을 한 저장소에 담을지 / 서비스마다 따로 둘지 / 따로 두되 하나로 엮을지"를 정해야 한다.

- Ex: 같은 회사의 여러 부서 문서를 보관하는 방식에 비유할 수 있다.
  **모노레포**는 모든 부서 문서를 한 캐비닛에 칸막이(폴더)로 나눠 넣는 것이고,
  **멀티레포**는 부서마다 별도 캐비닛을 두는 것이며,
  **서브모듈**은 부서별 캐비닛은 따로 두되 "각 캐비닛의 몇 번 서랍을 보라"는 색인표를 가진 통합 캐비닛을 하나 더 두는 것이다.

### 등장 배경 / 왜 필요한가
가장 흔한 혼동은 **"DB를 나눴으니 Git 저장소도 그에 맞춰 엮어야 한다"**는 인과 착각이다. 이 둘은 차원이 다르다.

| 구분 | 무엇인가 | 어디서 관리되나 | 나누는 이유 |
| --- | --- | --- | --- |
| **데이터 저장소 (DB)** | 회원 DB, 상품 DB, 주문 DB, 결제 DB | 런타임 인프라(RDS·Docker·k8s 등) | 독립 확장·장애 격리·스키마 독립(→ Database per Service) |
| **소스 저장소 (Git)** | 각 서비스의 코드(엔티티·마이그레이션·설정) | GitHub·GitLab 등 | 코드 소유권·배포 단위·협업 권한 |

[MSA](./msa.md)의 다이어그램에서 `── 회원 DB`는 **실행 중인 데이터베이스 인스턴스**를 나눈 것이지 Git이 관리하는 대상이 아니다.
Git이 관리하는 것은 그 DB를 다루는 **코드**일 뿐이다. 따라서 DB 분리는 **배포·운영의 문제**, 저장소 전략은 **소스 관리의 문제**로 완전히 분리해서 봐야 한다.

> 핵심 명제: **DB를 4개로 나눴다는 사실은 Git을 어떻게 둘지에 대해 아무것도 강제하지 않는다.**

### 핵심 개념 / 구성요소
도메인(서비스) 단위로 **소스 경계를 분리**하는 것은 어느 전략에서나 공통이다. 갈리는 지점은 **그 경계를 무엇으로 표현하느냐**다.

```
                    소스의 도메인 경계 분리 (공통 전제 / 필수)
                                  │
        ┌─────────────────────────┼─────────────────────────┐
        ▼                         ▼                         ▼
  경계 = 레포 경계           경계 = 폴더 경계          경계 = 별도 레포 + 통합 참조
   (멀티레포)                 (모노레포)                (서브모듈 / 서브트리)
        │                         │                         │
  레포 4개                    레포 1개                   자식 레포 N개 + 부모 레포 1개
```

#### (1) 멀티레포 (Polyrepo / Multi-repo) — MSA의 표준
각 서비스가 **완전히 독립된 별개의 Git 저장소**를 가진다. 서로를 엮는 장치 없이 그냥 따로 존재한다.

```
github.com/org/member-service     (독립 레포)
github.com/org/product-service    (독립 레포)
github.com/org/order-service      (독립 레포)
github.com/org/payment-service    (독립 레포)
```

- 서브모듈 같은 연동 장치가 **전혀 없다.** "따로따로 존재하는 것" 자체가 곧 전략이다.
- 팀별 독립 소유·독립 배포·독립 권한·독립 CI/CD가 자연스럽다 → MSA의 "독립 배포/확장" 철학과 가장 잘 맞는다.

#### (2) 모노레포 (Monorepo) — 한 레포 안에 폴더로
**하나의 Git 저장소** 안에 서비스들을 폴더로 나눠 담는다. Google·Meta 방식.

```
org/platform-repo/            (단 하나의 레포)
├── member-service/
├── product-service/
├── order-service/
├── payment-service/
└── shared-libs/              (공통 코드 공유가 쉬움)
```

- 서브모듈이 아니다. 그냥 **한 레포의 하위 폴더**다. 겉보기엔 폴더 나눔이라 서브모듈로 오해하기 쉽다.
- 공통 코드 공유·일괄 리팩터링·여러 서비스 동시 수정(원자적 커밋)이 쉽다.
- 규모가 커지면 Nx, Turborepo, Bazel, Gradle multi-module 등 빌드 도구로 변경 영향 범위만 빌드/배포한다.

#### (3) Git 서브모듈 (Submodule)
부모(슈퍼) 레포가 자식 레포들을 **특정 커밋(SHA)에 고정해 참조**한다. 자식은 여전히 별개 레포이고, 부모는 "이 시점의 자식"을 가리키는 포인터만 갖는다.

```
org/platform-repo/            (부모 레포)
├── .gitmodules               ← 어떤 자식 레포를 어디에 둘지 메타정보
├── member-service/    → member-service 레포의 특정 커밋을 가리킴
├── product-service/   → product-service 레포의 특정 커밋
├── order-service/     → ...
└── payment-service/   → ...
```

- 부모 레포에는 자식의 실제 코드가 아니라 **"어느 커밋을 쓸지"라는 참조만** 저장된다.
- 장점: 통합 진입점 하나로 전체 버전을 묶어 고정(스냅샷)할 수 있다.
- 단점: `git submodule update`를 잊으면 꼬이고, 커밋 동기화가 번거로워 초보 팀에서 오히려 사고가 잦다. MSA에서 굳이 권장되진 않는다.

#### (4) Git 서브트리 (Subtree) — 서브모듈의 대안
서브모듈처럼 참조만 하는 것이 아니라, 자식 레포의 **내용과 히스토리를 부모 레포 안으로 실제 병합**해 하위 폴더로 넣는다.

- `.gitmodules` 같은 별도 메타파일이 없어, 클론한 사람은 그냥 일반 폴더로 본다.
- 서브모듈보다 사용이 단순하지만, 히스토리가 섞여 업스트림과의 동기화 관리가 복잡해질 수 있다.

### 장점 / 단점(트레이드오프)
어느 것도 만능이 아니다. **조직 구조·배포 단위·팀 규모**에 따라 선택한다.

| 기준 | 멀티레포 | 모노레포 | 서브모듈 |
| --- | --- | --- | --- |
| 저장소 개수 | 서비스 수만큼(N개) | 1개 | 부모 1 + 자식 N |
| 독립 배포/CI | 가장 자연스러움 | 도구로 경로 필터링 필요 | 자식 단위로 가능 |
| 공통 코드 공유 | 어려움(버전 배포 필요) | 쉬움(같은 레포 내 참조) | 중간 |
| 원자적(동시) 변경 | 불가(레포별 PR 분리) | 쉬움(한 커밋에 여러 서비스) | 어려움 |
| 권한 분리 | 레포 단위로 깔끔 | 한 레포라 거칠음(경로 권한 필요) | 자식 단위로 분리 |
| 전체 버전 고정 | 어려움(태그 관리 분산) | 쉬움(한 커밋이 전체 스냅샷) | 쉬움(부모가 SHA 고정) |
| 학습/관리 난이도 | 낮음 | 중간(빌드 도구 필요) | 높음(동기화 함정) |
| 대표 사용처 | 대부분의 MSA 조직 | Google·Meta·Nx 생태계 | 라이브러리 벤더링·펌웨어 |

### 실무 예제
Ex: **멀티레포** — 각 서비스를 독립 레포로 만들고 따로 클론한다. 별도 연동 명령이 없다.

```bash
# 서비스마다 독립 레포. 그냥 각자 클론할 뿐이다.
git clone https://github.com/org/member-service.git
git clone https://github.com/org/product-service.git
git clone https://github.com/org/order-service.git
git clone https://github.com/org/payment-service.git
```

Ex: **모노레포** — 한 레포 안에서 폴더로 도메인을 나눈다. 클론은 한 번이다.

```bash
git clone https://github.com/org/platform-repo.git
cd platform-repo
ls
# member-service/  product-service/  order-service/  payment-service/  shared-libs/
```

```
# (Gradle 멀티모듈로 모노레포를 구성한 예) settings.gradle
rootProject.name = 'platform'
include 'member-service'
include 'product-service'
include 'order-service'
include 'payment-service'
include 'shared-libs'
```

Ex: **서브모듈** — 부모 레포에서 자식 레포들을 등록하고 클론할 때 함께 받아온다.

```bash
# 부모 레포에서 자식 레포를 서브모듈로 등록
git submodule add https://github.com/org/member-service.git member-service
git submodule add https://github.com/org/payment-service.git payment-service
git commit -m "chore: 서비스 서브모듈 등록"

# 부모를 클론하는 사람은 서브모듈까지 한 번에 받아야 한다
git clone --recurse-submodules https://github.com/org/platform-repo.git

# 자식 레포의 최신 커밋으로 참조를 갱신
git submodule update --remote member-service
```

```
# 서브모듈 등록 후 자동 생성되는 .gitmodules
[submodule "member-service"]
    path = member-service
    url = https://github.com/org/member-service.git
[submodule "payment-service"]
    path = payment-service
    url = https://github.com/org/payment-service.git
```

### 주의점 / 안티패턴
- **"DB를 나눴으니 서브모듈로 엮어야 한다"는 착각**: DB 분리(런타임)와 저장소 전략(소스)은 무관하다. 서브모듈은 통합 참조가 **정말 필요할 때만** 쓰는 도구이지 MSA의 필수품이 아니다.
- **"개별 저장소(레포)는 필수"라는 과장**: 필수인 것은 **도메인별 소스 경계 분리**이지 물리적 레포 분리가 아니다. 모노레포도 폴더로 경계를 똑같이 지킨다. 경계를 레포로 표현할지 폴더로 표현할지는 별개의 선택이다.
- **멀티레포인데 공통 코드를 복붙으로 공유**: 같은 코드를 여러 레포에 복사하면 수정 누락이 발생한다. 공통 모듈은 사내 패키지(npm registry·Maven repository 등)로 배포해 버전으로 공유한다.
- **모노레포인데 도구 없이 전체 빌드**: 한 서비스만 바꿔도 전체를 빌드·배포하면 모노레포의 의미가 사라진다. 변경 경로를 감지해 영향받는 서비스만 빌드(Nx·Turborepo·Bazel·CI 경로 필터)하도록 구성해야 한다.
- **서브모듈 동기화 함정**: `--recurse-submodules` 없이 클론하면 자식 폴더가 비어 있고, 자식 커밋을 갱신한 뒤 부모에 커밋하지 않으면 다른 사람은 옛 버전을 본다. 팀 숙련도가 낮으면 사고가 잦다.
- **분산 모놀리식과의 혼동**: 멀티레포로 나눴다고 자동으로 독립 배포가 되는 것이 아니다. 레포만 쪼개고 서로 강하게 결합하면 여전히 [분산 모놀리식](./msa.md)이다. 저장소 전략은 결합도 문제를 직접 해결해 주지 않는다.

> 권장 선택 기준
> - 학습·사이드 프로젝트, 혼자/소수 → **모노레포** (한 곳에서 전부 보고 한 번에 커밋, 관리 부담 최소)
> - 팀 분리·독립 배포·권한 분리가 명확 → **멀티레포** (MSA 정석)
> - 여러 레포를 특정 버전으로 묶어 고정해야 함 → **멀티레포 + 서브모듈/매니페스트** (난이도↑, 필요할 때만)

> document: https://monorepo.tools/
> document: https://git-scm.com/book/en/v2/Git-Tools-Submodules
> document: https://martinfowler.com/bliki/MonolithFirst.html

### 관련 문서
- [MSA(마이크로서비스)](./msa.md)
- [서비스 디스커버리](./service-discovery.md)
- [배포전략](../deployment-strategy.md)
