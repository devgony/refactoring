> Encapsulate Variable

```js
let defaultOwner = { firstName: "Martin", lastName: "Fowler" };
```

👇

```js
let defaultOwnerData = { firstName: "Martin", lastName: "Fowler" };
export function defaultOwner() {
  return defaultOwnerData;
}
export function setDefaultOwner(arg) {
  defaultOwnerData = arg;
}
```

# Motivation

- 리팩토링은 프로그래밍의 요소들을 조작하는 것이다
- Data 는 함수보다 조작하기 어렵다
- 함수 내부의 작은 scope를 가진 data 는 다루기가 수월하지만scope 가 증가함에 따라 난이도도 올라간다
  - 특히 global data 가 가장 어렵다
- 가장 쉬운 방법은 data 를 함수로 감싸는 것이다
  - data 재구성이 아니라 함수 재구성이 더 쉬움
  - data 의 변화와 사용을 모니터 하기 쉬움
  - update 시 validation 혹은 연속적인 로직을 추가하기 쉬움
- 기존 코드에서 변수에 새로 접근하거나 변경할 일이 생기면 항상 함수로 감싸는 것을 먼저 고려한다
- 이것이 OOP 에서 data field 는 private 으로 사용하는 이유이다
  - public field 를 발견한다면 `Encapsulate Variable(Field)` 를 적용하여 visibility 를 최대한 제한한다
- immutable data 인 경우 data 캡슐화는 안해도 괜찮다

# Mechanics

- 변수를 갱신할 수 있는 캡슐화 함수를 만든다
- 정적 검사를 수행한다
- 변수를 참조하는 각 코드를 함수 호출로 변경 하고 테스트 수행
- visibility 를 최대한 제한
- 테스트 수행
- 변수의 값이 record 인 경우, `Encapsulate Record` 를 적용한다

# Example

```js
let defaultOwner = { firstName: "Martin", lastName: "Fowler" };

spaceship.owner = defaultOwner;
defaultOwner = { firstName: "Rebecca", lastName: "Parsons" };
```

- 캡슐화 함수를 만든다

```js
function getDefaultOwner() {
  return defaultOwner;
}
function setDefaultOwner(arg) {
  defaultOwner = arg;
}
```

- 함수 호출로 변경

```js
spaceship.owner = getDefaultOwner();
setDefaultOwner({ firstName: "Rebecca", lastName: "Parsons" });
```

- js 의 경우 visibility 변경을 위해 파일을 분리한다
  - visibility 확인하기 위해 이름도 변경해본다. getter 는 굳이 get prefix 를 붙일 필요가 없다

```js
// defaultOwner.js…
let defaultOwnerData = { firstName: "Martin", lastName: "Fowler" };
export function defaultOwner() {
  return defaultOwnerData;
}
export function setDefaultOwner(arg) {
  defaultOwnerData = arg;
}
```

## Encapsulating the Value

- 기존 리팩토링은 구조의 변경은 막지 못했다

```js
const owner1 = defaultOwner();
assert.equal("Fowler", owner1.lastName, "when set");
const owner2 = defaultOwner();
owner2.lastName = "Parsons";
assert.equal("Parsons", owner1.lastName, "after change owner2"); // is this ok?
```

1. value 의 immutable 을 보장하기 위해 getter 가 data 의 복제본을 반환하도록 수정한다

```js
// defaultOwner.js…
let defaultOwnerData = { firstName: "Martin", lastName: "Fowler" };
export function defaultOwner() {
  return Object.assign({}, defaultOwnerData);
}
export function setDefaultOwner(arg) {
  defaultOwnerData = arg;
}
```

- `Encapsulate Record`

  - 객체가 한번 생성되면 필드는 변경되지 않는다

```js
let defaultOwnerData = { firstName: "Martin", lastName: "Fowler" };
export function defaultOwner() {
  return new Person(defaultOwnerData);
}
export function setDefaultOwner(arg) {
  defaultOwnerData = arg;
}

class Person {
  constructor(data) {
    this._lastName = data.lastName;
    this._firstName = data.firstName;
  }
  get lastName() {
    return this._lastName;
  }
  get firstName() {
    return this._firstName;
  }
  // and so on for other properties
}
```

- setter 에서도 복사하는것이 좋을 수 있다. 원본 데이터에 대한 링크를 유지할 필요가 없는 경우 복사본을 통해 데이터 변경으로 인한 사고를 막을 수 있다
  - TODO: link?
