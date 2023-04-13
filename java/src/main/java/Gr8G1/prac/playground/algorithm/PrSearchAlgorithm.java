package Gr8G1.prac.playground.algorithm;

public class PrSearchAlgorithm {
  /*
   * # 검색(탐색) 알고리즘
   *
   * 탐색 알고리즘들
   * 1. 선형 탐색(Linear Search)
   *  - 탐색 범위(요소)가 정렬된 상태에서 순차적으로 하나씩 확인하는 탐색 방법
   * 2. 이진 탐색(Binary Search)
   *  - 탐색 범위(요소)가 정렬된 상태에서 분할 정복(Divide-and-conquer) 기법을 이용하여 탐색하는 방법
   *    > 분할 정복: 특정 영역(위치: 지정 가능한 범위)를 선택 후 1/2 분할하여 조건에 따라 범위를 탐색 범위를 축소해 나가는 방식
   * 3. 해시 탐색(Hash Search)
   *  - 해시 함수를 통한 색인이 처리된 자료구조를 활용하여 빠른 삽입,삭제,검색을 목적으로 활용되는 탐색방법
   *
   * 키워드
   *  - Divide-and-conquer algorithm
   *  - Binary Tree vs Binary Search Tree
   *  - Binary Search Algorithm vs Binary Search Tree
   */
  public static int binarySearch(int[] arr, int target) {
    int low = 0;
    int high = arr.length - 1;
    int mid;

    while(low <= high) {
      mid = (low + high) / 2;

      if (arr[mid] == target) return mid;
      else if (arr[mid] > target) high = mid - 1;
      else low = mid + 1;
    }

    return -1;
  }

  public static int binarySearchRecursive(int[] arr, int target, int low, int high) {
    if (low > high) return -1;

    int mid = (low + high) / 2;

    if (arr[mid] == target) return mid;
    else if (arr[mid] > target) return binarySearchRecursive(arr, target, low, mid-1);
    else return binarySearchRecursive(arr, target, mid+1, high);
  }

  public static void main(String[] args) {
    System.out.println(binarySearch(new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, 10));
  }
}
