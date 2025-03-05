> Replace Primitive with Object

```js
orders.filter((o) => "high" === o.priority || "rush" === o.priority);
```

👇

```js
orders.filter((o) => o.priority.higherThan(new Priority("normal")));
```

# Motivation

- 간단한 개발 단계에서 phone 번호는 string 으로 쓰였겠지만 검증로직이 여기저기 중복되는 문제가 생겨남
- 처음에는 primitive type 에 대한 wrapper class 에 그쳤지만, 요구에 따라 행동도 넣게되었다
- 숙련된 개발자들은 이것을 가장 가치있는 리팩토링 중 하나로 꼽는다

# Mechanics

- 필요한 경우 `Encapsulate Variable` 을 수행
- 간단한 class 를 만들고 primitive type 을 field 로 추가. constructor 와 getter 제공
- Static check
- setter 가 새로운 instance 를 생성하고 field 에 저장하도록 수정
- getter 가 새로운 class 의 getter 를 호출하도록 수정
- Test
- `Rename Function`: 기존 accessor 가 더 나은 의미를 가지도록 변경
- 새 object 의 role 을 명확히 하기 위해 `Change Reference to Value` 혹은 `Change Value to Reference` 를 수행

# Example

```js
// class Order…
  constructor(data) {
    this.priority = data.priority;
    // more initialization

// client…
  highPriorityCount = orders.filter(o => "high" === o.priority
                                      || "rush" === o.priority)
                            .length;

```

- `Encapsulate Variable` 수행

```js
// class Order…
  get priority()        {return this._priority;}
  set priority(aString) {this._priority = aString;}

constructor(data) {
    priority(data.priority); // TODO: contructor 에서 setter 를 사용하는 것이 일반적인 사례인가?
    // more initialization
```

- class Priority 와 toString 추가
  - 사실은 gatter 이지만 class to string conversion 처럼 보이는 것이 더 자연스럽다

```js
class Priority {
  constructor(value) {
    this._value = value;
  }
  toString() {
    return this._value;
  }
}
```

- accessor 수정

```js
// class Order…
  get priority()        {return this._priority.toString();}
  set priority(aString) {this._priority = new Priority(aString);}
```

- `Rename Function` 으로 return 하는 대상을 명확히 표현

  - setter 의 경우 인자가 명확하게 string을 표현 해주므로 이름 유지

```js
// class Order…
  get priorityString()  {return this._priority.toString();}
  set priority(aString) {this._priority = new Priority(aString);}
client…

  highPriorityCount = orders.filter(o => "high" === o.priorityString
                                      || "rush" === o.priorityString)
                            .length;
```

- Priority class 자체를 원하는 client 를 위해 기본 getter 도 추가

```js
// class Order…
  get priority()        {return this._priority;}
```

- setter 가 priority instance 도 받을 수 있게 하고 그 경우에는 재 생성이 아니라 동등한 객체를 return 하도록 수정

```js
// class Priority…
  constructor(value) {
    if (value instanceof Priority) return value; // TODO: js 특화 문법인가?  java 의 constructor 는 return type 이 없다
    this._value = value;
  }

```

- validation logic 추가
  - VO 의 역할을 해야하므로 equals 도 추가

```js
// class Priority…
  constructor(value) {
    if (value instanceof Priority) return value;
    if (Priority.legalValues().includes(value))
      this._value = value;
    else
      throw new Error(`<${value}> is invalid for Priority`);
  }
  toString() {return this._value;}
  get _index() {return Priority.legalValues().findIndex(s => s === this._value);}
  static legalValues() {return ['low', 'normal', 'high', 'rush'];}

  equals(other) {return this._index === other._index;}
  higherThan(other) {return this._index > other._index;}
  lowerThan(other) {return this._index < other._index;}
```

- client 에서 priority 를 비교하는 부분 수정

```js
// client…
highPriorityCount = orders.filter((o) =>
  o.priority.higherThan(new Priority("normal")),
).length;
```
