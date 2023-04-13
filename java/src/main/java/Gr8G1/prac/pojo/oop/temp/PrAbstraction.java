package Gr8G1.prac.pojo.oop.temp;

interface A {
  void to();
}

interface B {
  void to();
}

class C implements A, B {
  @Override
  public void to() {
    System.out.println("It's implements A, B to");
  }
}

public class PrAbstraction {
  public static void main(String[] args) {
     C c = new C();

     c.to();;
  }
}
