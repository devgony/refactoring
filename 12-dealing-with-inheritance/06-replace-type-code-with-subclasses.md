> Replace Type Code with Subclasses

```js
function createEmployee(name, type) {
  return new Employee(name, type);
}
```

👇

```js
function createEmployee(name, type) {
  switch (type) {
    case "engineer": return new Engineer(name);
    case "salesman": return new Salesman(name);
    case "manager":  return new Manager (name);
  }
```

# Motivation

- 중복 해결을 위해 type code field 를 사용하는 경우
  - 주로 외부 서비스에서 제공
- subclass 를 사용하여 type code field 를 개선

  1. 조건문을 다형성으로 해결: `Replace Conditional with Polymorphism`
  2. 특정 type 에서는 쓰이는 필드가 있을때, subclass 생성하고 `Push Down Field` 할 수 있다
     - validation 로직으로 특정 type 을 확인하는 것보다 더 명시적인 관계를 나타낼 수 있다

- Class 전체 혹은 type code 자체에 적용할지도 정해야 한다
  - direct subclassing 의 경우 간단하지만 변화에 취약하고 type 이 mutable 이면 쓸 수 없다
  - subclass 를 type 필드로 이동하는 경우 `Replace Primitivbe with Object` 를 type code 에 적용하여 employee type class 를 만들고 그 class 에 `Replace Type Code with Subclasses` 를 적용한다

# Mechanics

- type code field 를 셀프 캡슐화한다
- type code 하나를 지정하여 대응하는 subclass 를 생성한다.
  - type code 의 getter 를 override 하여 literal type code 를 return
- type code 를 새 subclass 로 매핑하기 위한 selector 를 만든다
  - direct 상속에서는 `Replace Contructor with Factory Function` 사용하여 selector logic 을 내장
  - indirect 상속은 selector 가 constructor 에 남는다
- Test
- 모든 type code 에 대해 반복작업
- type code field 를 제거한다
- Test
- `Push Down Method` + `Replace Conditional with Polymorphism`
  - type code 에 접근하는 모든 메서드에 적용
  - 모두 변경 되면 type code 접근자는 모두 제거

# Example

```js
class Employee…
  constructor(name, type){
    this.validateType(type);
    this._name = name;
    this._type = type;
  }
  validateType(arg) {
    if (!["engineer", "manager", "salesman"].includes(arg))
      throw new Error(`Employee cannot be of type ${arg}`);
  }
  toString() {return `${this._name} (${this._type})`;}
```

- `Encapsulate Variable`: type code 를 self encapsulate 한다

```diff
class Employee…
+ get type() {return this._type;}
- toString() {return `${this._name} (${this._type})`;}
+ toString() {return `${this._name} (${this.type})`;}
```

- `engineer` type code 를 direct inheritance 로, employee class 자체를 subclassing 한다

```js
class Engineer extends Employee {
  get type() {
    return "engineer";
  }
}
```

- js 는 constructor 가 다른 object return 할 수 있어서 한번에 selector 로직 처리해도 되지만 더러워지므로 `Replace Constructor with Factory Function`

```js
function createEmployee(name, type) {
  return new Employee(name, type);
}
```

- 새 subclass 에 대한 selector logic 추가

```diff
function createEmployee(name, type) {
+ switch (type) {
+   case "engineer": return new Engineer(name, type);
+ }
  return new Employee(name, type);
}
```

- subclass 인 Engineer 에서 return 값을 다르게 바꿔서 테스트가 실패하는지 확인한다

- 다른 코드에 대해서도 반복

```diff
+class Salesman extends Employee {
+  get type() {
+    return "salesman";
+  }
+}

+class Manager extends Employee {
+  get type() {
+    return "manager";
+  }
+}

function createEmployee(name, type) {
  switch (type) {
    case "engineer":
      return new Engineer(name, type);
+   case "salesman":
+     return new Salesman(name, type);
+   case "manager":
+     return new Manager(name, type);
  }
  return new Employee(name, type);
}
```

- superclass 에서 type code 와 type getter 를 제거
  - TODO: java 의 경우 super 에서 type getter 를 제거하면 super의 생성자에서 sub의 type() 을 호출 못한다?

```diff
class Employee…
  constructor(name, type){
    this.validateType(type);
    this._name = name;
-   this._type = type;
  }

- get type() {return this._type;}
  toString() {return `${this._name} (${this.type})`;}
```

- selector logic 이 있으므로 validateType 메서드도 제거한다

```diff
class Employee…
  constructor(name, type){
-   this.validateType(type);
    this._name = name;
  }

function createEmployee(name, type) {
  switch (type) {
    case "engineer": return new Engineer(name, type);
    case "salesman": return new Salesman(name, type);
    case "manager":  return new Manager (name, type);
+   default: throw new Error(`Employee cannot be of type ${type}`);
  }
- return new Employee(name, type);
}
```

- type code 를 param 에서 제거: `Change Function Declaration`

```diff
class Employee…
- constructor(name, type){
+ constructor(name){
    this._name = name;
  }
function createEmployee(name, type) {
  switch (type) {
-   case "engineer": return new Engineer(name, type);
-   case "salesman": return new Salesman(name, type);
-   case "manager":  return new Manager (name, type);
+   case "engineer": return new Engineer(name);
+   case "salesman": return new Salesman(name);
+   case "manager":  return new Manager (name);
    default: throw new Error(`Employee cannot be of type ${type}`);
  }
}
```

- 다른 메서드가 아직 type code 를 사용할 수 있기 때문에 `Replace Conditional with Polymorphism` + `Push Down Method` 를 적용
- reference 가 하나도 없으면 `Remove Dead Code` 로 type code 제거

# Example: Using Indirect Inheritance

- 이미 Employee 에 대한 subclass 가 있다면?
- employee type 을 계속 수정하고 싶다면?
  - -> 간접 상속 사용

```js
class Employee…
  constructor(name, type){
    this.validateType(type);
    this._name = name;
    this._type = type;
  }
  validateType(arg) {
    if (!["engineer", "manager", "salesman"].includes(arg))
      throw new Error(`Employee cannot be of type ${arg}`);
  }
  get type()    {return this._type;}
  set type(arg) {this._type = arg;}

  get capitalizedType() {
    return this._type.charAt(0).toUpperCase() + this._type.substr(1).toLowerCase();
  }
  toString() {
    return `${this._name} (${this.capitalizedType})`;
  }
```

- `Replace Primitive with Object`: toString 이 복잡하므로

```diff
+class EmployeeType {
+  constructor(aString) {
+    this._value = aString;
+  }
+  toString() {
+    return this._value;
+  }
+}

class Employee…
  constructor(name, type){
    this.validateType(type);
    this._name = name;
    this.type = type;
  }
  validateType(arg) {
    if (!["engineer", "manager", "salesman"].includes(arg))
      throw new Error(`Employee cannot be of type ${arg}`);
  }
+ get typeString()    {return this._type.toString();}
  get type()    {return this._type;}
+ set type(arg) {this._type = new EmployeeType(arg);}

  get capitalizedType() {
+   return this.typeString.charAt(0).toUpperCase()
+     + this.typeString.substr(1).toLowerCase();
  }

  toString() {
    return `${this._name} (${this.capitalizedType})`;
  }
```

- EmployeeType 에 대해 `Replace Type Code with Subclasses` 수행

```diff
class Employee…
+ set type(arg) {this._type = Employee.createEmployeeType(arg);}

+  static createEmployeeType(aString) {
+    switch(aString) {
+      case "engineer": return new Engineer();
+      case "manager":  return new Manager ();
+      case "salesman": return new Salesman();
+      default: throw new Error(`Employee cannot be of type ${aString}`);
+    }
+  }
+
+class EmployeeType {
+}
+
+class Engineer extends EmployeeType {
+  toString() {return "engineer";}
+}
+
+class Manager extends EmployeeType {
+  toString() {return "manager";}
+}
+
+class Salesman extends EmployeeType {
+  toString() {return "salesman";}
+}
```

- 빈 EmployeeType 을 제거할 수도 있지만 명시적인 관계를 위해 남겨두며, 추가 메서드를 처리할 때도 용이하다

```diff
class Employee…
  toString() {
-   return `${this._name} (${this.capitalizedType})`;
+   return `${this._name} (${this.type.capitalizedName})`;
  }

class EmployeeType…
+ get capitalizedName() {
+   return this.toString().charAt(0).toUpperCase()
+     + this.toString().substr(1).toLowerCase();
  }
```
