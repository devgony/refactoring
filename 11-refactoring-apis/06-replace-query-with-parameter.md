> Replace Query with Parameter

```js
targetTemperature(aPlan)

function targetTemperature(aPlan) {
  currentTemperature = thermostat.currentTemperature;
  // rest of function...
```

👇

```js
targetTemperature(aPlan, thermostat.currentTemperature)

function targetTemperature(aPlan, currentTemperature) {
  // rest of function...
```

# Motivation

- global var, 다른모듈로 이동 예정인 개체 등을 참조하는 것은 불편하다
  - 내부 참조를 인수로 변경
  - 책임을 호출자쪽으로 이동
- 대부분 의존성 관계 변경을 원할 때 발생
- 쉬운 결정이 아니기 때문에 이해도를 높이면서 안정적으로 변경하는 것이 중요하다
- body에서 참조투명성이 부족한 요소에 접근하면 그 함수 역시도 참조투명성이 낮아진다

  - `Replace Query with Parameter` 를 통해 인자로 변경하고 책임을 외부로 보내어 해결
  - eg) I/O 또는 기타 가변 요소를 처리하는 로직으로 감싸진 순수 함수 구성

- 단, 호출자는 어떻게 인자를 보낼지 결정해야 하므로 호출자가 함수를 사용하기 더 어려워진다
  - 저자는 호출자가 더 쉬운 인터페이스를 추구한다

# Mechanics

- `Extract Variable` 을 통해 query 부분을 변수로 분리
- `Extract Function` 을 통해 query 가 아닌 부분을 함수로 분리
- `Inline Variable` 을 통해 query 변수를 제거
- `Inline Function` 을 통해 원본 함수를 제거
- 새 함수명을 기존 이름으로 변경

# Example

- 특정 범위만 선택가능한 온도조절기 예제

```js
class HeatingPlan…
  get targetTemperature() {
    if      (thermostat.selectedTemperature >  this._max) return this._max;
    else if (thermostat.selectedTemperature <  this._min) return this._min;
    else return thermostat.selectedTemperature;
  }

caller…
  if      (thePlan.targetTemperature > thermostat.currentTemperature) setToHeat();
  else if (thePlan.targetTemperature < thermostat.currentTemperature) setToCool();
  else setOff();
```

- `targetTemperature` 함수가 thermostat 전역변수에 의존하는것이 문제

- `Extract Variable` 을 통해 인자로 전환할 부분 추출

```diff
class HeatingPlan…
  get targetTemperature() {
+   const selectedTemperature = thermostat.selectedTemperature;
+   if      (selectedTemperature >  this._max) return this._max;
+   else if (selectedTemperature <  this._min) return this._min;
+   else return selectedTemperature;
  }
```

- 인자 부분을 제외하고 `Extract Function`

```diff
class HeatingPlan…
  get targetTemperature() {
    const selectedTemperature = thermostat.selectedTemperature;
+   return this.xxNEWtargetTemperature(selectedTemperature);
  }
+ xxNEWtargetTemperature(selectedTemperature) {
    if      (selectedTemperature >  this._max) return this._max;
    else if (selectedTemperature <  this._min) return this._min;
    else return selectedTemperature;
  }
```

- 추출했던 변수를 다시 `Inline Variable`

```diff
class HeatingPlan…
  get targetTemperature() {
+   return this.xxNEWtargetTemperature(thermostat.selectedTemperature);
  }
```

- `Inline Function`

```diff
caller…
+ if      (thePlan.xxNEWtargetTemperature(thermostat.selectedTemperature) >
           thermostat.currentTemperature)
    setToHeat();
+ else if (thePlan.xxNEWtargetTemperature(thermostat.selectedTemperature) <
           thermostat.currentTemperature)
    setToCool();
  else
    setOff();
```

- `Rename` to original function name

```diff
caller…
+ if      (thePlan.targetTemperature(thermostat.selectedTemperature) >
           thermostat.currentTemperature)
    setToHeat();
+ else if (thePlan.targetTemperature(thermostat.selectedTemperature) <
           thermostat.currentTemperature)
    setToCool();
  else
    setOff();
class HeatingPlan…
+ targetTemperature(selectedTemperature) {
    if      (selectedTemperature >  this._max) return this._max;
    else if (selectedTemperature <  this._min) return this._min;
    else return selectedTemperature;
  }
```

- 이제 `targetTemperature` 는 참조투명성이 보장된다
