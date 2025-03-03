> Extract Class

```js
class Person {
  get officeAreaCode() {return this._officeAreaCode;}
  get officeNumber()   {return this._officeNumber;}
```

👇

```js
class Person {
  get officeAreaCode() {
    return this._telephoneNumber.areaCode;
  }
  get officeNumber() {
    return this._telephoneNumber.number;
  }
}
class TelephoneNumber {
  get areaCode() {
    return this._areaCode;
  }
  get number() {
    return this._number;
  }
}
```

# Motivation

- class 는 명료한 추상화, 명확한 책임만 어쩌고.. 하지만 class 는 자라난다
- 언제 분리?
  - data의 subset 과 method 의 subset 이 함께 할 때
  - 특정 data 의 subset 이 함께 변경되거나 특정 부분에 의존적일 때
  - data 나 method 를 제거하면 어떤일이 일어날 지 스스로 고민해보자
- class 의 subtype 화
  - subtype 화가 일부 기능에만 영향을 미치거나
  - 일부기능은 한가지, 다른 기능은 또 다른 방식으로 subtype화가 될 수 있다

# Mechanics

- 책임을 어떻게 분리할 것인지 결정
- 분리된 책임을 표현할 새로운 child class 생성
  - 분리와 함께 부모의 책임이 달라지는 경우 rename class 수행
- 부모를 construct 할 때 자식 도 instance 를 만들어 parent 에서 child 로 링크 연결
- `Move Field` 를 통해 원하는 필드를 child class 로 이동
  - Test
- `Move Function` 를 통해 원하는 메소드를 child class 로 이동
  - lower-level method (부르는 것보단 불리는 것) 을 먼저 이동
  - Test
- 각 class 의 interface 를 검토
  - 불필요한 method 는 제거
  - 새 환경에 맞는 이름 변경
- 새 child class 를 export 할지 결정
  - expose 할 것이라면 `Change Reference to Value` 를 수행

# Example

```js
// class Person…
  get name()    {return this._name;}
  set name(arg) {this._name = arg;}
  get telephoneNumber() {return `(${this.officeAreaCode}) ${this.officeNumber}`;}
  get officeAreaCode()    {return this._officeAreaCode;}
  set officeAreaCode(arg) {this._officeAreaCode = arg;}
  get officeNumber() {return this._officeNumber;}
  set officeNumber(arg) {this._officeNumber = arg;}
```

- TelephoneNumber class 생성

```js
class TelephoneNumber {}
```

```js
// class Person…
  constructor() {
    this._telephoneNumber = new TelephoneNumber();
  }

// class TelephoneNumber…
  get officeAreaCode()    {return this._officeAreaCode;}
  set officeAreaCode(arg) {this._officeAreaCode = arg;}
```

- `Move Field` 를 통해 officeAreaCode 를 TelephoneNumber 로 이동

```js
// class Person…
  get officeAreaCode()    {return this._telephoneNumber.officeAreaCode;}
  set officeAreaCode(arg) {this._telephoneNumber.officeAreaCode = arg;}
```

- `Move Field`: officeNumber

```js
// class TelephoneNumber…
  get officeNumber() {return this._officeNumber;}
  set officeNumber(arg) {this._officeNumber = arg;}

// class Person…
  get officeNumber() {return this._telephoneNumber.officeNumber;}
  set officeNumber(arg) {this._telephoneNumber.officeNumber = arg;}
```

- `Move Function`: telephoneNumber

```js
// class TelephoneNumber…
  get telephoneNumber() {return `(${this.officeAreaCode}) ${this.officeNumber}`;}

// class Person…
  get telephoneNumber() {return this._telephoneNumber.telephoneNumber;}
```

- office 가 telephoneNumber 로 이동했으므로 office 라는 prefix 를 이름에서 제거

```js
// class TelephoneNumber…
  get areaCode()    {return this._areaCode;}
  set areaCode(arg) {this._areaCode = arg;}

  get number()    {return this._number;}
  set number(arg) {this._number = arg;}

// class Person…
  get officeAreaCode()    {return this._telephoneNumber.areaCode;}
  set officeAreaCode(arg) {this._telephoneNumber.areaCode = arg;}
  get officeNumber()    {return this._telephoneNumber.number;}
  set officeNumber(arg) {this._telephoneNumber.number = arg;}

```

- `Rename Function`: TelephoneNumber.telephoneNumber to toString

```js
// class TelephoneNumber…
  toString() {return `(${this.areaCode}) ${this.number}`;}

// class Person…
  get telephoneNumber() {return this._telephoneNumber.toString();}
```

- `TelephoneNumber` 노출 시키고 싶다면
  - 각 office 관련 method 를 tlephoneNumber 에 대한 접근자로 대체
  - VO 가 되는 것이 적합하기 때문에 `Change Reference to Value` 를 수행
