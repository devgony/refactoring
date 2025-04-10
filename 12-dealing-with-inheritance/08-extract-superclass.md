> Extract Superclass

```js
class Department {
  get totalAnnualCost() {...}
  get name() {...}
  get headCount() {...}
}

class Employee {
  get annualCost() {...}
  get name() {...}
  get id() {...}
}
```

👇

```js
class Party {
  get name() {...}
  get annualCost() {...}
}

class Department extends Party {
  get annualCost() {...}
  get headCount() {...}
}

class Employee extends Party {
  get annualCost() {...}
  get id() {...}
}
```

# Motivation

- 공통된 두 class 를 발견하면 data 는 `Pull Up Field`, 행동은 `Pull Up Method` 하여 superclass 로 끌어올린다
- 객체지향 전문가들은 상속이 현실세계의 분류 구조에 따라 신중하게 계획 되어야 한다고 하지만 프로그램이 발전하면서 나중에 공통된 요소에 대한 상속을 깨닫는 경우도 많다
- Extract Superclass 에 대한 대안은 `Extract Class` 이다
  - 상속을 할지 위임을 할지 결정
  - 보통 상속이 더 쉬우므로 일단 Extract Superclass 를 하고 나중에 `Replace Superclass with Delegate` 로 변경

# Mechanics

- 빈 superclass 를 생성하고 기존 class 를 그 subclass 로 만든다
- Test
- 하나씩 `Pull Up Constructor Body`, `Pull Up Method`, `Pull Up Field` 를 수행하여 sub에서 super 로 옮긴다
- subclass 에 남은 메서드를 검사하여 공통부분을 확인
  - 존재하면 `Extract Function` + `Pull Up Method`
- 기존 class 의 client 에서 superclass 를 바라보도록 변경

# Example

```js
class Employee {
  constructor(name, id, monthlyCost) {
    this._id = id;
    this._name = name;
    this._monthlyCost = monthlyCost;
  }
  get monthlyCost() {
    return this._monthlyCost;
  }
  get name() {
    return this._name;
  }
  get id() {
    return this._id;
  }

  get annualCost() {
    return this.monthlyCost * 12;
  }
}
class Department {
  constructor(name, staff) {
    this._name = name;
    this._staff = staff;
  }
  get staff() {
    return this._staff.slice();
  }
  get name() {
    return this._name;
  }

  get totalMonthlyCost() {
    return this.staff
      .map((e) => e.monthlyCost)
      .reduce((sum, cost) => sum + cost);
  }
  get headCount() {
    return this.staff.length;
  }
  get totalAnnualCost() {
    return this.totalMonthlyCost * 12;
  }
}
```

- 빈 Party superclass 생성

```diff
+class Party {}

+class Employee extends Party {
  constructor(name, id, monthlyCost) {
+   super();
    this._id = id;
    this._name = name;
    this._monthlyCost = monthlyCost;
  }
  // rest of class...

+class Department extends Party {
  constructor(name, staff){
+   super();
    this._name = name;
    this._staff = staff;
  }
  // rest of class...
```

- data 먼저 옮기는 게 좋다: `Pull Up Field`
  - TODO: 왜? 객체는 행동이 더 중요하지 않나.. -> compile 실패 여부 때문인가

```diff
class Party…
  constructor(name){
+   this._name = name;
  }

class Employee…
  constructor(name, id, monthlyCost) {
+   super(name);
    this._id = id;
    this._monthlyCost = monthlyCost;
  }

class Department…
  constructor(name, staff){
+   super(name);
    this._staff = staff;
  }
```

- `Pull Up Method` 수행

```diff
class Party…
+ get name() {return this._name;}

class Employee…
- get name() {return this._name;}

class Department…
- get name() {return this._name;}
```

- 유사한 두 메서드가 다른 이름을 가지고있으므로 통일: `Change Function Declaration`

```diff
class Department…
- get totalAnnualCost() {
+ get annualCost() {
-   return this.totalMonthlyCost * 12;
+   return this.monthlyCost * 12;
  }

+ get monthlyCost() { … }
```

- `Pull Up Method` 수행

```diff
class Party…
  get annualCost() {
    return this.monthlyCost * 12;
  }

class Employee…
- get annualCost() {
-   return this.monthlyCost * 12;
- }

class Department…
- get annualCost() {
-   return this.monthlyCost * 12;
- }
```
