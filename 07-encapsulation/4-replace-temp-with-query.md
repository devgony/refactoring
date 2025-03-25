> Replace Temp with Query

```js
const basePrice = this._quantity * this._itemPrice;
if (basePrice > 1000) return basePrice * 0.95;
else return basePrice * 0.98;
```

👇

```js
get basePrice() {this._quantity * this._itemPrice;}

...

if (this.basePrice > 1000)
  return this.basePrice * 0.95;
else
  return this.basePrice * 0.98;
```

# Motivation

- temp variable 은 특정 코드의 결과값을 캡처해서 추후에 함수에서 사용하기 위함이다
- 변수를 쓰는건 편하지만, 함수를 바로 사용하는 것이 나은 경우도 있다
- 큰 함수를 쪼갤 때는 변수를 함수로 바꾸는 것이 일부분을 추출하기 용이하다
  - 추출 된 함수에 인자를 전달할 필요가 없어지기 때문
- 함수에 두면 경계를 설정하게 되므로 잘못된 의존성이나 side effect 를 방지할 수 있다
- 이 리팩터링은 클래스 내부에 있을 때 가장 유용하다

  - class 가 shared context 제공 하므로
  - class 외부인 경우 인자가 너무 많이지므로 중첩함수를 사용하여 해결
    - 하지만 함수간에 로직을 공유하는 기능이 제한된다

- `Replace Temp with Query` 가 적합한 경우
  - 한번 계산한 후에는 조회만 하는 경우

# Mechanics

- 변수가 사용되기 전에 고정이 되었는지, 추후에 다른값으로 변경되어야하는지 확인
- 변수가 read-only 인지, read-only 로 바꿀 수 있는지 확인
- Test
- 변수 할당 부분을 함수로 추출
- Test
- `Inline Variable` 통해 temp 변수를 제거

# Example

```js
// class Order…
  constructor(quantity, item) {
    this._quantity = quantity;
    this._item = item;
  }

  get price() {
    var basePrice = this._quantity * this._item.price;
    var discountFactor = 0.98;
    if (basePrice > 1000) discountFactor -= 0.03;
    return basePrice * discountFactor;
  }
}

```

- `basePrice` 를 const 로 변경하여 read-only 인지 확인
- `basePrice` 에 할당된 로직을 함수로 추출

```js
// class Order…
  get price() {
    const basePrice = this.basePrice;
    //..
  }

  get basePrice() {
    return this._quantity * this._item.price;
  }

```

- `Inline Variable` 수행

```diff
// class Order…
  get price() {
-   const basePrice = this.basePrice;
    var discountFactor = 0.98;
+   if (this.basePrice > 1000) discountFactor -= 0.03;
+   return this.basePrice * discountFactor;
  }
```

- `discountFactor` 에도 동일 작업 수행
  - 두 개의 assignment 를 한번에 추출

```js
// class Order…
  get price() {
    const discountFactor = this.discountFactor;
    return this.basePrice * discountFactor;
  }

  get discountFactor() {
    var discountFactor = 0.98;
    if (this.basePrice > 1000) discountFactor -= 0.03;
    return discountFactor;
  }
```

- `Inline Variable` 수행

```js
get price() {
  return this.basePrice * this.discountFactor;
}
```
