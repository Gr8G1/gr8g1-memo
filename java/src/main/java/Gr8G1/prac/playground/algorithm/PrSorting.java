package Gr8G1.prac.playground.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class PrSorting {
  /*
   * # 정렬 알고리즘
   *  - 버블 정렬
   *  - 삽입 정렬
   *  - 합병(병합) 정렬
   *  - 퀵 정렬
   *  - 카운트 정렬
   *  - 기수 정렬
   *  - 힙 정렬
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
    // System.out.println(Arrays.toString(bubbleSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    // System.out.println(Arrays.toString(mergeSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    // System.out.println(Arrays.toString(quickSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    // System.out.println(Arrays.toString(insertionSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    // System.out.println(Arrays.toString(countSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    // System.out.println(Arrays.toString(radixSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    // System.out.println(Arrays.toString(heapSortWithPriorityQueue(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
    System.out.println(Arrays.toString(heapSort(new int[] {0, 9, 8, 7, 6, 1, 2, 3, 4, 5})));
  }
}
