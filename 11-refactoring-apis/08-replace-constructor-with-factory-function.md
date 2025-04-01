> Replace Constructor with Factory Function

```js
leadEngineer = new Employee(document.leadEngineer, "E");
```

👇

```js
leadEngineer = createEngineer(document.leadEngineer);
```

# Motivation

- 대부분의 OOP 언어들은 생성자 라는 특별한 함수가 있다
- 제약조건
  - java 의 경우 반드시 class 의 instance 를 return 해야함
    - subclass, proxy 등을 사용할 수 없음
  - 네이밍 룰이 class 의 이름으로 고정됨
  - new 키워드가 일반 함수와 다르게 동작함
- Factory 함수는 위 제약조건 없이 내부에서 생성자를 호출하거나 자유롭게 활용 가능

# Mechanics

- Factory 함수 생성, body 는 생성자를 호출
- 생성자 호출부를 Factory 함수로 변경
- Test
- 생성자의 접근제어자를 최대한 private 으로 변경

# Example

```js
class Employee…
  constructor (name, typeCode) {
    this._name = name;
    this._typeCode = typeCode;
  }
  get name() {return this._name;}
  get type() {
    return Employee.legalTypeCodes[this._typeCode];
  }
  static get legalTypeCodes() {
    return {"E": "Engineer", "M": "Manager", "S": "Salesman"};
  }

caller…
  candidate = new Employee(document.name, document.empType);

caller…
  const leadEngineer = new Employee(document.leadEngineer, 'E');
```

- Factory 함수 생성 및 생성자로 위임

```js
top level…
  function createEmployee(name, typeCode) {
    return new Employee(name, typeCode);
  }
```

- 각 호출자를 factory 함수로 변경

```js
caller…
  candidate = createEmployee(document.name, document.empType);

caller…
  const leadEngineer = createEmployee(document.leadEngineer, 'E');
```

- Type 인자를 두 개의 함수로 분리

```js
caller…
  const leadEngineer = createEngineer(document.leadEngineer);

top level…
  function createEngineer(name) {
    return new Employee(name, 'E');
  }
//..
```
