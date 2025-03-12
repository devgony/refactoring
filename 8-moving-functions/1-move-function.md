> Move Function

```js
class Account {
  get overdraftCharge() {...}
```

👇

```js
class AccountType {
    get overdraftCharge() {...}
```

# Motivation

- 좋은 소프트웨어 디자인의 핵심은 모듈성이다
  - 프로그램의 작은 부분만 이해하고도 수정을 할 수 있게 함
- 모듈화를 어떻게 할지는 정해진 방법이 없다
  - 내가 하는 일을 더 잘 이해할수록 잘 그룹화를 할 수 있게 된다
- 이해도를 높이기 위해서는 각 요소들을 잘 이동시킬 줄 알아야 한다

- 모든 함수는 특정 context 에 포함되어있고 이는 global 혹은 특정 module 이다
- OOP 의 경우 핵심 모듈은 class
- nesting function 의 경우 또다른 context 를 만들어낸다
- 가장 직관적인 예시는 현재 머무는 context 보다 타 context 를 더 많이 참조하는 것
  - 많이 참조되는 쪽으로 옮기면 encapsulation 이 더 잘 이루어진다
- caller 가 어디 있는지도 중요
  - 다른 함수 안에 헬퍼로 정의된 함수는 자체로 가치를 가지기도 하므로 접근하기 더 쉬운곳으로 옮겨도 좋다
- 함수를 옮길 위치를 결정하는 것이 어렵지만, 선택이 어려울수록 옮겨야 될 중요성은 줄어드는 경우가 많다
  - 일단 하나의 context 에서 함수가 잘 어울리는지 살펴보고 나중에 옮겨도 된다
  - 적합한 옮길 곳이 없는 경우, `Combine Functions into Class`, `Extract Class` 등을 통해 context 를 새로 만들수도 있다

# Mechanics

- 옮길 함수를 참조하는 모든 함수를 검사한다
  - 꼭 옮겨야 하는지도 확인
  - 같이 옮겨야 하는 called function 있으면 먼저 옮긴다
    - 그래야 함수의 묶음을 옮길 때 의존성이 줄어든다
    - 상위 함수가 하위 함수의 유일한 caller 인 경우, 일단 inline function 수행하고 옮긴 후에 다시 재 추출 한다
- 추출할 함수가 다형성을 가졌는지 확인
  - OOP 인 경우 super-sub class 선언 관계를 주의
- 함수를 target context 에 복제 하고 상황에 맞게 변경
  - source context 를 참조 하는 경우, 인자로 값을 받거나, ref 를 source context 로 전달
  - 새 context 에서는 이름을 바꾸는 것도 중요
- Static check
- target 함수를 source 에서 어떻게 참조할 지 확인
- source 함수를 deletegating 함수로 변경
- Test
- Inline Function 으로 source 함수 제거

# Example: Moving a Nested Function to Top Level

- GPS 트래킹의 전체 거리를 계산하는 코드

```js
  function trackSummary(points) {
    const totalTime = calculateTime();
    const totalDistance = calculateDistance();
    const pace = totalTime / 60 /  totalDistance ;
    return {
      time: totalTime,
      distance: totalDistance,
      pace: pace
    };

    function calculateDistance() {
      let result = 0;
      for (let i = 1; i < points.length; i++) {
        result += distance(points[i-1], points[i]);
      }
      return result;
    }
    function distance(p1,p2) { ... }
    function radians(degrees) { ... }
    function calculateTime() { ... }
  }
```

- calculateDistance 를 top lv 로 이동
  - 옮길 때 top\_ 과 같은 임시 이름을 붙인다

```js
  function trackSummary(points) {
    const totalTime = calculateTime();
    const totalDistance = calculateDistance();
    const pace = totalTime / 60 /  totalDistance ;
    return {
      time: totalTime,
      distance: totalDistance,
      pace: pace
    };

    function calculateDistance() {
      let result = 0;
      for (let i = 1; i < points.length; i++) {
        result += distance(points[i-1], points[i]);
      }
      return result;
    }
    ...
    function distance(p1,p2) { ... }
    function radians(degrees) { ... }
    function calculateTime() { ... }
  }

  function top_calculateDistance() {
    let result = 0;
    for (let i = 1; i < points.length; i++) {
      result += distance(points[i-1], points[i]);
    }
    return result;
  }
```

- 참조하는 대상을 인자로 넘긴다: points

```js
function top_calculateDistance(points) {
  let result = 0;
  for (let i = 1; i < points.length; i++) {
    result += distance(points[i - 1], points[i]);
  }
  return result;
}
```

- distance 의 경우에는 도출하는 함수 자체를 옮긴다
  - distance 는 radians 만 참조하므로 같이 `top_calculateDistance` 로 옮긴다

```js
  function top_calculateDistance(points) {
    let result = 0;
    for (let i = 1; i < points.length; i++) {
      result += distance(points[i-1], points[i]);
    }
    return result;

    function distance(p1,p2) { ... }
    function radians(degrees) { ... }
  }
```

- calculateDistance 가 top_calculateDistance 를 바로 호출

```js
function trackSummary(points) {
    //...
    function calculateDistance() {
      return top_calculateDistance(points);
    }
```

- 다른 callers 가 새 함수를 부르도록 변경

```js
function trackSummary(points) {
    //...
    const totalDistance = top_calculateDistance(points);
```

- 적합한 이름으로 변경
  - totalDistance 변수를 inline 으로 없애고 그 이름을 새로운 함수에 적용

```diff
 function trackSummary(points) {
     const totalTime = calculateTime();
+    const pace = totalTime / 60 /  totalDistance(points) ;
     return {
       time: totalTime,
+      distance: totalDistance(points),
       pace: pace
     };

+  function totalDistance(points) {
     let result = 0;
     for (let i = 1; i < points.length; i++) {
       result += distance(points[i-1], points[i]);
     }
     return result;
```

- distance 와 radians 모두 totalDistance 에 대한 의존성이 없으므로 top lv 로 분리

```js
function trackSummary(points) { ... }
function totalDistance(points) { ... }
function distance(p1,p2) { ... }
function radians(degrees) { ... }
```

- visibility 때문에 두 함수를 그대로 두고 싶어 하는 경향도 있지만, nested function 은 숨겨진 데이터의 관계를 알기 어려우므로 top lv 로 올리는 것이 더 좋다

# Example: Moving between Classes

```js
// class Account…
  get bankCharge() {
    let result = 4.5;
    if (this._daysOverdrawn > 0) result += this.overdraftCharge;
    return result;
  }

  get overdraftCharge() {
    if (this.type.isPremium) {
      const baseCharge = 10;
      if (this.daysOverdrawn <= 7)
        return baseCharge;
      else
        return baseCharge + (this.daysOverdrawn - 7) * 0.85;
    }
    else
      return this.daysOverdrawn * 1.75;
  }
```

- 위 코드는 account type 에 따라 다른 값을 매기고 있으므로 overdraftCharge 를 account type class 로 옮긴다
- 누굴 같이 데려갈 지 정해야 하는데, daysOverdrawn 의 경우 각 account 에 따라 달라질 수 있으므로 그대로 둔다

```js
// class AccountType…
  overdraftCharge(daysOverdrawn) {
    if (this.isPremium) {
      const baseCharge = 10;
      if (daysOverdrawn <= 7)
        return baseCharge;
      else
        return baseCharge + (daysOverdrawn - 7) * 0.85;
    }
    else
      return daysOverdrawn * 1.75;
  }
```

- 원본 함수를 deletegating call 로 변경

```js
//class Account…
  get bankCharge() {
    let result = 4.5;
    if (this._daysOverdrawn > 0) result += this.overdraftCharge;
    return result;
  }

  get overdraftCharge() {
    return this.type.overdraftCharge(this.daysOverdrawn);
  }
```

- inline 으로 `overdraftCharge` 를 없앤다

```js
//class Account…
  get bankCharge() {
    let result = 4.5;
    if (this._daysOverdrawn > 0)
      result += this.type.overdraftCharge(this.daysOverdrawn);
    return result;
  }
```

- 만약 새 함수에 넘겨야 할 인자 개수가 너무 많다고 하면 account 전체를 넘겨도 좋다

```js
//class Account…
  get bankCharge() {
    let result = 4.5;
    if (this._daysOverdrawn > 0) result += this.overdraftCharge;
    return result;
  }

  get overdraftCharge() {
    return this.type.overdraftCharge(this);
  }
class AccountType…

  overdraftCharge(account) {
    if (this.isPremium) {
      const baseCharge = 10;
      if (account.daysOverdrawn <= 7)
        return baseCharge;
      else
        return baseCharge + (account.daysOverdrawn - 7) * 0.85;
    }
    else
      return account.daysOverdrawn * 1.75;
  }
```
