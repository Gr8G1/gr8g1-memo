## Elasticsearch

### 정의
Elasticsearch는 **Apache Lucene** 기반의 **분산 검색·분석 엔진(distributed search and analytics engine)** 이다.
데이터를 **역색인(inverted index)** 구조로 저장해, 수억 건의 문서에서도 **전문 검색(full-text search)** 을 밀리초 단위로 수행한다.
단순 검색을 넘어 **집계(aggregation) 기반 분석**, **관련도 점수(relevance score)** 정렬, **수평 확장(horizontal scaling)** 을 기본으로 제공한다.

- Ex: 책 한 권에서 "트랜잭션"이라는 단어를 찾는다고 하자.
  본문을 1쪽부터 끝까지 훑으면(풀스캔) 느리다. 대신 책 뒤의 **'찾아보기(색인)'** 에서 "트랜잭션 → 41, 88, 233쪽"을 보고 바로 펼치면 빠르다.
  Elasticsearch의 역색인이 바로 이 '찾아보기'이며, **"단어 → 그 단어가 든 문서 목록"** 을 미리 만들어 두는 것이 핵심이다.

### 등장 배경 / 왜 필요한가
관계형 데이터베이스(RDB)는 정형 데이터의 정확한 일치 조회에는 강하지만, **자연어 텍스트 검색에는 근본적인 한계**가 있다.
대표적으로 `LIKE '%키워드%'` 검색은 다음 문제를 안고 있다.

- **인덱스를 못 탄다(풀스캔)**: 와일드카드가 앞에 붙은 `LIKE '%키워드%'` 는 B-Tree 인덱스를 활용하지 못해 **전체 행을 한 줄씩 훑는다**. 데이터가 커질수록 선형으로 느려진다.
- **형태소·유사도 분석이 불가능하다**: "달리다"로 검색해도 "달렸다", "달리는"은 찾지 못한다. 단순 문자열 포함 여부만 볼 뿐, **어간 추출이나 동의어 처리**를 하지 못한다.
- **관련도 정렬이 없다**: 키워드가 제목에 5번 나온 문서와 본문에 1번 스친 문서를 **얼마나 잘 맞는지** 기준으로 정렬할 수 없다. RDB는 "맞다/아니다"만 안다.
- **분석·집계가 약하다**: "최근 1시간 로그를 상태코드별로 묶어 1분 단위 추이"를 실시간으로 뽑는 것은 RDB의 강점이 아니다.

이 한계를 해결하기 위해 **텍스트를 미리 분석해 역색인으로 저장하고, 관련도 점수로 정렬하며, 수평 확장으로 대용량을 감당하는** 전문 검색 전용 엔진이 필요해졌다. Elasticsearch가 그 표준적 선택지다.

### 핵심 개념 / 구성요소
Elasticsearch는 데이터를 **문서(Document)** 단위로 저장하고, 이를 **인덱스(Index)** 로 묶으며, 인덱스를 **샤드(Shard)** 로 쪼개 여러 **노드(Node)** 에 분산한다. 노드들의 집합이 **클러스터(Cluster)** 다.

```
                          ┌──────────────────────────────────────────────┐
                          │                Cluster (클러스터)               │
                          │                                                │
        클라이언트 ──REST──▶│   ┌──────────────┐   ┌──────────────┐         │
        (검색/색인 요청)     │   │   Node A      │   │   Node B      │        │
                          │   │ ┌──────────┐ │   │ ┌──────────┐ │         │
                          │   │ │ Shard 1  │ │   │ │ Shard 2  │ │  ← Primary│
                          │   │ │(Primary) │ │   │ │(Primary) │ │           │
                          │   │ └──────────┘ │   │ └──────────┘ │         │
                          │   │ ┌──────────┐ │   │ ┌──────────┐ │         │
                          │   │ │ Shard 2  │ │   │ │ Shard 1  │ │  ← Replica│
                          │   │ │(Replica) │ │   │ │(Replica) │ │          │
                          │   │ └──────────┘ │   │ └──────────┘ │         │
                          │   └──────────────┘   └──────────────┘         │
                          │     같은 Shard의 Primary와 Replica는              │
                          │     항상 서로 다른 Node에 배치된다(가용성)          │
                          └──────────────────────────────────────────────┘

   하나의 Index(예: products)  =  Shard 1 + Shard 2 (+ 각각의 Replica)
   문서(Document)는 라우팅 규칙(보통 _id 해시)에 따라 어느 Shard에 들어갈지 결정된다.
```

- **문서(Document)**: 색인·검색의 최소 단위. JSON 객체 하나가 곧 한 문서다(RDB의 행(Row)에 해당). 각 문서는 고유한 `_id` 를 가진다.
- **인덱스(Index)**: 같은 성격의 문서를 모아 둔 논리적 묶음(RDB의 테이블(Table)에 해당). 예: `products`, `logs-2026-06`.
- **필드(Field)**: 문서를 구성하는 키-값(RDB의 컬럼(Column)에 해당). 각 필드는 타입(text, keyword, date, integer 등)을 가진다.
- **매핑(Mapping)**: 인덱스 내 필드의 이름·타입·분석 방식을 정의하는 스키마(RDB의 스키마(Schema)에 해당).
- **샤드(Shard)**: 인덱스를 물리적으로 쪼갠 조각. 각 샤드는 그 자체로 완전한 Lucene 인덱스다. **Primary Shard**(원본)와 **Replica Shard**(복제본)로 나뉜다.
- **노드(Node)**: Elasticsearch 프로세스 하나(보통 서버 1대당 1개). 샤드를 실제로 보관·처리한다.
- **클러스터(Cluster)**: 같은 이름으로 묶인 노드들의 집합. 데이터·검색 부하를 노드 전체에 분산한다.
- **역색인(inverted index)**: "단어 → 그 단어가 등장하는 문서 목록" 구조. 전문 검색이 빠른 근본 이유다.
- **분석기(Analyzer)**: 텍스트를 검색 가능한 토큰(token)으로 쪼개는 파이프라인. **Character filter → Tokenizer → Token filter** 순서로 동작한다.

#### 역색인(inverted index) 원리
RDB의 일반 인덱스가 **"행 → 그 행이 가진 값"** 을 정리한 **정방향 색인**이라면, 역색인은 그 방향을 **뒤집어** **"단어(term) → 그 단어가 든 문서 목록"** 을 정리한다.

예를 들어 다음 두 문서가 있다고 하자.

```
문서 1: "엘라스틱서치는 빠른 검색 엔진"
문서 2: "검색 엔진은 빠른 응답"
```

이를 분석해 만든 역색인은 다음과 같다.

| 단어(Term) | 등장 문서 목록(Posting List) |
| --- | --- |
| 엘라스틱서치 | [1] |
| 빠른 | [1, 2] |
| 검색 | [1, 2] |
| 엔진 | [1, 2] |
| 응답 | [2] |

- "검색"을 질의하면 전체 문서를 훑지 않고 **역색인 표에서 "검색" 행만 보고 즉시 [1, 2]** 를 얻는다. 데이터가 아무리 많아도 **단어 사전에서 한 번 찾는 비용**으로 끝난다.
- 정렬·관련도 계산을 위해 각 단어가 **문서 내 몇 번 등장했는지(term frequency)**, **전체 문서 중 몇 개에 등장하는지(document frequency)** 등도 함께 저장한다. 이 통계로 **관련도 점수(BM25 등)** 를 매겨 "더 잘 맞는 문서"를 위로 올린다.

```
[RDB 정방향 색인]                  [Elasticsearch 역색인]
  행 → 값                            단어 → 문서 목록
  Row1 → "검색 엔진"                  "검색" → [Row1, Row2 ...]
  Row2 → "빠른 응답"                  "엔진" → [Row1 ...]
  (특정 행이 가진 값을 안다)          (특정 단어가 어디 있는지 즉시 안다)
```

#### 분석기(Analyzer)와 형태소 분석
역색인을 만들려면 먼저 **원문 텍스트를 단어(토큰) 단위로 쪼개야** 한다. 이 작업을 **분석(analysis)** 이라 하고, 그 파이프라인이 **분석기(Analyzer)** 다. 분석기는 세 단계로 동작한다.

```
원문: "Elasticsearch는 빠른 검색!"
   │
   ▼ ① Character filter   (전처리: HTML 태그 제거, 특수문자 치환 등)
"Elasticsearch는 빠른 검색"
   │
   ▼ ② Tokenizer          (토큰 분리: 공백/형태소 기준으로 단어 쪼개기)
["Elasticsearch는", "빠른", "검색"]
   │
   ▼ ③ Token filter       (후처리: 소문자화, 불용어 제거, 어간/동의어 처리)
["elasticsearch", "빠르다", "검색"]
   │
   ▼  → 이 토큰들이 역색인에 저장된다
```

- **색인 시점과 검색 시점에 같은 분석기를 적용**하는 것이 핵심이다. 그래야 "검색"으로 색인된 토큰을 "검색"이라는 질의 토큰으로 매칭할 수 있다.
- **한글은 어절(공백) 단위로 자르면 검색이 잘 안 된다.** "검색을", "검색은", "검색이"가 모두 다른 토큰이 되어 "검색"으로는 안 잡힌다. 그래서 한국어는 **nori** 같은 **형태소 분석기(morphological analyzer)** 를 써서 조사·어미를 떼고 어간("검색")만 추출해 색인한다.
- 영어는 보통 **stemming**(어간 추출: running → run)과 **불용어(stopword)** 제거(the, a, is)를 token filter로 처리한다.

#### 샤드와 레플리카
하나의 인덱스가 단일 서버 용량을 넘어서거나 검색 부하가 커지면, 인덱스를 **샤드로 쪼개 여러 노드에 분산**한다. 이것이 **수평 확장**의 토대다.

- **Primary Shard(주 샤드)**: 데이터의 원본. 인덱스 생성 시 개수를 정하며, 색인된 문서는 라우팅 규칙(보통 `_id` 해시)에 따라 특정 Primary 샤드에 저장된다.
- **Replica Shard(복제 샤드)**: Primary의 복사본. 두 가지 역할을 한다.
  - **가용성(고가용성)**: Primary가 있는 노드가 죽어도 다른 노드의 Replica가 Primary로 승격되어 데이터 손실을 막는다.
  - **읽기 처리량 향상**: 검색 요청은 Primary와 Replica **어느 쪽에서나** 처리할 수 있어, Replica가 많을수록 동시 검색을 더 많이 소화한다.
- **배치 규칙**: 같은 샤드의 Primary와 Replica는 **절대 같은 노드에 두지 않는다.** 같은 노드에 두면 그 노드가 죽을 때 원본과 복제본이 동시에 사라져 복제의 의미가 없어지기 때문이다.

> 주의: **Primary 샤드 개수는 인덱스 생성 후 변경하기 어렵다**(재색인 필요). 반면 Replica 개수는 운영 중에도 자유롭게 늘리고 줄일 수 있다.

### RDB 용어 대응
Elasticsearch를 처음 접할 때는 RDB 개념에 대응시켜 이해하면 빠르다.

| Elasticsearch | RDB | 설명 |
| --- | --- | --- |
| Index(인덱스) | Table(테이블) | 같은 성격의 문서/행을 모은 묶음 |
| Document(문서) | Row(행) | 데이터의 최소 단위(ES는 JSON, RDB는 레코드) |
| Field(필드) | Column(컬럼) | 문서/행을 구성하는 항목 |
| Mapping(매핑) | Schema(스키마) | 필드 이름·타입·제약 정의 |
| Query DSL | SQL | 데이터 조회·검색 질의 언어 |

- 주의: 위 대응은 **이해를 돕는 비유일 뿐 완전히 같지 않다.** Elasticsearch에는 **트랜잭션·외래키·JOIN이 사실상 없고**, 데이터는 비정규화(denormalization)해서 한 문서에 통째로 담는 것을 선호한다. RDB의 정규화된 사고를 그대로 가져오면 안 된다.

### ELK / Elastic Stack
Elasticsearch는 단독으로도 쓰지만, 보통 **로그·메트릭을 수집·저장·시각화하는 파이프라인**의 한 축으로 함께 묶인다. 이 묶음을 과거에는 **ELK 스택**, 지금은 **Elastic Stack** 이라 부른다.

```
   [수집]            [가공/전송]          [저장·검색]          [시각화]
  ┌────────┐       ┌──────────┐       ┌──────────────┐    ┌──────────┐
  │ Beats   │──────▶│ Logstash │──────▶│ Elasticsearch │───▶│ Kibana   │
  │(경량 수집)│       │(파싱·변환) │       │(색인·검색·집계) │    │(대시보드) │
  └────────┘       └──────────┘       └──────────────┘    └──────────┘
  서버 로그/메트릭     필드 추출·정제        역색인 저장          그래프·조회
```

- **Elasticsearch**: 데이터를 역색인으로 저장하고 검색·집계를 담당하는 중심 엔진.
- **Logstash**: 다양한 소스의 데이터를 수집해 **파싱·필터·변환** 후 Elasticsearch로 보내는 파이프라인. 무겁지만 강력하다.
- **Kibana**: Elasticsearch 데이터를 **그래프·대시보드로 시각화**하고 질의·탐색하는 웹 UI.
- **Beats**: 서버에 설치하는 **경량 수집기**(Filebeat=로그, Metricbeat=메트릭 등). Logstash를 거치거나 Elasticsearch로 바로 전송한다.

### 장점 / 단점(트레이드오프)
Elasticsearch는 만능 데이터베이스가 아니라 **검색·분석에 특화된 도구**이며, 명확한 트레이드오프를 가진다.

- 장점
  - **빠른 전문 검색**: 역색인 덕에 대용량에서도 자연어 검색이 밀리초 단위로 끝난다.
  - **관련도 점수(relevance)**: BM25 등으로 "얼마나 잘 맞는지"를 계산해 의미 있는 순서로 정렬한다.
  - **강력한 집계 분석**: 로그·메트릭을 그룹핑·통계·시계열로 실시간 분석한다.
  - **수평 확장**: 샤드 분산으로 노드를 추가하면 용량과 처리량이 선형에 가깝게 늘어난다.
  - **고가용성**: Replica로 노드 장애에도 검색·데이터가 살아남는다.
  - **스키마 유연성**: JSON 문서 기반이라 필드 추가가 RDB의 `ALTER TABLE` 보다 가볍다.
- 단점
  - **준실시간(near real-time)**: 색인한 문서는 즉시가 아니라 **refresh interval**(기본 1초) 후에 검색된다. **즉시 일관성(read-your-writes)이 보장되지 않는다.**
  - **트랜잭션 없음**: ACID 트랜잭션·다중 문서 롤백이 없다. "주문+결제 원자성" 같은 요구에 부적합하다.
  - **주 저장소로 부적합**: 원본 데이터의 신뢰 보관소(System of Record)로 쓰기엔 위험하다. **검색 보조용**으로 두는 것이 정석이다.
  - **매핑 변경 어려움**: 한번 정해진 필드 타입은 바꾸기 어렵고, 대개 **재색인(reindex)** 이 필요하다.
  - **리소스 비용**: 역색인·캐시를 메모리에 올려 쓰므로 **RAM(특히 힙·페이지 캐시) 요구가 크다.** 운영·튜닝 난이도도 높다.

### 실무 예제
Elasticsearch는 **REST API + Query DSL**(JSON 질의 언어)로 다루는 것이 기본이며, 자바/스프링 진영에서는 **Spring Data Elasticsearch** 로 추상화해 쓴다.

Ex: REST Query DSL — "노트북" 키워드를 제목·설명에서 검색하되(`match`), 카테고리가 `electronics`이고 가격이 100만 원 이하인 것만 거르는(`bool` + `must` + `filter`) 질의.

```json
POST /products/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "match": {
            "name": "노트북"
          }
        }
      ],
      "filter": [
        { "term":  { "category": "electronics" } },
        { "range": { "price": { "lte": 1000000 } } }
      ]
    }
  }
}
```

- `must` 절은 **관련도 점수에 영향**을 준다(검색어가 잘 맞을수록 위로). `filter` 절은 **점수 계산 없이 통과/탈락만** 판정하므로 빠르고 캐시되기 좋다. "검색어는 must, 조건 필터는 filter"가 기본 원칙이다.

Ex: Spring Data Elasticsearch — `@Document` 엔티티와 Repository 정의.

```java
@Document(indexName = "products")   // 이 클래스가 매핑될 인덱스 이름
public class Product {

    @Id
    private String id;

    // 전문 검색 대상 필드: 분석기로 토큰화해 역색인에 저장
    @Field(type = FieldType.Text, analyzer = "nori")
    private String name;

    // 정확 일치·집계용 필드: 분석하지 않고 통째로 저장
    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Long)
    private Long price;

    // getters / setters
}
```

```java
public interface ProductRepository
        extends ElasticsearchRepository<Product, String> {

    // 메서드 이름만으로 name 필드 전문 검색 질의가 자동 생성된다
    List<Product> findByName(String name);

    // category 정확 일치 + price 범위 조회
    List<Product> findByCategoryAndPriceLessThanEqual(String category, Long price);
}
```

- **`Text` vs `Keyword` 구분이 핵심**이다. `Text`는 분석기를 거쳐 전문 검색에 쓰고, `Keyword`는 분석하지 않아 **정확 일치·정렬·집계**에 쓴다. 한 필드를 두 용도로 다 쓰려면 멀티필드(`name` + `name.keyword`)로 매핑한다.

### 주의점 / 안티패턴
Elasticsearch는 RDB와 사고방식이 다르므로, RDB 습관을 그대로 들고 오면 곳곳에서 문제가 터진다.

- **강한 일관성 기대 금지**: Elasticsearch는 **준실시간(near real-time)** 이다. 방금 색인한 문서가 즉시 검색에 잡히지 않을 수 있다(기본 **refresh interval 1초**). "저장 직후 바로 조회되어야 하는" 흐름을 ES에 의존하면 안 된다.
- **트랜잭션·조인 의존 금지**: 다중 문서 ACID 트랜잭션과 RDB식 JOIN이 없다. 관계는 **비정규화(한 문서에 통째로 담기)** 또는 제한적인 `nested`/`join` 타입으로 풀되, 후자는 성능 비용이 크므로 남용하지 않는다.
- **주 데이터 저장소(System of Record)로 사용 금지**: **원본의 신뢰 보관소는 RDB가 맡고, Elasticsearch는 검색·분석 보조**로 둔다. RDB→ES 동기화는 **CDC(Change Data Capture)** 나 **이벤트 기반 파이프라인**으로 단방향 복제하는 것이 정석이다. ES만 믿고 원본을 안 두면 인덱스 손상·재색인 시 데이터를 잃는다.
- **dynamic mapping에 의존한 매핑 폭발 금지**: 매핑을 설계하지 않고 들어오는 JSON대로 필드를 자동 생성(dynamic mapping)하게 두면, 예상치 못한 필드가 무한히 늘어나(**mapping explosion**) 클러스터가 불안정해진다. **매핑은 사전에 명시 설계**하고 필요 시 `dynamic: strict` 로 막는다.
- **샤드 과다 설계 금지**: "나중을 위해" 샤드를 과도하게 잡으면, 샤드마다 메모리·파일 핸들·오버헤드가 붙어 **작은 샤드가 수천 개 생기는 오버샤딩**으로 성능이 무너진다. 샤드 크기는 보통 **수십 GB 수준**을 목표로 적정 개수를 잡는다.
- **무거운 와일드카드·딥 페이지네이션 금지**: 앞에 와일드카드가 붙는 `*keyword` 질의는 역색인의 이점을 버리고 사전 전체를 훑어 느리다. 또한 `from + size` 로 **수만 건 뒤 페이지**를 넘기는 딥 페이지네이션은 비용이 폭증하므로, 대신 **`search_after`** 나 스크롤/PIT를 쓴다.

> document: https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html
> document: https://www.elastic.co/what-is/elasticsearch
> document: https://www.elastic.co/guide/en/elasticsearch/reference/current/analysis.html

### 관련 문서
- [MSA](../cs/architecture/msa.md)
- [Database란?](../database/database.md)
- [분산 데이터 일관성](../cs/architecture/data-consistency.md)
