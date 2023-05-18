package Gr8G1.prac.playground.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class PrSorting {
  /*
   * # 정렬 알고리즘
   *  - 버블 정렬 : Bubble Sort
   *    특징
   *      - 비교 기반 정렬 알고리즘: 인접한 요소를 비교하여 정렬을 수행하는 비교 기반의 정렬 알고리즘이다.
   *      - 안정적인 정렬 알고리즘: 동일한 값에 대해 순서가 변경되지 않으므로 안정적인 정렬 알고리즘이다.
   *          > 동일한 값의 상대적인 순서가 유지된다.
   *      - 시간 복잡도: 최선, 평균, 최악의 경우 모두 O(n^2)의 시간 복잡도를 가진다.
   *          > 비교 및 교환 연산이 배열의 크기에 비례하여 이루어지기 때문
   *      - 추가적인 메모리 공간 필요 없음: 버블 정렬은 입력 배열 외에는 추가적인 메모리 공간을 필요로하지 않는다.
   *          > 인접한 요소의 위치를 교환하는 것으로 정렬을 수행하므로, 입력 배열 내에서 직접 연산이 이루어진다.
   *      - 최선의 경우 교환 없음: 입력 배열이 이미 정렬되어 있는 경우, 버블 정렬은 최선의 경우로서 교환이 필요하지 않고 한 번의 패스로 정렬을 완료할 수 있다.
   *          > 이러한 경우에는 시간 복잡도가 O(n)으로 개선된다.
   *      - 비효율적인 정렬 알고리즘: 버블 정렬은 배열의 크기가 클수록 비효율적인 정렬 알고리즘이다.
   *          > 큰 배열에서는 많은 비교와 교환 연산이 발생하므로, 다른 정렬 알고리즘을 고려하는 것이 바람직하다.
   *  - 삽입 정렬 : Insertion Sort
   *    특징
   *      - 비교 기반 정렬 알고리즘: 요소들을 비교하여 정렬하는 비교 기반의 정렬 알고리즘이다.
   *      - 안정적인 정렬 알고리즘: 동일한 값에 대해 순서가 변경되지 않으므로 안정적인 정렬 알고리즘이다.
   *          > 동일한 값의 상대적인 순서가 유지된다.
   *      - 시간 복잡도: 최선의 경우 시간 복잡도는 O(n)이며, 평균 및 최악의 경우에는 O(n^2)의 시간 복잡도를 가진다.
   *          > 정렬되지 않은 요소를 정렬된 부분에 삽입하는 작업이 이루어지기 때문
   *      - 추가적인 메모리 공간 필요 없음: 입력 배열 외에는 추가적인 메모리 공간을 필요로하지 않는다.
   *          > 요소들을 이동하면서 정렬을 수행하므로, 입력 배열 내에서 직접 연산이 이루어진다.
   *      - 최선의 경우 교환 없음: 입력 배열이 이미 정렬되어 있는 경우, 삽입 정렬은 최선의 경우로서 교환 없이 한 번의 패스로 정렬을 완료할 수 있다.
   *          > 이러한 경우에는 시간 복잡도가 O(n)으로 개선된다.
   *      - 장점과 한계: 배열의 크기가 작을 때 빠른 정렬 알고리즘으로 알려져 있다. 또한, 배열이 거의 정렬되어 있는 경우에도 효율적으로 동작한다.
   *                    그러나 배열의 크기가 클수록 비효율적이며, 다른 고급 정렬 알고리즘에 비해 성능이 떨어질 수 있다.
   *  - 합병(병합) 정렬 : Merge Sort
   *    특징
   *      - 분할 정복 알고리즘: 분할 정복(Divide and Conquer) 알고리즘의 대표적인 예이다.
   *          > 주어진 배열을 반으로 분할하고, 각 부분 배열을 재귀적으로 정렬한 뒤, 병합하여 정렬된 배열을 생성한다.
   *      - 안정적인 정렬 알고리즘: 동일한 값에 대해 순서가 변경되지 않으므로 안정적인 정렬 알고리즘이다.
   *          > 동일한 값의 상대적인 순서가 유지된다.
   *      - 시간 복잡도: 최선, 평균, 최악의 경우 모두 O(n log n)의 시간 복잡도를 가진다.
   *          > 배열을 반으로 분할하고 병합하는 과정에서 비교 연산이 이루어지기 때문
   *      - 추가적인 메모리 공간 필요: 입력 배열 외에도 추가적인 메모리 공간을 필요로 합니다.
   *          > 분할된 부분 배열을 임시로 저장할 배열이 필요하며, 이후에 병합되면서 정렬된 배열을 생성한다.
   *      - 장점과 한계: 안정적이고 빠른 정렬 알고리즘으로 크기가 큰 배열에도 효과적으로 적용할 수 있으며, 최악의 경우에도 일정한 성능을 보장한다다.
   *                    그러나 추가적인 메모리 공간을 필요로 하기 때문에 공간 복잡도가 높을 수 있다.
   *  - 퀵 정렬 : Quick Sort
   *    특징
   *      - 분할 정복 알고리즘: 분할 정복(Divide and Conquer) 알고리즘의 대표적인 예이다.
   *          > 주어진 배열에서 피벗을 선택하고, 피벗을 기준으로 작은 값과 큰 값으로 분할한 뒤, 각 부분 배열을 재귀적으로 정렬한다.
   *      - 비교 기반 정렬 알고리즘: 요소들을 비교하여 정렬하는 비교 기반의 정렬 알고리즘이다.
   *          > 요소 간의 비교를 통해 정렬을 진행한다.
   *      - 시간 복잡도: 최선의 경우 시간 복잡도는 O(n log n)이며, 평균적으로도 O(n log n)의 시간 복잡도를 가집니다. 하지만 최악의 경우(피벗이 항상 최솟값이나 최댓값인 경우)에는 O(n^2)의 시간 복잡도를 가질 수 있다.
   *      - 추가적인 메모리 공간 필요 없음: 입력 배열 외에는 추가적인 메모리 공간을 필요로하지 않는다.
   *          > 분할된 부분 배열을 재귀적으로 정렬하는 방식으로 진행되며, 배열 내에서 위치를 교환하여 정렬을 수행한다.
   *      - 피벗의 선택: 퀵 정렬에서 피벗의 선택은 정렬의 성능에 영향을 미친다.
   *          > 피벗을 선택하는 방법에는 여러 가지 전략이 있지만, 일반적으로는 배열의 첫 번째 요소, 마지막 요소, 중간 요소 등을 선택한다.
   *      - 장점과 한계: 일반적으로 빠른 정렬 알고리즘으로 알려져 있다. 평균적으로 다른 정렬 알고리즘보다 빠르게 동작합니다.
   *                    그러나 최악의 경우에는 성능이 저하될 수 있으며, 정렬된 배열에 대해서는 성능이 좋지 않을 수 있다.
   *  - 카운트 정렬 : Counting Sort
   *    특징
   *      - 정수 배열에 최적화된 정렬 알고리즘: 정수 배열에 대해서 특히 효과적인 정렬 알고리즘이다.
   *          > 배열의 요소가 정수이고, 정수의 범위가 제한되어 있는 경우에 적합하다.
   *      - 비교 기반 정렬이 아닌 계수 정렬 알고리즘: 각 요소의 개수를 계수하여 정렬하는 계수 정렬 알고리즘이다.
   *      - 추가적인 메모리 공간 필요: 입력 배열 외에 추가적인 메모리 공간을 필요로 한다.
   *          > 입력 배열의 최댓값에 따라서 개수를 기록할 카운트 배열을 필요로 한다.
   *      - 안정적인 정렬 알고리즘: 카운트 정렬은 안정적인 정렬 알고리즘이다
   *          > 동일한 값의 상대적인 순서가 유지된다.
   *      - 시간 복잡도: 시간 복잡도는 입력 배열의 크기를 N, 입력 값의 범위를 K라고 할 때, O(N+K)이다.
   *          > 개수를 세는 단계와 누적하여 정렬된 배열을 생성하는 단계에서 선형 시간에 처리되기 때문
   *      - 제한된 범위에 적용 가능: 입력 값의 범위가 제한되어 있을 때 가장 효율적으로 동작한다.
   *          > 입력 값의 범위가 크거나 실수형 데이터일 경우에는 다른 정렬 알고리즘을 고려하는 것이 좋다.
   *  - 기수 정렬 : Radix Sort
   *    특징
   *      - 비교 기반 정렬이 아닌 정렬 알고리즘: 키(key)의 비교를 기반으로 정렬하는 알고리즘이 아닌 키를 각 자리 숫자나 문자로 분리하여 정렬한다.
   *      - 가장 작은 자리 숫자부터 정렬: 가장 작은 자리 숫자부터 시작하여 큰 자리 숫자로 진행하여 정렬한다.
   *          > 각 자리 숫자에 대한 정렬을 반복 수행하여 최종적으로 정렬된 배열을 얻는다.
   *      - 안정적인 정렬 알고리즘: 안정적인 정렬 알고리즘이다.
   *          > 동일한 값의 상대적인 순서가 유지됩니다.
   *      - 시간 복잡도: 시간 복잡도는 입력 배열의 크기를 N, 키(key)의 길이(자리 수)를 K라고 할 때, O(KN)이다.
   *          > 각 자리 숫자에 대한 정렬을 반복하며, 각 반복마다 모든 요소를 확인해야 하기 때문
   *      - 추가적인 메모리 공간 필요: 입력 배열 외에도 추가적인 메모리 공간을 필요로 한다.
   *          > 정렬할 요소들을 각 자리 숫자에 따라 분배하여 임시 배열에 저장하고, 이후에 정렬된 순서로 다시 복사한다.
   *      - 제한된 키(key) 범위에 적용 가능: 기수 정렬은 키(key)의 범위가 제한되어 있는 경우에 가장 효율적으로 동작한다.
   *          > 키의 길이가 일정하고, 키의 가능한 값의 범위가 작을 때 적합한 정렬 알고리즘이라고 할 수 있다.
   *  - 힙 정렬 : Heap Sort
   *    특징
   *      - 힙 자료구조: 힙 자료구조를 사용한다. 힙은 완전 이진 트리의 형태를 가지며, 부모 노드와 자식 노드 사이에 우선순위를 정의하는 힙 조건을 만족한다.
   *          > 최대 힙(Max Heap)은 부모 노드가 자식 노드보다 큰 값을 가지는 힙
   *          > 최소 힙(Min Heap)은 부모 노드가 자식 노드보다 작은 값을 가지는 힙
   *      - 힙 구성: 정렬되지 않은 배열을 최대 힙 또는 최소 힙으로 구성한다.
   *                 배열의 요소를 힙에 삽입하면서 힙 조건을 유지한다.
   *                 최대 힙의 경우 요소를 삽입할 때 부모보다 큰 값이 삽입되도록 조정하고,
   *                 최소 힙의 경우는 부모보다 작은 값이 삽입되도록 조정한다.
   *      - 정렬: 구성된 힙에서 최대값(또는 최소값)을 루트 노드로 가져와서 배열의 끝으로 이동시킨다.
   *              그리고 힙의 크기를 1 감소시키고, 힙 조건을 유지하기 위해 루트 노드를 재조정한다.
   *              이 과정을 반복하여 정렬된 배열을 얻는다.
   *      - 안정적인 정렬 알고리즘: 힙 정렬은 안정적인 정렬 알고리즘은 아니다.
   *          > 동일한 값의 상대적인 순서가 유지되지 않을 수 있다.
   *      - 시간 복잡도: 시간 복잡도는 O(n log n)이다.
   *          > 힙을 구성하는 과정에서는 각 요소를 삽입하는 데 O(log n)의 시간이 걸리고, 정렬하는 단계에서는 모든 요소를 교환하는 데 O(n)의 시간이 걸린다.
   *      - 추가적인 메모리 공간 필요 없음: 힙 정렬은 입력 배열 외에 추가적인 메모리 공간을 필요로 하지 않는다.
   *          > 배열 내에서 위치를 교환하여 정렬을 수행한다.
   *
   */
  public static void swap(int[] arr, int idx1, int idx2) {
    int temp = arr[idx1];
    arr[idx1] = arr[idx2];
    arr[idx2] = temp;
  }

  // # 버블 정렬
  public static int[] bubbleSortBasic(int[] arr) {
    int n = arr.length - 1;

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n - i; j++) {
        if (arr[j] > arr[j + 1]) {
          int temp = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;
        }
      }
    }

    return arr;
  }

  public static int[] bubbleSort(int[] arr) {
    boolean isSwap;
    int n = arr.length - 1;

    for (int i = 0; i < n; i++) {
      isSwap = false;

      for (int j = 0; j < n - i; j++) {
        if (arr[j] > arr[j + 1]) {
          int temp = arr[j];
          arr[j] = arr[j + 1];
          arr[j + 1] = temp;

          isSwap = true;
        }
      }

      if (!isSwap) break;
    }

    return arr;
  }

  // # 삽입 정렬
  public static int[] insertionSort(int[] arr) {
    int currentVal;
    int i, j;

    for (i = 1; i < arr.length; i++){
      currentVal = arr[i];

      for (j = i - 1; j >= 0 && arr[j] > currentVal; j--) arr[j + 1] = arr[j];

      arr[j + 1] = currentVal;
    }

    return arr;
  }

  // # 합병(병합) 정렬
  public static int[] mergeSort(int[] arr) {
    if (arr.length <= 1) return arr;

    int mid = arr.length / 2;

    int[] left = mergeSort(Arrays.copyOfRange(arr, 0, mid));
    int[] right = mergeSort(Arrays.copyOfRange(arr, mid, arr.length));

    return merge(left, right);
  }

  public static int[] merge(int[] arrLeft, int[] arrRight) {
    int ll = arrLeft.length;
    int rl = arrRight.length;

    ArrayList<Integer> result = new ArrayList<>(ll + rl);

    int i = 0;
    int j = 0;

    while (i < ll && j < rl) {
      if (arrLeft[i] < arrRight[j]) {
        result.add(arrLeft[i]);
        i++;
      } else {
        result.add(arrRight[j]);
        j++;
      }
    }

    while (i < ll) {
      result.add(arrLeft[i]);
      i++;
    }

    while (j < rl) {
      result.add(arrRight[j]);
      j++;
    }

    return result.stream().mapToInt(Integer::valueOf).toArray();
  }

  // # 퀵 정렬
  public static int[] quickSort(int[] arr) {
    sort(arr, 0, arr.length - 1);

    return arr;
  }

  public static void sort(int[] arr, int left, int right) {
    int p = pivot(arr, left, right);

    if (left < p - 1) sort(arr, left, p - 1);
    if (right > p + 1) sort(arr, p + 1, right);
  }

  public static int pivot(int[] arr, int left, int right) {
    int pivot = arr[(left + right) / 2];

    while (left <= right) {
      while (arr[left] < pivot) left++;
      while (arr[right] > pivot) right--;

      if (left <= right) {
        swap(arr, left, right);

        left++;
        right--;
      }
    }

    return left;
  }

  // # 카운트 정렬
  public static int[] countSort(int[] arr) {
    int max = Arrays.stream(arr).max().orElse(0);
    int min = Arrays.stream(arr).min().orElse(0);
    int range = max - min + 1;

    int[] count = new int[range];
    int[] output = new int[arr.length];

    for (int v : arr) count[v - min]++;
    for (int i = 1; i < count.length; i++) count[i] += count[i - 1];

    for (int i = arr.length - 1; i >= 0; i--) {
      int p = arr[i] - min;

      output[count[p] - 1] = arr[i];
      count[p]--;
    }

    return output;
  }

  // # 기수 정렬
  public static void countSortWithExp(int[] arr, int exp) {
    int[] count = new int[10];
    int[] output = new int[arr.length];

    for (int v : arr) count[v / exp % 10]++;

    for (int i = 1; i < count.length; i++) count[i] += count[i - 1];

    for (int i = arr.length - 1; i >= 0; i--) {
      int p = (arr[i] / exp) % 10;

      output[count[p] - 1] = arr[i];
      count[p]--;
    }

    System.arraycopy(output, 0, arr, 0, arr.length);
  }

  public static int[] radixSort(int[] arr) {
    int max = Arrays.stream(arr).max().orElse(0);

    for (int exp = 1; max / exp > 0; exp *= 10) countSortWithExp(arr, exp);

    return arr;
  }

  // # 힙 정렬
  public static int[] heapSortWithPriorityQueue(int[] arr) {
    PriorityQueue<Integer> heap = new PriorityQueue<>();

    for (int j : arr) heap.add(j);
    for (int i = 0; i < arr.length; i++) arr[i] = heap.remove();

    return arr;
  }

  public static int[] heapSort(int[] arr) {
    if (arr.length <= 1) return arr;

    int n = arr.length;

    for (int i = n / 2 - 1; i >= 0; i--) heapify(arr, n, i);
    for (int i = n - 1; i >= 0; i--) {
      swap(arr, 0, i);
      heapify(arr, i, 0);
    }

    return arr;
  }

  private static void heapify(int[] arr, int n, int i) {
    int left = i * 2 + 1;
    int right = i * 2 + 2;

    int dir = i;

    // dir Max
    if (left < n && arr[left] > arr[dir]) dir = left;
    if (right < n && arr[right] > arr[dir]) dir = right;

    // dir Min
    // if (left < n && arr[left] < arr[dir]) dir = left;
    // if (right < n && arr[right] < arr[dir]) dir = right;

    if (dir != i) {
      swap(arr, i, dir);
      heapify(arr, n, dir);
    }
  }

  public static void main(String[] args) {
    System.out.println(Arrays.toString(bubbleSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    System.out.println(Arrays.toString(mergeSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    System.out.println(Arrays.toString(quickSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    System.out.println(Arrays.toString(insertionSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    System.out.println(Arrays.toString(countSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    System.out.println(Arrays.toString(radixSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    System.out.println(Arrays.toString(heapSortWithPriorityQueue(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    System.out.println(Arrays.toString(heapSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
  }
}
