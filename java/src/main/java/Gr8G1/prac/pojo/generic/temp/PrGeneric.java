package Gr8G1.prac.pojo.generic.temp;

import java.util.Arrays;
import java.util.List;

public class PrGeneric {

}

class Box<T> {
  private T meterial;

  public Box() {}
  public Box(T meterial) {
    this.meterial = meterial;
  }

  public T getMeterial() {
    return meterial;
  }
}

class GlassBox<T> extends Box<T> { // 상위 클래스 Box<T>: 일반화(가변형) 선언
  public GlassBox(T meterial) {
    super(meterial);
  }

  public static void main(String[] args) {
    List<Box<Integer>> list = Arrays.asList(
        new GlassBox<>(100),
        new GlassBox<>(80)
    );

    Printer.printSuperBox(list);
    Printer.printExtendsBox(list);
  }
}

class PlasticBox extends Box<String> { // 상위 클래스 Box<String> : 명시적 제네릭 타입 선언
  public PlasticBox(String meterial) { // 상위 클래스의 명시적 제네릭 타입 선언에 맞춰 하위 클래스 생성자의 매개변수 타입 지정
    super(meterial);
  }

  public static void main(String[] args) {
    List<Box<String>> list = Arrays.asList(
        new PlasticBox("Plastic recyle 100%"), // 컴파일 단계에서 제네릭 타입 유추 가능 <String> -> <> : 타입 생략
        new PlasticBox("Plastic recycle 80%")  // 컴파일 단계에서 제네릭 타입 유추 가능 <String> -> <> : 타입 생략
    );

    Printer.printSuperBox(list); // 제네릭 메소드 <T> 생략 : 컴파일 단계에서 제네릭 타입 유추 가능
    Printer.printExtendsBox(list); // 제네릭 메소드 <T> 생략 : 컴파일 단계에서 제네릭 타입 유추 가능
  }
}

class Printer {
  public static <T> void printSuperBox(List<? super Box<T>> list) { // 제네릭 타입을 매개변수로 받는 흔하지 않은 시그니처 선언 방식
    list.set(0, (Box<T>) new Box<Integer>(1)); // 억지로 맞춘 형 변환
  }

  public static <T> void printExtendsBox(List<? extends Box<T>> list) {
    for (Box<T> b : list) {
      System.out.println(b.getMeterial());
    }
  }
}
