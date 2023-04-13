package Gr8G1.prac.pojo.grammar;

public class PrException {
  /*
   * # 예외처리
   *
   * try/catch/finnaly 구문
   * > try { // 구문 실행중 발생한 예외는 catch 블록으로 전달된다.
   *    ...
   * } catch (예외1 | 예외2) { // 예외1 또는 예외2일 경우 실행된다.
   *    ...
   * } catch (예외3) { // 예외3의 경우 실행된다.
   *    ...
   * } finally { // 예외 발생 유무와 관계없이 실행된다.
   *    ...
   * }
   *
   * Exception & RuntimeExeption
   *
   * Exception
   *  - 컴파일 단계에서의 에러 (Checked)
   *    - 예외처리를 강제 (필수사항)
   *  - 종류
   *    - IOExeption, ClassNotFound ...
   *
   * RuntimeExeption
   *  - 런타임 단계에서의 에러 (Unchecked)
   *    - 예외처리를 개발자 스스로에게 위임 (선택사항)
   *  - 종류
   *    - ArtimesticException, NullPointerException, ClassCastException ...
   *
   * 예외 던지기
   *  - throw new Exception(); // 예외처리 try/catch 구문 블록 *필수*
   *  - throw new RuntimeException(); // 예외처리 try/catch 구문 블록 선택
   *
   * 메소드 예외 선언
   *  - public void excepMothod() throws [CheckedException] {
   *    // 예외처리 필수인 Checked의 경우 예외 선언 (Unchecked 예외 또한 선언 가능하지만 적지 않는것이 정석)
   *    ...
   *  }
   *
   * 사용자 정의 예외 생성
   *  - 조상은 Exception | RuntimeException | ..선택
   *  > class MyException extends [Eqxception | RuntimeException | ..] {
   *      MyException(String msg) {
   *        super(msg)
   *      }
   *
   *      ...
   *
   *    }
   *
   * 예외 되던지기
   *  - 특정 상황(ex: API return 값의 Validation 위임 상황)에서 throw 예외를 되던져 처리를 위임하는것
   *
   * 예외 확인
   *  - 예외.printStackTrace(): 예외발생 호출 스택 출력
   *  - 예외.getMessage(): 예외 구문 출력
   *
   */
}
