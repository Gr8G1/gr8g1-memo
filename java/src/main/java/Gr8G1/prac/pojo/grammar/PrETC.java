package Gr8G1.prac.pojo.grammar;

import java.util.Date;

public class PrETC {
  /*
   * # Java Spec
   *  ~ https://docs.oracle.com/javase/specs/jls/se13/html/index.html
   */

  /*
   * # Env on Mac
   *  ~ https://phoenixnap.com/kb/set-environment-variable-mac
   *
   */

  /*
   * # Base64 Gen With OpenSSL
   *  - $ openssl rand -out openssl-secret.txt -hex 20
   *
   */

  /*
   * # 이스케이프 시퀀스
   *
   * \a: 경고음
   * \b: 백스페이스
   * \t: 수평 탭
   * \r: 복귀 (Carriage return)
   * \n: 개행 (Line Feed)
   * \v: 수직 탭 (프린트 전용)
   * \f: 페이지 나누기(폼 피드) (프린트 전용)
   * \\: \
   * \ooo: 8진수 표기 (ASCII)
   * \xhh: 16진수 표기 (ASCII)
   * \xhhh: 16진수 표기(유니코드)
   *
   */

  /*
   * # 포맷 (개략)
   *
   * %n: 개행
   * %b: 불리언
   * %c: 문자
   * %s: 문자열
   * %d: 정수 (10진법 표기)
   * %f: 실수
   * %e: 지수
   * %u: 부호 없는 정수
   * %o: 8진수 (octal)
   * %i: 10진수 (decimal)
   * %x: 16진수 (hexadecimal)
   * %t: 날짜/시간
   * %%: % (프린트)
   * \%: % (프린트)
   *
   * ! [%공간.표기개수]:개별 설정 가능
   * ! '<' or '{n}$': 재사용 플래그
   *
   * ~ 참조: http://www.java2s.com/Tutorials/Java/Java_Format/index.htm
   *
   */

  /*
   * # 파일 권한 설정 (Unix 기반)
   * ~ 참조: https://kb.iu.edu/d/abdb
   */

  /*
   * # .(Dot notation)
   *  - Java 클래스에 대한 모든 Java 생성자, 메소드 및 필드에 대한 전체 액세스 권한을 제공
   *
   */

  /*
   * # Objects... args (Arbitrary Number of Arguments)
   *  - 일정한 타입의 인자를 전달 받을때 사용할 수 있다. (javascript destructure 유사)
   *
   */

  public static void main(String[] args) {
    System.out.printf("[%15s] %d%n", "ABC", 123456);
    System.out.printf("[%-15s]%n", "ABC");

    System.out.printf("[%d]%n", 123456);

    System.out.printf("[%15.2f]%n", 1.23456);
    System.out.printf("[%15.2f]%n", 12.3456);

    Date date = new Date();

    System.out.printf("%tF-%<tT%n", date);
    System.out.printf("%tF-%1$tT%n", date);
  }
}
