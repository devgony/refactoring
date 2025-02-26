> Change Function Declaration

```js
function circum(radius) {...}
```

👇

```js
function circumference(radius) {...}
```

# Motivation

- 함수는 프로그램을 부분으로 나누는 가장 기본적입 방법
- 함수의 정의는 시스템에 어떻게 연계되는지를 나타낸다

  - 나쁜 함수 정의는 시스템을 더 이해하기 어렵게 만든다
  - 하지만 software 는 soft 하므로 변경이 가능하다

- 그 중 가장 중요한 요소는 함수의 이름이다
- 잘못된 이름을 발견하면 더 나은 이름이 떠오르는 즉시 변경해야 다음번에 볼 때 다시 노력을 하지 않아도 된다
- 일단 주석으로 함수의 목적을 적어보고 이를 함수 이름으로 변경하는 것도 방법이다
- 함수의 인자에도 동일한 로직이 적용된다
- 함수의 인자는 함수가 세상과 어떻게 연결되어 있는지를 나타낸다
- 함수를 어떤 context 에서 사용할 수 있는지를 결정한다

  - 사람과 회사의 전화번호를 모두 처리하는 함수를 위해서는 사람보다는 번호를 인자를 받는게 더 좋다

- 함수의 적용 범위를 확장하는것도 중요하지만 커플링을 없애는 것도 중요하다

  - 전화번호 포매팅 함수는 사람 모듈에 대해 알 필요가 없다
  - 필요한 모듈을 줄이는 것은 함수를 변경할 때 고려해야할 부분을 줄여준다

- 올바른 파라미터를 결정하는 것은 단순한 규칙을 따르지 않는다
- 무엇이 최고의 연계인지 찾으면서 지속적으로 `Change Fuction Declaration` 을 수행해야 한다

- 함수의 이름을 바꾸는것은 매우 중요하므로 `Rename Function` 으로 별도로 명명하겠다

# Mechanics

- 다른 리팩토링 기법의 경우 보편적인 mechanic 이 존재하나 ``Change Function Declaration` 의 경우는 다양하다

## Simple Mechanics

- 인자를 제거한다면, 참조되고 있지 않은지 확인한다
- 원하는 함수의 정의로 변경한다
- 모든 참조들을 찾아 새 함수의 정의에 맞춰 변경한다
- 테스트 수행
- 각 변화는 분리해서 진행한다

## Migration Mechanics

- 필요하다면 함수 본문을 미리 리팩토링 한다
- 새 함수로 `Extract Function` 을 수행한다. 새 함수에서 같은 이름을 쓰고 싶으면 일단은 임시 이름을 부여한다
- 추출한 함수가 추가 인자를 필요로 한다면, 위의 Simple Mechanics 를 따라 변경한다
- 테스트 수행
- 다형성을 가진 class 의 메서드를 변경한다면 간접 바인딩을 추가해야 한다

  - 단일 클래스 계층 구조 내에서 다형성을 가지는 경우, 슈퍼클래스의 전달 메서드만 있으면 됌
  - 슈퍼클래스의 링크가 없으면, 각 구현 클래스에 전달 메서드가 필요하다

- API 배포의 경유 신규 함수 생성 후 과거 함수를 deprecated 처리한 상태로 client 가 모든 migration 을 마칠 때까지 기다린다. 모두 옮겼다는 확신이 생겼을 떄 과거 함수를 삭제한다

# Example

```js
function circum(radius) {
  return 2 * Math.PI * radius;
}
```

```js
function circumference(radius) {
  return 2 * Math.PI * radius;
}
```

- 앞서 언급된 리팩토링 기법들은 참조하는 곳이 많은 경우 공수가 많이 드므로 LSP 의 도움을 받는것이 좋다
- 이름이 겹쳐서 곤란한 경우 Migration Mechanics 를 사용하면 된다

# Example: Renaming Function (Migration Mechanics)

```js
function circum(radius) {
  return 2 * Math.PI * radius;
}
```

- 일단 `Extract Function` 을 통해 새 함수를 만든다

```js
function circum(radius) {
  return circumference(radius);
}
```

- 테스트 수행 결과가 이상이 없으면 `Inline Function` 으로 기존함수 제거

```js
function circumference(radius) {
  return 2 * Math.PI * radius;
}
```

# Example: Adding a Parameter

```js
class Book {
  addReservation(customer) {
    this._reservations.push(customer);
  }
  //..
}
```

- 일반 queue 와 high-priority quere 를 구분하기 위해 새로운 인자를 추가한다
- 이때 `Extract Function` 을 통해 새 함수를 만들어둔다
- 이름을 유지하고 싶으므로 임시 이름을 부여한다

```js
class Book {
  addReservation(customer) {
    this.zz_addReservation(customer);
  }

  zz_addReservation(customer) {
    this._reservations.push(customer);
  }
}
```

- 새 인자를 추가한다

```js
class Book {
  addReservation(customer) {
    this.zz_addReservation(customer, false);
  }

  zz_addReservation(customer, isPriority) {
    this._reservations.push(customer);
  }
}
```

- 호출자를 수정하기 전에 js 경우 `Introduce Assertion` 을 통해 type guard 를 추가한다
  - 호출부에서 `isPriority` 를 사용하지 않아 undefined 가 되거나 다른 타입을 넣으면 에러가 발생한다

```js
class Book {
  //..
  zz_addReservation(customer, isPriority) {
    assert(isPriority === true || isPriority === false);
    this._reservations.push(customer);
  }
}
```

- 호출부를 `Inline Function` 을 통해 수정한다

# Example: Changing a Parameter to One of Its Properties

```js
function inNewEngland(aCustomer) {
  return ["MA", "CT", "ME", "VT", "NH", "RI"].includes(aCustomer.address.state);
}
```

```js
const newEnglanders = someCustomers.filter((c) => inNewEngland(c));
```

- Customer 에 대한 의존성을 제거하고 필요한 state 만 남기고 싶다

- 먼저, 변수 추출

```js
function inNewEngland(aCustomer) {
  const stateCode = aCustomer.address.state;
  return ["MA", "CT", "ME", "VT", "NH", "RI"].includes(stateCode);
}
```

- 함수로 추출

```js
function inNewEngland(aCustomer) {
  const stateCode = aCustomer.address.state;
  return xxNEWinNewEngland(stateCode);
}

function xxNEWinNewEngland(stateCode) {
  return ["MA", "CT", "ME", "VT", "NH", "RI"].includes(stateCode);
}
```

- 다시 `Inline Variable` 로 stateCode 를 제거한다?
  - TODO: 의도 확인 필요
  - 함수 추출시 이름을 인자에도 그대로 반영하려고 잠깐 변수로 추출했던 것으로 보임

```js
function inNewEngland(aCustomer) {
  return xxNEWinNewEngland(aCustomer.address.state);
}
```

- caller 쪽도 새 함수로 교체

```js
const newEnglanders = someCustomers.filter((c) =>
  xxNEWinNewEngland(c.address.state),
);
```

- 이제 `Rename Function` 으로 함수 이름을 변경한다

```js
const newEnglanders = someCustomers.filter((c) =>
  inNewEngland(c.address.state),
);

function inNewEngland(stateCode) {
  return ["MA", "CT", "ME", "VT", "NH", "RI"].includes(stateCode);
}
```

- 대부분의 경우에는 IDE 가 리팩토링 대상을 자동으로 찾아주지만 전체 리팩토링을 수행할 수 없는 경우, Migration Mechanics 를 사용하면 안전하고 더 빠르게 리팩토링을 수행할 수 있다
