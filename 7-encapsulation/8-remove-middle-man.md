> Remove Middle Man

```js
manager = aPerson.manager;

class Person {
  get manager() {return this.department.manager;}
```

👇

```js
manager = aPerson.department.manager;
```

# Motivation

- 기능 추가 될때마다 계속 delegate method 를 추가하는 것은 불필요하다
- 이 경우에 중간 class 는 middle mana 으로 남을 뿐이므로 delegate 필드를 바로 부르는 것이 낫다.
- Hide Delegate 와 Remove Middle Man 중에 뭐가 나은지 정답은 없다. 코드가 변하면서 더 나은 방법을 선택하면 된다.

# Mechanics

- delegate 에 대해 getter 를 만들어준다
- 각 client 가 delegate method 를 호출하는 부분을 delegate 의 getter 로 변경
  - 이때 delegate method 를 호출하는 부분이 더이상 없어지면 해당 method 삭제한다
  - `Encapsulate Variable` + `Inline Function` 을 활용 할 수도 있다.

# Example

```js
// client code…
  manager = aPerson.manager;

// class Person…
  get manager() {return this._department.manager;}

// class Department…
  get manager()    {return this._manager;}
```

- 이러한 메서드 개수가 늘어난다면 삭제하는 것이 좋다.

```js
// class Person…
  get department()    {return this._department;}

// client code…
  manager = aPerson.department.manager;
```

- 모든 client 가 department 를 거치지 않고 바로 manager 를 부르도록 수정되면 delegate method 를 삭제한다.

- 중간에 `Encapsulate Variable`

```js
// class Person…
  get manager() {return this.department.manager;}
```

- 이후에 변경이 완료되면 `Inline Function` 수행
