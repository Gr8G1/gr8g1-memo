## Spring Rest Docs

### Spring Rest Docs vs Swagger
#### RestDocs
- 특징
  - 테스트 코드 기반의 API 문서화 방식
    > 슬라이스 테스트 케이스(컨트롤러 테스트) 를 기반으로 API 문서화 정보 생성
  - 작성된 테스트 케이스가 'passed' 일시 API 문서가 생성된다 하여 문서를 신뢰할 수 있다..
  - 테스트 코드에서 명세를 작성하기 때문에 기타 로직에 영향을 미치지 않는다.
  - API툴 기능은 제공하지 않는다.

> Document:
>  - 2.0.5: Rest Docs styles: [docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE](https://docs.spring.io/spring-restdocs/docs/2.0.5.RELEASE/reference/html5/#introduction)
>  - 3.0.0: Spring Docs styles: [docs.spring.io/spring-restdocs/docs/3.0.0](https://docs.spring.io/spring-restdocs/docs/3.0.0/reference/htmlsingle/#introduction)

#### Swagger
- 특징
  - 애터네이션 기반의 API 문서화 방식
    - 문서화될 컨트롤러 / DTO에 직접 API 관련 애너테이션 선언 및 정의
  - 가독성 및 유지 보수성 떨어진다.
    - API 문서화 관련 애너테이션이 로직의 복잡도를 증가시킨다.
  - API 툴 기능을 제공한다.

> Document: [Swagger-2.X](https://docs.spring.io/spring-restdocs/docs/3.0.0/reference/htmlsingle/#introduction)

### Spring Rest Docs 의존성 추가
#### build.gradle
```groovy
plugins {
  ...
  id "org.asciidoctor.jvm.convert" version "3.3.2"
}

ext {
  set('snippetsDir', file("build/generated-snippets"))
}

configurations {
  asciidoctorExtensions
}

dependencies {
  testImplementation 'org.springframework.restdocs:spring-restdocs-mockmvc'
  asciidoctorExtensions 'org.springframework.restdocs:spring-restdocs-asciidoctor'
}

tasks.named('test') {
  outputs.dir snippetsDir
  useJUnitPlatform()
}

tasks.named('asciidoctor') {
  configurations "asciidoctorExtensions"
  inputs.dir snippetsDir
  dependsOn test
}

task copyDocument(type: Copy) {
  dependsOn asciidoctor
  from file("${asciidoctor.outputDir}")
  into file("src/main/resources/static/docs")
}

build {
  dependsOn copyDocument
}

bootJar {
  dependsOn copyDocument
  from ("${asciidoctor.outputDir}") {
    into 'static/docs'
  }
}
```

### Resource
**java/src/test 하위 초기 폴더 구성 및 기본 설정 예시 참조**

- src/docs/asciidoc/docinfo.html
  - 공통으로 사용될 html 파일 생성 (CSS, Script 적용 가능: Template)
    - index.adoc - :docinfo: shared <- 추가 (docinfo 적용 범위 지정도 가능하다: 도큐먼트 참조)
- src/docs/asciidoc/index.adoc
  - html 변환될 resource 생성 및 adoc 작성
    - Syntax reference: [https://docs.asciidoctor.org/asciidoc/latest/syntax-quick-reference](https://ant.apache.org/manual/dirtasks.html#patterns)

### *.adoc 스니핏 파일 추출 명령어
```bash
# .bashrc | .zshrc 
snippet-files() {
    echo -n "[snippets='"
    ls "$1"/*.adoc | sed 's|.*/||;s/\.adoc$//' | sort -df | xargs | sed 's/ /,/g;s/$/'\'']/'
}
```

#### 작성 예시 (Markdown과 유사)
```asciidoc
= REST Docs
:doctype: book
:docinfo: shared-head
:icons: font
:source-highlighter: highlightjs     // 문서에 표기되는 코드들의 하이라이팅을 highlightjs를 사용
:toc: left                           // (Table Of Contents) 위치 지정
:toclevels: 2                        // (Table Of Contents) 레벨 지정

:sectlinks:                          // 구역: #(해시이동)
:sectnums:                           // 구역: 넘버링

include::overview.adoc[]

[[API-List]]
== APIs
link:file[Title, window=blank]   - 문서 링크 연결 및 새창 띄우기

~ Or

[(..)Controller]]   - 링크 생성
=== Uris
:operation-{snippet}-title: 제목                                   // 특정 스니핏 제목 변경 필요시 적용
operation::{generated-snippets}[]                                  // 경로에 포함된 모든 스니핏 출력
operation::{generated-snippets}[snippets='curl-request, ...']      // 경로에 포함된 스니핏 개별 지정

include::{snippets}/index/curl-request.adoc[]
```

### document(.., Obejcts... args)
API 문서를 생성하기 위한 메소드
- (..)
  - API 문서 snippet 식별자 (.adoc: 파일명)
- getRequestPreProcessor(): Utils. 참조
  - MvcMock.perform() -> Reqeust 식별 -> http-request.adoc 생성
- getResponsePreProcessor() Utils. 참조
  - MvcMock.perform() -> Response 식별 -> http-response.adoc 생성
- requestHeaders(..)
  - 요청 헤더 정의
- responseHeaders(..)
  - 응답 헤더 정의
- pathParameters(..)
  - parameterWithName() .. 사용 내부 정의
- requestFields(..),
- responseFields(..)
  - new FieldDescriptor[] {..} | List.of(..) .. 사용 내부 정의
- headerWithName(HttpHeaders.)
  - 응답 헤더에 포함되는 필드 존재시 필드명 정의
- parameterWithName()
  - 파라미터명 정의
- fieldWithPath()
  - 필드명 정의
- type(JsonFieldType.)
  - 필드 타입 정의
- description()
  - 설명 구문 정의
- beneathPath()
  - Ex: beneathPath("body.data[]")
    - adoc 생성: -beneath-body.data[].adoc
- beneathPath("body.data[]").withSubsectionId("data")
  - adoc 생성: -data.adoc
- attribute(key(..).value(..))
  - Default snippet 커스텀
    - original resource: org.springframework.restdocs.templates.asciidoctor
    - override resource: src/test/resources/org/springframework/restdocs/templates/asciidoctor/{snippetName}.snippet

- Custom snippet 
  - 경로 설정 클래스: StandardTemplateResourceResolver.class
    - custom resource: src/test/resources/org/springframework/restdocs/templates/{snippetName}.snippet

#### **AbstractFieldsSnippet 추상 클래스 구현** 예시
```java
public class CustomRequestFieldsSnippet extends AbstractFieldsSnippet {
 public CustomRequestFieldsSnippet(
   String type,
   List<FieldDescriptor> descriptors,
   Map<String, Object> attributes,
   boolean ignoreUndocumentedFields
 ) {
   super(type, descriptors, attributes, ignoreUndocumentedFields);
 }

   @Override
   protected MediaType getContentType(Operation operation) {
     return operation.getRequest().getHeaders().getContentType();
   }

   @Override
   protected byte[] getContent(Operation operation) throws IOException {
     return operation.getRequest().getContent();
   }

   public static CustomRequestFieldsSnippet customRequestFields(
     String type,
     Map<String, Object> attributes,
     FieldDescriptor... descriptors
   ) {
     return new CustomRequestFieldsSnippet(type, Arrays.asList(descriptors), attributes, true);
   }

   public static CustomRequestFieldsSnippet customRequestFields(
     String type,
     FieldDescriptor... descriptors
   ) {
     HashMap<String, Object> attributes = new HashMap<>();
     return new CustomRequestFieldsSnippet(type, Arrays.asList(descriptors), attributes, true);
   }
}
```

#### **custom-request-fields.snippet** 예시
[cols="2,2,2,3,3"]
.{{title}}
|===
|Path|Type|Optional|Description|Constraints
{{#fields}}
|{{#tableCellContent}}`+{{path}}+`{{/tableCellContent}}
|{{#tableCellContent}}`+{{type}}+`{{/tableCellContent}}
|{{#tableCellContent}}_{{optional}}_{{/tableCellContent}}
|{{#tableCellContent}}{{description}}{{/tableCellContent}}
|{{#tableCellContent}}{{#constraints}}{{.}}{{/constraints}}{{/tableCellContent}}
{{/fields}}
|===
