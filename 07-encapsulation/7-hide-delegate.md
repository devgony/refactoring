> Hide Delegate

```js
manager = aPerson.department.manager;
```

👇

```js
manager = aPerson.manager;

class Person {
  get manager() {return this.department.manager;}
```

# Motivation

- Encapsulation 은 모듈이 다른 시스템의 부분을 적게 알아도 되도록 한다
  - 변화가 발생했을 때 변경을 알려야 하는 모듈이 적어져서 변화가 더 쉬워진다

# Mechanics

- 위임할 각 method 에 대해 delegating method 를 server 측에 만든다
- client 가 delegating method 를 호출하도록 수정
- delete 대상에 대해 더이상 접근하는 client 코드가 없게되면 server 의 accessor 를 삭제한다
- Test

# Example

```js
// class Person…
  constructor(name) {
    this._name = name;
  }
  get name() {return this._name;}
  get department()    {return this._department;}
  set department(arg) {this._department = arg;}

// class Department…
  get chargeCode()    {return this._chargeCode;}
  set chargeCode(arg) {this._chargeCode = arg;}
  get manager()    {return this._manager;}
  set manager(arg) {this._manager = arg;}
```

- client 가 특정 person 의 manager 를 알고 싶을 때

```js
// client code…
manager = aPerson.department.manager;
```

- department 를 거쳐서 manager 를 알아내는데, 이는 department 의 책임이므로 person 이 알 필요가 없다

```js
// class Person…
  get manager() {return this._department.manager;}

// client code…
  manager = aPerson.manager;
```

- 모든 client 에 대해 수정이 완료되면 department 를 거치는 accessor 를 삭제한다
