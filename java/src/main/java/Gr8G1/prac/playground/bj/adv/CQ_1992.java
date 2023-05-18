package Gr8G1.prac.playground.bj.adv;

import java.util.Scanner;

// # Q: https://www.acmicpc.net/problem/1992
public class CQ_1992 {
  static int[][] image = new int[][] {
    {1, 0, 1, 1},
    {0, 1, 1 ,1},
    {0, 0, 1, 1},
    {0, 0, 0, 0}
  };
  static StringBuilder sb = new StringBuilder();

  public static void QuadTree(int x, int y, int size) {
    if (isPossible(x, y, size)) {
      sb.append(image[x][y]);
      return;
    }

    int newSize = size / 2;

    sb.append('(');

    QuadTree(x, y, newSize);                             // 좌 상단
    QuadTree(x, y + newSize, newSize);               // 우 상단
    QuadTree(x + newSize, y, newSize);               // 좌 하단
    QuadTree(x + newSize, y + newSize, newSize); // 우 하단

    sb.append(')');
  }

  public static boolean isPossible(int x, int y, int size) {
    int value = image[x][y];

    for (int i = x; i < x + size; i++) {
      for (int j = y; j < y + size; j++) {
        if (value != image[i][j]) {
          return false;
        }
      }
    }

    return true;
  }

  public static void main(String[] args) {
    Scanner in = new Scanner(System.in);

    int N = in.nextInt();
    image = new int[N][N];

    for (int i = 0; i < N; i++) {
      String str = in.next();

      for (int j = 0; j < N; j++) {
        image[i][j] = str.charAt(j) - '0';
      }
    }

    QuadTree(0, 0, image.length);

    System.out.println(sb.toString());
  }
}
