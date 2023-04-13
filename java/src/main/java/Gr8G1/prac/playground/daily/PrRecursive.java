package Gr8G1.prac.playground.daily;

import java.util.Arrays;

public class PrRecursive {
  public static void timesTable(int t, int c) {
    final int MAX_COUNT = 9;

    if (c > MAX_COUNT) return;

    System.out.printf("%d x %d = %d %n", t, c, (t * c));

    timesTable(t, ++c);
  }

  public static boolean isOdd(int n) {
    n = Math.abs(n);

    if (n == 0) return false;
    if (n == 1) return true;

    return isOdd(n - 2);
  }

  public static int factorial(int n) {
    if (n <= 1) return n;

    return n * factorial(--n);
  }

  public static int fibonacci(int n) {
    // final double sqrt = Math.sqrt(5);
    // return (int) (1 / sqrt * (Math.pow((1 + sqrt) / 2, n) - Math.pow((1 - sqrt) / 2, n)));

    if (n <= 1) return n;

    return fibonacci(n - 1) + fibonacci(n - 2);
  }

  public static int toSum(int[] arr) {
    if (arr.length == 0) return 0;

    return arr[0] + toSum(Arrays.copyOfRange(arr, 1, (arr.length)));
  }

  public static int[] take(int c, int[] arr) {
    if (c >= arr.length) return arr;

    return take(c, Arrays.copyOfRange(arr, 0, arr.length - 1));
  }

  public static int[] drop(int c, int[] arr) {
    if (c == 0 || arr.length == 0) return arr;

    return drop(--c, Arrays.copyOfRange(arr, 1, arr.length));
  }

  public static int[] reverse(int[] arr) {
    if (arr.length == 0) return new int[] {};
    int leng = arr.length;

    int[] h = Arrays.copyOfRange(arr, leng - 1, leng);
    int[] t = reverse(Arrays.copyOfRange(arr, 0, leng - 1));
    int[] r = new int[h.length + t.length];

    System.arraycopy(h, 0, r, 0, h.length);
    System.arraycopy(t, 0, r, h.length, t.length);

    return r;
  }

  public static boolean and(boolean[] arr) {
    if (arr.length == 0) return true;

    return arr[0] && and(Arrays.copyOfRange(arr, 1, arr.length));
  }

  public static String reString(String s, int repeat) {
    if (repeat == 0) return s;

    s = reString(s + s.charAt(0), repeat - 1);

    return s; // Tail Recursion???
  }

  public static int reInt(int i, int repeat) {
    if (repeat == 0) return i;

    return reInt(i + i, repeat - 1); // Tail Recursion
  }

  public static void main(String[] args) {
    int[] ia = new int[] {1, 2, 3};

    // System.out.println(PrRecursive.isOdd(-11));
    // System.out.println(PrRecursive.factorial(5));
    // System.out.println(PrRecursive.fibonacci(5));
    // System.out.println(PrRecursive.toSum(ia));
    // System.out.println(Arrays.toString(PrRecursive.take(4, ia)));
    // System.out.println(Arrays.toString(PrRecursive.drop(2, ia)));
    // System.out.println(Arrays.toString(PrRecursive.reverse(ia)));
    // System.out.println(PrRecursive.and(new boolean[] {}));
    // System.out.println(PrRecursive.reString("1", 3));
    System.out.println(PrRecursive.reInt(1, 5));
  }
}
