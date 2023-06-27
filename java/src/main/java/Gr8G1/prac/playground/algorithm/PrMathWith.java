package Gr8G1.prac.playground.algorithm;

import java.util.*;

public class PrMathWith {
  public static int fact(int n) {
    int f = 1;
    int i = 0;

    while (++i <= n) f *= i;

    return f;
  }

  public static void swap(int[] arr, int idx1, int idx2) {
    int temp = arr[idx1];
    arr[idx1] = arr[idx2];
    arr[idx2] = temp;
  }

  public static void permu(int[] arr, int n, int r) {
    // Permutation(nPr) = n! / (n - r)!

    // int f1, f2, t;
    //
    // f1 = n;
    // f2 = n - r != 0 ? n - r : 1;
    //
    // for (int i = f1 - 1; i > 0; i--) f1 = f1 * i;
    // for (int i = f2 - 1; i > 0; i--) f2 = f2 * i;
    //
    // t = f1 / f2;
    System.out.printf("P: %s, P(%d, %d) = %d%n", Arrays.toString(arr), n, r, (fact(n) / fact(n - r)));
    getPermu(0, r, arr, new int[r], new boolean[arr.length]);
    System.out.println();
  }

  public static void getPermu(int start, int r, int[] arr, int[] result, boolean[] visited) {
    if (start == r) {
      System.out.print(Arrays.toString(result) + " ");
      return;
    }

    for (int i = 0; i < arr.length; i++) {
      if (!visited[i]) {
        visited[i] = true;
        result[start] = arr[i];
        getPermu(start + 1, r, arr, result, visited);
        visited[i] = false;
      }
    }
  }

  public static void permRe(int[] arr, int n, int r) {
    // Permutation with repetition(nΠr) = n^r
    // int i = 0;
    // int t = n;
    // while (++i < r) t *= t;
    System.out.printf("Pwr: %s, Pi(%d, %d) = %d%n", Arrays.toString(arr), n, r, (int) Math.pow(n, r));
    getPermuRe(0, r, arr, new int[r]);
    System.out.println();
  }

  public static void getPermuRe(int start, int r, int[] arr, int[] result) {
    if (start == r) {
      System.out.print(Arrays.toString(result) + " ");
      return;
    }

    for (int j : arr) {
      result[start] = j;
      getPermuRe(start + 1, r, arr, result);
    }
  }

  public static void combi(int[] arr, int n, int r) {
    // #Combination(nCr) = n! / (r! * (n - r)!)
    // fact(n) / (fact(r) * fact(n - r));

    // ~ Recursive
    // if(n==r || r==0) return 1;
    // return combination(n - 1, r - 1) + combination(n - 1, r);

    System.out.printf("C: %s, C(%d, %d) = %d%n", Arrays.toString(arr), n, r, fact(n) / (fact(r) * fact(n - r)));
    getCombi(0, 0, r, arr, new int[r], new boolean[arr.length]);
    System.out.println();
  }

  public static void getCombi(int start, int depth, int r, int[] arr, int[] result, boolean[] visited) {
    if (depth == r) {
      System.out.print(Arrays.toString(result) + " ");
      return;
    }

    for (int i = start; i < arr.length; i++) {
      if (!visited[i]) {
        visited[i] = true;
        result[depth] = arr[i];
        getCombi(i + 1, depth + 1, r, arr, result, visited);
        visited[i] = false;
      }
    }
  }

  public static void combiRe(int[] arr, int n, int r) {
    // Combination with repetition = ((n - 1 + r)Cr || (n + r - 1)Cr
    // Combination = n! / (r! * (n - r)!)

    System.out.printf("Cwr: %s, C(%d, %d) = %d%n", Arrays.toString(arr), n, r, fact((n - 1) + r) / (fact(r) * fact((n + r - 1) - r)));
    getCombiRe(0, 0, r, arr, new int[r]);
    System.out.println();
  }

  public static void getCombiRe(int start, int depth, int r, int[] arr, int[] result) {
    if (depth == r) {
      System.out.print(Arrays.toString(result) + " ");
      return;
    }

    for (int i = start; i < arr.length; i++) {
      result[depth] = arr[i];
      getCombiRe(i, depth + 1, r, arr, result);
    }
  }

  public static void main(String[] args) {
    permu(new int[] {1, 2, 3}, 3, 2);
    permRe(new int[] {1, 2, 3}, 3, 3);

    System.out.println();

    combi(new int[] {1, 2, 3}, 3, 2);
    combiRe(new int[] {1, 2, 3}, 3, 2);
  }
}
