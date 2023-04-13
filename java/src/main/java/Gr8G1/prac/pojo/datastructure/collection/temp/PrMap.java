package Gr8G1.prac.pojo.datastructure.collection.temp;

import java.util.*;

public class PrMap {
  /*
   * # Map<K, V>
   * Map 인터페이스
   *  - 중첩 클래스
   *    - Map.Entry<K,V>: 맵 항목(키-값 쌍)
   *  - 추가
   *    - V put(K key, V value): 전달된 키에 대응하는 값으로 특정 값을 매핑
   *    - void putAll(Map<? extends K,? extends V> m): 전달된 맵 모든 값을 매핑
   *  - 검색
   *    - V get(Object key): 전달된 키에 대응하는 값을 반환 (값이 없을 시 null 반환)
   *    - int size(): 매핑된 총 개수를 반환
   *    - int	hashCode(): 해시 코드 값을 반환
   *    - boolean containsKey(Object key): 전달된 키를 포함하고 있는지 확인
   *    - boolean containsValue(Object value): 전달된 값에 해당하는 하나 이상의 키를 포함하고 있는지 확인
   *    - boolean isEmpty(): 매핑된 값 존재 유무 확인
   *    - Set<K> keySet(): 맵에 포함된 모든 키를 Set 객체로 반환
   *  - 삭제
   *    - void clear(): 해당 맵의 모든 매칭을 제거함
   *    - V remove(Object key): 전달된 키에 대응하는 매핑을 제거
   *    - boolean remove(Object key, Object value): 특정 값에 대응하는 키의 매핑을 제거
   *  - 제어
   *    - V replace(K key, V value): 전달된 키에 대응하는 값을 특정 값으로 대체함
   *    - boolean replace(K key, V oldValue, V newValue): 특정 값에 대응하는 전달된 키의 값을 새로운 값으로 대체
   *
   * HashMap 인터페이스
   *  - Set<Map.Entry<K,V>>	entrySet(): 맵에 포함된 모든 Entry를 Set으로 반환
   *
   * LinkedHashMap 인터페이스
   *
   * TreeMap 인터페이스
   *  - Object clone(): 단순 복사본을 반환(Shallow Copy)
   *  - Comparator<? super K>	comparator(): 비교자를 반환 (Natural order)
   *  - K ceilingKey(K key): 전달된 키보다 크거나 같은 키의 값을 반환 (값이 없을시 null 반환)
   *  - Map.Entry<K,V> ceilingEntry(K key): 전달된 키보다 크거나 같은 최소 키와 연결된 키-값 매핑을 반환 (값이 없을시 null 반환)
   *  - NavigableMap<K,V>	descendingMap(): 역순 맵 반환
   *  - NavigableSet<K>	descendingKeySet(): 역순 키셋 반환
   *  - K	firstKey(): 첫 번째(가장 작은(낮은)) 키를 반환
   *  - K	lastKey(): 마지막(가장 큰(높은)) 키를 반환
   *  - K	floorKey(K key): 주어진 키보다 작거나 같은 가장 큰 키를 반환  (값이 없을시 null 반환)
   *  - K	higherKey(K key): 주어진 키보다 엄격하게 큰 키를 반환 (값이 없을시 null 반환)
   *  - K	lowerKey(K key): 전달된 키보다 엄격하게 작은 키를 반환 (값이 없을시 null 반환)
   *  - Map.Entry<K,V> firstEntry(): 가장 작은 키와 연결된 키-값 매핑을 반환 (값이 없을시 null 반환)
   *  - Map.Entry<K,V>: lastEntry(): 가장 큰 키와 연결된 키-값 매핑을 반환 ()
   *  - Map.Entry<K,V> floorEntry(K key): 주어진 키보다 작거나 같은 가장 큰 키와 연결된 키-값 매핑을 반환 (값이 없을시 null 반환)
   *  - Map.Entry<K,V> higherEntry(K key): 전달된 키보다 엄격하게 큰 키와 연결된 키-값 매핑을 반환 (값이 없을시 null 반환)
   *  - Map.Entry<K,V>	lowerEntry(K key): 전달된 키보다 엄격하게 작은 키와 연관된 키-값 매핑을 반환 (값이 없을시 null 반환)
   *  - NavigableSet<K>	navigableKeySet(): 맵에 포함된 키의 Set 반환
   *  - Map.Entry<K,V> pollFirstEntry(): null이 맵에서 또는 맵이 비어 있는 경우 가장 작은 키와 연결된 키-값 매핑을 제거하고 반환합니다 .
   *  - Map.Entry<K,V>	pollLastEntry(): null이 맵에서 또는 맵이 비어 있는 경우 가장 큰 키와 연결된 키-값 매핑을 제거하고 반환합니다 .
   *  - SortedMap<K,V>	headMap(K toKey),
   *  - NavigableMap<K,V>	headMap(K toKey, boolean inclusive):  주어진 키보다 엄격하게 작은 맵의 부분을 반환
   *  - SortedMap<K,V> subMap(K fromKey, K toKey),
   *  - NavigableMap<K,V> subMap(K fromKey, boolean fromInclusive, K toKey, boolean toInclusive): fromKey에서 toKey까지의 맵의 부분을 반환
   *  - SortedMap<K,V> tailMap(K fromKey),
   *  - NavigableMap<K,V>	tailMap(K fromKey, boolean inclusive): fromKey보다 크거나 같은 맵의 부분을 반환.
   *
   */

  public static void main(String[] args) {
    // ~ HashMap
    HashMap<Character, Integer> cHashMap = new HashMap<>() {{
      put('A', 1);
      put('B', 1);
      put('C', 1);
    }};


    for (Character c: "Banana".toCharArray()) {
      /*
       * ! Extract side effect
       * ~: !cHashMap.containsKey(c) ? cHashMap.put(c, 1) : cHashMap.put(c, cHashMap.get(c) + 1);
       *  ->
       * ~: cHashMap.put(c, !cHashMap.containsKey(c) ? 1 : cHashMap.get(c) + 1);
       */
      if (!cHashMap.containsKey(c)) cHashMap.put(c, 1);
      else cHashMap.put(c, cHashMap.get(c) + 1);
    }

    HashMap<String, Integer> hashMap = new HashMap<>();

    System.out.println(cHashMap);

    hashMap.put("Java", 100);
    hashMap.put("Java", 300); // 중복키값 저장 불가
    hashMap.put("Javascript", 100);

    System.out.println("hashMap.size(): " + hashMap.size());
    System.out.println("hashMap.get(\"Java\"): " + hashMap.get("Java"));

    Set<String> keySet = hashMap.keySet();
    for (String key : keySet) {
      Integer value = hashMap.get(key);
      System.out.println("keySet - Key: " +key + ", Value: " + value);
    }

    Iterator<String> keyIt = keySet.iterator();
    while (keyIt.hasNext()) {
      System.out.println(keyIt.next());
    }

    Set<Map.Entry<String, Integer>> entrySet = hashMap.entrySet();
    for (Map.Entry<String, Integer> entries: entrySet) {
      System.out.println(entries);
      System.out.println("entrySet - Key: " + entries.getKey() + ", Value: " + entries.getValue());
    }

    Iterator<Map.Entry<String, Integer>> entryIt = entrySet.iterator();
    while (entryIt.hasNext()) {
      System.out.println(entryIt.next().getKey() + entryIt.next().getValue());
    }

    Comparator<Map.Entry<Integer, Integer>> comparator = new Comparator<>() {
      @Override
      public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
        return o1.getValue().compareTo(o2.getValue());
      }
    };

    // ~ Hashtable
    Hashtable<String, Integer> hashtable = new Hashtable<>(); // Hashtable <- table *소문자* 주의

    hashtable.put("Java", 100);
    hashtable.put("Java", 100); // 중복키값 저장 불가
    hashtable.put("Javascript", 100);

    Iterator<String> kit = hashtable.keySet().iterator();
    while (kit.hasNext()) {
      System.out.println(kit.next());
    }
  }
}
