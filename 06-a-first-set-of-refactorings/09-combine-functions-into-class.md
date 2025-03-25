> Combine Functions into Class

```js
function base(aReading) {...}
function taxableCharge(aReading) {...}
function calculateBaseCharge(aReading) {...}
```

👇

```js
class Reading {
  base() {...}
  taxableCharge() {...}
  calculateBaseCharge() {...}
}
```

# Motivation

- 공통된 data body 에 대해 수행되는 함수들은 class 로 묶어서 관리하는 것이 좋다
  - 불필요한 많은 인자들을 제거할 수 있고, 시스템의 다른 부분으로 전달할 참조를 제공한다
- 이미 함수로 정의된 부분 이외에도 추가적인 computation 을 식별하여 class 로 묶을 수도 있다

- 또다른 함수 병합 방법은 `Combine Functions into Transform` 으로 객체를 enrich 하는 방법이 있다
  - 클래스 사용의 장점은 객체의 상태를 변경하고 그 결과물을 일관되게 유지할 수 있다는 것
- 클래스 대신 nested function 으로 묶을수 있지만 테스트가 어려우므로 클래스를 사용하는 것이 좋다
- 노출하고 싶은 함수가 두 개 이상 있는 경우에도 클래스를 활용하는 것이 좋다

- first-class element 가 지원되지 않는 언어의 경우 `Function As Object` 기법을 사용할 수 있다

```js
function createPerson(name) {
  let birthday;
  return {
    name: () => name,
    setName: (aString) => (name = aString),
    birthday: () => birthday,
    setBirthday: (aLocalDate) => (birthday = aLocalDate),
    age: age,
    canTrust: canTrust,
  };
  function age() {
    return birthday.until(clock.today(), ChronoUnit.YEARS);
  }
  function canTrust() {
    return age() <= 30;
  }
}
```

# Mechanics

- 함수가 공유하는 data record 에 대해 `Encapsulate Record` 를 수행한다
  - 함수들간의 데이터 공통부가 묶여있지 않은 경우 `Introduce Parameter Object` 를 수행한다
- 공통 record 를 사용하는 함수들을 class 로 `Move function`
  - 함수 호출에 대한 인자는 제거한다
- 데이터를 조작하는 모든 로직은 `Extract Function` 을 통해 class 로 이동한다

# Example

- tea 를 계량하는 업무

```js
reading = { customer: "ivan", quantity: 10, month: 5, year: 2017 };
```

- 유사한 계산이 수행되는 곳 이 많았다

```js
// client 1…

const aReading = acquireReading();
const baseCharge = baseRate(aReading.month, aReading.year) * aReading.quantity;
```

- 필수적인 수준의 tea 에는 세금이 부과되지 않음

```js
// client 2…
const aReading = acquireReading();
const base = baseRate(aReading.month, aReading.year) * aReading.quantity;
const taxableCharge = Math.max(0, base - taxThreshold(aReading.year));
```

- 두 fragment 사이에 기본 요금 계산하는 중복 코드가 존재
  - `Extract Function` 통해 추출 => client 3 에서 이미 수행

```js
// client 3…
const aReading = acquireReading();
const basicChargeAmount = calculateBaseCharge(aReading);

function calculateBaseCharge(aReading) {
  return baseRate(aReading.month, aReading.year) * aReading.quantity;
}
```

- 바로 client1, 2 에 적용할 수도 있겠지만 관련된 data 와 함수를 class 로 묶어 관리하는 것이 좋다: `Encapsulate Record`

```js
class Reading {
  constructor(data) {
    this._customer = data.customer;
    this._quantity = data.quantity;
    this._month = data.month;
    this._year = data.year;
  }
  get customer() {
    return this._customer;
  }
  get quantity() {
    return this._quantity;
  }
  get month() {
    return this._month;
  }
  get year() {
    return this._year;
  }
}

- create Reading from raw data
```

```js
// client 3…
const rawReading = acquireReading();
const aReading = new Reading(rawReading);
const basicChargeAmount = calculateBaseCharge(aReading);
```

- `Move Function`

```js
// class Reading…
  get calculateBaseCharge() {
    return  baseRate(this.month, this.year) * this.quantity;
  }

// client 3…
  const rawReading = acquireReading();
  const aReading = new Reading(rawReading);
  const basicChargeAmount = aReading.calculateBaseCharge;
```

- `Rename Function` 으로 더 연결된 느낌을 가지는 이름으로 변경

  - js 경우 이러한 이름을 통해 client 는 field 인지 derived value 인지 알지를 못한다 => 좋은 현상

```js
get baseCharge() {
  return  baseRate(this.month, this.year) * this.quantity;
}
```

- client 1 에 적용

```js
const rawReading = acquireReading();
const aReading = new Reading(rawReading);
const baseCharge = aReading.baseCharge;
```

- client 2 의 경우 inline variable 로 남겨두기보다는 함수로 묶어 처리한다

```js
const rawReading = acquireReading();
const aReading = new Reading(rawReading);
const taxableCharge = Math.max(
  0,
  aReading.baseCharge - taxThreshold(aReading.year),
);
```

```js
// class Reading…
  get taxableCharge() {
    return  Math.max(0, this.baseCharge - taxThreshold(this.year));
  }
```

- 모든 derived data 는 onDemand 로 처리되기 때문에 data 가 mutable 하더라도 문제가 없다
