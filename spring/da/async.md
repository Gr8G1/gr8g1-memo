## Async
스프링에서 비동기적인 방식으로 작업을 처리할 수 있도록 지원하는 기능

### 특징
- 비동기적인 방식으로 작업을 처리할 수 있다.
- 별도의 쓰레드에서 작업을 처리하기 때문에, 블로킹 작업이 있다면 이를 별도의 쓰레드에서 실행할 수 있다.
- 비동기적으로 실행된 작업의 결과를 Future 객체를 통해 받을 수 있다.
- 다양한 설정 옵션을 제공하여 비동기 작업을 세밀하게 제어할 수 있다.

### 비동기 활성화
1. @EnableAsync 
   - @EnableAsync 사용하여 비동기적인 작업을 활성화한다.
2. @Async 
   - @Async 사용하여 비동기적으로 실행할 메서드에 적용한다.
   - 이 어노테이션을 적용하면 해당 메서드는 별도의 쓰레드에서 실행되며, 메서드가 반환하는 Future 객체를 통해 비동기 작업의 결과를 받을 수 있다.

### Future<?>
Future는 자바 5부터 추가된 인터페이스 중 하나로, 비동기 처리를 위해 도입된 인터페이스이다. 
Future는 결과값을 가져오기 위한 get() 메소드와 작업이 완료되었는지 확인하기 위한 isDone() 메소드를 제공한다.
Future를 사용하면 작업이 완료되지 않은 상태에서 결과값을 기다리는 동기 방식이 아닌, 작업 완료 후 결과값을 가져오는 비동기 방식으로 처리할 수 있다. 
또한 여러 작업을 동시에 처리하고 결과값을 받아오는 것도 가능하다.

@Async 어노테이션을 이용해 비동기 처리를 진행할 시, CompletableFuture 클래스를 사용해 Future를 대체할 수 있다. 
CompletableFuture는 Future의 기능을 확장하고 개선한 클래스로, 자체적으로 콜백 기능도 제공한다. 
또한, Future와는 달리 CompletableFuture는 비동기 작업을 직접 실행할 수 있어서, 작업의 결과값을 기다리지 않고 다른 작업을 수행할 수 있다.

#### Future 사용 예시
```java
@Service
public class AsyncService {

    @Async
    public Future<String> asyncMethodWithReturnType() {
        System.out.println("Start executing asyncMethodWithReturnType()");
        try {
            Thread.sleep(5000); // delay for 5 seconds to simulate a long-running task
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done executing asyncMethodWithReturnType()");
        return new AsyncResult<>("Hello, World!"); // return a Future<String> with a result of "Hello, World!"
    }

    @Async
    public void asyncMethodWithoutReturnType() {
        System.out.println("Start executing asyncMethodWithoutReturnType()");
        try {
            Thread.sleep(3000); // delay for 3 seconds to simulate a long-running task
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Done executing asyncMethodWithoutReturnType()");
    }
}
```

#### @Async와 @Pointcut 조합 예시
```java
@Component
public class MyService {

    @Async
    @MyCustomAnnotation
    public Future<String> asyncMethod() {
        // 비동기적으로 실행될 코드
        return new AsyncResult<>("asyncMethod 결과");
    }

    @Async
    public void asyncMethod2() {
        // 비동기적으로 실행될 코드
    }

}

@Aspect
@Component
public class MyAspect {

    @Pointcut("@annotation(com.example.MyCustomAnnotation)")
    public void myCustomAnnotationPointcut() {}

    @AfterReturning(pointcut = "myCustomAnnotationPointcut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        if (result instanceof Future) {
            try {
                ((Future) result).get();
            } catch (InterruptedException | ExecutionException e) {
                // 예외 처리
            }
        }
    }
}
```
