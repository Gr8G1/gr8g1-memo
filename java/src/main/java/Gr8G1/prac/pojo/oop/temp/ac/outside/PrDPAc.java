package Gr8G1.prac.pojo.oop.temp.ac.outside;

import Gr8G1.prac.pojo.oop.temp.ac.PrAccessControl;

import java.util.Arrays;

public class PrDPAc extends PrAccessControl {
  public void PrPublicMethod(int[] args) {
    System.out.println(Arrays.toString(args));
  }

  public static void main(String[] args) {
    PrAccessControl pac = new PrAccessControl();
    PrDPAc dpac = new PrDPAc();

    System.out.println("PrDPAc");
    System.out.println(pac.publicS);

    System.out.println("PrDPAc extends");
    System.out.println(dpac.protectedS); // 상속 결과에 따라 protected 접근 가능

    dpac.PrPublicMethod(new String[] {"1", "2", "3"});
    dpac.PrPublicMethod(new int[] {1,2,3,4});
  }
}
