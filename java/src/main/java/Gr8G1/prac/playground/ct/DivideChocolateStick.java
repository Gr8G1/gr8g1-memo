package Gr8G1.prac.playground.ct;

import java.util.ArrayList;

public class DivideChocolateStick {
  public static ArrayList<Integer[]> divideChocolateStick(int M, int N) {
    // [직원수, M, N]
    ArrayList<Integer[]> result = new ArrayList<>();
    int gcd = GCD(M, N);

    for (int i = 1; i <= gcd; i++) {
      if (gcd % i == 0) result.add(new Integer[]{i, M / i, N / i});
    }

    return result;
  }

  public static int GCD(int a, int b) {
    return a % b != 0 ? GCD(b, a % b) : b;
  }

  public static int LCM(int a, int b) {
    return a * b / GCD(a, b);
  }

  public static void main(String[] args) {
    System.out.println(divideChocolateStick(20, 10));
  }
}
