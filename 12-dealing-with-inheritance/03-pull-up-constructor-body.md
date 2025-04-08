> Pull Up Constructor Body

```js
class Party {...}

class Employee extends Party {
  constructor(name, id, monthlyCost) {
    super();
    this._id = id;
    this._name = name;
    this._monthlyCost = monthlyCost;
  }
}
```

👇

```js
class Party {
  constructor(name) {
    this._name = name;
  }
}

class Employee extends Party {
  constructor(name, id, monthlyCost) {
    super(name);
    this._id = id;
    this._monthlyCost = monthlyCost;
  }
}
```

# Motivation

- 생성자는 일반적인 메서드가 아니기 때문에 제약이 많다
- 일반적인 동작을 하는 자식 메서드를 발견하면 `Extract Fucntion` + `Pull Up Method`
- 리팩토링이 지저분해지면, `Replace Constructor with Factory Function`

# Mechanics

- 부모에 생성자가 존재하지 않는다면 정의한다. 자식에 의해 불리는 지 확인
- `Slide Statements`: super call 이후의 구문들을 옮긴다
- 각 자식에서 공통 코드를 제거하고 부모로 옮긴다. 참조되는 모든 값들은 생성자의 파라미터로 전달한다
- Test
- 생성자의 도입부로 옮길 수 없는 코드들은 `Extract Function` + `Pull Up Method` 활용

# Example

```js
class Party {}
class Employee extends Party {
  constructor(name, id, monthlyCost) {
    super();
    this._id = id;
    this._name = name;
    this._monthlyCost = monthlyCost;
  }
  // rest of class...
class Department extends Party {
  constructor(name, staff){
    super();
    this._name = name;
    this._staff = staff;
  }
  // rest of class...
```

- 이름을 할당하는 중복 부분을 제거하자: `Slide Statements`

```diff
class Employee extends Party {
  constructor(name, id, monthlyCost) {
-   super();
+   super(name);
-   this._name = name;
    this._id = id;
    this._monthlyCost = monthlyCost;
  }
  // rest of class...

class Party…
+ constructor(name){
+   this._name = name;
  }
```

- Test 이후 Deparement 에서도 super 에서 name 전달

```diff
class Department…
  constructor(name, staff){
+   super(name);
    this._staff = staff;
  }
```

- 후속작업이 있는 예제

```js
class Employee…
  constructor (name) {...}

  get isPrivileged() {...}

  assignCar() {...}
class Manager extends Employee…

  constructor(name, grade) {
    super(name);
    this._grade = grade;
    if (this.isPrivileged) this.assignCar(); // every subclass does this
  }

  get isPrivileged() {
    return this._grade > 4;
  }
```

- isPrivileged 는 grade 가 채워져야 사용 가능하기 때문에 자식만 호출 가능하다

- `Extract Function`

```diff
class Manager…
  constructor(name, grade) {
    super(name);
    this._grade = grade;
+   this.finishConstruction();
  }

+ finishConstruction() {
    if (this.isPrivileged) this.assignCar();
  }
```

- `Pull Up Method`: 부모로 이동

```js
class Employee…
  finishConstruction() {
    if (this.isPrivileged) this.assignCar();
  }
```
