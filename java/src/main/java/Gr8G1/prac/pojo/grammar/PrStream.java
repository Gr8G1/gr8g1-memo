package Gr8G1.prac.pojo.grammar;

import java.util.*;
import java.util.stream.*;

public class PrStream {
  /*
   * # Stream
   *
   * Stream
   *  - 다양한 데이터를 표준화된 방법으로 다루기 위한 라이브러리
   *
   * 특징
   *  - 데이터를 변경하지 않는다.
   *  - *1회성 사용*: (스트림 종료 단계 이후 재생성 필요)
   *  - 지연 연산, 병렬 실행이 가능하다.
   *
   * 단계
   *  - 생성(자료)(generate): 컬렌셕 타입(Collection, List, Set)의 스트림 인스턴스 생성
   *  - 중간(가공)(intermediate): 연산(filterin, mapping) 처리중인 스트림 반환
   *  - 최종(출력)(terminal): 스트림 최종 결과 반환
   *
   */

  /*
   * # Stream<T>
   *
   *  - public Stream<T> filter(Predicate<? super T> predicate): 조건(람다식) 충족값 스트림 생성
   *  - public <R> Stream<R> map(Function<? super T, ? extends R> mapper): T 입력 R타입 요소로 변환후 스트림 생성
   *  - public IntStream mapToInt(ToIntFunction<? super T> mapper),
   *  - public LongStream mapToLong(ToLongFunction<? super T> mapper),
   *  - public DoubleStream mapToDouble(ToDoubleFunction<? super T> mapper): map Type 형 변환
   *  - public <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> mapper): T타입 요소를 1:N의 R타입 요소로 변환후 스트림 생성
   *  - public IntStream flatMapToInt(Function<? super T, ? extends IntStream> mapper),
   *  - public LongStream flatMapToLong(Function<? super T, ? extends LongStream> mapper),
   *  - public DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> mapper): flatMap Type 형변환
   *  - public Stream<T> distinct(): Stream의 중복값 제거
   *  - public Stream<T> sorted(): Stream의 정렬
   *  - public Stream<T> sorted(Comparator<? super T> comparator):
   *  - public Stream<T> peek(Consumer<? super T> action): T타입 요소에 대응하는 작업 수행
   *  - public Stream<T> limit(long maxSize): 주어진 maxSize 대응 Stream 생성
   *  - public Stream<T> skip(long n): 주어진 n개의 요소를 제외한 Stream 생성
   *  - public void forEach(Consumer<? super T> action): Stream 의 각 요소에 지정된 작업 수행
   *  - public void forEachOrdered(Consumer<? super T> action):
   *  - public Object[] toArray(): Stream 의 모든 요소를 배열로 반환
   *  - public <A> A[] toArray(IntFunction<A[]> generator):
   *  - public T reduce(T identity, BinaryOperator<T> accumulator): Stream 요소 누산 실행 결과 반환
   *  - public Optional<T> reduce(BinaryOperator<T> accumulator):
   *  - public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator, BinaryOperator<U> combiner):
   *  - public <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super String> accumulator, BiConsumer<R, R> combiner):
   *  - public <R, A> R collect(Collector<? super String, A, R> collector):  Stream 요소 수집 후 반환
   *  - public Optional<T> min(Comparator<? super T> comparator): Stream 요소의 최대 값 반환
   *  - public Optional<T> max(Comparator<? super T> comparator): Stream 요소의 최소 값 반환
   *  - public long count(): Stream 의 요소 개수 반환
   *  - public boolean anyMatch(Predicate<? super T> predicate): Stream의 값이 하나라도 만족하는지의 결과 반환
   *  - public boolean allMatch(Predicate<? super T> predicate): Stream의 값이 모두 만족하는지의 결과 반환
   *  - public boolean noneMatch(Predicate<? super T> predicate): Stream 의 값이 하나라도 만족하지않는지의 결과 반환
   *  - public Optional<T> findFirst(): Stream 첫 번째 요소 반환
   *  - public Optional<T> findAny(): Stream 랜덤 요소 반환
   *  - public Iterator<T> iterator(): Stream Iterator 반환
   *  - public Spliterator<T> spliterator():
   *  - public Stream<T> sequential():
   *  - public Stream<T> parallel(): Stream 병렬 처리
   *  - public Stream<T> unordered():
   *  - public Stream<T> onClose(Runnable closeHandler):
   *  - public void close(): Stream 종료
   *  - public boolean isParallel(): 병렬 Stream 여부 확인
   *
   */
  public static void main(String[] args) {
    // # 생성 & 출력
    String[] strArr = {"3", "2", "1"};
    Stream<String> sS = Arrays.stream(strArr);
    // Or: Stream<String> sS = Arrays.stream(new String[] {"3", "2", "1"});
    sS.forEach(System.out::println);
    // Or: for (Iterator<String> it = sS.iterator(); it.hasNext(); ) System.out.println(it.next());

    Integer[] intArr = {1, 2, 3};
    Stream<Integer> iS = Arrays.stream(intArr);
    // Or: Stream<Integer> iS = Arrays.stream(new Integer[]{ 1, 2, 3 });
    iS.forEach(System.out::println);
    // Or: for (Iterator<Integer> it = iS.iterator(); it.hasNext(); ) System.out.println(it.next());

    // iterate
    Stream<Integer> itStream = Stream.iterate(0, (n) -> n + 1).limit(5);
    System.out.println(Arrays.toString(itStream.toArray()));

    // IntStream, LongStream
    IntStream intS = IntStream.of( 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    // Or: IntStream intS = IntStream.range(0, 10); // 범위: 0 <= x < 10
    System.out.println(intS.reduce(0, Integer::sum));
    // Or: System.out.println(intS.sum());

    // DoubleStream
    DoubleStream dS = DoubleStream.generate(() -> 0.0).map(n -> n * (Math.random() * 45)).limit(3);
    System.out.println(dS.average().orElse(0.0));

    // # 중간(가공) & 최종
    int[] strToNums = Arrays.stream(strArr)
        .mapToInt(Integer::parseInt)
        .toArray();

    String numsToStr = Arrays.stream(intArr)
        .map(Object::toString)
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.joining(", ", "<", ">"));

    // # concat & filter & distinct & sotred
    Stream<String> cS = Stream.concat(Stream.of("A", "P", "P", "L", "E"), Stream.of("B", "A", "N", "A", "N", "A"));
    List<String> fcS = cS.filter(n -> n.startsWith("A") || n.endsWith("B"))
        .distinct()
        .sorted(Comparator.reverseOrder())
        .collect(Collectors.toList());

    System.out.println(fcS);
  }
}
