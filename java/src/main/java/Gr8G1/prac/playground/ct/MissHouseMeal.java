package Gr8G1.prac.playground.ct;

import java.util.ArrayList;
import java.util.Arrays;

public class MissHouseMeal {
  public static ArrayList<String[]> missHouseMeal(String[] sideDishes) {
    ArrayList<String[]> result = new ArrayList<>();
    Arrays.sort(sideDishes);

    powerset(new String[]{}, sideDishes, 0, result);

    result.sort((a, b) -> {
      if (a.length == 0 || b.length == 0) return 0;
      return -1;
    });

    return result;
  }

  public static void powerset(String[] subset, String[] arr, int idx, ArrayList<String[]> result) {
    if (idx == arr.length) {
      result.add(subset);
      return;
    }

    // 부분집합 미포함
    powerset(subset, arr, idx + 1, result);

    // 부분집합 포함
    // String[] b = concat(subset, new String[] { arr[idx] });
    String[] b = new String[subset.length + 1];

    System.arraycopy(subset, 0, b, 0, subset.length);
    System.arraycopy(new String[]{arr[idx]}, 0, b, subset.length, 1);

    powerset(b, arr, idx + 1, result);
  }

  public static void main(String[] args) {
    System.out.println(
      Arrays.deepToString(
        missHouseMeal(new String[] { "쌀밥", "김치", "어묵", "감자" }).toArray()
      )
    );
  }
}
