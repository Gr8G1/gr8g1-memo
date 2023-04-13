package Gr8G1.prac.pojo.datastructure.collection.temp;

import java.util.*;

public class PrSet {
  /*
   * # Set<E>
   * Set 인터페이스 : Collection 인터페이스
   * HashSet 인터페이스 : Collection 인터페이스
   * TreeSet 인터페이스
   *  - Object clone(): 단순 복사본을 반환 (Shallow Copy)
   *  - Comparator<? super E>	comparator(): 요소 순서를 지정하는 비교자를 반환
   *  - Iterator<E>	descendingIterator(): 역순(내림차순) 반복자를 반환
   *  - E	ceiling(E e): 전달된 요소보다 크거나 같은 요소가 없다면 가장 가까운 작은 요소를 반환 (값이 없을시 null 반환)
   *  - E	floor(E e): 전달된 요소보다 작거나 같은 요소가 없다면 가장 가까운 큰 요소를 반환 (값이 없을시 null 반환)
   *  - E	first(): 첫 번째(가장 작은(낮은)) 요소를 반환
   *  - E	last(): 마지막(가장 큰(높은)) 요소를 반환
   *  - E	higher(E e): 전달된 요소보다 엄격하게 큰 최소 요소를 반환 (값이 없을시 null 반환)
   *  - E	lower(E e): 주어진 요소보다 엄격하게 작은 이 집합에서 가장 큰 요소를 반환하거나 null그러한 요소가 없는 경우 반환
   *  - E	pollFirst(): 첫 번째(가장 작은(낮은)) 요소를 검색 및 제거 (값이 없을시 null 반환)
   *  - E	pollLast(): 마지막(가장 큰(높은)) 요소를 검색 및 제거 (값이 없을시 null 반환)
   *  - NavigableSet<E>	descendingSet(): 역순(내림차순)으로 반환
   *  - SortedSet<E> subSet(E fromElement, E toElement),
   *  - NavigableSet<E>	subSet(E fromElement, boolean fromInclusive, E toElement, boolean toInclusive): 요소 범위가 fromElement에서 toElement까지인 부분 보기를 반환
   *  - SortedSet<E> tailSet(E fromElement),
   *  - NavigableSet<E>	tailSet(E fromElement, boolean inclusive): fromElement 보다 크거나 같은 부분을 반환
   *  - SortedSet<E> headSet(E toElement):
   *  - NavigableSet<E>	headSet(E toElement, boolean inclusive): 내부 요소가 엄격하게 toElement 미만인 이 집합의 부분 보기를 반환
   *
   */

  public static void main(String[] args) {
    // ~ HashSet
    HashSet<String> hash = new HashSet<>() {{
      add("Hello World!");
    }};

    hash.add("Java");
    hash.add("Java"); // 중복값 저장 불가
    hash.add("Javascript");

    System.out.println(hash.size());
    for (String s : hash) {
      System.out.println("HashSet: " + s); // 중복값 확인
    }

    // ~ TreeSet
    TreeSet<Integer> tree = new TreeSet<>();
    TreeSet<Integer> newTree = new TreeSet<>();

    tree.add(1);
    tree.add(3);
    tree.add(5);
    tree.add(7);
    tree.add(9);

    newTree.add(1);
    newTree.add(3);
    newTree.add(5);
    newTree.add(7);
    newTree.add(9);

    tree.addAll(newTree);

    System.out.println("tree.size(): " + tree.size());
    System.out.println("tree.first(): " + tree.first());
    System.out.println("tree.last(): " + tree.last());
    System.out.println("tree.higher(1): " + tree.higher(1));
    System.out.println("tree.subSet(2, 5): " + tree.subSet(2, 5));

    for (Integer s : tree) {
      System.out.println("TreeSet: " + s); // ASC(오름차순) 정렬
    }

    System.out.println(tree.descendingSet());
    System.out.println(tree.ceiling(4));
    System.out.println(tree.floor(4));
  }
}
