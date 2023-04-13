package Gr8G1.prac.pojo.lambda.temp;

import Gr8G1.prac.pojo.lambda.temp.MyFunc;

import java.util.Arrays;
import java.util.function.*;

public class PrLambda {
  public enum MinOrMax {
    MAX_SCORE("MAX", v -> v >= 5000 && v < 10000),
    MIN_SCORE("MIN", v -> v >= 3000 && v < 5000),
    ZERO_SCORE("ZERO", v -> v > 0 && v < 3000),
    NONE("NONE", v -> false);

    private final String value;
    private final Predicate<Integer> condition;

    MinOrMax(String value, Predicate<Integer> condition) {
      this.value = value;
      this.condition = condition;
    }

    public static MinOrMax findMinOrMax(int score) {
      return Arrays.stream(MinOrMax.values())
          .filter(v -> v.condition.test(score))
          .findAny()
          .orElse(NONE);
    }
  };

  // Lambda 생성 Test
  static class A {}

  public static void main(String[] args) {
    MyFunc<String> myFuncString = System.out::println;
    myFuncString.apply("Hello World!");

    Function<String, Integer> checkLeng = String::length;
    System.out.println(checkLeng.apply("Hello World!"));

    System.out.println(MinOrMax.findMinOrMax(9090));;
    System.out.println(MinOrMax.findMinOrMax(4890));;

    Supplier<? extends A> genA = A::new;
    A newA = genA.get();
    System.out.println("It's " + newA.getClass().getSimpleName());

    Consumer<String> con = t -> System.out.println("문자열의 길이: " + t.length());
    con.accept("문자열의길이");

    UnaryOperator<Integer> unaryOperator = i -> i + 1;
    System.out.println(unaryOperator.apply(100));

    BinaryOperator<String> binaryOperator = (s1, s2) -> s1 + s2;
    System.out.println(binaryOperator.apply("Ja", "va"));
  }
}
