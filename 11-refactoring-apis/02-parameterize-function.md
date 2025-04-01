> Parameterize Function

```js
function tenPercentRaise(aPerson) {
  aPerson.salary = aPerson.salary.multiply(1.1);
}
function fivePercentRaise(aPerson) {
  aPerson.salary = aPerson.salary.multiply(1.05);
}
```

👇

```js
function raise(aPerson, factor) {
  aPerson.salary = aPerson.salary.multiply(1 + factor);
}
```

# Motivation

- 유사한 로직의 함수들이 있는 경우 파라미터로 다양한 경우를 처리

# Mechanics

- 유사한 함수들을 선정한다
- `Chnage Function Declaration`: literal 값을 파라미터로 변경
- 각 함수 호출부에 literal value 추가
- Test
- 주어진 파라미터를 함수 body 에서 사용하도록 변경
- 각 유사한 한 함수들에서 파라미터를 사용하도록 변경

# Example

```js
function tenPercentRaise(aPerson) {
  aPerson.salary = aPerson.salary.multiply(1.1);
}

function fivePercentRaise(aPerson) {
  aPerson.salary = aPerson.salary.multiply(1.05);
}
```

- factor 를 인자로 받도록 변경

```js
function raise(aPerson, factor) {
  aPerson.salary = aPerson.salary.multiply(1 + factor);
}
```

- 더 개선 가능한 또다른 예제

```js
function baseCharge(usage) {
  if (usage < 0) return usd(0);
  const amount =
    bottomBand(usage) * 0.03 + middleBand(usage) * 0.05 + topBand(usage) * 0.07;
  return usd(amount);
}
function bottomBand(usage) {
  return Math.min(usage, 100);
}
function middleBand(usage) {
  return usage > 100 ? Math.min(usage, 200) - 100 : 0;
}
function topBand(usage) {
  return usage > 200 ? usage - 200 : 0;
}
```

- 범위의 개념을 가지므로 middleBand 부터 `Change Function Declaration` 수행

```diff
+function withinBand(usage, bottom, top) {
  return usage > 100 ? Math.min(usage, 200) - 100 : 0;
}

function baseCharge(usage) {
  if (usage < 0) return usd(0);
  const amount =
    bottomBand(usage) * 0.03 +
+   withinBand(usage, 100, 200) * 0.05 +
    topBand(usage) * 0.07;
  return usd(amount);
}
```

- 각 literal 을 인자로 변경

```diff
function withinBand(usage, bottom, top) {
+ return usage > bottom ? Math.min(usage, 200) - bottom : 0;
}

function withinBand(usage, bottom, top) {
+ return usage > bottom ? Math.min(usage, top) - bottom : 0;
}
```

- 호출부 변경
  - Top 의 경우 Infinity 사용

```diff
function baseCharge(usage) {
  if (usage < 0) return usd(0);
  const amount =
+   withinBand(usage, 0, 100) * 0.03 +
    withinBand(usage, 100, 200) * 0.05 +
+   withinBand(usage, 200, Infinity) * 0.07;
  return usd(amount);
}
```

- 불필요해진 guard clause 를 삭제해도 되지만 문서용으로 남겨둠
