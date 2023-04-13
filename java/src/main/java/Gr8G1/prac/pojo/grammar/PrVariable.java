package Gr8G1.prac.pojo.grammar;

// ### 필드 타입별 초기화 정보
// |             타입              |      초기값     |
// |:-----------------------------:|:---------------:|
// |            byte               |        0        |
// |            char               | \u0000(빈 공백) |
// |           short               |        0        |
// |            int                |        0        |
// |            long               |        0        |
// |           float               |       0.0F      |
// |           double              |       0.0       |
// |          boolean              |      false      |
// | 참조형(Array/Class/interface) |       null      |

public class PrVariable {
  // ~ 정수
  /*
   * # byte
   * 메모리: 1byte
   * 표현 범위: -128(-2^7) ~ 127(2^7-1)
   *
   */
  static byte b = (byte) (Math.pow(2, 7) - 1);

  /*
   * # short
   * 메모리: 2byte
   * 표현 범위: 32,768(-2^15) ~ 32,767(2^15-1)
   *
   */
  static short s = (short) (Math.pow(2, 15) - 1);

  /*
   * # int
   * 메모리: 4byte
   * 표현 범위: -2,147,483,648(-2^31) ~ 2,147,483,647(2^31-1)
   *
   */
  static int i = (int) (Math.pow(2, 31) - 1);

  /*
   * # int
   * 메모리: 8byte
   * 표현 범위: -9,223,372,036,854,775,808(-2^63) ~ 9,223,372,036,854,775,807(2^63-1)
   *
   */
  static long l = (long) (Math.pow(2, 63) - 1);

  // ~ 실수
  /*
   * # float
   *
   * 메모리: 4byte
   * 표현 범위:
   *  - 음수: -3.4 * 10^38 ~ -1.4 * 10^-45
   *  - 양수: 1.4 * 10^-45 ~ 3.4 * 10^38
   * 정밀도: 7자리
   *
   */
  static float f = 1.23F; // (float) 1.23 == 1.23(f|F) : f|F 필수

  /*
   * # double
   *
   * 메모리: 8byte
   * 표현 범위:
   *  - 음수: -1.8 * 10^308 ~ -4.9 * 10^-324
   *  - 양수: 4.9 * 10^-324 ~ 1.8 * 10^308
   * 정밀도: 15자리
   *
   * * 실수의 기본 데이터 타입은 double 이다.
   *
   */
  static double d = 1.23D; // (double) 1.23 == 1.23(d|D) : d|D 생략가능

  // ~ 불리언
  /*
   * # boolean
   * 메모리: 1byte
   * 저장형식: 유니코드
   * 특징:
   *  true/false 사용에 1bit만 필요하나 JVM 에서 다루는 데이터의 최소 단위(컴퓨터의 정보 활용 최소 단위)가
   *  1byte 이기 때문에 1byte(8bit) 크기를 갖는다.
   */
  static boolean bt = true;
  static boolean bf = false;

  // ~ 문자
  /*
   * # char
   * 메모리: 2byte
   *
   */
  static char c = 'A';
  static char u = 65; // 65는 ASCII 코드 테이블에서 대문자 A의 코드 값
  // char c = 'ab'; // Error: 단 하나의 문자만 허용한다.
  // char c = "a";  // Error: 큰따옴표 사용 불가

  public static void main(String[] args) {
    String regEx = "\\B(?=(\\d{3})+(?!\\d))";

    System.out.println(Integer.toString(b).replaceAll(regEx, ","));
    System.out.println(Integer.toString(s).replaceAll(regEx, ","));
    System.out.println(Integer.toString(i).replaceAll(regEx, ","));
    System.out.println(Long.toString(l).replaceAll(regEx, ",") + "\n");

    // ! 정수 데이터 표현 범위를 넘어설 경우(Under/Over:flow) 최소/최대값으로 치환된다.
    System.out.println((byte) (127 + 1) + "\n"); // Over: -128
    System.out.println((byte) (-128 - 1)); // Under: 127

    // ! 실수 데이터 표현 범위를 넘어설 경우(Under/Over:flow) 0.0/Infinity 으로 치환된다.
    System.out.println((float) (-3.4 * Math.pow(10, 38 + 1)));  // Over: -Infinity
    System.out.println((float) (3.4 * Math.pow(10, 38 + 1)));   // Over: Infinity
    System.out.println((float) (-1.4 * Math.pow(10, -45 - 1))); // Under: -0.0
    System.out.println((float) (1.4 * Math.pow(10, -45 - 1)) + "\n");  // Under: 0.0

    // ! 문자 코드 포인트 값 비교
    System.out.println(Character.toString(c).codePointAt(0));
    System.out.println(u);
  }
}
