## 파일 입출력
자바에서 데이터의 흐름을 Stream으로 표현하며 단방향 읽기/쓰기 클래스가 각각 존재한다.

- 입출력 사용구문
  - File: FileInputStream / FileOutputStream ...
  - Process: PipedInputStream / PipedOutputStream ...
    - Buffered[...] : 입출력 성능 향상 보조 스트림이 존재한다.

- byte 단위 입출력
  - InputStream/OutputStream -> ...

- 문자 단위 입출력
  - Reader/Writer -> ...

- 한글 입출력
  - BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
  - BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));

### 현대적 파일 I/O (java.nio.file + try-with-resources)
Java 7+에서는 `java.io`의 장황한 스트림 대신 **NIO.2의 `Path`/`Files`** 로 간결하게 처리하고, 자원은 **try-with-resources**로 자동 반납한다.

```java
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

Path path = Path.of("data.txt");

// 한 번에 읽고 쓰기 (작은 파일에 적합)
List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
Files.writeString(path, "내용", StandardCharsets.UTF_8);   // Java 11+

// 큰 파일은 버퍼 스트림으로, try-with-resources 가 close()를 자동 호출
try (BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
    String line;
    while ((line = br.readLine()) != null) {
        // 처리
    }
} // br.close() 자동 호출 (예외가 나도 닫힘)
```

- **try-with-resources**: `try(...)` 괄호 안에서 연 자원(`AutoCloseable`)은 블록을 벗어날 때 자동으로 `close()`된다. `finally`에서 직접 닫던 누수 위험을 없앤다.
- 인코딩은 문자열 `"utf-8"` 대신 **`StandardCharsets.UTF_8`** 상수를 쓰는 것이 안전하다(오타·미지원 예외 방지).
