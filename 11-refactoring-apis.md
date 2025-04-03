> Refactoring APIs

- 모듈과 그 기능은 소프트웨어의 구성 요소이고 API 는 이를 서로 연결하는 관절이다
- API 를 쉽게 사용할 수 있도록 리팩터링 해야한다
- 좋은 API 는 immutable 과 mutable 을 구분. 만약 결합되어있다면 `Separate Query from Modifier`
- `Parameterize Function` 을 통해 값에 따라 달라지는 함수들을 통합
  - 하지만 일부 파라미터는 전혀 다른 동작에 대한것이므로 `Remove Flag Argument` 수행 필요
- 불필요하게 분산된 Data structure 는 `Preserve Whole Object` 로 통합
- 어느 부분을 파라미터로 받고 어느 부분을 함수 호출로 해결 할 지는 `Replace Parameter with Query` 와 `Replace Query with Parameter` 를 반복해보며 개선
- class 는 모듈의 가장 흔한 형태이고, 가능하면 `Remove Setting Method` 를 통해 최대한 immutable 하게 유지
- 단순 생성자보다 더 많은 유연성이 필요한 경우 `Replace Constructor with Factory Function`
- 많은 데이터를 전달하는 복잡한 함수 리팩토링 경우 `Replace Function with Command` 으로 `Extract Function` 을 좀더 쉽게 도움
- 함수가 충분히 단순화 되었다면: `Replace Command with Function`

1. [Separate Query from Modifier](11-refactoring-apis/01-separate-query-from-modifier.md)

- 값을 반환하지만 side effect 가 있는 함수를 발견하면 query 와 modifier 를 분리한다
- ⁉️ return 문을 제거 시 테스트가 실패하는 예제가 있었다

```diff
  function alertForMiscreant (people) {
    for (const p of people) {
      if (p === "Don") {
        setOffAlarms();
-       return;
      }
      if (p === "John") {
        setOffAlarms();
-       return;
      }
    }
-   return;
  }
```

```java
class SeparateQueryFromModifierTest {
    @Test
    void client1() {
        List<String> people = Arrays.asList("Don", "John");

        try (MockedStatic<SeparateQueryFromModifier> mockedStatic = Mockito
                .mockStatic(SeparateQueryFromModifier.class)) {
            mockedStatic.when(() -> SeparateQueryFromModifier.alertForMiscreant(people)).thenCallRealMethod();
            mockedStatic.when(() -> SeparateQueryFromModifier.findMiscreant(people)).thenCallRealMethod();
            String actual = findMiscreant(people);
            alertForMiscreant(people);

            assertThat(actual).isEqualTo("Don");
            mockedStatic.verify(() -> SeparateQueryFromModifier.setOffAlarms(), Mockito.times(1)); // ⁉️ shouyld be 1 but got 2
        }
    }
}
```

2. [Parameterize Function](11-refactoring-apis/02-parameterize-function.md)

- 유사한 로직의 함수들이 있는 경우 파라미터로 다양한 경우를 처리

3. [Remove Flag Argument](11-refactoring-apis/03-remove-flag-argument.md)

- flag arguement: 인자를 bool 로 사용 + 제어 흐름에 영향을 주는 용도로 활용
- flag 를 제거하므로서 중요한 로직과 일반 로직의 구분이 용이해진다

- 인자를 데이터로 사용하는 호출자가 없는 경우에는 visibility를 제한한다
  - 💡 아.. visibility 가 private 같은 키워드가 아니라 wrapper 함수로 감추라는 거구나

```js
function rushDeliveryDate(anOrder) {
  return deliveryDate(anOrder, true);
}
function regularDeliveryDate(anOrder) {
  return deliveryDate(anOrder, false);
}
```

4. [Preserve Whole Object](11-refactoring-apis/04-preserve-whole-object.md)

- 여러 인자를 필요로 하는 함수는 데이터를 묶어서 전달하는 것이 향후 변경에 유연하다
  - 함수 시그니처를 변경하지 않아도 된다
- 분리되어있으면 로직이 중복되는 경우가 많다
- 다른 모듈일 경우는 객체 전체의 의존성이 생기기 때문에 지양해야 할 수 있다
- 로직을 객체 밖으로 꺼내서 무언가를 한다면 로직 전체가 그 객체 안으로 들어가야 할 수 있다

5. [Replace Parameter with Query](11-refactoring-apis/05-replace-parameter-with-query.md)

- 그 때 마다 derived value 를 구하는 것
- 함수가 자체적으로 쉽게 결정할 수 있는 값이라면 인자보다는 body 에서 직접 구하는게 복잡성을 줄인다
- 인자를 제거하면서 값결정에 대한 책임을 요청자에서 함수 body 로 넘기는 것이기 때문에
  - 책임을 함수 내부 구현으로 넘기는게 적합할 경우만 사용
- 적용예외
  - 인자 제거로 인해 함수body 에 원치않는 종속성이 추가되는 경우
  - 나중에 제거하려는 수신자 객체 내의 무언가에 엑세스 해야 할 때
- 좋은경우
  - 다른 인자만으로 쿼리하여 얻을 수 있는 값일 때
- 매개 변수를 mutable global var 로 변경하면 참조투명성이 낮아지므로 주의

6. [Replace Query with Parameter](11-refactoring-apis/06-replace-query-with-parameter.md)

- global var, 다른모듈로 이동 예정인 개체 등을 참조하는 것은 불편하다
  - 내부 참조를 인수로 변경
  - 책임을 호출자쪽으로 이동
- 대부분 의존성 관계 변경을 원할 때 발생

7. [Remove Setting Method](11-refactoring-apis/07-remove-setting-method.md)

- immutable 을 위해 setter 제거

8. [Replace Constructor with Factory Function](11-refactoring-apis/08-replace-constructor-with-factory-function.md)

- Factory 함수는 제약조건 없이 내부에서 생성자를 호출하거나 자유롭게 활용 가능

9. [Replace Function with Command](11-refactoring-apis/09-replace-function-with-command.md)

- Command object (or just Command): 하나의 메서드만을 가지며, 그 메서드의 실행이 유일한 목적인 객체
- 일반 함수보다 유연성을 제공
- 하지만 풍부한 유연성은 복잡성을 동반함
  - 95% 의 경우에는 일급함수를 쓰는게 나음
  - 훨씬 더 복잡한 기능을 제공해야 할 경우만 Command 를 사용

10. [Replace Command with Function](11-refactoring-apis/10-replace-command-with-function.md)

- Command 가 제공하는 가치보다 복잡도가 더 높다면 일반 함수로 전환

11. [Return Modified Value](11-refactoring-apis/11-return-modified-value.md)

- mutate 의 흐름을 명확하게 보여주는 것이 좋다
- 명확한 단일 값 리턴하는 함수 에 적용하기 좋다

12. [Replace Error code with Exception](11-refactoring-apis/12-replace-error-code-with-exception.md)

- 에러코드를 외우거나 콜스택에 넣어서 처리하는 복잡함 없이 Exception 을 던지는 것으로 쉽게 해결할 수 있다.
- 엄격하게 예외적인 상황일 때만 쓰여야 한다
- Exception 던지는 부분들 프로그램 종료라고 생각했을때, 여전히 동작해야하는 상황이라면 사용하지 말아야 한다

13. [Replace Exception with Precheck](11-refactoring-apis/13-replace-exception-with-precheck.md)

- 호출자가 호출 전에 조건을 확인하여 합리적으로 처리할 수 있다면 예외를 던지는 대신 호출자가 사용할 테스트를 제공해야한다
- Assertion 어차피 지울거 왜 추가할까 라는 생각이었는데 혹시나 돌려봤더니 테스트가 실패했고, else 문을 빠트린 것을 발견했다.
  - 💡 역시 테스트는 중요하며 assert 는 유용할 수 있다

```diff
class ResourcePool…
    ..
    else {
      try {
        result = available.pop();
        allocated.add(result);
      } catch (NoSuchElementException e) {
+       throw new AssertionError("unreachable");
      }
```
