package Gr8G1.prac.pojo.annotation.temp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class PrAnnotation {
  public static void main(String[] args) {
    // @사용자(커스텀) 어노테이션
    Annotation[] annoType =  B.class.getDeclaredAnnotations();
    for (Annotation a: annoType) System.out.println(((Gr8G1.prac.pojo.annotation.temp.MyCustomAnno) a).name());

    Field[] annoFileds = B.class.getDeclaredFields();
    for (Field f: annoFileds) {
      System.out.println(f.getAnnotation(Gr8G1.prac.pojo.annotation.temp.MyCustomAnno.class).name());
    }
  }
}

class A {
  void start() {}
  @Deprecated
  static void run() {}
}

@Gr8G1.prac.pojo.annotation.temp.MyCustomAnno(name = "B Class")
class B extends A {
  @Gr8G1.prac.pojo.annotation.temp.MyCustomAnno(name = "Java")
  private String name;

  @Override
  void start() {
    super.start();
  }

  @SuppressWarnings("deprecation") // @Deprecated 경고 생략
  public static void main(String[] args) {
    A.run(); // @Deprecated 알림
  }

  public String getName() {
    return name;
  }
}
