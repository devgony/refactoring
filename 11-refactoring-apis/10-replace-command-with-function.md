> Replace Command with Function

```js
class ChargeCalculator {
  constructor(customer, usage) {
    this._customer = customer;
    this._usage = usage;
  }
  execute() {
    return this._customer.rate * this._usage;
  }
}
```

👇

```js
function charge(customer, usage) {
  return customer.rate * usage;
}
```

# Motivation

- 대부분의 경우 Command 보다는 단순 함수 호출을 하고싶어함
- Command 가 제공하는 가치보다 복잡도가 더 높다면 일반 함수로 전환

# Example

```js
class ChargeCalculator {
  constructor (customer, usage, provider){
    this._customer = customer;
    this._usage = usage;
    this._provider = provider;
  }
  get baseCharge() {
    return this._customer.baseRate * this._usage;
  }
  get charge() {
    return this.baseCharge + this._provider.connectionCharge;
  }
}

caller…
  monthCharge = new ChargeCalculator(customer, usage, provider).charge;
```

- 함수가 단순하므로 일반함수로 변경 가능
- `Extract Function` 으로 class 생성과 호출자를 감쌈

```js
caller…
  monthCharge = charge(customer, usage, provider);

top level…
  function charge(customer, usage, provider) {
    return new ChargeCalculator(customer, usage, provider).charge;
  }
```

- 지원 함수 들을 어떻게 처리 할지 결정한다
  - 일단은 `Extract Variable`

```diff
class ChargeCalculator…
  get baseCharge() {
    return this._customer.baseRate * this._usage;
  }
  get charge() {
+   const baseCharge = this.baseCharge;
+   return baseCharge + this._provider.connectionCharge;
  }
```

- `Inline Function` 으로 `baseCharge()` 를 제거

```diff
class ChargeCalculator…
  get charge() {
+   const baseCharge = this._customer.baseRate * this._usage;
    return baseCharge + this._provider.connectionCharge;
  }
```

- Constructor 에서 받은 데이터를 main 으로 이동
  - 먼저 모든 필드를 메서드의 파라미터로 이동

```diff
class ChargeCalculator…
  constructor (customer, usage, provider){
    this._customer = customer;
    this._usage = usage;
    this._provider = provider;
  }

+ charge(customer, usage, provider) {
    const baseCharge = this._customer.baseRate * this._usage;
    return baseCharge + this._provider.connectionCharge;
  }

top level…
  function charge(customer, usage, provider) {
    return new ChargeCalculator(customer, usage, provider)
+                       .charge(customer, usage, provider);
  }

```

- charge 메서드 내부에서 필드변수가 아닌 인자를 사용하도록 변경

- `Inline Function` 으로 생성자 호출 및 함수 수행 제거

```diff
top level…
  function charge(customer, usage, provider) {
+     const baseCharge = customer.baseRate * usage;
+     return baseCharge + provider.connectionCharge;
  }
```

- `Remove Dead Code`: ChargeCalculator class 제거
