# Q&A

- 원본 코드가 JS 였다고 해도 모든걸 다 static 으로 개발하는게 괜찮은걸까?
- Java에서는 nested function 을 지원하지 않아 문제가 생김
  - lambda 로 정의할 수도 있겠지만 input 이 3개 이상이 될 경우 커스텀 function 정의를 해야하므로

```java
Function<Performance, Play> playFor = (aPerformance) -> plays.get(aPerformance.playID);
//..
Function<Performance, Performance> enrichPerformance = (aPerformance) -> {
    Performance result = new Performance(aPerformance.playID, aPerformance.audience, null);
    result.play = playFor(result);

    return result;
};
```

- 토비님의 가이드 대로 생성자에서 미리 멤버 변수를 받아놓는 방식으로 진행
  - 다만 이렇게 진행하니까 파라미터를 넘겨주는 부분을 줄이는 리팩토링 하거나, nested function 이 local var 로 bind 되어있는 부분을 리팩토링할 때는 차이가 발생한다.

```java
class CreateStatementData {
    Map<String, Play> plays;
    //..
}
```

- Java 에서는 inner class 를 정의해서 사용하려니 static으로 정의하지 않으면 아래 에러가 발생한다.

```java
@AllArgsConstructor
class CreateStatementData {
    Map<String, Play> plays;

    @Data
    class Invoice {
        String customer;
        List<Performance> performances;
    }
    //..
}
```

```java
[ERROR]   StatementTest.setUp:23 » InvalidDefinition Cannot construct instance of `chapter01.CreateStatementData$Invoice`: non-static inner classes like this can only by instantiated using default, no-argument constructor
 at [Source: (BufferedInputStream); line: 3, column: 5] (through reference chain: java.util.ArrayList[0])
```
