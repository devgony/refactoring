> Change Reference to Value

```js
class Product {
  applyDiscount(arg) {this._price.amount -= arg;}
```

👇

```js
class Product {
  applyDiscount(arg) {
    this._price = new Money(this._price.amount - arg, this._price.currency);
  }
```

# Motivation

- reference 로 처리하면 같은 inner object 를 유지하면서 특정 필드만 변경한다
- value 로 처리하면 원하는 필드를 가진 새로운 inner object 를 생성하여 대체한다

- field 를 value로 다룬다면, inner object 를 Value Object 로 변경 가능
- VO 는 immutable 하므로 이해하기 쉽다
- value 를 복제해서 사용하기 때문에 memory link 고민도 없어 분산, 동시성 처리에 유용하다
- 여러 객체들이 특정 객체를 공유해서 써야하는 경우 value 가 아닌 reference 를 유지해야 한다

# Mechanics

- 후보 class 가 immutable 인지, 될 수 있는지 확인
- `Remove Setting Method`
- VO 의 필드를 사용하는 value 기반 동등성 메서드 제공
  - override hashcode, equals

# Example

```js
// class Person…
  constructor() {
    this._telephoneNumber = new TelephoneNumber();
  }

  get officeAreaCode()    {return this._telephoneNumber.areaCode;}
  set officeAreaCode(arg) {this._telephoneNumber.areaCode = arg;}
  get officeNumber()    {return this._telephoneNumber.number;}
  set officeNumber(arg) {this._telephoneNumber.number = arg;}

// class TelephoneNumber…
  get areaCode()    {return this._areaCode;}
  set areaCode(arg) {this._areaCode = arg;}

  get number()    {return this._number;}
  set number(arg) {this._number = arg;}
```

- `Extract Class` 의 결과로 `TelephoneNumber` 가 생성 되었지만 여전히 `Person` 이 새 object 에 대한 setter 를 들고있다
  - `Change Reference to Value` 가 필요한 상황
- `Remove Setting Method` 를 통해 `TelephoneNumber` 를 immutable 로 변경
  - `Change Function Declaration`: setter 를 대응하기 위해 constructor 에 두 개 필드를 추가

```diff
+class TelephoneNumber…

  constructor(areaCode, number) {
    this._areaCode = areaCode;
    this._number = number;
  }
```

- areaCdoe 에 대해 setter 내부에서 mutate 가 아니라 객체 재생후 할당으로 변경

```diff
class Person…
  get officeAreaCode()    {return this._telephoneNumber.areaCode;}
  set officeAreaCode(arg) {
+   this._telephoneNumber = new TelephoneNumber(arg, this.officeNumber);
  }
  get officeNumber()    {return this._telephoneNumber.number;}
  set officeNumber(arg) {this._telephoneNumber.number = arg;}
```

- number 에도 동일 작업 수행

```diff
class Person…
  get officeAreaCode()    {return this._telephoneNumber.areaCode;}
  set officeAreaCode(arg) {
    this._telephoneNumber = new TelephoneNumber(arg, this.officeNumber);
  }
  get officeNumber()    {return this._telephoneNumber.number;}
  set officeNumber(arg) {
+   this._telephoneNumber = new TelephoneNumber(this.officeAreaCode, arg);
  }
```

- js 의 경우 수동으로 eqauls 를 구현해준다
  - TODO: 그래도 instance 의 주소값은 모르는데.. 이게 ref 인지 value 인지어떻게 구분하지

```js
// class TelephoneNumber…
  equals(other) {
    if (!(other instanceof TelephoneNumber)) return false;
    return this.areaCode === other.areaCode &&
      this.number === other.number;
  }

  it('telephone equals', function() {
    assert(        new TelephoneNumber("312", "555-0142")
           .equals(new TelephoneNumber("312", "555-0142")));
  })
```
