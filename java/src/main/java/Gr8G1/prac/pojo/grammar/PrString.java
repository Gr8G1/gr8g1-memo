package Gr8G1.prac.pojo.grammar;

import java.util.StringTokenizer;

public class PrString {
  /*
   * # 문자열(String)
   * String 객체는 문자열의 갯수보다 더 큰 용량을 차지한다.
   *
   * 메모리: 문자열 갯수 용량 + 20byte 사용
   * 메모리 구성:
   *  - 객체 헤더 4byte
   *  - 문자열에 대한 레퍼런스 4byte
   *  - 문자열에 offset 4byte
   *  - 문자열의 길이 필드 4byte
   *  - 해쉬값 필드 4byte
   *
   * ! String 객체는 char[]로 문자열을 갖는다.
   * 메모리 계산: 10개의 문자를 가진 String 객체 = 20(기본 구성 필드값) + 4(char[] 객체) + 10 x 2byte = 44byte
   * > char[]도 객체이므로, 객체 헤더 용량 4byte를 갖는다.
   *
   * ! String str = 'abcd'; // Error: 작은따옴표 사용 불가
   */

  public static void buildString(int[] arr) {
    StringBuilder v = new StringBuilder();
    for (int j : arr) v.append(j);

    System.out.println(v);

    // Integer.parseInt(v.toString()));
  }

  static String str = "String";

  public static void main(String[] args) {
    // ~ 문자열 순회
    String str = "Apple";

    for (Character c: str.toCharArray()) {
      System.out.println(c);
    }

    // ~ 문자열 다룰 수 있는 다양한 클래스들이 존재한다.
    // StringTokenizer
    StringTokenizer strTk = new StringTokenizer("New%String%Tokenizer", "%");
    while (strTk.hasMoreTokens()) {
      System.out.println(strTk.countTokens());
      System.out.println(strTk.nextToken());
    }

    // StringBuilder
    StringBuilder strBd = new StringBuilder();
    strBd.append("New String Builder");
    System.out.println(strBd);
    strBd.delete(strBd.length() - 2, strBd.length());
    System.out.println(strBd);

    // StringBuffer
    StringBuffer strBf = new StringBuffer("String");
    strBf.append("Buffer");
    strBf.insert(0, "New");
    System.out.println(strBf);

    //
    buildString(new int[] {1, 2, 3, 4, 5});
  }
}
