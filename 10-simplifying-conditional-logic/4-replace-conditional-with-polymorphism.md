> Replace Conditional with Polymorphism

```js
switch (bird.type) {
  case 'EuropeanSwallow':
    return "average";
  case 'AfricanSwallow':
    return (bird.numberOfCoconuts > 2) ? "tired" : "average";
  case 'NorwegianBlueParrot':
    return (bird.voltage > 100) ? "scorched" : "beautiful";
  default:
    return "unknown";
```

👇

```js
class EuropeanSwallow {
  get plumage() {
    return "average";
  }
}
class AfricanSwallow {
  get plumage() {
    return this.numberOfCoconuts > 2 ? "tired" : "average";
  }
}
class NorwegianBlueParrot {
  get plumage() {
    return this.voltage > 100 ? "scorched" : "beautiful";
  }
}
```

# Motivation

- 복잡한 조건문은 프로그래밍에서 가장 어려운 부분 중 하나이다
- 클래스와 다형성을 이용하면 더 명시적으로 분리하여 표현
- 공통 로직은 상속받고, 특정 로직은 오버라이드하여 사용할 수도 있다

# Mechanics

- 다형성을 위한 class 가 존재하지 않는다면, factory method 와 함께 정의
- factory method 를 통해 객체 생성
- 조건문의 함수를 superclass 로 이동

  - 함수의 형태가 아닌 경우 `Ex`tract Method`

- Subclass 하나를 골라 조건에 상응하는 메서드를 override 하여 기존 조건문의 내용을 붙여넣음
- 각 Subclass 에 대해 반복 수행
- default case 의 경우 superclass 의 메서드에 남겨놓음
  - superclass 를 abstract 로 정의하여 책임을 subclass 로 위임할 수도 있다

# Example

- 조류의 속도를 측정하는 예제

```js
function plumages(birds) {
  return new Map(birds.map((b) => [b.name, plumage(b)]));
}
function speeds(birds) {
  return new Map(birds.map((b) => [b.name, airSpeedVelocity(b)]));
}

function plumage(bird) {
  switch (bird.type) {
    case "EuropeanSwallow":
      return "average";
    case "AfricanSwallow":
      return bird.numberOfCoconuts > 2 ? "tired" : "average";
    case "NorwegianBlueParrot":
      return bird.voltage > 100 ? "scorched" : "beautiful";
    default:
      return "unknown";
  }
}

function airSpeedVelocity(bird) {
  switch (bird.type) {
    case "EuropeanSwallow":
      return 35;
    case "AfricanSwallow":
      return 40 - 2 * bird.numberOfCoconuts;
    case "NorwegianBlueParrot":
      return bird.isNailed ? 0 : 10 + bird.voltage / 10;
    default:
      return null;
  }
}
```

- 조류 종류에 따라 다른 속도와 깃털 상태를 반환하므로 다형성을 적용
  - `Combine Functions into Class`: Bird::{airSpeedVelocity, plumage}

```js
function plumage(bird) {
  return new Bird(bird).plumage;
}

function airSpeedVelocity(bird) {
  return new Bird(bird).airSpeedVelocity;
}

class Bird {
  constructor(birdObject) {
    Object.assign(this, birdObject);
  }
  get plumage() {
    switch (this.type) {
      case "EuropeanSwallow":
        return "average";
      case "AfricanSwallow":
        return this.numberOfCoconuts > 2 ? "tired" : "average";
      case "NorwegianBlueParrot":
        return this.voltage > 100 ? "scorched" : "beautiful";
      default:
        return "unknown";
    }
  }
  get airSpeedVelocity() {
    switch (this.type) {
      case "EuropeanSwallow":
        return 35;
      case "AfricanSwallow":
        return 40 - 2 * this.numberOfCoconuts;
      case "NorwegianBlueParrot":
        return this.isNailed ? 0 : 10 + this.voltage / 10;
      default:
        return null;
    }
  }
}
```

- 각 종류에 대한 subclass 를 생성
  - createBird 팩토리 메서드 추가

```js
function plumage(bird) {
  return createBird(bird).plumage;
}

function airSpeedVelocity(bird) {
  return createBird(bird).airSpeedVelocity;
}

function createBird(bird) {
  switch (bird.type) {
    case "EuropeanSwallow":
      return new EuropeanSwallow(bird);
    case "AfricanSwallow":
      return new AfricanSwallow(bird);
    case "NorweigianBlueParrot":
      return new NorwegianBlueParrot(bird);
    default:
      return new Bird(bird);
  }
}

class EuropeanSwallow extends Bird {}

class AfricanSwallow extends Bird {}

class NorwegianBlueParrot extends Bird {}
```

- plumage 의 EuropeanSwallow 부터 subclass 로 이동
  - 이동 후 일단은 throw 하여 비정상 접근을 방지

```js
class EuropeanSwallow…
  get plumage() {
    return "average";
  }

class Bird…
  get plumage() {
    switch (this.type) {
    case 'EuropeanSwallow':
      throw "oops";
    //..
```

- AfricanSwallow 의 plumage 도 subclass 로 이동

```js
class AfricanSwallow…
  get plumage() {
     return (this.numberOfCoconuts > 2) ? "tired" : "average";
  }
```

- NorwegianBlueParrot 의 plumage 도 subclass 로 이동

```js
class NorwegianBlueParrot…
  get plumage() {
     return (this.voltage > 100) ? "scorched" : "beautiful";
  }
```

- default case 는 superclass 에 남겨둠

```js
class Bird…
  get plumage() {
    return "unknown";
  }
```

- airSpeedVelocity 도 동일 작업 수행

```js
function plumages(birds) {
  return new Map(
    birds.map((b) => createBird(b)).map((bird) => [bird.name, bird.plumage]),
  );
}
function speeds(birds) {
  return new Map(
    birds
      .map((b) => createBird(b))
      .map((bird) => [bird.name, bird.airSpeedVelocity]),
  );
}

function createBird(bird) {
  switch (bird.type) {
    case "EuropeanSwallow":
      return new EuropeanSwallow(bird);
    case "AfricanSwallow":
      return new AfricanSwallow(bird);
    case "NorwegianBlueParrot":
      return new NorwegianBlueParrot(bird);
    default:
      return new Bird(bird);
  }
}

class Bird {
  constructor(birdObject) {
    Object.assign(this, birdObject);
  }
  get plumage() {
    return "unknown";
  }
  get airSpeedVelocity() {
    return null;
  }
}
class EuropeanSwallow extends Bird {
  get plumage() {
    return "average";
  }
  get airSpeedVelocity() {
    return 35;
  }
}

class AfricanSwallow extends Bird {
  get plumage() {
    return this.numberOfCoconuts > 2 ? "tired" : "average";
  }
  get airSpeedVelocity() {
    return 40 - 2 * this.numberOfCoconuts;
  }
}
class NorwegianBlueParrot extends Bird {
  get plumage() {
    return this.voltage > 100 ? "scorched" : "beautiful";
  }
  get airSpeedVelocity() {
    return this.isNailed ? 0 : 10 + this.voltage / 10;
  }
}
```

- 현재의 경우에 superclass Bird 는 필수 요소가 아니지만 관련된 도메인에 대한 설명을 위해 남겨놓는다

# Example: Using Polymorphism for Variation

- 한 객체가 다른 객체와 대부분 유사하지만 약간의 차이가 있는 경우
- risk 와 potential 에 따라 A 혹은 B 를 평가하는 예제

```js
function rating(voyage, history) {
  const vpf = voyageProfitFactor(voyage, history);
  const vr = voyageRisk(voyage);
  const chr = captainHistoryRisk(voyage, history);
  if (vpf * 3 > vr + chr * 2) return "A";
  else return "B";
}

function voyageRisk(voyage) {
  let result = 1;
  if (voyage.length > 4) result += 2;
  if (voyage.length > 8) result += voyage.length - 8;
  if (["china", "east-indies"].includes(voyage.zone)) result += 4;
  return Math.max(result, 0);
}
function captainHistoryRisk(voyage, history) {
  let result = 1;
  if (history.length < 5) result += 4;
  result += history.filter((v) => v.profit < 0).length;
  if (voyage.zone === "china" && hasChina(history)) result -= 2;
  return Math.max(result, 0);
}
function hasChina(history) {
  return history.some((v) => "china" === v.zone);
}
function voyageProfitFactor(voyage, history) {
  let result = 2;
  if (voyage.zone === "china") result += 1;
  if (voyage.zone === "east-indies") result += 1;
  if (voyage.zone === "china" && hasChina(history)) {
    result += 3;
    if (history.length > 10) result += 1;
    if (voyage.length > 12) result += 1;
    if (voyage.length > 18) result -= 1;
  } else {
    if (history.length > 8) result += 1;
    if (voyage.length > 14) result -= 1;
  }
  return result;
}
```

- voyageRisk 와 captainHistoryRisk 는 risk 에 대한 점수를 매기고, voyageProfitFactor 는 profit 에 대한 점수를 매긴다. rating 은 모두를 합쳐 전체 점수를 매긴다.
- client 코드는 아래와 같다

```js
const voyage = { zone: "west-indies", length: 10 };
const history = [
  { zone: "east-indies", profit: 5 },
  { zone: "west-indies", profit: 15 },
  { zone: "china", profit: -2 },
  { zone: "west-africa", profit: 7 },
];

const myRating = rating(voyage, history);
```

- 선장이 이전에 중국에 다녀온 적이 있는 중국행 항해의 경우를 처리하는 조건절이 몇군데 인지 확인

```diff
function captainHistoryRisk(voyage, history) {
  let result = 1;
  if (history.length < 5) result += 4;
  result += history.filter((v) => v.profit < 0).length;
+ if (voyage.zone === "china" && hasChina(history)) result -= 2;
  return Math.max(result, 0);
}

function voyageProfitFactor(voyage, history) {
  let result = 2;
  if (voyage.zone === "china") result += 1;
  if (voyage.zone === "east-indies") result += 1;
+ if (voyage.zone === "china" && hasChina(history)) {
    result += 3;
    if (history.length > 10) result += 1;
    if (voyage.length > 12) result += 1;
    if (voyage.length > 18) result -= 1;
  } else {
    if (history.length > 8) result += 1;
    if (voyage.length > 14) result -= 1;
  }
  return result;
}
```

- `Combine Functions into Class`: Rating::{voyageRisk, captainHistoryRisk, voyageProfitFactor}

```js
function rating(voyage, history) {
  return new Rating(voyage, history).value;
}

class Rating {
  constructor(voyage, history) {
    this.voyage = voyage;
    this.history = history;
  }
  get value() {
    const vpf = this.voyageProfitFactor;
    const vr = this.voyageRisk;
    const chr = this.captainHistoryRisk;
    if (vpf * 3 > vr + chr * 2) return "A";
    else return "B";
  }
  get voyageRisk() {
    let result = 1;
    if (this.voyage.length > 4) result += 2;
    if (this.voyage.length > 8) result += this.voyage.length - 8;
    if (["china", "east-indies"].includes(this.voyage.zone)) result += 4;
    return Math.max(result, 0);
  }
  get captainHistoryRisk() {
    let result = 1;
    if (this.history.length < 5) result += 4;
    result += this.history.filter((v) => v.profit < 0).length;
    if (this.voyage.zone === "china" && this.hasChinaHistory) result -= 2;
    return Math.max(result, 0);
  }
  get voyageProfitFactor() {
    let result = 2;

    if (this.voyage.zone === "china") result += 1;
    if (this.voyage.zone === "east-indies") result += 1;
    if (this.voyage.zone === "china" && this.hasChinaHistory) {
      result += 3;
      if (this.history.length > 10) result += 1;
      if (this.voyage.length > 12) result += 1;
      if (this.voyage.length > 18) result -= 1;
    } else {
      if (this.history.length > 8) result += 1;
      if (this.voyage.length > 14) result -= 1;
    }
    return result;
  }

  get hasChinaHistory() {
    return this.history.some((v) => "china" === v.zone);
  }
}
```

- 빈 subclass 생성

```js
class ExperiencedChinaRating extends Rating {}
```

- factory 함수 생성

```js
function createRating(voyage, history) {
  if (voyage.zone === "china" && history.some((v) => "china" === v.zone))
    return new ExperiencedChinaRating(voyage, history);
  else return new Rating(voyage, history);
}
```

- caller 를 factory 함수로 변경

```js
function rating(voyage, history) {
  return createRating(voyage, history).value;
}
```

- `ExperiencedChinaRating` 을 override

```diff
class ExperiencedChinaRating
+ get captainHistoryRisk() {
+   const result =  super.captainHistoryRisk - 2;
+   return Math.max(result, 0);
+ }

class Rating…
  get captainHistoryRisk() {
    let result = 1;
    if (this.history.length < 5) result += 4;
    result += this.history.filter(v => v.profit < 0).length;
-   if (this.voyage.zone === "china" && this.hasChinaHistory) result -= 2;
    return Math.max(result, 0);
  }
```

- `voyageProfitFactor` 의 경우 로직이 복잡하므로 먼저 `Extract Function`

```diff
class Rating…
  get voyageProfitFactor() {
    let result = 2;

    if (this.voyage.zone === "china") result += 1;
    if (this.voyage.zone === "east-indies") result += 1;
+   result += this.voyageAndHistoryLengthFactor;
    return result;
  }

+ get voyageAndHistoryLengthFactor() {
+   let result = 0;
+   if (this.voyage.zone === "china" && this.hasChinaHistory) {
+     result += 3;
+     if (this.history.length > 10) result += 1;
+     if (this.voyage.length > 12) result += 1;
+     if (this.voyage.length > 18) result -= 1;
+   }
+   else {
+     if (this.history.length > 8) result += 1;
+     if (this.voyage.length > 14) result -= 1;
+   }
+   return result;
+ }
```

- 각 class 별로 다른 구현을 적용

```js
class Rating…
  get voyageAndHistoryLengthFactor() {
    let result = 0;
    if (this.history.length > 8) result += 1;
    if (this.voyage.length > 14) result -= 1;
    return result;
  }

class ExperiencedChinaRating…
  get voyageAndHistoryLengthFactor() {
    let result = 0;
    result += 3;
    if (this.history.length > 10) result += 1;
    if (this.voyage.length > 12) result += 1;
    if (this.voyage.length > 18) result -= 1;
    return result;
  }
```

- 함수명에 And 가 들어가는 두 개의 책임을 분리하는게 좋다

  - `Extract Function`

```diff
class Rating…
  get voyageAndHistoryLengthFactor() {
    let result = 0;
+   result += this.historyLengthFactor;
    if (this.voyage.length > 14) result -= 1;
    return result;
  }
+ get historyLengthFactor() {
+   return (this.history.length > 8) ? 1 : 0;
+ }
```

- subclass 에서도 `Extract Function`

```diff
class ExperiencedChinaRating…

  get voyageAndHistoryLengthFactor() {
    let result = 0;
    result += 3;
+   result += this.historyLengthFactor;
    if (this.voyage.length > 12) result += 1;
    if (this.voyage.length > 18) result -= 1;
    return result;
  }
+ get historyLengthFactor() {
+   return (this.history.length > 10) ? 1 : 0;
+ }
```

- `Move Statements to Callers`: sub -> super

```diff
class Rating…
  get voyageProfitFactor() {
    let result = 2;
    if (this.voyage.zone === "china") result += 1;
    if (this.voyage.zone === "east-indies") result += 1;
+   result += this.historyLengthFactor;
    result += this.voyageAndHistoryLengthFactor;
    return result;
  }

  get voyageAndHistoryLengthFactor() {
    let result = 0;
-   result += this.historyLengthFactor;
    if (this.voyage.length > 14) result -= 1;
    return result;
  }

class ExperiencedChinaRating…
  get voyageAndHistoryLengthFactor() {
    let result = 0;
    result += 3;
-   result += this.historyLengthFactor;
    if (this.voyage.length > 12) result += 1;
    if (this.voyage.length > 18) result -= 1;
    return result;
  }
```

- `Rename Function`
  - super.voyageLenthFactor의 body 는 ternary 로 변경

```diff
class Rating…
  get voyageProfitFactor() {
    let result = 2;
    if (this.voyage.zone === "china") result += 1;
    if (this.voyage.zone === "east-indies") result += 1;
    result += this.historyLengthFactor;
+   result += this.voyageLengthFactor;
    return result;
  }

+ get voyageLengthFactor() {
    return (this.voyage.length > 14) ? - 1: 0;
  }

class ExperiencedChinaRating…
+ get voyageLengthFactor() {
    let result = 0;
    result += 3;
    if (this.voyage.length > 12) result += 1;
    if (this.voyage.length > 18) result -= 1;
    return result;
  }

```

- `Move Statements to Callers`: 3 은 caller 쪽에서 최종 결과에 더하는게 자연스럽다

```diff
class ExperiencedChinaRating…
+ get voyageProfitFactor() {
+   return super.voyageProfitFactor + 3;
+ }

  get voyageLengthFactor() {
    let result = 0;
-   result += 3;
    if (this.voyage.length > 12) result += 1;
    if (this.voyage.length > 18) result -= 1;
    return result;
  }
```

- refactoring 이 끝난 최종 결과물

```js
class Rating {
  constructor(voyage, history) {
    this.voyage = voyage;
    this.history = history;
  }
  get value() {
    const vpf = this.voyageProfitFactor;
    const vr = this.voyageRisk;
    const chr = this.captainHistoryRisk;
    if (vpf * 3 > vr + chr * 2) return "A";
    else return "B";
  }
  get voyageRisk() {
    let result = 1;
    if (this.voyage.length > 4) result += 2;
    if (this.voyage.length > 8) result += this.voyage.length - 8;
    if (["china", "east-indies"].includes(this.voyage.zone)) result += 4;
    return Math.max(result, 0);
  }
  get captainHistoryRisk() {
    let result = 1;
    if (this.history.length < 5) result += 4;
    result += this.history.filter((v) => v.profit < 0).length;
    return Math.max(result, 0);
  }
  get voyageProfitFactor() {
    let result = 2;
    if (this.voyage.zone === "china") result += 1;
    if (this.voyage.zone === "east-indies") result += 1;
    result += this.historyLengthFactor;
    result += this.voyageLengthFactor;
    return result;
  }
  get voyageLengthFactor() {
    return this.voyage.length > 14 ? -1 : 0;
  }
  get historyLengthFactor() {
    return this.history.length > 8 ? 1 : 0;
  }
}

class ExperiencedChinaRating extends Rating {
  get captainHistoryRisk() {
    const result = super.captainHistoryRisk - 2;
    return Math.max(result, 0);
  }
  get voyageLengthFactor() {
    let result = 0;
    if (this.voyage.length > 12) result += 1;
    if (this.voyage.length > 18) result -= 1;
    return result;
  }
  get historyLengthFactor() {
    return this.history.length > 10 ? 1 : 0;
  }
  get voyageProfitFactor() {
    return super.voyageProfitFactor + 3;
  }
}
```
