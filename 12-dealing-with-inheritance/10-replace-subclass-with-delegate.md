> Replace Subclass with Delegate

```js
class Order {
  get daysToShip() {
    return this._warehouse.daysToShip;
  }
}

class PriorityOrder extends Order {
  get daysToShip() {
    return this._priorityPlan.daysToShip;
  }
}
```

👇

```js
class Order {
  get daysToShip() {
    return this._priorityDelegate
      ? this._priorityDelegate.daysToShip
      : this._warehouse.daysToShip;
  }
}

class PriorityOrderDelegate {
  get daysToShip() {
    return this._priorityPlan.daysToShip;
  }
}
```

# Motivation

1. 상속은 단방향으로만 쓰인다. 사람을 나이로도 상속하고 소득수준으로도 상속할 수는 없다
2. 상속은 class 간의 관계를 너무 가깝게 만든다
   - 부모에 만든 변경은 자식을 쉽게 부순다
   - 두 클래스가 다른 모듈에 있어서 각자 다른팀이 나중에 인지하면 더 최악이다

- 위임은 위 문제들을 해결
- 위임으로 다양한 다른 클래스들을 각자 다른 이유로 위임 할 수 있다
- 위임은 객체간에 정규 관계를 맺는다
  - 명확한 인터페이스를 만들 수 있다
- 객체 합성을 상속보다 선호하라
  - 그렇다고 상속을 지양하라는 건 아니다
  - 일단 상속을 하고 나중에 얼마든지 위임을 통해 합성으로 바꿀 수 있음
- Gang of Four Book 에서는 `Replace subclass with the State or Strategy patterns` 라고 부름
- 모든 경우가 위임에 상속 계층 구조를 포함하는 것은 아니지만, 상태 또는 전략에 대한 계층 구조를 설정하는 것이 유용한 경우가 많다

# Mechanics

- 생성자 호출이 많은 경우 `Replace Constructor with Factory Function`
- 위임을 위한 빈 class 생성
  - 생성자는 subclass 한정의 인수도 포함: superclass 에 대한 back-ref
- superclass 를 들고있기 위한 필드 생성
- subclass 생성 시 위임 필드에 위임 인스턴스를 할당
  - factory function 혹은 생성자 내 에서 수행
- 위임 class 를 옮길 subclass 선정
- `Move Function`: 위임 class 이동
  - 위임하는 코드의 원본은 제거하지 않는다
- 원본 메서드가 class 밖에 요청자가 있다면, 위임하는 코드를 subclass 에서 superclass 로 옮긴다.

  - 대신 delegrate 가 존재하는지에 대한 guard 필요
    - 그렇지 않은 경우 `Remove Dead Code`

- Test
- subclass 의 모든 메서드가 사라질때까지 반복
- subclass 의 생성자에 대한 모든 요청자를 찾아 superclass 생성자로 변경
- Test
- `Remove Dead Code`: subclass 제거

# Example

```js
class Booking…
  constructor(show, date) {
    this._show = show;
    this._date = date;
  }

  get hasTalkback() {
    return this._show.hasOwnProperty('talkback') && !this.isPeakDay;
  }

  get basePrice() {
    let result = this._show.price;
    if (this.isPeakDay) result += Math.round(result * 0.15);
    return result;
  }
```

- subclass 에서 변화되는게 거의 없다
  - 약간의 로직이 override 됨
  - hasDinner 의 경우 신규 추가

```js
class PremiumBooking extends Booking…
  constructor(show, date, extras) {
    super(show, date);
    this._extras = extras;
  }

  get hasTalkback() {
    return this._show.hasOwnProperty('talkback');
  }

  get basePrice() {
    return Math.round(super.basePrice + this._extras.premiumFee);
  }

  get hasDinner() {
    return this._extras.hasOwnProperty('dinner') && !this.isPeakDay;
  }
```

- subclass 를 알아야지만 이해되는 부분이 superclass 에 존재한다
  - 여러 메서드들이 subclass 를 쉽게 구현하기 위해 존재하는 것처럼 보임
- PremiumBooking 보다 더 중요한 subclass 를 구현해야 한다면?
- 일반 Booking 과 Premium 을 자유롭게 전환해야 한다면?
  - Subclass 를 위임으로 변경한다

```js
booking client

  aBooking = new Booking(show,date);
premium client

  aBooking = new PremiumBooking(show, date, extras);
```

- 위 client 와의 호환을 위해 `Replace Constructor with Factory Function`

```diff
top level…
+ function createBooking(show, date) {
    return new Booking(show, date);
  }
+ function createPremiumBooking(show, date, extras) {
    return new PremiumBooking (show, date, extras);
  }

booking client
+ aBooking = createBooking(show, date);

premium client
+ aBooking = createPremiumBooking(show, date, extras);
```

- 새 위임 class 를 생성

  - superclass 의 data 에 접근해야 하는 경우를 대비해서 back-ref(`_host`) 를 추가

```diff
class PremiumBookingDelegate…
  constructor(hostBooking, extras) {
+   this._host = hostBooking;
    this._extras = extras;
  }
```

- factory function 에서 bePremium 을 호출하여 delete 를 연결한다

```diff
top level…
  function createPremiumBooking(show, date, extras) {
+   const result = new PremiumBooking (show, date, extras);
+   result._bePremium(extras);
+   return result;
  }

class Booking…
+ _bePremium(extras) {
+   this._premiumDelegate = new PremiumBookingDelegate(this, extras);
+ }
```

- `_` 를 붙여 public interface 로 노출되면 안됨을 강조.

  - 추후에 mutable 하게 premium 변환이 필요하면 public 이 될 순 있겠다.

- factory function 이 아니더라도 생성자에 파라미터를 받아 Premium 으로 만들 수도 있지만 저자는 명시적인 factory 를 선호

- 행동을 옮긴다
  - override hasTalkback
    - `Move Function` 으로 subclass 메서드를 delegate 로 옮긴다
      - superclass 에 대한 접근을 `_host` 를 통해 수행한다

```diff
class PremiumBookingDelegate…
  get hasTalkback() {
+   return this._host._show.hasOwnProperty('talkback');
  }

class PremiumBooking…
  get hasTalkback() {
+   return this._premiumDelegate.hasTalkback;
  }
```

- Test 가 성공하면 subclass 메서드 삭제

```diff
class PremiumBooking…
- get hasTalkback() {
-   return this._premiumDelegate.hasTalkback;
- }
```

- Test 가 실패한다면, dispatch logic 을 superclass 메서드에 추가
  - delegate 가 존재한다면 사용하도록.

```diff
class Booking…
  get hasTalkback() {
+   return (this._premiumDelegate)
+     ? this._premiumDelegate.hasTalkback
      : this._show.hasOwnProperty('talkback') && !this.isPeakDay;
}
```

- basePrice 의 경우 super 를 사용하기 때문에

  - superclass 에서 delegate 로 옮길 경우 부모를 호출해야한다
    - 하지만 다시 `this._host._basePrice` 호출하면서 무한루프에 빠진다

1. 해결을 위해 `Extract Function` 수행

- dispatch logic 과 price 계산을 분리할 수 있다

```diff
class Booking…
  get basePrice() {
+   return (this._premiumDelegate)
+     ? this._premiumDelegate.basePrice
+     : this._privateBasePrice;
  }

+ get _privateBasePrice() {
    let result = this._show.price;
    if (this.isPeakDay) result += Math.round(result * 0.15);
    return result;
  }

class PremiumBookingDelegate…
  get basePrice() {
+   return Math.round(this._host._privateBasePrice + this._extras.premiumFee);
  }
```

2. 혹은 delegate 메서드를 base 메서드의 extension 으로 recast 하는 방법

```diff
class Booking…
  get basePrice() {
    let result = this._show.price;
    if (this.isPeakDay) result += Math.round(result * 0.15);
+   return (this._premiumDelegate)
+     ? this._premiumDelegate.extendBasePrice(result)
      : result;
  }

class PremiumBookingDelegate…
+ extendBasePrice(base) {
+   return Math.round(base + this._extras.premiumFee);
  }
```

- 저자는 더 간결한 후자를 선호

- 마지막은 subclass 에만 존재하는 메서드
  - delegate 로 이동

```diff
class PremiumBookingDelegate…
  get hasDinner() {
+   return this._extras.hasOwnProperty('dinner') && !this._host.isPeakDay;
  }
```

- dispatch logic 을 추가

```diff
class Booking…
+ get hasDinner() {
+   return (this._premiumDelegate)
+     ? this._premiumDelegate.hasDinner
+     : undefined;
+ }
```

- 참고: js 의 경우 object 에 필드가 없어도 에러가 아닌 undefined 를 리턴하므로 활용
- 모든 행동을 옮겼으므로 factory 메서드가 superclass 를 리턴

- 테스트 결과가 이상 없으면 subclass 삭제

```diff
top level…
  function createPremiumBooking(show, date, extras) {
-   const result = new PremiumBooking (show, date, extras);
+   const result = new Booking (show, date, extras);
    result._bePremium(extras);
    return result;
  }

- class PremiumBooking extends Booking ...
```

- 하지만 refactoring 만으로 코드 개선이 되질 않는다
  - 상속은 이 상황에 적합하지만 위임은 추가적인 dispatch logic, 양방향 ref, 추가 복잡도를 가진다
  - mutable premium status, 혹은 다른 목적의 상속이 필요하다면 상속을 잃어도 이점이 더 클 수는 있다

# Example: Replacing a Hierarchy

- 위 예제는 단일 subclass 에 대해서 `Replace Subclass with Delegate` 수행 했지만
  - 전체 계층 구조에 대해서도 가능하다

```js
function createBird(data) {
  switch (data.type) {
    case "EuropeanSwallow":
      return new EuropeanSwallow(data);
    case "AfricanSwallow":
      return new AfricanSwallow(data);
    case "NorweigianBlueParrot":
      return new NorwegianBlueParrot(data);
    default:
      return new Bird(data);
  }
}

class Bird {
  constructor(data) {
    this._name = data.name;
    this._plumage = data.plumage;
  }
  get name() {
    return this._name;
  }

  get plumage() {
    return this._plumage || "average";
  }
  get airSpeedVelocity() {
    return null;
  }
}

class EuropeanSwallow extends Bird {
  get airSpeedVelocity() {
    return 35;
  }
}

class AfricanSwallow extends Bird {
  constructor(data) {
    super(data);
    this._numberOfCoconuts = data.numberOfCoconuts;
  }
  get airSpeedVelocity() {
    return 40 - 2 * this._numberOfCoconuts;
  }
}

class NorwegianBlueParrot extends Bird {
  constructor(data) {
    super(data);
    this._voltage = data.voltage;
    this._isNailed = data.isNailed;
  }

  get plumage() {
    if (this._voltage > 100) return "scorched";
    else return this._plumage || "beautiful";
  }

  get airSpeedVelocity() {
    return this._isNailed ? 0 : 10 + this._voltage / 10;
  }
}
```

- `wild` 혹은 `captivity` 태그를 가진 새들을 큰 차이를 가진다
- 하지만 상속은 한번 만 사용할 수 있다
- wild 와 captivity 모두를 사용하고 싶으면 species 를 위해 제거 해야한다
- 여러 subclass 들이 있다면 하나씩 정복한다.
  - EuropeanSwallow 부터 시작
  - 빈 위임 class 생성

```js
class EuropeanSwallowDelegate {}
```

- 지금은 back-ref 생성 하지 않고 나중에 필요할 때 추가
- 생성자의 단일 데이터 인자가 모든 정보를 가지고 있으므로 생성자에서 다 처리한다
- 여러 위임을 처리해야하므로 typo code 에 따라 선택하는 함수 생성

```diff
class Bird…
  constructor(data) {
    this._name = data.name;
    this._plumage = data.plumage;
+   this._speciesDelegate = this.selectSpeciesDelegate(data);
  }

+ selectSpeciesDelegate(data) {
+   switch(data.type) {
+     case 'EuropeanSwallow':
+       return new EuropeanSwallowDelegate();
+     default: return null;
+   }
+ }
```

- `Move Function`: EuropeanSwallow의 airSpeedVelocity 를 위임으로 이동

```diff
class EuropeanSwallowDelegate…
+ get airSpeedVelocity() {return 35;}

class EuropeanSwallow…
+ get airSpeedVelocity() {return this._speciesDelegate.airSpeedVelocity;}
```

- superclass 에서 위임이 존재한다면 호출

```diff
class Bird…
  get airSpeedVelocity() {
+   return this._speciesDelegate ? this._speciesDelegate.airSpeedVelocity : null;
  }
```

- subclass 제거

```diff
-class EuropeanSwallow extends Bird {
-  get airSpeedVelocity() {return this._speciesDelegate.airSpeedVelocity;}
-}

top level…
  function createBird(data) {
    switch (data.type) {
-     case 'EuropeanSwallow':
-       return new EuropeanSwallow(data);
      case 'AfricanSwallow':
        return new AfricanSwallow(data);
      case 'NorweigianBlueParrot':
        return new NorwegianBlueParrot(data);
      default:
        return new Bird(data);
    }
  }
```

- 다음은 AfricanSwallow
  - 위임 class 생성
  - 이번에는 data document 필요

```diff
class AfricanSwallowDelegate…
  constructor(data) {
    this._numberOfCoconuts = data.numberOfCoconuts;
  }

class Bird…
  selectSpeciesDelegate(data) {
    switch(data.type) {
      case 'EuropeanSwallow':
        return new EuropeanSwallowDelegate();
      case 'AfricanSwallow':
        return new AfricanSwallowDelegate(data);
      default: return null;
    }
  }
```

- `Move Function`: airSpeedVelocity

```diff
class AfricanSwallowDelegate…
+ get airSpeedVelocity() {
+   return 40 - 2 * this._numberOfCoconuts;
+ }

class AfricanSwallow…
  get airSpeedVelocity() {
+   return this._speciesDelegate.airSpeedVelocity;
  }
```

- AfricanSwallow subclass 제거
- NorwegianBlue 차례
  - 위와 동일. 최종 결과는..

```js
class Bird…
  selectSpeciesDelegate(data) {
    switch(data.type) {
      case 'EuropeanSwallow':
        return new EuropeanSwallowDelegate();
      case 'AfricanSwallow':
        return new AfricanSwallowDelegate(data);
      case 'NorweigianBlueParrot':
        return new NorwegianBlueParrotDelegate(data);
      default: return null;
    }
  }

class NorwegianBlueParrotDelegate…
  constructor(data) {
    this._voltage = data.voltage;
    this._isNailed = data.isNailed;
  }

  get airSpeedVelocity() {
    return (this._isNailed) ? 0 : 10 + this._voltage / 10;
  }
```

- NorwegianBlue 는 plumage 를 override 하지만 다른 class경우에는 그렇지 않으므로
  - `Move Function`: plumage
    - back-ref 를 추가하긴 해야함

```diff
class NorwegianBlueParrot…
  get plumage() {
+   return this._speciesDelegate.plumage;
  }

class NorwegianBlueParrotDelegate…
+ get plumage() {
+   if (this._voltage > 100) return "scorched";
+   else return this._bird._plumage || "beautiful";
+ }

+ constructor(data, bird) {
+   this._bird = bird;
    this._voltage = data.voltage;
    this._isNailed = data.isNailed;
  }

class Bird…
  selectSpeciesDelegate(data) {
    switch(data.type) {
      case 'EuropeanSwallow':
        return new EuropeanSwallowDelegate();
      case 'AfricanSwallow':
        return new AfricanSwallowDelegate(data);
      case 'NorweigianBlueParrot':
+       return new NorwegianBlueParrotDelegate(data, this);
      default: return null;
    }
  }
```

- 하지만 plumage 에서 subclass 를 어떻게 제거할 것인가?

```diff
class Bird…
  get plumage() {
+   if (this._speciesDelegate)
+     return this._speciesDelegate.plumage;
+   else
      return this._plumage || "average";
}
```

- 이렇게 바꾸면 에러난다

  - 위임 class 에는 plumage 가 없기 때문

- 더 견고한 조건을 설정할 수 있다

```diff
class Bird…
  get plumage() {
+   if (this._speciesDelegate instanceof NorwegianBlueParrotDelegate)
      return this._speciesDelegate.plumage;
    else
      return this._plumage || "average";
  }
```

- 하지만 명시적으로 class check 을 하는 것은 언제나 나쁜 선택이다
  - 대안으로 다른 위임들에 대해 default plumage 를 설정할 수 있다

```js
class Bird…
  get plumage() {
    if (this._speciesDelegate)
      return this._speciesDelegate.plumage;
    else
      return this._plumage || "average";
  }

class EuropeanSwallowDelegate…
  get plumage() {
    return this._bird._plumage || "average";
  }

class AfricanSwallowDelegate…
  get plumage() {
    return this._bird._plumage || "average";
  }
```

- 하지만 중복이 있으므로 상속으로 해결: `Extract Superclass`

```diff
+class SpeciesDelegate {
+  constructor(data, bird) {
+    this._bird = bird;
+  }
+  get plumage() {
+    return this._bird._plumage || "average";
+  }

+class EuropeanSwallowDelegate extends SpeciesDelegate {

+class AfricanSwallowDelegate extends SpeciesDelegate {
  constructor(data, bird) {
+   super(data,bird);
    this._numberOfCoconuts = data.numberOfCoconuts;
  }

+class NorwegianBlueParrotDelegate extends SpeciesDelegate {
  constructor(data, bird) {
+   super(data, bird);
    this._voltage = data.voltage;
    this._isNailed = data.isNailed;
  }
```

- 생성자에 대해서 default 를 확실하게 지정

```diff
class Bird…
  selectSpeciesDelegate(data) {
    switch(data.type) {
      case 'EuropeanSwallow':
        return new EuropeanSwallowDelegate(data, this);
      case 'AfricanSwallow':
        return new AfricanSwallowDelegate(data, this);
      case 'NorweigianBlueParrot':
        return new NorwegianBlueParrotDelegate(data, this);
+     default: return new SpeciesDelegate(data, this);
    }
  }
  // rest of bird's code...

+ get plumage() {return this._speciesDelegate.plumage;}

+ get airSpeedVelocity() {return this._speciesDelegate.airSpeedVelocity;}

class SpeciesDelegate…
+ get airSpeedVelocity() {return null;}
```

- 최종 결과물

```js
function createBird(data) {
  return new Bird(data);
}

class Bird {
  constructor(data) {
    this._name = data.name;
    this._plumage = data.plumage;
    this._speciesDelegate = this.selectSpeciesDelegate(data);
  }
  get name() {
    return this._name;
  }
  get plumage() {
    return this._speciesDelegate.plumage;
  }
  get airSpeedVelocity() {
    return this._speciesDelegate.airSpeedVelocity;
  }

  selectSpeciesDelegate(data) {
    switch (data.type) {
      case "EuropeanSwallow":
        return new EuropeanSwallowDelegate(data, this);
      case "AfricanSwallow":
        return new AfricanSwallowDelegate(data, this);
      case "NorweigianBlueParrot":
        return new NorwegianBlueParrotDelegate(data, this);
      default:
        return new SpeciesDelegate(data, this);
    }
  }
  // rest of bird's code...
}

class SpeciesDelegate {
  constructor(data, bird) {
    this._bird = bird;
  }
  get plumage() {
    return this._bird._plumage || "average";
  }
  get airSpeedVelocity() {
    return null;
  }
}

class EuropeanSwallowDelegate extends SpeciesDelegate {
  get airSpeedVelocity() {
    return 35;
  }
}

class AfricanSwallowDelegate extends SpeciesDelegate {
  constructor(data, bird) {
    super(data, bird);
    this._numberOfCoconuts = data.numberOfCoconuts;
  }
  get airSpeedVelocity() {
    return 40 - 2 * this._numberOfCoconuts;
  }
}

class NorwegianBlueParrotDelegate extends SpeciesDelegate {
  constructor(data, bird) {
    super(data, bird);
    this._voltage = data.voltage;
    this._isNailed = data.isNailed;
  }
  get airSpeedVelocity() {
    return this._isNailed ? 0 : 10 + this._voltage / 10;
  }
  get plumage() {
    if (this._voltage > 100) return "scorched";
    else return this._bird._plumage || "beautiful";
  }
}
```

- 이번 예제는 원본 subclass 들을 모두 제거했지만 유사한 상속구조가 위임에 남아있다
- 장점?
  - 단순히 데이터와 함수를 감싸는 것 뿐만아니라 기본구현과 확장성을 가지게 되었다

> Favor a judicious mixture of composition and inheritance over either alone

# Homework

- 첫 예제에서 superclass 의 위임을 만들 때 두번 째 예제 방식을 적용
  - dispatch logic 이 있는 메서드 들을 위임에 대한 간단한 호출로 대체하고 그 상속이 dispatch 를 정렬하도록 하라
