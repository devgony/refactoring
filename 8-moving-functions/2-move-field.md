> Move Field

```js
class Customer {
  get plan() {return this._plan;}
  get discountRate() {return this._discountRate;}
```

👇

```js
class Customer {
  get plan() {return this._plan;}
  get discountRate() {return this.plan.discountRate;}
```

# Motivation

- 프로그래밍의 진짜 힘은 데이터 구조에 있다
- 이번 주에는 합리적이였던 문제 도메인과 디자인 구조가 다른 주에는 잘못된 결정이 될 수 있다
- 데이터 구조가 잘못되었다는것을 깨닫는 즉시 변경하는 것이 중요하다
- 필드를 옮겨야 하는 신호
  - 한 레코드의 변경이 다른 레코드의 필드도 변경하는 경우
  - 여러 구조에서 동일한 필드를 업데이트 해야 하는 경우
- 필드 이동 후에는 원본 소스 보다는 대상 object 를 통해 접근하는 것이 좋다

# Mechanics

- 원본 필드가 encapsulate 되어 있는지 확인한다
- Test
- target 에 필드와 접근자 생성
- static check
- source 에서 target 으로 reference 존재하는지 확인
- target field 를 사용하는 접근자 적용
  - target 과 source 가 공유되고 있다면 두 필드를 모두 업데이트 하고 `Introduce Assertion` 을 통해 검증
- Test
- source field 제거
- Test

# Example

```js
// class Customer…
  constructor(name, discountRate) {
    this._name = name;
    this._discountRate = discountRate;
    this._contract = new CustomerContract(dateToday());
  }
  get discountRate() {return this._discountRate;}
  becomePreferred() {
    this._discountRate += 0.03;
    // other nice things
  }
  applyDiscount(amount) {
    return amount.subtract(amount.multiply(this._discountRate));
  }

// class CustomerContract…
  constructor(startDate) {
    this._startDate = startDate;
  }

```

- discountRate 필드를 customer 에서 CustomerContract 로 변경
  - `Encapsulate Variable` 을 통해 discountRate 에 대한 접근을 encapsulate 한다

```js
// class Customer…
  constructor(name, discountRate) {
    this._name = name;
    this._setDiscountRate(discountRate);
    this._contract = new CustomerContract(dateToday());
  }
  get discountRate() {return this._discountRate;}
  _setDiscountRate(aNumber) {this._discountRate = aNumber;}
  becomePreferred() {
    this._setDiscountRate(this.discountRate + 0.03);
    // other nice things
  }
  applyDiscount(amount) {
    return amount.subtract(amount.multiply(this.discountRate));
  }
```

- property setter 가 아닌 일반 메서드를 사용하여 discountRate 변경
  - TODO: 해석불가

```js
// class CustomerContract…
  constructor(startDate, discountRate) {
    this._startDate = startDate;
    this._discountRate = discountRate;
  }
  get discountRate()    {return this._discountRate;}
  set discountRate(arg) {this._discountRate = arg;}
```

- `Customer._setDiscountRate`가 `_contract` 객체 생성 이후에 호출 될 수 있도록 `Slide Statements` 수행

```diff
// class Customer…
  constructor(name, discountRate) {
    this._name = name;
-   this._setDiscountRate(discountRate);
-   this._contract = new CustomerContract(dateToday());
+   this._contract = new CustomerContract(dateToday());
+   this._setDiscountRate(discountRate);
  }
```

- `_coutract` 를 사용하도록 변경

```js
// class Customer…
  get discountRate() {return this._contract.discountRate;}
  _setDiscountRate(aNumber) {this._contract.discountRate = aNumber;}
```

- js 의 경우 source 에서 필드를 선언해서 사용 한 것이 아니기 때문에 따로 삭제할 필요도 없다

# Example: Moving to a Shared Object

```js
// class Account…
  constructor(number, type, interestRate) {
    this._number = number;
    this._type = type;
    this._interestRate = interestRate;
  }
  get interestRate() {return this._interestRate;}
// class AccountType…
  constructor(nameString) {
    this._name = nameString;
  }
```

- `_interestRate` 를 AccountType 으로 이동

```js
// class AccountType…
  constructor(nameString, interestRate) {
    this._name = nameString;
    this._interestRate = interestRate;
  }
  get interestRate() {return this._interestRate;}
```

- 기존에 각 Account 별로 type 을 가지고 있었지만, 현재는 같은 타입이라면 AccountType 을 공유하게 하고싶다

  - DB 의 경우 FK를,
  - DB 에서 제약할 수 없는 경우 `Introduce Assertion` 활용

```js
// class Account…
  constructor(number, type, interestRate) {
    this._number = number;
    this._type = type;
    assert(interestRate === this._type.interestRate);
    this._interestRate = interestRate;
  }
  get interestRate() {return this._interestRate;}
```

- assertion, DB 제약조건 등의 로그를 한동안 지켜보고 이상이 없다면 접근을 신규 필드로 변경하고 기존 필드는 삭제

```js
// class Account…
  constructor(number, type) {
    this._number = number;
    this._type = type;
  }
  get interestRate() {return this._type.interestRate;}
```
