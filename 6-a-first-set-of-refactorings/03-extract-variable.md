> Extract Variable

```js
return (
  order.quantity * order.itemPrice -
  Math.max(0, order.quantity - 500) * order.itemPrice * 0.05 +
  Math.min(order.quantity * order.itemPrice * 0.1, 100)
);
```

👇

```js
const basePrice = order.quantity * order.itemPrice;
const quantityDiscount =
  Math.max(0, order.quantity - 500) * order.itemPrice * 0.05;
const shipping = Math.min(basePrice * 0.1, 100);
return basePrice - quantityDiscount + shipping;
```

# Motivation

- expression 은 복잡해 질 수 있지만 특정 상황에서는 일부분에 이름을 부여하여 목적에 대한 이해를 높일 수 있다
- debug 하거나 print 하기도 쉽다
- 현재 작업하고 있는 함수 내에서 추출된 변수의 의미가 충분하다면 괜찮지만
- 더 넒은 context에 있어야 의미가 생긴다면 대체로 더 상위 context 에 함수로 추출한다.
  - 그렇게 되면 다른 곳에서도 재사용하여 중복을 더욱 줄일수 있다

# Mechanics

- 추출하려는 expression 이 영향도가 없는지 파악한다
- immutable variable 선언하고 추출할 부분을 복사하여 할당한다
- 기존 expression 을 새 variable 로 교체한다
- 동일한 expression 이 더 존재하면 하나 씩 바꾸면서 테스트를 수행한다

# Example

```diff
function price(order) {
  //price is base price - quantity discount + shipping
+ const basePrice = order.quantity * order.itemPrice;
  return (
-   order.quantity * order.itemPrice -
+   basePrice
    Math.max(0, order.quantity - 500) * order.itemPrice * 0.05 +
-   Math.min(order.quantity * order.itemPrice * 0.1, 100)
+   Math.min(basePrice * 0.1, 100)
  );
}
```

```diff
function price(order) {
  //price is base price - quantity discount + shipping
  const basePrice = order.quantity * order.itemPrice;
+ const quantityDiscount = Math.max(0, order.quantity - 500) * order.itemPrice * 0.05;
  return basePrice -
+   quantityDiscount +
    Math.min(basePrice * 0.1, 100);
}
```

```diff
function price(order) {
  const basePrice = order.quantity * order.itemPrice;
  const quantityDiscount =
    Math.max(0, order.quantity - 500) * order.itemPrice * 0.05;
+ const shipping = Math.min(basePrice * 0.1, 100);
  return basePrice - quantityDiscount +
+   shipping;
}
```

# Example: With a Class

- 위와 동일한 예제이지만 class context 내에서 수행

```js
class Order {
  constructor(aRecord) {
    this._data = aRecord;
  }

  get quantity() {
    return this._data.quantity;
  }
  get itemPrice() {
    return this._data.itemPrice;
  }

  get price() {
    return (
      this.quantity * this.itemPrice -
      Math.max(0, this.quantity - 500) * this.itemPrice * 0.05 +
      Math.min(this.quantity * this.itemPrice * 0.1, 100)
    );
  }
}
```

- 추출한 이름이 Order class 전체에 영향을 미치므로 이를 활용하여 메서드로 추출

```js
class Order {
  constructor(aRecord) {
    this._data = aRecord;
  }
  get quantity() {
    return this._data.quantity;
  }
  get itemPrice() {
    return this._data.itemPrice;
  }

  get price() {
    return this.basePrice - this.quantityDiscount + this.shipping;
  }
  get basePrice() {
    return this.quantity * this.itemPrice;
  }
  get quantityDiscount() {
    return Math.max(0, this.quantity - 500) * this.itemPrice * 0.05;
  }
  get shipping() {
    return Math.min(this.basePrice * 0.1, 100);
  }
}
```

- 이처럼 다른 로직과 데이터에 의미를 공유할 수 있는 것이 class 의 장점
