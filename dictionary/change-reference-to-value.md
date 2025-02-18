```js
class Product {
  applyDiscount(arg) {
    this._price.amount -= arg;
  }
}
```

👇

```js
class Product {
  applyDiscount(arg) {
    this._price = new Money(this._price.amount - arg, this._price.currency);
  }
}
```
