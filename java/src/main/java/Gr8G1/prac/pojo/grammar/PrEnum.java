package Gr8G1.prac.pojo.grammar;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrEnum {
  /*
   * # enum
   *
   * enum(열거형)
   *  - 여러 상수들을 편리하게 선언할 수 있도록 만들어진 java 문법요소
   *    > 타입에 안전한 열거형 제공 (동등 비교시 값만을 비교하는것이 아닌 타입 또한 비교한다.)
   *  - 상수명의 중복을 피하고, 타입에 대한 안정성을 보장한다.
   *  - 간결하고 가독성이 좋은 코드 작성에 용이하고 switch 문에서 사용이 가능하다.
   *
   *  > enum 열거형이름 { 상수명, ...}
   *
   * 열거형의 비교
   *  - == | compareTo 사용 (비교 연사자 사용 불가)
   *
   * 열거형이 활용
   *
   *
   */
  enum Seasons {SPRING, SUMMER, FALL, WINTER};

  public enum Day {
    MON("Monday", 10),
    TUE("Tuesday", 20),
    WED("Wednesday", 30),
    THU("Thursday", 40),
    FRI("Friday", 50),
    SAT("Saturday", 60),
    SUN("Sunday", 70)
    ;

    private final String label;
    private final int number;

    Day(String label, int number) {
      this.label = label;
      this.number = number;
    }

    public String label() {
      return label;
    }

    public int number() {
      return number;
    }

    private static final Map<String, Day> BY_LABEL = Stream.of(Day.values()).collect(Collectors.toMap(Day::label, Function.identity()));

    private static final Map<Integer, Day> BY_NUMBER = Stream.of(values()).collect(Collectors.toMap(Day::number, Function.identity()));

    public static Day valueOfLabel(String label) {
      return BY_LABEL.get(label);
    }

    public static Day valueOfNumber(int number) {
      return BY_NUMBER.get(number);
    }
  }

  public static void main(String[] args) {
    for (Seasons S: Seasons.values()) {
      System.out.printf("Enum - name: %s, ordinal: %d \n", S.name(), S.ordinal());
    }

    int rn = (int) (Math.random() * Seasons.values().length);
    switch (Seasons.values()[rn]) {
      case SPRING:
        System.out.println("봄, " + Seasons.valueOf("SPRING"));
        break;
      case SUMMER:
        System.out.println("여름, " + Seasons.valueOf("SUMMER"));
        break;
      case FALL:
        System.out.println("가을, " + Seasons.valueOf("FALL"));
        break;
      case WINTER:
        System.out.println("겨울, " + Seasons.valueOf("WINTER"));
        break;
      default:
        System.out.println('밤');
    }

    System.out.println(Arrays.toString(Day.values())); // [MON, TUE, WED, THU, FRI, SAT, SUN]
    System.out.println(Day.MON); // MON
    System.out.println(Day.MON.name()); // MON
    System.out.println(Day.MON.label()); // Monday
    System.out.println(Day.MON.number()); // 10

    Day[] values = Day.values();
    String[] strings = Arrays.stream(values)
                           .map(Day::label)
                           .toArray(String[]::new);
    System.out.println("strings = " + Arrays.toString(strings));
  }
}
