> Consolidate Conditional Expression

```js
if (anEmployee.seniority < 2) return 0;
if (anEmployee.monthsDisabled > 12) return 0;
if (anEmployee.isPartTime) return 0;
```

👇

```js
if (isNotEligableForDisability()) return 0;

function isNotEligableForDisability() {
  return (
    anEmployee.seniority < 2 ||
    anEmployee.monthsDisabled > 12 ||
    anEmployee.isPartTime
  );
}
```

# Motivation

- check 문이 다르지만 action 은 동일한 경우, and 와 or 를 사용하여 하나의 check 으로 통합한다
  - check 의 순서는 동일하게 보장되지만 하나로 합쳐져 조건문이 명확해진다
  - 통합된 check 을 `Extract Function` 하여 의도를 표현할 수 있다

# Mechanics

- side effect 를 가진 조건이 있는지 확인
  - 있다면, `Separate Query from Modifier` 를 먼저 수행
- 두 개의 조건문을 논리 연산자로 통합
  - 연속된 조건은 or, nested if 는 and 로 통합
- Test
- 단일 조건문이 될 때까지 반복
- 최종 단일 조건문에 대해 `Extract Function` 수행

# Example

```js
function disabilityAmount(anEmployee) {
  if (anEmployee.seniority < 2) return 0;
  if (anEmployee.monthsDisabled > 12) return 0;
  if (anEmployee.isPartTime) return 0;
  // compute the disability amount
}
```

- action 이 모두 0 을 리턴하므로, 먼저 상위 두 개 조건문을 통합

```js
if (anEmployee.seniority < 2 || anEmployee.monthsDisabled > 12) return 0;
```

- Test 통과하면 나머지 조건도 통합

```js
if (
  anEmployee.seniority < 2 ||
  anEmployee.monthsDisabled > 12 ||
  anEmployee.isPartTime
)
  return 0;
```

- `Extract Function` 으로 의도를 표현

```js
function disabilityAmount(anEmployee) {
  if (isNotEligableForDisability()) return 0;
  // compute the disability amount

function isNotEligableForDisability() {
return ((anEmployee.seniority < 2)
        || (anEmployee.monthsDisabled > 12)
        || (anEmployee.isPartTime));
}
```

# Example: Using ands

```js
if (anEmployee.onVacation) {
  if (anEmployee.seniority > 10) return 1;
}
return 0.5;
```

- nested if 문을 and 로 통합

```js
if (anEmployee.onVacation && anEmployee.seniority > 10) return 1;
return 0.5;
```

- `Extract Function` 으로 의도를 표현
