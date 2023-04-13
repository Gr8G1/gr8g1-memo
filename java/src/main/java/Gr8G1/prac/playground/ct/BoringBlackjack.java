package Gr8G1.prac.playground.ct;

import java.util.ArrayList;
import java.util.Arrays;

public class BoringBlackjack {
  public static int boringBlackjack(int[] cards) {
    ArrayList<Integer> result = new ArrayList<>();
    int primeCount = 0;

    getC(0, 0, 3, cards, new boolean[cards.length], new int[cards.length], result);

    for (Integer i : result) if (isPrime(i)) primeCount++;

    return primeCount;
  }

  public static void getC(int s, int d, int r, int[] arr, boolean[] visited, int[] selected, ArrayList<Integer> result) {
    if (d == r) {
      result.add(Arrays.stream(selected).sum());
      return;
    }

    for (int i = s; i < arr.length; i++) {
      if (!visited[i]) {
        visited[i] = true;
        selected[d] = arr[i];
        getC(i + 1, d + 1, r, arr, visited, selected, result);
        visited[i] = false;
      }
    }
  }

  // 소수 확인
  public static boolean isPrime(int num) {
    for (int i = 2; i * i <= num; i++) if (num % i == 0) return false;
    return true;
  }

  public static boolean isPrimeWithSqrt(int num) {
    if (num % 2 == 0) return false;
    int sqrt = (int) (Math.sqrt(num));

    for (int divider = 3; divider <= sqrt; divider += 2) if (num % divider == 0) return false;

    return true;
  }

  public static void main(String[] args) {
    System.out.println(boringBlackjack(new int[] {1, 2, 3, 4}));
  }
}
