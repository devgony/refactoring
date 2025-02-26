> Combine Functions into Transform

```js
function base(aReading) {...}
function taxableCharge(aReading) {...}
```

👇

```js
function enrichReading(argReading) {
  const aReading = _.cloneDeep(argReading);
  aReading.baseCharge = base(aReading);
  aReading.taxableCharge = taxableCharge(aReading);
  return aReading;
}
```

# Motivation

- derived data 를 한 군데에 모아 중복을 방지하고 손쉽게 찾아 쓴다
- 한군데 모으기 위한 방법 중 하나는 data transformation function 을 만들고 계산의 결과를 output의 각 필드에 붙여 리턴하는 것
- 또다른 방법은 Combine Functions into Class 를 통해 해당 로직을 클래스의 메서드에 할당하는 것이다
- 어느것을 선택할지는 기존 코드에서 선택한 스타일을 따른다
- source data 가 코드 내에서 자주 변경된다면 class 가 유리하다
  - Transform 을 사용하면 derived data 는 새로운 레코드에 저장되므로 변경되면 불일치 발생
- 단순히 `Extract function` 만 수행할 수도 있겠지만, data structure 에 가깝게 보관되어 있지 않으면 추출된 함수를 찾기 어렵다.
  - 따라서 트랜스폼(혹은 클래스) 를 사용하는 것이 함수를 쉽게 찾을 수 있게 한다

# Mechanics

- record 를 받아서 그대로 리턴하는 트랜스폼 함수를 만든다
  - 주로 deep copy 를 해서 리턴하거나 원본 record 가 변경되지 않았는지 테스트를 짜는게 좋다
- 추출할 로직을 트랜스폼 함수 내로 옮기고 신규 필드를 record 에 추가하고, client 에서 해당 필드를 사용하도록 수정한다
  - 로직이 복잡하다면 `Extract Function` 을 통해 함수를 분리한다
- 테스트를 수행한다
- 다른 관련된 함수들에 대해 같은 작업을 수행한다

# Example

- 고객들이 차를 얼마나 구매했는지 확인하는 코드

```js
reading = { customer: "ivan", quantity: 10, month: 5, year: 2017 };
```

- 기본 요금 계산

```js
// client 1…
const aReading = acquireReading();
const baseCharge = baseRate(aReading.month, aReading.year) * aReading.quantity;
```

- 세금 계산

```js
// client 2…
const aReading = acquireReading();
const base = baseRate(aReading.month, aReading.year) * aReading.quantity;
const taxableCharge = Math.max(0, base - taxThreshold(aReading.year));
```

- 단순히 함수를 추출하면 여기저기 혼재된다

```js
// client 3…

const aReading = acquireReading();
const basicChargeAmount = calculateBaseCharge(aReading);

function calculateBaseCharge(aReading) {
  return baseRate(aReading.month, aReading.year) * aReading.quantity;
}
```

- 트랜스폼 함수를 먼저 생성한다

  - loadash 의 cloneDeep 을 사용하여 deep copy 를 수행한다

```js
function enrichReading(original) {
  const result = _.cloneDeep(original);
  return result;
}
```

- 추가정보 생성인 경우 `enrich`, 뭔가 다른 것을 만들어 내는 경우 `transform` 을 이름으로 사용한다

- calculateBaseCharge 를 트랜스폼 함수로 옮긴다

```js
// client 3…
const rawReading = acquireReading();
const aReading = enrichReading(rawReading);
const basicChargeAmount = calculateBaseCharge(aReading);
```

```js
function enrichReading(original) {
  const result = _.cloneDeep(original);
  result.baseCharge = calculateBaseCharge(result);
  return result;
}
```

- client3 에 적용

```js
// client 3…
const rawReading = acquireReading();
const aReading = enrichReading(rawReading);
const basicChargeAmount = aReading.baseCharge;
```

- client2(세금) 에 적용

```js
// client 2…
const rawReading = acquireReading();
const aReading = enrichReading(rawReading);
const taxableCharge = Math.max(
  0,
  aReading.baseCharge - taxThreshold(aReading.year),
);
```

- 로직을 트랜스폼 함수로 이동

```js
function enrichReading(original) {
  const result = _.cloneDeep(original);
  result.baseCharge = calculateBaseCharge(result);
  result.taxableCharge = Math.max(
    0,
    result.baseCharge - taxThreshold(result.year),
  );
  return result;
}
```

- 최종 client 코드

```js
const rawReading = acquireReading();
const aReading = enrichReading(rawReading);
const taxableCharge = aReading.taxableCharge;
```

- 하지만 수량 필드와 같은 데이터를 client 가 변경해버리면 데이터가 일관되지 않게 됌
  - immutable -> transform 을 사용
  - mutable -> class 를 사용
