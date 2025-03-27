> Simplifying Conditional Logic

- 프로그래밍의 힘은 대부분 조건절에서 오지만, 그와 동시에 복잡하게 만드는 요인이기도 하다
- `Decompose Conditional`
- `Consolidate Conditional Expression`: 논리적 조합을 명확하게
- `Replace Nested Conditional with Guard Clauses`: 사전점검을 하고싶을 때
- `Replace Conditional with Polymorphism`: 같은 조건 로직이 여러개 일 때
- `Introduce Null Object`(`Introduce Null Object`): null 과 같은 특수한 상황들에서 중복 코드 제거
- `Introduce Assertion`: 조건절 대신에 사용하여 상태확인

1. [Decompose Conditional](./10-simplifying-conditional-logic/1-decompose-conditional.md)

- 조건절의 복잡한 코드만으로는 왜 필요한지 목적을 표현하지 못하므로 함수로 추출하여 의도를 명확히 한다
  - 💡 굳이 추출해야 하나 싶은 경우도 있었는데, 협업자를 위해서는 추출하는게 좋겠다
- ⁉️ 아래와 같은 ternary operator(삼항연산자~사망연산자~) 는 지양하라고 하던데 함수로 추출했으면 이 정도는 괜찮은 것인가?

```js
charge = summer() ? summerCharge() : regularCharge();
```

2. [Consolidate Conditional Expression](./10-simplifying-conditional-logic/2-consolidate-conditional-expression.md)

- 연속된 조건은 or, nested if 는 and 로 통합
- 통합된 check 을 `Extract Function` 하여 의도를 표현

3. [Replace Nested Conditional with Guard Clauses](./10-simplifying-conditional-logic/3-replace-nested-conditional-with-guard-clauses.md)

- 한 쪽 조건이 특별한 경우라면 guard clause 로 early return 하여 이것은 메인 로직이 아님을 강조하고 명확성을 높인다.
  - 💡 단순히 코드 중복 제거라고 생각했는데 각 경우에 대한 강조의 기능도 있었구나

4. [Replace Conditional with Polymorphism](./10-simplifying-conditional-logic/4-replace-conditional-with-polymorphism.md)

- 클래스와 다형성을 이용하면 더 명시적으로 분리하여 표현
- 공통 로직은 상속받고, 특정 로직은 오버라이드하여 사용할 수도 있다
- 💡 `Move Statements to Callers` 하나 만으로 기존에는 다형성을 챙기지 못했던 부분을 리팩토링 할 수 있다는 점이 참신하다

```diff
class ExperiencedChinaRating…
+ get voyageProfitFactor() {
+   return super.voyageProfitFactor + 3;
+ }

  get voyageLengthFactor() {
    let result = 0;
-   result += 3;
    if (this.voyage.length > 12) result += 1;
    if (this.voyage.length > 18) result -= 1;
    return result;
  }
```

5. [Introduce Speical Case](./10-simplifying-conditional-logic/5-introduce-special-case.md)

- 특정 경우에 동일한 동작을 한다면 하나로 묶을 수 있다
  - Subclass 내 메서드로 처리
  - Object Literal 의 특정 필드값으로 처리
  - 특정 필드값을 trasform (enrich) 를 통해 채워서 처리

6. [Introduce Assertion](./10-simplifying-conditional-logic/6-introduce-assertion.md)

- assertion 으로 조건을 명시적으로 표현하면, 코드의 의도를 명확히 할 수 있다
- assertion 은 의사소통의 한 형태이다. 디버깅도 돕긴 하지만 unit test 를 작성하는 것이 더 좋다.
  - 💡 처음 이 챕터를 보고 test 하기 귀찮으면 메인 코드에 넣으면 좋겠다는 했는데 그런 방식이 아니라 정말 의사 소통을 위해 써야 하는 것이구나
- assertion 은 버그 추적의 최후 수단이기 때문에, 역설적으로, 절대 실패해서는 안될 때, 소통을 위해 사용한다

7. [Replace Control Flag with Break](./10-simplifying-conditional-logic/7-replace-control-flag-with-break.md)

- early return 을 사용하면 control flag 를 제거할 수 있다

- 💡 loop 횟수를 어떻게 테스트로 작성하지 하다가 spy를 활용하여 빈 함수(`sendAlert`)가 호출된 횟수를 비교하기로 함

```java
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Test
void client1() {
    List<String> people = Arrays.asList("Don", "John", "Kent");
    ReplaceControlFlagWithBreak spyObj = spy(new ReplaceControlFlagWithBreak());
    spyObj.client1(people);
    verify(spyObj, times(1)).sendAlert();
}
```
