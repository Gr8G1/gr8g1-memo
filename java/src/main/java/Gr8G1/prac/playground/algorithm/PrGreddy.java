package Gr8G1.prac.playground.algorithm;

import java.util.*;
import java.util.stream.Collectors;


public class PrGreddy {
  /*
   * # 탐욕 알고리즘
   *
   * 탐욕 알고리즘이란?
   *  -  매 순간, 최적이라 생각되는 해답(locally optimal solution)을 찾아 이를 기반으로
   *     최종 해답(globally optimal solution)에 도달하는 문제 해결 방식
   *
   * 탐욕 알고리즘 문제 해결 3단계
   *  1. 선택 절차(Selection Procedure): 현재 상태에서의 최적의 해답 선택
   *  2. 적절성 검사(Feasibility Check): 선택된 해가 문제의 조건을 만족하는지 검사
   *  3. 해답 검사(Solution Check): 문제가 해결되었는지 검사하고, 해결되지 않았다면 선택 절차로 돌아가 과정 반복
   *
   * 탐욕 알고리즘 구현시 최적의 결과를 보장(최소) 받을 수 있는 2가지 조건
   *  1. 탐욕적 선택 속성(Greedy Choice Property) : 이전의 선택이 이후의 선택에 영향을 주지 않는다.
   *  2. 최적 부분 구조(Optimal Substructure) : 문제에 대한 최종 해결 방법은 부분 문제에 대한 최적 문제 해결 방법으로 구성한다.
   *
   */

  // 박스포장
  public static int movingStuff(int[] stuff, int limit) {
    ArrayList<Integer> stuffList = (ArrayList<Integer>) Arrays.stream(stuff).boxed().collect(Collectors.toList());

    int firstStuff;
    int lastStruff = -1;

    int boxWeight;    // 박스 무게
    int boxCount = 0; // 박스 개수

    while (!stuffList.isEmpty()) {
      if (lastStruff != -1) stuffList.remove(lastStruff);

      firstStuff = stuffList.remove(0);
      lastStruff = -1;

      boxWeight = 0;
      boxCount++;

      for (int i = 0; i < stuffList.size(); i++) {
        int weight = firstStuff + stuffList.get(i); // 박스에 2개 이상의 물건은 담을 수 없다.

        if (limit >= weight && boxWeight <= weight) {
          boxWeight = weight;
          lastStruff = i;
        }
      }
    }

    return boxCount;
  }

  // 거스름돈 계산
  public static int partTimeJob(int k) {
    int[] unit = {500, 100, 50, 10, 5, 1};
    int unitCount = 0;

    for (int u : unit) {
      unitCount += Math.floorDiv(k, u); // quotient: k / u
      k = Math.floorMod(k, u); // remainder: k % u || k - (quotient * u)

      if (k == 0) break;
    }

    return unitCount;
  }

  public static void main(String[] args) {
    System.out.println(movingStuff(new int[]{60, 73, 80, 87, 103, 109, 119, 123, 128, 129, 136, 146, 153, 168, 182}, 200));
    System.out.println(partTimeJob(4972));
  }
}
