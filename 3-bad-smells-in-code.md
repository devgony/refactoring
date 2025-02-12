> 3. Bad Smells in Code

- 리팩토링을 어떻게 하는지 알았다고 해서 언제 할 지/ 멈춰야 할 지 아는건 아니다
- 인간의 직관이 가장 뛰어난 지표이기 때문에, 책에서는 문제가 있다는 징후만 알려줄 것이며, 결국은 스스로 판단할 수 있어야 한다

# Mysterious Name

- function, moudle, variable, class 가 무엇인지 어떻게 사용하는지를 표현할 수 있는 일반적이고 명확한 이름을 사용해야 한다
- Renaming 은 단순히 이름을 바꾸는 것이 아니다. 좋은 이름을 짓기 힘들다면 디자인에 문제가 있는 신호인 경우가 많다. 이름을 짓다가 코드를 먼저 단순하게 리팩토링 하는 경우도 많다.

# Duplicated Code

- 코드 중복이 있다면 차이점이 무엇인지 비교하고, 변경할 때마다 모두를 변경해야 하는 번거로움이 생김
- 중복이 동등하다면: `Extract Function`
- 유사하지만 다른 부분이 있다면: `Slide Statements` 시도 후 `Extract Function`
- 중복 부분이 같은 공통 class 하위의 subclass 에 존재한다면 [Pull Up Method](./dictionary/pull-up-method.md)

# Long Function

- 설명 공유 선택 등 위임의 모든 이점은 작은 함수를 통해 지원된다
- 최신 언어에서는 in-process 호출에 대한 오버헤드를 거의 제거했다
- 유일하게 남은 오버헤드는 코드를 읽는 사람의 context-switch 인데, 이를 해결하는 것은 좋은 이름을 짓는 것이다
  - 함수의 이름이 좋으면 body 를 볼 필요가 없다
- 결국 우리는 함수를 작게 분해 하는데 더 공격적으로 임해야 한다
- 주석을 달 필요를 느낀다면 함수로 추출한다

  - 코드의 작동 방식이 아닌 코드의 의도에 따라 이름을 지정한다
  - 추출한 결과가 더 길어지어라도 추출 한다
  - 핵심은 함수의 길이가 아니라 함수가 무엇을 하는지와 어떻게 하는지의 의미적 거리이다

- 90% 의 경우에는 `Extract Fuction`
- 임시 변수가 많다면 `Replace Temp with Query`
- 추출된 함수에 너무 많은 인자를 전달하면 가독성이 더 떨어진다: [Introduce Parameter Object](./dictionary/introduce-parameter-object.md) and [Preserve Whole Object](./dictionary/preserve-whole-object.md)
- 조건문: [Decompose Conditional](./dictionary/decompose-conditional.md)
- 같은 조건에 대한 스위치문이 한 개 이상 있다면: [Replace Conditional with Polymorphism](./dictionary/replace-conditional-with-polymorphism.md)
- 반복문 역시 함수로 추출, 추출이 어려운 경우는 두 가지 이상의 일을 하고있기 때문: [Split Loop](./dictionary/split-loop.md)

# Long Parameter List

> 과거에는 global data 를 쓰는것보다는 인자로 다 넘기는게 좋다고 했다. 하지만 인자가 너무 많아지면 그것 또한 문제가 된다.

- [Replace Parameter with Query](./dictionary/replace-parameter-with-query.md): 종속성이 있는 다른 인자를 제거하고 함수 내에서 계산
- [Preserve Whole Object](./dictionary/preserve-whole-object.md): 객체 전체를 전달하고 필요한 값만 추출
- [Introduce Parameter Object](./dictionary/introduce-parameter-object.md): 항상 같이 쓰이는 인자들을 객체로 묶어 전달
- [Remove Flag Argument](./dictionary/remove-flag-argument.md): boolean 인자를 제거하고 서로다른 행동을 두 함수로 분리
- [Combine Functions into Class](./dictionary/combine-functions-into-class.md): 여러 함수들이 같은 인자를 공유한다면 클래스로 묶는다
  - 함수형의 경우 [Partially applied function](./dictionary/partialy-applied-function.md) 을 활용할 수 있다

# Global Data

- 전역 변수는 어느 곳에서나 변경될 수 있어 버그가 어디서 발생했는지 찾기 어렵다
- 전역 변수 뿐만아니라 클래스 변수, 싱글톤 역시 문제가 될 수 있다
- [Encapsulate Variable](./dictionary/encapsulate-variable.md): 변수를 감싸고, 변수를 변경하는 함수를 만들어 사용
  - 함수로 감싸져 있다면 어디에서 수정되었는지 확인하고 엑세스를 제어하기 시작할 수 있다
  - 감싼 함수를 해당 모듈의 코드만 볼 수 있는 클래스나 모듈 내로 이동하여 가능하다면 최대한 범위를 제한하는 것이 좋다
- 작은 global data 라도 캡슐화 해서 보관하는 건이 변화에 대처할 핵심이다

# Mutable Data

- 함수형 프로그래밍에서는 데이터의 변화가 있을 경우 복제본을 리턴한다
- [Encapsulate Variable](./dictionary/encapsulate-variable.md): 모든 변경이 추적 가능한 근접 함수에 의해서만 이루어지도록 감싼다
- 변수가 변경되어 저장되어야 할 경우 `Split Variable` 로 위험한 update 를 격리
- `Slide Statements`, `Extract Function`을 최대한 활용하여 action 과 calculation 을 분리한다
- API 에서는 [Separate Query from Modifier](./dictionary/separate-query-from-modifier.md) 를 통해 side effect 를 발생시키는 코드를 최대한 호출하지 않도록 한다
- 최대한 빨리 `Remove Setting Method` 를 활용하여 불필요한 setter 를 제거하면 변수의 scope 를 줄일 수 있다
- 모든 곳에서 사용할 수 있는 mutable data 는 불필요하다. [Replace Derived Variable with Query](./dictionary/replace-derived-variable-with-query.md) 로 제거 하자

- 변수의 scope 이 넓지 않으면 mutable data 자체가 큰 문제가 되지는 않는다. scope 이 증가하면서 위험성도 함께 증가
- [Combine Functions into Class](./dictionary/combine-functions-into-class.md), [Combine Functions into Transform](./dictionary/combine-functions-into-transform.md): update 에 필요한 범위 제한
- [Change Reference to Value](./dictionary/change-reference-to-value.md): 변수가 내부 구조를 가진 data 를 포함하는 경우, 일부 수정보다 전체 구조를 교체하는게 낫다

# Divergent Change

- 수정 가능한 명확한 포인트를 한번에 찾기 어렵다면, 냄새가 나는 코드이다
- 발산적 변화는 여러 가지 이유로 다른 방식으로 자주 변경될 때 발생
- 두 가지 측면이 순서를 형성하는 경우 명확한 데이터 구조로 [Split Phase](./dictionary/split-phase.md)
- 더 많은 상호 관계가 있는 경우 적절한 모듈을 만들고 `Move Function` 으로 프로세스를 분리
- 모듈이 Class 인 경우 `Extract Class` 로 분리

# Shotgun Surgery

- `Move Function` 과 `Move Field` 를 통해 여러 곳에서 변경되는 코드를 한 곳으로 모아라
- 여러 함수들이 유사한 데이터들에 대해 수행되고 있으면 [Combine Functions into Class](./dictionary/combine-functions-into-class.md)
- 특정 데이터 구조를 변화시키거나 enrich 하는 함수들이면 [Combine Functions into Transform](./dictionary/combine-functions-into-transform.md)
- `Inline Function` or `Inline Class` 를 사용하면 하나로 모으기 쉽다
- 나중에는 Function 혹은 Class 가 너무 커지게 될텐데, 이는 재구성을 위한 중간 단계일 뿐, 그때가 되면 중요 부분만 다시 추출하면 된다

# Feature Envy

- 프로그램을 모듈화 할 떄, 내부 영역 통신은 최대화 하고 바깥 영역 과의 통신은 최소화 한다
- 모듈 내 함수가 자체모듈 보다 외부 함수나 데이터와 통신하는데 더 많은 시간을 소비하면 기술질투가 발생한다

  - 해결하기 위해서 함수가 외부 데이터와 함께 있을 수 있도록 `Move Fuction` 으로 옮겨준다
  - 함수의 일부분만 질투를 당하는 경우, `Extract Function` 으로 분리 후 `Move Function` 으로 옮겨준다

- 위의 규칙에 해당 되지 않는 Strategy, Visitor, Self Delegation 등의 패턴 도 있다
- 하지만 기본적인 원칙은 함께 변화하는 것들을 한 곳에 모으는 것이다
- 데이터와 그것을 참조하는 행동은 대부분 함께 변화하지만 그렇지 않은 예외의 경우 행동을 옮겨서 한곳에 유지한다
  Strategy 와 Vistor 패턴은 추가 지시사항을 희생하는 대신, 재 정의해야하는 소량의 행동을 격리하기 때문에 행동을 쉽게 변경하도록 한다

# Data Clumps

- `Extract Class` 로 데이터를 하나의 객체로 묶어준다
- [Introduce Parameter Object](./dictionary/introduce-parameter-object.md) 나 [Preserve Whole Object](./dictionary/preserve-whole-object.md) 로 메서드 시그니처를 줄인다
- 새롭게 생긴 객체의 모든 필드를 사용하지는 않더라도, 두 개 이상의 필드를 새 객체로 바꿨다면 결국에는 이득이 될 것이다

# Primitive Obsession

- 화폐단위, 전화번호 등을 primitive type 이 아닌 객체로 만들어 사용해야 한다: `Replace Primitive with Object`
- primitive type 이 조건을 제어한다면 [Replace Type Code With Subclasses](./dictionary/replace-type-code-with-subclasses.md) + [Replace Conditional with Polymorphism](./dictionary/replace-conditional-with-polymorphism.md)
- Data와 함께라면 `Extract Class`, [Introduce Parameter Object](./dictionary/introduce-parameter-object.md)

# Repeated Switches

- switch 문이 두 번 이상 반복된다면 clause 를 추가 할 때마다 모든 switch 문을 업데이트 해주어야 함: [Replace Conditional with Polymorphism](./dictionary/replace-conditional-with-polymorphism.md)

# Loops

- [Replace Loop with Pipeline](./dictionary/replace-loop-with-pipeline.md): 과거에는 반복문을 처리할 방법이 없었지만 현대에는 first-class function 들이 도입되면서 반복문을 함수로 추출할 수 있게 되었다

# Lazy Element

- 별도의 구조가 불필요한 경우도 있다
- 본질적으로 단순한 함수/클래스 이거나, 로직이 복잡해질 것으로 예상했으나 실제로 그렇지 않은경우, 한 때는 제 역할을 했지만 리팩토링으로 규모가 축소된 경우에는 과감하게 없앨수도 있어야한다.
- `Inline Function`, `Inline Class`, [Callapse Hierarchy](./dictionary/collapse-hierarchy.md)

# Speculative Generality

- 미래에 사용할 것이라고 추측하는 코드는 불필요하다
- abstract classs 가 하는 일이 없다면 [Collapse Hierarchy](./dictionary/collapse-hierarchy.md)
- 불필요한 위임을 제거: `Inline Function`, `Inline Class`
- 사용되지 않는 인자 제거: `Change Function Declaration`
- 유일한 사용처가 테스트 케이스라면 냄새가 나는것이다: `Remove Dead Code`

# Temporary Field

- 특정 상황에서만 사용되는 필드는 이해하기 어렵다
- `Extract Class` 로 분리, 관련된 모든 코드를 `Move Function`
- [Introduce Sepacial Case](./dictionary/introduce-sepecial-case.md) 로 대체 클래스를 만들어 조건부 코드 제거 가능

# Message Chains

- [Hide Delegate](./dictionary/hide-delegate.md): Middle man 역할의 함수를 만들어 위임으로 인한 chaining 을 숨긴다
  - 연속된 method chaining 이 발생하는 경우 chain 의 어느 부분에서든 수행할 수 있지만 그렇게 되면 모든 중간 객체가 middle man 으로 남게 된다.
- 객체가 어떤 용도로 사용되는지 확인하고, `Exatract Function` 으로 해당 객체를 사용하는 코드의 일부를 가져온 후, `Move Function` 으로 체인 아래에 밀어 넣는다.
- 체인에 있는 객체 중 하나의 클라이언트가 나머지 부분을 탐색하려는 경우, 이를 수행하는 메서드를 추가한다

# Middle Man

- 너무 많은 위임이 발생하는 경우 추적이 어려우므로: [Remove Middle Man](./dictionary/remove-middle-man.md)
- 일부 메서드만 위임하는 경우 `Inline Function` 으로 중간 객체를 제거한다
- 추가 로직이 있는 경우 [Replace Superclass with Delegate](./dictionary/replace-superclass-with-delegate.md) 혹은 [Replace Subclass with Delegate](./dictionary/replace-subclass-with-delegate.md) 를 통해 middle man 을 실제 Object 로 변경한다

# Insider Trading

- 모듈 사이의 데이터 교환은 최소화 해야 한다
- 데이터 교환이 많은 모듈은 `Move Function`, `Move Field` 를 통해 분리해서 데이터 교환을 최소화 한다
- 모듈이 공통의 관심사를 가지고 있다면 세 번째 모듈을 만들어서 공통 관심사를 분리하거나, [Hide Delegate](./dictionary/hide-delegate.md) 를 통해 다른 모듈이 중개자 역할을 하도록 한다
- 상속은 담합으로 이어질 수 있으므로 [Replace Subclass with Delegate](./dictionary/replace-subclass-with-delegate.md) 혹은 [Replace Superclass with Delegate](./dictionary/replace-superclass-with-delegate.md) 를 통해 상속을 제거한다

# Large Class

- Class 가 너무 많은 것을 하려고하면 필드의 개수가 늘어난다. 필드가 많으면 코드가 중복될 수 밖에 없다
- `Exatract Class` 를 통해 여러 변수를 하나로 묶을 수 있다
- 클래스 변수의 하위 집합에 공통된 접두사나 접미어가 있으면 동일 컴포넌트에 속할 가능성이 높다
- 클래스가 모든필드를 사용하지 않는 경우도 있기 때문에 추출을 여러번 수행하여 해결할 수 있다
- 코드 가 긴 경우에는 클라이언트가 클래스의 하위 집합을 사용하는지 확인.
  - `Extract Class`, `Extract Superclass`, or [Replace Type Code with Subclasses](./dictionary/replace-type-code-with-subclasses.md) 사용하여 분리

# Alternative Classes with Different Interfaces

- 클래스의 장점 중 하나는 interface 만 같다면 필요할 때 언제든지 교체할 수 있다는 점이다
- `Change Function Declaration`, `Move Function` 으로 interface 를 통일
- 이 과정에서 중복이 발생하면, `Extract Superclass` 로 중복을 제거

# Data Class

- 데이터 클래스는 데이터를 set 하고 get 하는 field 만 가지고 있다
- public field를 가지고 있다면 즉시 [Encapsulate Record](./dictionary/encapsulate-record.md) 를 통해 getter, setter 를 만들어 사용
  - `Remove Setting Method`: immutable 한 경우 setter 를 제거하고, 생성자에서 값을 설정하도록 한다
- getter, setter 를 통해 데이터 클래스를 참조하는 부분을 찾고 해당 로직을 데이터 클래스 안으로 `Move Function`
- 옮길 수 없는 경우 `Extract Function` 으로 옮길 수 있는 함수로 추출
- Data class 는 잘못된 곳에 위치되는 경우가 많고 client 에서 Data class 로 옮기는 것만으로 많은 개선이 된다
  - 단, 별도 함수 호출에서 결과 레코드로 사용되는 경우는 예외이다
    - 이 경우의 특징은 데이터 클래스가 immutable 하다는 점이다
    - immutable field 는 캡슐화가 필요없으므로 메서드 대신 필드를 사용해도 된다

# Refused Bequest

- subclass 가 superclass 의 일부 메서드나 데이터를 사용하지 않는 경우 전통적으로는 잘못된 설계이다
  - 새로운 sibling class 를 만들어서 쓰이지 않는 부분을 옮기고 superclass 에는 공통된 부분만 남긴다
    - [Push Down Method](./dictionary/push-down-method.md), [Push Down Field](./dictionary/push-down-field.md)
- 90% 의 경우에는 약간의 냄새가 나도 넘어갈 수 있는 수준
- subclass 가 행동을 재활용 하고있지만 superclass 의 interface 를 벗어나려하면 [Replace Subclass with Delegate](./dictionary/replace-subclass-with-delegate.md) or [Replace Superclass with Delegate](./dictionary/replace-superclass-with-delegate.md) 를 로 계층 구조를 완전히 바꾼다
  - 구현을 거부하는 것은 상관 없지만 인터페이스를 거부해서는 안된다

# Comments

> When you feel the need to write a comment, first try to refactor the code so that any comment becomes superfluous.

- 코드에 대한 설명을 주석으로 달고싶으면 `Extract Function`
- 여전히 주석이 달고 싶으면 `Change Function Declaration` 으로 함수를 의미있는 이름으로 변경
- 특정 규칙에 대한 주석을 달고 싶으면 [Introduce Assertion](./dictionary/introduce-assertion.md)

## 주석을 작성해야 하는 경우

- 무엇을 해야할지 모를 때
- 무엇을 하는 코드인지를 적는게 아니라 확신이 없는 영역에 대해서만 작성
- 왜 이것을 하는지에 대해 작성
