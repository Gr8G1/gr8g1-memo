package Gr8G1.prac.pojo.grammar;

public class PrOperator {
  /*
   * # 연산자 우선 순위
   *
   * 0. 그룹화: (), []
   * // ~ 단항 - 결합 방향: <-
   * 1. 단항(unary | n-ary) 연산자: !(논리 not), ~(비트 not), ++, --, sizeof(기타), instanceof(기타)
   * // ~ 이항 - 결합 방향: ->
   * 2. 산술(arithmetic)연산자 : +, -, *, /, %
   * 3. 시프트(shift) 연산자: <<, >>, >>>
   * 4. 관계|비교(relational|comparison) 연산자: <, <=, >, >=, ==, !=
   * 5. 비트(bitwise) 연산자: &(비트 and), ^(비트 xor), |(비트or)
   * 6. 논리(logical) 연산자: &&(논리 and), ∥(논리 or)
   * // ~ 삼항: 결합 방향: ->
   * 7. 삼항(ternary) 연산자: 조건식 ? 피연산1 : 피연산2
   * // ~ 대입: 결합 방향 <-
   * 8. 대입(assignment)(복합)연산자: =, +=, -=, *=, %=, /=, >>=, <<=
   * // ~ 순서: 결합 방향 ->
   * 9. 순서연산자: ,
   *
   * > 결합방식: 같은 순위의 연산자가 한 수식에 여러개(두개 이상) 존재하는 경우 우선적으로 연산하는 방향을 말한다.
   *  - 예) 산술 (->)
   *    - (1 + 2 + 3):
   *      > 1 + 2 -> + 3
   *  - 예) 대입 (<-)
   *    - a = b = c = 1
   *      > c = 1 -> b = c -> b = a
   *
   */
  public static void main(String[] args) {
    /*
     * # 단항 연산
     *  - !(논리 not), ~(비트 not), 증감(++, --), sizeof(기타)
     *
     * ! 주의: 증감 연산 사용시 전위(pre), 후위(post) 값의 변경에 주의
     *
     */
    boolean isTrue = true;
    isTrue = !isTrue; // isTrue = false;
    isTrue = !!isTrue; // isTrue = true; (실사용 X)
    isTrue = !(true && false); // isTrue = true

    int no1 = 1;
    int preNo1 = ++no1;
    int no2 = 1;
    int postNo2 = no2++;

    System.out.println("preNo1: " + preNo1);
    System.out.println("postNo2: " + postNo2);

    /*
     * # 산술 연산
     *  - 사칙연산이 적용된 (+, -, *, %) 와 나머지(%) 연산을 수행
     *
     * ! 주의: 연산 그룹화는 "()" 사용 묶음 처리 한다.
     * ! 주의: 그룹화 미지정시 연산 우선 순위가 적용되어 계산 된다.
     *
     */
    int one = 1;
    int two = 2;
    int three = 2;
    int five = 5;
    int sum = 0;

    System.out.println("그룹화 미지정: 1 + 2 + 3 / 5 = " + (one + two + three / five)); // 실수 미표현 출력
    // System.out.println((3F / 5F)); // 실수 표현 출력
    System.out.println("그룹화 지정: (1 + 2 + 3) / 5: " + (1 + 2 + 3) / 5);

    /*
     * # 대입 연산자
     *  - "=" 연산 결과를 대입(할당)한다.
     *
     * # 복합 대입 연산
     *  - "+=, -=, *=, /=, %=, <<=, >>="과 같은 다양한 대입 연사자와 다른 연산자를 결합한 연산자들이 있다.
     *
     */
    // 복합 대입 연산
    sum += one + two / three;
    System.out.println(sum);

    /*
     * # 비교 연산
     *  - "==, !=, >, >=, <, <=," 두 값의 관계를 비교하여 참 또는 거짓을 결과로 얻는 연산
     *
     * ! 왼쪽을 기준으로 크다 또는 크거나 같다로 해석
     *
     */
    char c = 'a';

    System.out.println(true == true);
    System.out.println(1 != 2);
    System.out.println(3 > 4);

    if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) System.out.println("유효한 문자입니다.");
    else System.out.println("유효하지 않은 문자입니다.");

    /*
     * # 삼항 연산
     *  - 조건신 ? 피연산1 : 피연산2;
     *
     * ! 삼항 연산자 사용시 주의사항
     * 피연산1과 피연산2의 타입(형) 다른 경우 조건 표현식의 타입을 결정하는 부분은 크게 아래 3가지 경우로 결정된다.
     *
     * 1. boolean: 1과 2의 타입이 불리언 인 경우 타입은 boolean이 된다.
     * 2. numeric: 1과 2의 타입이 numeric 인 경우 타입은 BNP(Binary Numeric Promotion) 의해 결정된다.
     *  BNP
     *    1. 레퍼런스 언박싱
     *    2. bnp(피연산자1, 피연산자2): double > float > long > int
     * 3. 그 외: LUB(Least Upper Bound) 의해 결정된다.
     *
     * ~ 참조: https://docs.oracle.com/javase/specs/jls/se13/html/jls-15.html#jls-15.25
     *
     */
    System.out.println((5 == 4) ? true : false); // return 타입(형) 일치
    System.out.println((5 == 4) ? 'Y' : false); // ! return 타입(형) 불일치 주의

    /*
     * # 시프트 연산: 정수 데이터의 비트를 왼쪽 또는 오른쪽으로 이동시키는 연산을 수행
     *
     * x << y: x의 각 비트를 y만큼 왼쪽으로 이동 (빈자리는 0으로 채운다)
     * x >> y: x의 각 비트를 y만큼 오른쪽으로 이동 (빈자리는 정수 x의 최상위 부호비트와 같은 값으로 채운다)
     * x >>> y: x의 각 비트를 y만큼 오른쪽으로 이동 (빈자리는 0으로 채운다)
     *
     * ! 비트 연산은 연산자를 32bit 분해 후 연산 수행한다.
     *
     */
    int x = 9;
    int y = 2;

    System.out.println("x: " + x + ", Binary: " + Integer.toBinaryString(x));
    System.out.println("y: " + y);
    System.out.println("x << y: " + (x << y) + ", Binary: " + Integer.toBinaryString(x << y));
    System.out.println("x >> y: " + (x >> y) + ", Binary: " + Integer.toBinaryString(x >> y));
    System.out.println("x >>> y: " + (x >>> y) + ", Binary: " + Integer.toBinaryString(x >>> y));
    System.out.println();

    /*
     * # 비트 논리 연산: 연산 대상이 boolean 타입일 경우에는 일반 논리 연산자로, 정수형일 경우 비트 논리연산자로 연산을 수행
     *
     * &(AND): 두 비트 모두 값이 1일 경우 결과는 1이 된다.
     * |(OR): 두 비트중 값이 1이 존재할 경우 결과는 1이 된다.
     * ^(XOR): 두 비트의 값이 서로 다른 경우 결과는 1이된다.
     * ~(NOT): 비트값 반전(보수)-((0 -> 1) | (1 -> 0))
     *
     * ! 비트 연산은 두 값을 비트 단위로 나열한 뒤 각 자릿수를 비교하는데 각 자릿수의 연산은 독립적이며 다른 자릿수에 영향을 주지 않는다.
     * ! 비트 연산은 연산자를 32bit 분해 후 연산 수행
     *
     */
    System.out.println("15, Binary: " + Integer.toBinaryString(15)); // 1111
    System.out.println("25, Binary: " + Integer.toBinaryString(25)); // 11001
    System.out.println("15 & 25 = " + (15 & 25) + ", Binary: " + Integer.toBinaryString((15 & 25)));
    System.out.println("> 15(1111) & 25(11001) -> 01111 & 11001 -> 01001 -> 1001");
    System.out.println("15 | 25 = " + (15 | 25) + ", Binary: " + Integer.toBinaryString((15 | 25)));
    System.out.println("> 1111 | 11001 -> 01111 & 11001 -> 11111");
    System.out.println("15 ^ 25 = " + (15 ^ 25) + ", Binary: " + Integer.toBinaryString((15 ^ 25)));
    System.out.println("> 1111 ^ 11001 -> 01111 & 11001 -> 10110");
    System.out.println("~25 = " + (~25) + ", Binary: " + Integer.toBinaryString((~25)));
    System.out.println("~-25 = " + (~-25) + ", Binary: " + Integer.toBinaryString((~-25)));
    System.out.println();
  }
}
