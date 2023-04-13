## Documents
- SpringBoot: [https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- MovkMvc: [https://docs.spring.io/spring-framework/docs/6.0.3/reference/html/testing.html#spring-mvc-test-framework](https://docs.spring.io/spring-framework/docs/6.0.3/reference/html/testing.html#spring-mvc-test-framework)
- AssertJ: [https://assertj.github.io/doc/](https://assertj.github.io/doc/)
- Hamcrest: [https://github.com/hamcrest/JavaHamcrest](https://github.com/hamcrest/JavaHamcrest)
- Mockito: [https://site.mockito.org/](https://site.mockito.org/)
- Mockito Wiki: [https://github.com/mockito/mockito/wiki](https://github.com/mockito/mockito/wiki)
- JsonPath: [https://github.com/json-path/JsonPath](https://github.com/json-path/JsonPath)
- Gson: [https://github.com/google/gson](https://github.com/google/gson)

### Boot
@SpringBootTest
  - 전체 컨텍스트 실행 후 테스트 진행 (통합 테스트 시 사용)

### MvcMock
  - Spring MVC 테스트 클래스 (내부 설명 구문 확인 가능)
    - perform(), get() ... (내부 설명 구문 확인 가능)
    - andExpect, andDo ... (내부 설명 구문 확인 가능)

#### Annotations
@WebMvcTest(Controller.class)
개별 컨트롤러 슬라이스 테스트시 사용

@MockBean(JpaMetamodelMappingContext.class)
개별 컨트롤러 슬라이스 테스트시 JPA 모듈 컨텍스트 삽입 (필요시)

@AutoConfigureMockMvc
MVC 테스트 설정 삽입 (내부 확인)

@RunWith(MockitoJUnitRunner.class)
JUnit4

@ExtendWith(MockitoExtension.class)
JUnit5

@Mock(Bean) vs @InjectMocks
- @Mock
  - Mock 클래스 생성 (모의 객체)
- @InjectMocks
  - 클래스 인스턴스 생성(실객체) 이후 생성된 클래스 인스턴스 내부 필드의 @Mock 객체를 주입받는다.
- @Spy
  - 클래스 인스턴스 생성(실객체) stub 데이터가 필요한 부분에서 활용할 수 있다.
  - field inject
    - 실객체 생성 이후 내부 필드 정보를 FieldUtils 활용하여 임시로 채울 수 있다.
      ```
      // Test Base64 SecretKey inject
      FieldUtils.writeField(SpyInstance, "secretKey", "d297f22853e39936052a15a41266866bf058923f", true);
      ```
