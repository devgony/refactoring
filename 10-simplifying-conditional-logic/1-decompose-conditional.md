> Decompose Conditional

```js
if (!aDate.isBefore(plan.summerStart) && !aDate.isAfter(plan.summerEnd))
  charge = quantity * plan.summerRate;
else charge = quantity * plan.regularRate + plan.regularServiceCharge;
```

👇

```js
if (summer()) charge = summerCharge();
else charge = regularCharge();
```

# Motivation

- 코드는 무엇이 일어나는지는 알려주지만, 왜 일어나는지 의도를 알려주지 않기 때문에 조건절이 복잡해진다
- 코드 블럭이 크다면 분해해서 함수로 감쌈으로서 함수이름이 의도 를 말해줄 수 있다

# Mechanics

- `Extract Function` 을 사용하여 각 조건절을 함수로 분리한다

# Example

- 여름과 겨울에 따른 요금 계산 예제

```js
if (!aDate.isBefore(plan.summerStart) && !aDate.isAfter(plan.summerEnd))
  charge = quantity * plan.summerRate;
else charge = quantity * plan.regularRate + plan.regularServiceCharge;
```

- if 절 조건을 함수로 변경

```js
if (summer()) charge = quantity * plan.summerRate;
else charge = quantity * plan.regularRate + plan.regularServiceCharge;

function summer() {
  return !aDate.isBefore(plan.summerStart) && !aDate.isAfter(plan.summerEnd);
}
```

- then 절 action을 함수로 변경

```js
if (summer()) charge = summerCharge();
//..
function summerCharge() {
  return quantity * plan.summerRate;
}
```

- else 절 action을 함수로 변경

```js
if (summer()) charge = summerCharge();
else charge = regularCharge();
//..
function regularCharge() {
  return quantity * plan.regularRate + plan.regularServiceCharge;
}
```

- 조건절을 ternary operator 로 변경
  - TODO: ternary 써도 괜찮은가?

```js
charge = summer() ? summerCharge() : regularCharge();
```
