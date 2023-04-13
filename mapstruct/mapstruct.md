## MapStruct
Object Mapping 라이브러리 (DTO -> Entity -> Response : Cycle)
> Controller <-> Service 역할과 책임 분리를 위해 사용

- Home: [https://mapstruct.org/](https://mapstruct.org/)
- Examples: [https://github.com/mapstruct/mapstruct-examples](https://github.com/mapstruct/mapstruct-examples)
- interface 기반 맵핑 정의
- build시 generated 하위 폴더에 interface 구현체가 생성된다.

### Annotations
@Mapper
 - componentModel = "spring"
   - Spring의 Bean으로 등록 (필수)
 - injectionStrategy
   - InjectionStrategy.(*) 주입 전략 설정
 - unmappedTargetPolicy
   - ReportingPolicy.(*) 매핑되지 않은 정보가 있을시 대응 방법을 규정

@Mappings
 - 여러개의 조건 매핑이 필요할 시 ({ @Mapping(..), ..) 전달하여 묶음으로 적용할 수 있다. (개별 매핑 또한 가능하다,)

@Mapping
 - target
   - 매핑값을 적용할 필드명
 - source
   - 매핑값을 제공할 필드명
 - ignore
   - 필드의 매핑값을 무시
