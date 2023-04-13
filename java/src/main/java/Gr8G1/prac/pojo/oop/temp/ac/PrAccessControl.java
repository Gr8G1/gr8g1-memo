package Gr8G1.prac.pojo.oop.temp.ac;

import java.util.Arrays;

public class PrAccessControl {
  /*
   * # 접근 제어자
   *            --------------------------  접근 여부  --------------------------
   *            | 동일 클래스 | 동일 패키지 | 하위 클래스 | 외부 패키지(클래스) |
   * private    |      O      |      X      |      X      |          X          |
   * (default)  |      O      |      O      |      X      |          X          |
   * protected  |      O      |      O      |      O      |          X          |
   * public     |      O      |      O      |      O      |          O          |
   *            -----------------------------------------------------------------
   * * private: 동일 객체(클래스) 내에서만 접근 허용
   * * default: 동일 객체(클래스), 같은 패키지(폴더)에 있는 객체들만 접근을 허용
   * * protected: 같은 패키지에 있는 객체(클래스)와 하위(*상속*) 관계의 객체들만 허용
   * * public: 모든 접근 허용
   *
   * 그 외
   * static
   *  - 클래스가 인스턴스화되지 않아도 접근 가능하다.
   *  - 메소드 내에서는 인스턴스 멤버들을 직접 사용할 수 없다.
   * static 변수
   *  - 모든 인스턴스에 공통적으로 사용되는 클래스 변수가 된다. (default 접근 여부와 동일)
   *  - 클래스 변수는 인스턴스를 생성하지 않고 사용이 가능하다.
   *  - 클래스가 메모리에 로딩될 때 생성된다.
   * static 메소드
   *  - 인스턴스를 생성하지 않고도 호출이 가능한 메소드가 된다.
   * final
   *  - 기존 클래스로부터 새로운 서브(확장) 클래스를 만들 수 없다.
   * final 클래스
   *  - 변경될 수 없는 클래스, 확장될 수 없는 클래스가 된다.
   *  - final로 지정된 클래스는 다른 클래스의 조상이 될 수 없다.
   * final 메소드
   *   - 변경될 수 없는 메소드, final로 지정된 메소드는 오버라이딩을 통해 재정의될 수 없다.
   * final 멤버변수 / final 지역변수
   *   - 변수 앞에 final이 붙으면 값을 변경할 수 없는 상수가 된다.
   * abstract : 클래스, 인터페이스, 메소드가 추상적임을 알린다.
   *   abstract 클래스
   *    - 클래스 내에 추상메소드가 선언되어 있음을 알린다.
   *   abstract 메소드
   *    - 선언부만 작성하고 구현부는 작성하지 않은 추상 메소드임을 알린다.
   * native
   *  - 자바가 아닌 언어로 구현한 후 자바에서 사용하려고 할 때 이용하는 키워드이다.
   * transient
   *  - 객체의 직렬화 시에 해당 필드를 직렬화 대상에서 제외한다.
   * synchronized
   *  - 스레드를 동기화하기 위해서 제공하는 키워드이다.
   * volatile
   *  - 변수를 메인 메모리에 저장하겠다고 명시한다.
   * strictfp
   *  - 부동소수(floating point) 계산의 특정한(restrict) 사항을 제어하는 데 사용된다.
   */
  static String staticS = "Static String";

  private final String privateS = "Private String";
  String defaultS = "Default String";
  protected String protectedS = "protected String";
  public String publicS = "Public String";

  public PrAccessControl() {
    System.out.println("Init PrAccessControl Constructor");
    System.out.println(this.privateS);
    System.out.println(this.protectedS);
    System.out.println(this.defaultS);
    System.out.println(this.publicS);
    System.out.println();
  }

  public void PrPublicMethod(String[] args) {
    System.out.println("PrPublicMethod" + Arrays.toString(args));
  }

  public static void main(String[] args) {
    System.out.println(staticS);
  }
}

class SameLevelPackage {
  public static void main(String[] args) {
    PrAccessControl pac = new PrAccessControl();

    System.out.println(pac.defaultS);
  }
}
