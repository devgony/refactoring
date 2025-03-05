> Inline Class

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

👇

```js
class Person {
  get officeAreaCode() {return this._officeAreaCode;}
  get officeNumber()   {return this._officeNumber;}
```

# Motivation

- class 로서의 가치가 더 이상 없거나 책임 이동의 결과로서 책임이 조금만 남았다면 타 class 에 합친다
- 두 클래스를 기능 할당이 다른 한쌍의 클래스로 리팩터링 하고싶은경우
  - 일단 `Inline Class` 로 하나의 class 를 만든 후 `Extract Class` 로 두개로 나눈다

# Mechanics

- target class 에 public 함수를 만들어 source class 의 함수로 그대로 위임하게 한다
- source class method 에 대한 모든 참조를 target class 로 변경
- source class 의 모든 method 를 target class 로 옮긴다
- source class 를 삭제한다

# Example

- shipment 예제 코드

```js
class TrackingInformation {
  get shippingCompany() {
    return this._shippingCompany;
  }
  set shippingCompany(arg) {
    this._shippingCompany = arg;
  }
  get trackingNumber() {
    return this._trackingNumber;
  }
  set trackingNumber(arg) {
    this._trackingNumber = arg;
  }
  get display() {
    return `${this.shippingCompany}: ${this.trackingNumber}`;
  }
}

class Shipment {
  get trackingInfo() {
    return this._trackingInformation.display;
  }
  get trackingInformation() {
    return this._trackingInformation;
  }
  set trackingInformation(aTrackingInformation) {
    this._trackingInformation = aTrackingInformation;
  }
}

//caller..
aShipment.trackingInformation.shippingCompany = request.vendor;
```

- TrackingInformation 을 Shipment 로 합치기 위해서..
  - `shippingCompany` 복제하여 생성
  - 모든 method 에 대해 반복 수행

```js
// class Shipment…
  set shippingCompany(arg) {this._trackingInformation.shippingCompany = arg;}

// caller…
  aShipment.shippingCompany = request.vendor;
```

- `display` method 를 `Inline Function` 통해 이동

````js
// class Shipment…
  get trackingInfo() {
    return `${this.shippingCompany}: ${this.trackingNumber}`;
  }

- shipping company field 이동
```js
 get shippingCompany()    {return this._shippingCompany;}
  set shippingCompany(arg) {this._shippingCompany = arg;}
````

- 모두 옮겨지면 TrackingInformation class 를 삭제한다

```js
// class Shipment…
  get trackingInfo() {
    return `${this.shippingCompany}: ${this.trackingNumber}`;
  }
  get shippingCompany()    {return this._shippingCompany;}
  set shippingCompany(arg) {this._shippingCompany = arg;}
  get trackingNumber()    {return this._trackingNumber;}
  set trackingNumber(arg) {this._trackingNumber = arg;}
```
