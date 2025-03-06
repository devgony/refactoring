# ch7. Encapsulation QA

## [2. Encapsulate Collection](/7-encapsulation/2-encapsulate-collection.md)

- courses 전체를 수정할 일이 없다면 `Remove Setting Method` 를 수행
  - 특별한 이유로 해당 기능이 필요하다면 collection 을 복제해서 전달하도록 수정

```js
// class Person…
set courses(aList) {this._courses = aList.slice();} // TODO why slice?
```

## [3. Replace Primitive With Object](/7-encapsulation/3-replace-primitive-with-object.md)

- `Encapsulate Variable` 수행

```js
// class Order…
  get priority()        {return this._priority;}
  set priority(aString) {this._priority = aString;}

constructor(data) {
    priority(data.priority); // TODO contructor 에서 setter 를 사용하는 것이 일반적인 사례인가?
    // more initialization
```

- setter 가 priority instance 도 받을 수 있게 하고 그 경우에는 재 생성이 아니라 동등한 객체를 return 하도록 수정

```js
// class Priority…
  constructor(value) {
    if (value instanceof Priority) return value; // TODO js 특화 문법인가?  java 의 constructor 는 return type 이 없다 => static factory method 로 대체 가능?
    this._value = value;
  }
```

- validation logic 추가
  - VO 의 역할을 해야하므로 equals 도 추가

```js
// class Priority…
  constructor(value) {
    if (value instanceof Priority) return value;
    if (Priority.legalValues().includes(value)) // TODO 정적타입 언어에서는 전혀 불필요한 validation 인가?
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

## [5. Extract Class](/7-encapsulation/5-extract-class.md)

- `TelephoneNumber` 노출 시키고 싶다면
  - 각 office 관련 method 를 telephoneNumber 에 대한 접근자로 대체
  - VO 가 되는 것이 적합하기 때문에 `Change Reference to Value` 를 수행
    - TODO 예시 필요
