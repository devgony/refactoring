# ch7. Encapsulation QA

## [Encapsulate Collection](/7-encapsulation/2-encapsulate-collection.md)

- 왜 굳이 setter 에서도 slice 로 복사를 할까?

> Rust 의 경우를 생각해보면 생명주기안에서 참조가 끝난다면 할당시에 clone 을 했었다. Java 에서도 그라운드 룰로 적용한 형태라고 볼 수 있겠다

```js
// class Person…
set courses(aList) {this._courses = aList.slice();} // TODO why slice?
```

## [Replace Primitive With Object](/7-encapsulation/3-replace-primitive-with-object.md)

- contructor 에서 setter 를 사용하는 것이 일반적인 사례인가?

> 흔하진 않지만 setter 에 로직을 넣을 때 사용 가능

```js
// class Order…
  get priority()        {return this._priority;}
  set priority(aString) {this._priority = aString;}

constructor(data) {
    priority(data.priority); // TODO contructor 에서 setter 를 사용하는 것이 일반적인 사례인가?
    // more initialization
```

- js 특화 문법인가? java 의 constructor 는 return type 이 없다 => static factory method 로 대체 가능?

> java 의 constructor 에는 로직을 넣어서는 안된다. 이 경우는 static factory method 로 대체 하는게 맞다

```js
// class Priority…
  constructor(value) {
    if (value instanceof Priority) return value; // TODO js 특화 문법인가?  java 의 constructor 는 return type 이 없다 => static factory method 로 대체 가능?
    this._value = value;
  }
```

## [Extract Class](/7-encapsulation/5-extract-class.md)

- `TelephoneNumber` 노출 시키고 싶다면
  - 각 office 관련 method 를 telephoneNumber 에 대한 접근자로 대체
  - VO 가 되는 것이 적합하기 때문에 `Change Reference to Value` 를 수행
    - TODO 예시 필요

> TelephoneNumbe 를 VO 로서 노출 한 이후에는 변경되면 안되기 때문에 ref 가 아니라 value 를 복제하여 return 해야 한다
