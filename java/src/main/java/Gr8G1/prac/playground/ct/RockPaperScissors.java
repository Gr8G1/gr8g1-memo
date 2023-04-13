package Gr8G1.prac.playground.ct;

import java.util.ArrayList;
import java.util.Arrays;

public class RockPaperScissors {
  /*
   * # 가위 바위 보 게임 (중복 순열)
   *  세 판의 가위바위보 게임을 할 경우, 한 사람은 세 번의 선택(예. 가위, 가위, 보)을 할 수 있다.
   *  세 번의 선택으로 가능한 모든 경우의 수를 구하는 함수를 작성합니다.
   *
   * ~ In
   *  - int rounds: 3
   * ~ Out
   *  -
   *
   * ! Warn
   *  - 최종적으로 리턴되는 배열의 순서는 가중치 적용 정렬(Weighted Sort)을 따른다.
   *    > 중요도: rock > paper > scissors
   *
   */
  public static ArrayList<String[]> rockPaperScissors(int rounds) {
    ArrayList<String[]> result = new ArrayList<>();
    String[] p = {"rock", "paper", "scissors"};

    getPwr(0, rounds, p, new String[rounds], result);

    return result;
  }

  // Pwr(Permutaion with recursive)
  public static void getPwr(int s, int r, String[] p, String[] selected, ArrayList<String[]> result) {
    if (s == r) {
      result.add(Arrays.copyOfRange(selected, 0, selected.length));
      return;
    }

    for (String v : p) {
      selected[s] = v;
      getPwr(s + 1, r, p, selected, result);
    }
  }

  public static void main(String[] args) {
    System.out.println(Arrays.deepToString(rockPaperScissors(3).toArray())
        .replace("[[", "[\n  [")
        .replace("], ", "]\n  ")
        .replace("]]", "]\n]")
    );
  }
}
