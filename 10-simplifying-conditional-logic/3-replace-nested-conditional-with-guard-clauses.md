> Replace Nested Conditional with Guard Clauses

```js
function getPayAmount() {
  let result;
  if (isDead) result = deadAmount();
  else {
    if (isSeparated) result = separatedAmount();
    else {
      if (isRetired) result = retiredAmount();
      else result = normalPayAmount();
    }
  }
  return result;
}
```

👇

```js
function getPayAmount() {
  if (isDead) return deadAmount();
  if (isSeparated) return separatedAmount();
  if (isRetired) return retiredAmount();
  return normalPayAmount();
}
```

# Motivation

- 조건 문이 두 경우 모두 일반적인 케이스라면 else 문까지 써서 동등하고 강조
- 한 쪽 조건이 특별한 경우라면 guard clause 로 early return 하여 이것은 메인 로직이 아님을 강조하고 명확성을 높인다
  - TODO: 강조가 핵심
- 프로그래밍에서 진입점은 하나로 강제되지만, 출구는 특별한 이유가 아니라면 다양한 것이 좋다

# Mechanics

- 가장 바깥 조건문을 guard clause 로 변경
- Test
- 필요한 만큼 반복
- 모든 guard clause 가 동일한 값을 return 한다면, `Consolidate Conditional Expression` 을 적용

# Example

- 급여 계산 시, separated, retired 는 제외 하는 예제

```js
function payAmount(employee) {
  let result;
  if (employee.isSeparated) {
    result = { amount: 0, reasonCode: "SEP" };
  } else {
    if (employee.isRetired) {
      result = { amount: 0, reasonCode: "RET" };
    } else {
      // logic to compute amount
      lorem.ipsum(dolor.sitAmet);
      consectetur(adipiscing).elit();
      sed.do.eiusmod = tempor.incididunt.ut(labore) && dolore(magna.aliqua);
      ut.enim.ad(minim.veniam);
      result = someFinalComputation();
    }
  }
  return result;
}
```

- 현재 코드는 조건에 해당되지 않는 경우가 메인 로직이므로 nested if 를 사용할 경우 의도가 모호하게 감춰진다
- 최상위 조건문부터 early return 적용

```js
function payAmount(employee) {
  let result;
  if (employee.isSeparated) return {amount: 0, reasonCode: "SEP"};
//..
```

- 다음 조건문에 early return 적용

```js
function payAmount(employee) {
  let result;
  if (employee.isSeparated) return {amount: 0, reasonCode: "SEP"};
  if (employee.isRetired)   return {amount: 0, reasonCode: "RET"};
  // logic to compute amount
```

- result 는 큰 의미가 없으므로 변수 제거

```js
function payAmount(employee) {
  let result;
  if (employee.isSeparated) return { amount: 0, reasonCode: "SEP" };
  if (employee.isRetired) return { amount: 0, reasonCode: "RET" };
  // logic to compute amount
  lorem.ipsum(dolor.sitAmet);
  consectetur(adipiscing).elit();
  sed.do.eiusmod = tempor.incididunt.ut(labore) && dolore(magna.aliqua);
  ut.enim.ad(minim.veniam);
  return someFinalComputation();
}
```

# Example: Reversing the Conditions

```js
function adjustedCapital(anInstrument) {
  let result = 0;
  if (anInstrument.capital > 0) {
    if (anInstrument.interestRate > 0 && anInstrument.duration > 0) {
      result =
        (anInstrument.income / anInstrument.duration) *
        anInstrument.adjustmentFactor;
    }
  }
  return result;
}
```

- 첫 번쨰 조건을 반대로 뒤집어서 guard clause 로 변경

```js
function adjustedCapital(anInstrument) {
  let result = 0;
  if (anInstrument.capital <= 0) return result;
  //..

```

- 두 번째 조건에 not 을 우선 추가

```js
function adjustedCapital(anInstrument) {
  let result = 0;
  if (anInstrument.capital <= 0) return result;
  if (!(anInstrument.interestRate > 0 && anInstrument.duration > 0)) return result;
  //..
```

- 범위 조건을 반대 의미로 수정

```js
function adjustedCapital(anInstrument) {
  let result = 0;
  if (anInstrument.capital <= 0) return result;
  if (anInstrument.interestRate <= 0 || anInstrument.duration <= 0)
    return result;
  //..
}
```

- `Consolidate Conditional Expression` 적용

```js
function adjustedCapital(anInstrument) {
  let result = 0;
  if (
    anInstrument.capital <= 0 ||
    anInstrument.interestRate <= 0 ||
    anInstrument.duration <= 0
  )
    return result;
  //..
}
```

- 의미없는 result 변수를 inline 으로 변경

```js
function adjustedCapital(anInstrument) {
  if (
    anInstrument.capital <= 0 ||
    anInstrument.interestRate <= 0 ||
    anInstrument.duration <= 0
  )
    return 0;
  return (
    (anInstrument.income / anInstrument.duration) *
    anInstrument.adjustmentFactor
  );
}
```
