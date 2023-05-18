package Gr8G1.prac.playground.bj.io;

import java.util.Scanner;

// # Q: https://www.acmicpc.net/problem/2588
public class CQ_2588 {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);

    int a;
    int b;
    int c;
    int t;

    a = sc.nextInt();
    b = sc.nextInt();
    c = 1;
    t = 0;

    while(b > 0) {
      System.out.println(a * (b % 10));

      t += a * (b % 10) * c;
      b /= 10;
      c *= 10;
    }

    System.out.println(t);
  }
}
