> Introduce Parameter Object

```js
function amountInvoiced(startDate, endDate) {...}
function amountReceived(startDate, endDate) {...}
function amountOverdue(startDate, endDate) {...}
```

👇

```js
function amountInvoiced(aDateRange) {...}
function amountReceived(aDateRange) {...}
function amountOverdue(aDateRange) {...}
```

# Motivation

- 항상 함께 전달되는 매개변수들은 하나의 객체로 묶어주는 것이 좋다
- 각 데이터 간에 명시적인 관계를 만들어준다
- 해당 객체를 참조하는 코드에서 일관성을 유지할 수 있다
- 함수에 전달해야 하는 인자의 개수를 줄여준다
- 묶인 데이터를 사용하는 공통 함수를 모아서 추상화를 할 수 있다

# Mechanics

- 기존에 적절한 객체가 없다면 새로운 객체를 생성한다
  - class 를 사용하는것이 권장되며 그래야 행동을 묶기도 쉽다.
  - 이러한 객체를 Value Object 라고 한다
- 테스트 수행
- `Change Function Declaration` 을 통해 새로운 객체를 위한 인자를 추가한다
- 테스트 수행
- 각 호출자에 대해 새 객체에 대한 instance를 전달해보고 테스트한다
- 새 객체의 일부 필드를 사용하는 코드도 새 객체의 필드를 사용하도록 변경한다

# Example

- 작동범위를 벗어나는 온도 데이터를 찾는 코드

```js
const station = {
  name: "ZB1",
  readings: [
    { temp: 47, time: "2016-11-10 09:10" },
    { temp: 53, time: "2016-11-10 09:20" },
    { temp: 58, time: "2016-11-10 09:30" },
    { temp: 53, time: "2016-11-10 09:40" },
    { temp: 51, time: "2016-11-10 09:50" },
  ],
};
```

```js
function readingsOutsideRange(station, min, max) {
  return station.readings.filter((r) => r.temp < min || r.temp > max);
}
```

```js
// caller
alerts = readingsOutsideRange(
  station,
  operatingPlan.temperatureFloor,
  operatingPlan.temperatureCeiling,
);
```

- 이러한 범위 개념을 가진 인자들은 하나로 모아주는 것이 좋다
  - 동작을 옮길수도 있으므로 object 가 아닌 clss 로 선언 하는 습관
  - 하지만 현재는 VO 로 생성할 것이므로 setter 를 만들지는 않는다

```js
class NumberRange {
  constructor(min, max) {
    this._data = { min: min, max: max };
  }
  get min() {
    return this._data.min;
  }
  get max() {
    return this._data.max;
  }
}
```

- `Change Function Declaration` 을 통해 새로운 객체를 위한 인자를 추가한다

```js
function readingsOutsideRange(station, min, max, range) {
  return station.readings.filter((r) => r.temp < min || r.temp > max);
}
```

```js
// caller
alerts = readingsOutsideRange(
  station,
  operatingPlan.temperatureFloor,
  operatingPlan.temperatureCeiling,
  null,
);
```

- 테스트 성공 확인 후

```js
// caller
const range = new NumberRange(
  operatingPlan.temperatureFloor,
  operatingPlan.temperatureCeiling,
);
alerts = readingsOutsideRange(
  station,
  operatingPlan.temperatureFloor,
  operatingPlan.temperatureCeiling,
  range,
);
```

- range 의 필드를 사용하도록 변경

```js
function readingsOutsideRange(station, min, max, range) {
  return station.readings.filter((r) => r.temp < min || r.temp > range.max);
}
```

```js
// caller
alerts = readingsOutsideRange(
  station,
  operatingPlan.temperatureFloor,
  operatingPlan.temperatureCeiling,
  null,
);
```

```diff
// caller
+const range = new NumberRange(
+  operatingPlan.temperatureFloor,
+  operatingPlan.temperatureCeiling,
+);
alerts = readingsOutsideRange(
  station,
  operatingPlan.temperatureFloor,
  operatingPlan.temperatureCeiling,
+ range,
);
```

- range.max 사용하도록 변경

```diff
function readingsOutsideRange(station,
    min,
-   max,
    range) {
  return station.readings.filter((r) => r.temp < min || r.temp > range.max);
}
```

```diff
// caller
const range = new NumberRange(
  operatingPlan.temperatureFloor,
_ operatingPlan.temperatureCeiling,
);
alerts = readingsOutsideRange(
  station,
  operatingPlan.temperatureFloor,
- operatingPlan.temperatureCeiling,
  range,
);
```

- range.min 사용으로 변경

```diff
function readingsOutsideRange(station,
-   min,
    range) {
        return station.readings
        .filter(r => r.temp < range.min || r.temp > range.max);
    }

```

```diff
const range = new NumberRange(operatingPlan.temperatureFloor, operatingPlan.temperatureCeiling);
alerts = readingsOutsideRange(station,
-                           operatingPlan.temperatureFloor,
                            range);
```

- validation 행동을 옮길 수 있다

```js
function readingsOutsideRange(station, range) {
  return station.readings
    .filter(r => !range.contains(r.temp));
}

// class NumberRange…
  contains(arg) {return (arg >= this.min && arg <= this.max);}
```

- temperatureFloor 와 temperatureCeiling 을 범위로 묶는 등 추가 리팩토링 요소들을 찾을 수 있다
