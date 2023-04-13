## 쓰레드(Thread)
프로세스 내에서 독립적으로 실행될 수 있는 하나의 작업 흐름
쓰레드는 프로세스 내에서 메모리와 자원을 공유하면서 실행될 수 있으며, 여러 개의 쓰레드를 이용하여 병렬적으로 작업을 처리할 수 있다.

### Process
- 프로그램 실행시 OS에서 실행에 필요한 자원(메모리)를 할당받아 Process가 된다.
  - Process는 실행중인 프로그램을 말한다.
 - Process의 자원을 이용해 실제 작업을 수행하는 것이 *Thread* 이다.
 - 모든 Process는 최소 하나 이상의 Thread가 존재하고, Thread는 작업 수행을 위해 개별적인 자원(메모리) 공간(Call Stack)이 필요하다.
 - CPU 코어(core)가 한 번에 오직 하나의 작업만 수행할 수 있으므로, 동시에 처리되는 작업의 수는 코어의 개수와 일치한다.

### Thread
- 실행
  - start()를 호출시 Thread가 실행되지만 바로 실행되는것이 아닌 실행대기 상태로 머물다가 자신의 차례가 돌아오면 실행된다.
    > 실행 순서(차례)는 OS의 스케쥴러에 의해 결정된다.
  - 한번 실행 종료된 Thread는 재실행 할 수 없다.

- 실행제어
  - sleep: 일정 시간동안 Thread를 멈춘다.
    - 일시정지 상태가 된 Thread는 지정된 시간이 지나거나, interrupt가 호출되면, InterruptedException이 발생하며 일시정지 상태에서 실행대기 상태가 된다.
    - InterruptedException이 발생하기 때문에 try-catch문으로 예외를 처리해줘야 한다.
  - interrupt & interrupted : Thread의 작업을 취소한다.
    - interrupt: interrupted 상태값을 true로 변경
    - interrupted: 현재 Thread의 interrupted 상태값을 반환 후, 상태값을 false로 변경
      - isInterrupted: interrupted 상태값 반환
  - suspend, resume, stop: 교착상태(Dead-lock)를 일으키는 요소로 현재 deprecated 된 메소드들이다.
    - suspend: Thread를 멈추게 한다.
    - resume: Thread를 싱행대기 상태로 변경한다.
    - stop: 호출 즉시 Thread를 종료한다.
  - yield: 다른 Thread에게 자신의 자원(메모리) 실행(사용)시간을 양도한다.
  - join: Thread 자신의 작업을 잠시 멈추고 다른 Thread의 작업 수행을 지정 시간만큼 기다린다.
    - 시간을 지정하지 않는다면 다른 Thread의 작업 수행이 끝날때까지 기다린다.
    - InterruptedException이 발생하기 때문에 try-catch문으로 예외를 처리해줘야 한다.
  - wait, notify, notifyAll: 임계영역과 잠금 제어
    - wait: 객체의 lock을 풀고 해당 객체의 Waiting-pool로 들어간다.
    - notify: Waiting-pool 대기중인 Thread중 하나를 깨운다.
    - notifyAll: Waiting-pool 대기중인 Thread를 모두 깨운다.
      - waiting-pool은 객체마다 존재하므로 notifyAll을 호출해도 모든 Thread에게 통보되는것은 아니다.

- 동기화
  - 개별 Thread의 작업을 다른 Thread의 간섭으로부터 보호하는것

- 임계영역(critical section)과 잠금(lock)
  - 공유 자원을 사용하는 영역을 임계영역으로 지정하고, 공유 데이터(객체)의 lock을 획득한 단 하나의 Thread의 작업 수행을 보장한다.  
    lock을 획득한 Thread가 임계영역 내의 모든 작업을 수행하고 lock을 반납하면 다른 Thread의 접근(작업 수행)을 가능하게 한다.
  ```java
  // ReentrantLock 사용 예시
  public class Counter {
  private int count;
  private Lock lock;
  
      public Counter() {
          count = 0;
          lock = new ReentrantLock();
      }
  
      public void increment() {
          lock.lock();
          try {
              count++;
          } finally {
              lock.unlock();
          }
      }
  
      public void decrement() {
          lock.lock();
          try {
              count--;
          } finally {
              lock.unlock();
          }
      }
  
      public int getCount() {
          lock.lock();
          try {
              return count;
          } finally {
              lock.unlock();
          }
      }
  }
  ```
- **Synchronized** (임계 영역 설정 제어자)
  - 메소드 전체 또는 특정 역역을 임계 영역으로 설정
    - synchronized 메소드() {}
    - synchronized(참조변수) {}

### 동시성 문제
여러 쓰레드가 동시에 공유 자원에 접근하면서 발생하는 문제를 동시성 문제라 한다.  

- 동시성 세 가지 종류
  1. 경쟁 상태(Race Condition)
     - 여러 쓰레드가 동시에 공유 자원에 접근하여 자원을 변경하거나 읽는 경우 발생한다. 
       이때, 어떤 쓰레드가 먼저 자원을 변경했는지에 따라서 결과가 달라질 수 있으며, 예측할 수 없는 결과를 초래할 수 있다.
  2. 교착 상태(Deadlock)
     - 여러 쓰레드가 서로가 가지고 있는 자원을 점유하고 있으면서, 서로가 가진 자원을 요청하는 상황에서 발생한다.
       이때, 각 쓰레드가 다른 쓰레드가 점유한 자원을 기다리면서, 무한정 대기하는 상황이 발생한다.
  3. 일관성 없는 상태(Inconsistent State)
     - 여러 쓰레드가 동시에 공유 자원에 접근하여 자원의 일부를 변경하거나 읽는 경우 발생한다. 
       이때, 어떤 쓰레드가 자원을 변경하는 동안 다른 쓰레드가 자원을 읽을 수 있으며, 이로 인해 자원의 일부분만 변경되는 경우가 발생할 수 있다.

### 동시성 문제 해결
동기화(synchronization)를 이용하여 쓰레드 동시성 문제를 해결할 수 있다. 
여러 쓰레드가 동시에 공유 자원에 접근하는 것을 막아서, 오직 하나의 쓰레드만이 자원에 접근할 수 있도록 한다. 
이때 synchronized 키워드를 이용하여 메소드나 블록을 동기화할 수 있다. 

```java
// synchronized 예시
public class Counter {
    private int count;

    public synchronized void increment() {
        count++;
    }

    public synchronized void decrement() {
        count--;
    }

    public synchronized int getCount() {
        return count;
    }
    
    // 쓰레드는 하나의 메소드가 실행되는 동안 다른 메소드를 실행할 수 없다.
}
```
