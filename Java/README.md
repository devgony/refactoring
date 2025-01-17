# Q&A

- JS 를 따라한답시고 모든걸 다 static 으로 개발하는게 맞나?
- Java에서는 nested function 을 지원하지 않아 문제가 생김
- lambda 로 정의할 수도 있겠지만 input 이 3개 이상이 될 경우 커스텀 function 정의를 해야하므로

```java
 Function<Performance, Play> playFor = (aPerformance) -> plays.get(aPerformance.playID);
```

- 토비님의 가이드 대로 생성자에서 미리 멤버 변수를 받아놓는 방식으로 진행
  - 다만 이렇게 진행하니까 파라미터를 넘겨주는 부분을 줄이는 리팩토링 하거나, nested function 이 local var 로 bind 되어있는 부분을 리팩토링할 때는 차이가 발생한다.

```java
Function<Performance, Performance> enrichPerformance = (aPerformance) -> {
    Performance result = new Performance(aPerformance.playID, aPerformance.audience, null);
    result.play = playFor(result);

    return result;
};
```
