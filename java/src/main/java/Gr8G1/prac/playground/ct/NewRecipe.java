package Gr8G1.prac.playground.ct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class NewRecipe {
  /*
   * # 재료 검사 (순열)
   *  N 가지의 재료 중에 단 M 가지만을 사용하여 조합한 모든 경우의 수 를 반환하라.
   *  재료는 0과 1로만 이루어진 숫자로 암호화가 되어 있고, 항상 1로 시작하며 복호화를 할 수 없다.
   *  단, 0이 3개 이상인 재료는 상한 재료이기 때문에 제외한다.
   *
   * ~ In
   *  - int[] stuffArr: [1, 10, 1000, 1110]
   *    >  0과 1로만 이루어진 숫자이며, 항상 1로 시작한다.
   *  - int choiceNum: 2
   * ~ Out
   *    > 사용 가능 재료: [1, 10, 11000, 1111] => [1, 10, 1111]
   *    > 조합 가능 경우의 수 = 6
   *
   * ! Warn
   *  - 내부(오름 차순 정렬)
   */
  public static ArrayList<Integer[]> newRecipe(int[] stuffArr, int choiceNum) {
    ArrayList<Integer[]> result = new ArrayList<>();
    int[] arr = Arrays.stream(stuffArr)
                  .filter(n ->
                            Integer.toString(n)
                              .replace(String.valueOf('1'), "")
                              .length() < 3
                  )
                  .toArray();
    Arrays.sort(arr);

    if (stuffArr.length <= choiceNum) return null;

    getP(0, choiceNum, arr, new boolean[arr.length], new Integer[choiceNum], result);

    return result;
  }

  public static void getP(int s, int r, int[] arr, boolean[] visited, Integer[] selected, ArrayList<Integer[]> result) {
    if (s == r) {
      result.add(Arrays.copyOfRange(selected, 0, selected.length));
      return;
    }

    for (int i = 0; i < arr.length; i++) {
      if (!visited[i]) {
        visited[i] = true;
        selected[s] = arr[i];
        getP(s + 1, r, arr, visited, selected, result);
        visited[i] = false;
      }
    }
  }

  public static void main(String[] args) {
    System.out.println(Arrays.deepToString(
        Objects.requireNonNull(newRecipe(new int[]{11, 1, 10, 10001, 10000}, 2)).toArray()
      )
    );
  }
}
