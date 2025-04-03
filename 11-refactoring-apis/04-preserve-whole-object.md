> Preserve Whole Object

```js
const low = aRoom.daysTempRange.low;
const high = aRoom.daysTempRange.high;
if (aPlan.withinRange(low, high))
```

👇

```js
if (aPlan.withinRange(aRoom.daysTempRange))
```

# Motivation

- 여러 인자를 필요로 하는 함수는 데이터를 묶어서 전달하는 것이 향후 변경에 유연하다
  - 함수 시그니처를 변경하지 않아도 된다
- 인자 개수를 줄일 수 있다.
- 분리되어있으면 로직이 중복되는 경우가 많다
- 다른 모듈일 경우는 객체 전체의 의존성이 생기기 때문에 지양해야 할 수 있다
- 로직을 객체 밖으로 꺼내서 무언가를 한다면 로직 전체가 그 객체 안으로 들어가야 할 수 있다
- 보통 `Introduce Parameter Object` 이후에 `Preserve Whole Object` 를 수행한다
- 일부 코드가 공통되는 경우 `Extract Class`
- 로직 내에서 현재 객체를 사용하는 경우 this 와 같은 self-reference 를 사용할 수도 있다

# Mechanics

- 원하는 인자와 함께 빈 함수를 임시 이름으로 생성
- 새 함수의 bdoy 에서 원래 함수를 호출하도록 변경
- Static check
- 각 요청자가가새 함수를 사용
- 모든 요청자가 갱신되면 원본함수에 대해 `Inline Function` 수행
- 새 함수의 이름을 적절하게 변경

# Example

- 방 온도 모니터링 예시

```js
// caller…
  const low = aRoom.daysTempRange.low;
  const high = aRoom.daysTempRange.high;
  if (!aPlan.withinRange(low, high))
    alerts.push("room temperature went outside range");

// class HeatingPlan…
  withinRange(bottom, top) {
    return (bottom >= this._temperatureRange.low) && (top <= this._temperatureRange.high);
  }
```

- bottom, top 의 범위를 하나 로 묶어서 전달하기 위해 새 함수 생성

```js
// class HeatingPlan…
  xxNEWwithinRange(aNumberRange) {
  }
```

- 기존 함수를 그대로 호출

```js
class HeatingPlan…
  xxNEWwithinRange(aNumberRange) {
    return this.withinRange(aNumberRange.low, aNumberRange.high);
  }
```

- 요청자가 신규 함수를 부르도록 변경

```js
// caller…
const low = aRoom.daysTempRange.low;
const high = aRoom.daysTempRange.high;
if (!aPlan.xxNEWwithinRange(aRoom.daysTempRange))
  alerts.push("room temperature went outside range");
```

- `Remove Dead Code`: 원본함수에서 불필요한부분 제거

```diff
caller…
- const low = aRoom.daysTempRange.low;
- const high = aRoom.daysTempRange.high;
  if (!aPlan.xxNEWwithinRange(aRoom.daysTempRange))
    alerts.push("room temperature went outside range");
```

- 원본 함수를 `Inline Function`

```diff
class HeatingPlan…
  xxNEWwithinRange(aNumberRange) {
+   return (aNumberRange.low >= this._temperatureRange.low) &&
+     (aNumberRange.high <= this._temperatureRange.high);
  }
```

- xxNEWwithinRange -> withinRange 로 이름 변경

# Example: A Variation to Create the New Function

- refactring 을 통해 새 함수를 구성하는 경우

```js
caller…
  const low = aRoom.daysTempRange.low;
  const high = aRoom.daysTempRange.high;
  if (!aPlan.withinRange(low, high))
    alerts.push("room temperature went outside range");
```

- `Extract Variable`

```diff
caller…
  const low = aRoom.daysTempRange.low;
  const high = aRoom.daysTempRange.high;
+ const isWithinRange = aPlan.withinRange(low, high);
+ if (!isWithinRange)
    alerts.push("room temperature went outside range");
```

- Extract Input Parameter

```diff
caller…
+ const tempRange = aRoom.daysTempRange;
+ const low = tempRange.low;
+ const high = tempRange.high;
  const isWithinRange = aPlan.withinRange(low, high);
  if (!isWithinRange)
    alerts.push("room temperature went outside range");
```

- `Extract Function` 을 통해 새 함수를 생성

```diff
caller…
  const tempRange = aRoom.daysTempRange;
+ const isWithinRange = xxNEWwithinRange(aPlan, tempRange);
  if (!isWithinRange)
    alerts.push("room temperature went outside range");

top level…
+ function xxNEWwithinRange(aPlan, tempRange) {
    const low = tempRange.low;
    const high = tempRange.high;
    const isWithinRange = aPlan.withinRange(low, high);
    return isWithinRange;
  }
```

- `Move Function`: 원본함수가 있던 위치로 새 함수를 이동

```diff
caller…
  const tempRange = aRoom.daysTempRange;
+ const isWithinRange = aPlan.xxNEWwithinRange(tempRange);
  if (!isWithinRange)
    alerts.push("room temperature went outside range");

+class HeatingPlan…
+  xxNEWwithinRange(tempRange) {
+   const low = tempRange.low;
+   const high = tempRange.high;
+   const isWithinRange = this.withinRange(low, high);
+   return isWithinRange;
+ }
```

- 요청자 새 함수로 전환
- `Inline Function`
- 리팩토링 과 함께 진행되므로 더 간편하다
