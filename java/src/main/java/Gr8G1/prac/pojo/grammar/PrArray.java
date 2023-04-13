package Gr8G1.prac.pojo.grammar;

import java.lang.reflect.Array;
import java.util.*;

public class PrArray {
  /*
   * # 배열(Array)
   *  - 1차원 배열
   *    - 타입[] 배열이름 = new 타입[배열길이];
   *    - 타입[] 배열이름 = new 타입[] { ... };
   *    - 타입[] 배열이름 = { ... };
   *  - 2차원 배열
   *    - 타입[][] 배열이름 = new 타입[배열길이][];
   *    - 타입[][] 배열이름 = new 타입[배열길이][] {{ ... }, { ... }};
   *  - 가변 배열(dynamic array)
   *    - Java 에서 2차원 배열 생성시 열의 길이를 명시하지 않음으로써, 행마다 다른 길이의 배열을 요소로 저장할 수 있다.
   *
   * ! 주의: 배열의 길이(선언, 초기화)에 항상 유념하도록 하자.
   *
   */

  public static <T> T[] concat(T[] a, T[] b) {
    int aLen = a.length;
    int bLen = b.length;

    @SuppressWarnings("unchecked")
    T[] c = (T[]) Array.newInstance(a.getClass().getComponentType(), aLen + bLen);
    System.arraycopy(a, 0, c, 0, aLen);
    System.arraycopy(b, 0, c, aLen, bLen);

    return c;
  }

  public static void main(String[] args) {
    Integer[] arr = {1, 2, 3, 4, 5}; // Java Array indexOf 미제공
    ArrayList<Integer> newArr = new ArrayList<>(Arrays.asList(arr)); // ArrayList <-> List 변환 후 사용

    System.out.println(Arrays.asList(arr).indexOf(2));
    System.out.println(newArr.indexOf(1));

    Integer[] destArr = new Integer[arr.length];

    System.arraycopy(arr, 0, destArr, 0, arr.length); // 기존 배열의 길이를 초과할 경우 Throw Exception 발생
    System.out.println(Arrays.toString(Arrays.copyOfRange(arr, 0, arr.length + 1))); // 복사 시점에 신규 배열 생성 및 배열 길이 정의
    System.out.println(Arrays.toString(destArr));

    int[] A = new int[] {1, 2, 3};
    int[] B = new int[] {4, 5, 6};

    List<String> list = new ArrayList<>() {{
      add("http://localhost:3000");
      add("http://localhost:8080");
    }};

    String findAddress = list.stream()
                  .filter(s -> s.contains("http://localhost:3000"))
                  .findAny()
                  .orElseThrow(RuntimeException::new);

  }
}
