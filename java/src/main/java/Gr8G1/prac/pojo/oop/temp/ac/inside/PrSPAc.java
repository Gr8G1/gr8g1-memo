package Gr8G1.prac.pojo.oop.temp.ac.inside;

import Gr8G1.prac.pojo.oop.temp.ac.PrAccessControl;

public class PrSPAc {
  public static void main(String[] args) {
    PrAccessControl pac = new PrAccessControl();

    System.out.println("PrSPAc");
    System.out.println(pac.publicS);
    System.out.println();

    System.out.println("PrDPAc extends");
    // System.out.println(pac.defaultS); : -> Cannot be accessed from outside packag
    // 계층 구조가 상위/하위로 존재한다(보여진다)하여도 default값에 접근 불가하다. (동등한 계층 레벨에 존재해야 접근 가능하다).
  }
}
