package Gr8G1.prac.pojo.datastructure.collection.temp;

import java.util.*;

public class PrList {
  /*
   * # List<E>
   * List 인터페이스: (Collection 인터페이스 확인)
   *  - 추가
   *    - void add(E e): 전달된 요소를 List의 마지막 요소로 추가
   *    - void add(int index, Object el): 전달된 위치에 요소 추가
   *    - boolean addAll(int index, Collection c): 주어진 인덱스에 컬렉션의 모든 요소 추가
   *    - Object set(int index, Object el): 전달된 위치에 요소 저장
   *  - 삭제
   *    - Object remove(int index): 전달된 인덱스에 저장된 요소 삭제, 삭제된 요소 반환
   *    - boolean remove(Object o): 전달된 요소 삭제
   *  - 검색
   *    - Object: get(int index): 전달된 인덱스 저장된 요소를 반환
   *    - int	indexOf(Object o): (정방향 탐색) 주어진 위치의 요소를 확인 (결과에따라 위치값 반환 또는 -1)
   *    - int lastIndexOf(Object o): (역방향 탐색) 주어진 위치의 요소를 확인 (결과에따라 위치값 반환 또는 -1)
   *    - List subList(int fromIndex, int toIndex): 주어진 fromIndex부터 toIndex에 있는 (배타적) 부분 목록을 반환
   *    - ListIterator listIterator(): List를 탐색할 수 있는 ListIterator 반환
   *    - ListIterator listIterator(int index): 주어진 index부터 List를 탐색할 수 있는 ListIterator 반환
   *  - 정렬
   *    - void sort(Comparator c): 주어진 비교자(comparator)로 List를 정렬
   *
   * ArrayList 인터페이스
   *  - Object clone(): 단순 복사본을 반환 (Shallow Copy)
   *  - void ensureCapacity(int minCapacity): 주어진값으로 최소 용량(size) 지정 (필요한 경우 이 ArrayList 인스턴스 의 용량을 늘린다)
   *  - void trimToSize(): 전체 용량을 목록의 현재 크기로 줄인다.
   *
   * LinkedList 인터페이스
   *  - Iterator<E>	descendingIterator(): 역순(내림차순) 반복자를 반환
   *  - void addFirst(E e): 전달된 요소를 첫번째 위치에 삽입
   *  - void addLast(E e): 전달된 요소를 마지막 위치에 삽입
   *  - E element(): 헤드(첫 번째 요소)를 검색 (제거 X)
   *  - E getFirst(): 첫 번째 요소 반환
   *  - E getLast(): 마지막 요소 반환
   *  - boolean offer(E e): 지정된 요소를 꼬리(마지막 요소)로 추가
   *  - boolean	offerFirst(E e): 지정된 요소를 첫번째 위치에 삽입
   *  - boolean	offerLast(E e): 지정된 요소를 마지막 위치에 삽입
   *  - E	peek(): 헤드(첫 번째 요소)를 검색 (제거 X)
   *  - E	peekFirst(): 첫 번째 요소를 검색 (제거 X) 목록이 비어있을시 null 반환
   *  - E	peekLast(): 마지막 요소를 검색 (제거 X) 목록이 비어있을시 null 반환
   *  - E	poll(): 첫 번째 요소를 검색 (제거O)
   *  - E	pollFirst(): 첫 번째 요소를 검색 (제거 O) 목록이 비어있을시 null 반환
   *  - E	pollLast(): 마지막 요소를 검색 (제거 O) 목록이 비어있을시 null 반환
   *  - E	pop(): 요소를 꺼낸다. (stack)
   *  - void push(E e): 요소를 추가한다 (stack)
   *
   *
   * ArrayList VS LinkedList
   *  - ArrayList: *(인덱스 순차 나열)* - 검색(O(n)), 순회(O(n)), 나열(O(n)) 유리
   *  - LinkedList: *(Prev, Next 연결형)* - 추가(O(1)) 삭제(O(1)) 유리
   *
   */

  public static void main(String[] args) {
    // ~ List
    List<Integer> list = Arrays.asList(1, 2, 3, 4, 5); // 5의 저장 공간 선언 (한번 지정된 공간을 늘릴 수 없다.)
    // list.add(0, 5); // RuntimeError: UnsupportedOperationException -> 저장 공간을 늘릴 수 없다.
    list.set(0, 5); // OK

    for (Integer l: list) {
      System.out.println("List: " + l);
    }

    // ~ ArrayList
    ArrayList<Integer> a10List = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)); // initialCapacity 없이 선언하면 10의 기본 저장 공간을 갖는다.

    while (a10List.size() < 10) {
      a10List.add(a10List.size());
    }

    for (Integer l: a10List) {
      System.out.println("For loop: " + l);
    }

    for (Iterator<Integer> it = a10List.iterator(); it.hasNext();) {
      Integer l = it.next();
      System.out.println("Iterator loop: " + l);
    }

    System.out.println(a10List);
    a10List.clear();
    System.out.println(a10List);

    // ~ LinkedList
    LinkedList<Integer> lkList = new LinkedList<>();

    while (lkList.size() < 10) {
      lkList.add(lkList.size());
    }

    for (Integer l: lkList) {
      System.out.println("For loop: " + l);
    }

    for (Iterator<Integer> it = lkList.iterator(); it.hasNext();) {
      Integer l = it.next();
      System.out.println("Iterator loop: " + l);
    }
  }
}
