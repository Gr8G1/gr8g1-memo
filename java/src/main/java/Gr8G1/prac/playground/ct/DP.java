package Gr8G1.prac.playground.ct;

public class DP {
  public static long coinChange(int total, int[] coins) {
    long[] table = new long[total + 1];

    table[0] = 1;

    for (int k : coins)
      for (int j = k; j <= total; j++)
        table[j] += table[j - k];

    return table[total];
  }

  // public static int coinChangeA(int[] coins, int leng, int sum) {
  //   if (sum == 0) return 1;
  //   if (sum < 0 || leng <= 0) return 0;
  //
  //   return coinChangeA(coins, leng - 1, sum) + coinChangeA(coins, leng, sum - coins[leng - 1]);
  // }

  public static int tiling(int num) {
    // Top Down
    Integer[] dp = new Integer[num + 1];

    if (num <= 2) return num;
    if (dp[num] != null) return dp[num];

    dp[num] = tiling(num - 1) + tiling(num - 2);

    return dp[num];

    // Bottom Up
    // if (num <= 1) return 1;
    //
    // int[] dp = new int[num + 1];
    //
    // dp[0] = 1;
    // dp[1] = 1;
    //
    // for (int i = 2; i < dp.length; i++) {
    //   dp[i] = dp[i - 1] + dp[i - 2];
    //   System.out.println("Arrays.toString(dp) = " + Arrays.toString(dp));
    // }
    //
    // return dp[num];
  }

  public static void main(String[] args) {
    // # 동전 교환 알고리즘
    System.out.println(coinChange(10, new int[] {1, 5}));
    // System.out.println(coinChangeA(new int[] {1, 2, 3, 4, 5}, 5, 10));

    // # 2 x N 타일 채우기
    System.out.println(tiling(5));
  }
}
