## 파일 입출력
자바에서 데이터의 흐름을 Stream으로 표현하며 단반향 읽기/쓰기 클래스가 각각 존재한다.

- 입출력 사용구문
  - File: FileInputStream / FileOutputStream ...
  - Process: PipedInputStream / PipedOutputStream ...
    - Buffered[...] : 입출력 성능 향상 보조 스트림이 존재한다.

- byte 단위 입출력
  - InputStream/OutStream -> ...

- 문자 단위 입출력
  - Reader/Writer -> ...

- 한글 입출력
  - BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
  - BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));
