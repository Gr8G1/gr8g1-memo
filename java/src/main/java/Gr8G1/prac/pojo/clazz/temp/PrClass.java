package Gr8G1.prac.pojo.clazz.temp;

public class PrClass {
  PrClass() {
    this("생성자를 호출합니다."); // 생성자 셀프 호출
  }

  // 생성자 오버로딩
  PrClass(String s) {
    System.out.println(s);
  }

  // 필드 (클래스 변수)
  static String sf = "static field";
  // 필드 (인스턴스 변수)
  String instf = "instance field";

  // 메소드 (|| 클래스 메소드)
  static void sClassMethod() {}
  // 메소드 (|| 인스턴스 메소드)
  void iMethod() {
    String instf = "local";

    System.out.println(this.instf); // -> "instance field"
    System.out.println(instf); // -> "local"
  }
  // 메소드 오버로딩
  void iMethod(int i) {}

  // 중첩 클래스 (스태틱 클래스)
  static class nClass {}
  // 내부 클래스 (inner 클래스)
  class iClass {}

  public static void main(String[] args) {
    // 인스턴스화
    PrClass iPrClass = new PrClass();
    // 중첩 클래스 인스턴스화
    PrClass.nClass nClass = new PrClass.nClass();
    // 내부 클래스 인스턴스화
    PrClass.iClass iClass = iPrClass.new iClass();

    System.out.println(iPrClass);
    System.out.println(nClass);
    System.out.println(iClass);
  }
}
