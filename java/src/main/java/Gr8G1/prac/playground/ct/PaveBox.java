package Gr8G1.prac.playground.ct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PaveBox {
  /*
   * # 박스 포장(Queue)
   *  박스를 포장하는 데는 폭이 너무 좁아서, 한 줄로 서 있어야 하고, 들어온 순서대로 한 명씩 나가야 합니다.
   *  불행 중 다행은, 인원에 맞게 포장할 수 있는 기구들이 놓여 있어, 모두가 포장을 할 수 있다는 것입니다.
   *  짐이 많은 사람은 짐이 적은 사람보다 포장하는 시간이 길 수밖에 없습니다.
   *  뒷사람이 포장을 전부 끝냈어도 앞사람이 끝내지 못하면 기다릴 수밖에 없는 환경입니다.
   *  앞사람이 포장을 끝나면, 포장을 마친 뒷사람들과 함께 한 번에 나가게 됩니다.
   *  만약, 앞사람의 박스는 5 개고, 뒷사람 1의 박스는 4 개, 뒷사람 2의 박스는 8 개라고 가정했을 때,
   *  뒷사람 1이 제일 먼저 박스 포장을 끝내게 되고, 앞사람 1의 포장이 마칠 때까지 기다렸다가 같이 나가게 됩니다.
   *  이때, 통틀어 최대 몇 명이 한꺼번에 나가는지 알 수 있도록 함수를 구현해 주세요.
   *
   * ~ In
   *  - Integer[] boxes: [1, 2, 3, 4]
   * ~ Out
   *  > 한번에 나갈 수 있는 최대 인원 수: 4
   *
   * ! Warn
   *
   */
  public static int paveBox(Integer[] boxes) {
    List<Integer> pave = new ArrayList<>();
    Integer[] temp = Arrays.copyOfRange(boxes, 0, boxes.length);

    while (temp.length > 0) {
      // ! Variable used in lambda expression should be final or effectively final
      // ! 새로운 배열의 참조를 넘겨야 되는 이유: 람다 캡쳐링
      Integer[] fiTemp = temp;

      int fv = Arrays.stream(fiTemp).filter(n -> n > fiTemp[0]).findFirst().orElse(-1);
      int fIdx = Arrays.stream(fiTemp).collect(Collectors.toList()).indexOf(fv);

      if (fIdx != -1) {
        pave.add(fIdx);
        temp = Arrays.copyOfRange(fiTemp, fIdx, fiTemp.length);
      } else {
        pave.add(fiTemp.length);
        temp = Arrays.copyOfRange(fiTemp, 0, 0);
      }
    }

    return pave.stream().max(Integer::compare).orElse(1);
  }

  public static void main(String[] args) {
    System.out.println(paveBox(new Integer[] {5, 2, 3, 4, 6}));
  }
}
