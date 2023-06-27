package Gr8G1.prac.playground.bj.adv;

// # Q: https://www.acmicpc.net/problem/2444
public class CQ_2444 {
  private static void printStar(int n, int i) {
    for (int j = 1; j <= n - i; j++) System.out.print(" ");
    for (int k = 1; k <= 2 * i - 1; k++) System.out.print("*");

    System.out.println();
  }

  public static void main(String[] args) {
    int n = 5; // 다이아몬드의 크기

    // 위쪽 부분 출력
    for (int i = 1; i <= n; i++) printStar(n, i);

    // 아래쪽 부분 출력
    for (int i = n - 1; i >= 1; i--) printStar(n, i);
  }
}
