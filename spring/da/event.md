## Application Event
어플리케이션 내부에서 발생하는 이벤트를 일컫는다. 
이벤트는 일반적으로 Spring의 ApplicationContext에서 발생하며, 이벤트를 받는 객체인 ApplicationListener 인터페이스를 구현한다. 
Application Event는 일반적으로 다음과 같은 상황에서 사용된다.

1. 서비스에서 특정 작업이 완료되었을 때, 다른 서비스나 시스템에 알림을 보내는 경우
2. 이벤트 로깅 및 감시
3. 서비스 레이어와 프레젠테이션 레이어 간의 통신

Spring에서 Application Event를 발생시키려면, ApplicationEventPublisher 인터페이스를 구현해야된다. 
이 인터페이스를 구현한 객체를 통해 Application Event를 발생시킬 수 있다. 
또한, 이벤트 핸들러를 등록하려면 ApplicationListener 인터페이스를 구현해야된다. 
ApplicationEventPublisher를 사용하여 이벤트를 발생시키면, Spring은 이벤트를 받아들이고 이벤트 핸들러를 호출한다.

### 활용 예시
```java
@Service
public class MyService {

  private final ApplicationEventPublisher applicationEventPublisher;

  public MyService(ApplicationEventPublisher applicationEventPublisher) {
      this.applicationEventPublisher = applicationEventPublisher;
  }

  public void doSomething() {
      // 이벤트 발생
      MyEvent myEvent = new MyEvent(this);
      applicationEventPublisher.publishEvent(myEvent);
  }
}

// 이벤트 클래스
public class MyEvent extends ApplicationEvent {
  public MyEvent(Object source) {
      super(source);
  }
}

// 이벤트 핸들러
@Component
public class MyEventHandler implements ApplicationListener<MyEvent> {

  @Override
  public void onApplicationEvent(MyEvent event) {
      // 이벤트 처리 로직
  }
}
```
