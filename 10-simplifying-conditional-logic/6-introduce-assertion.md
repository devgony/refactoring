> Introduce Assertion

```js
if (this.discountRate) base = base - this.discountRate * base;
```

```js
assert(this.discountRate >= 0);
if (this.discountRate) base = base - this.discountRate * base;
```

# Motivation

- 종종 코드의 일부분은 특정 조건에서만 동작한다
- assertion 으로 조건을 명시적으로 표현하면, 코드의 의도를 명확히 할 수 있다
- assertion 은 모두 제거핻도 프로그램이 동일하고 올바르게 작동하도록 작성해야 한다
- assertion 은 의사소통의 한 형태이다. 디버깅도 돕긴 하지만 unit test 를 작성하는 것이 더 좋다.

# Mechanics

- 조건절이 true 가 될 것으로 보이면 assertion 을 추가한다

# Example

- 할인 계산에 대한 예제

```js
class Customer…
  applyDiscount(aNumber) {
    return (this.discountRate)
      ? aNumber - (this.discountRate * aNumber)
      : aNumber;
  }
```

- ternary 는 assertion 추가가 어려우므로 if-else 문으로 변경

```js
class Customer…
  applyDiscount(aNumber) {
    if (!this.discountRate) return aNumber;
    else return aNumber - (this.discountRate * aNumber);
  }
```

- assertion 추가

```js
class Customer…
  applyDiscount(aNumber) {
    if (!this.discountRate) return aNumber;
    else {
      assert(this.discountRate >= 0);
      return aNumber - (this.discountRate * aNumber);
    }
  }
```

- setter 에 assertion 을 넣어서 필드에 비정상 값이 들어가는것 자체를 차단하는게 낫겠다

```js
class Customer…
  set discountRate(aNumber) {
    assert(null === aNumber || aNumber >= 0);
    this._discountRate = aNumber;
  }
```

- 과도한 assertion 남용의 위험: 모든 true 인 조건을 다 검사하면 안된다
- true 가 되야만 하는 조건을 검사해야한다
- 중복된 assertion 은 `Extract Function`
- assertion 은 주로 programmer error 검출에 사용한다
- 외부 source 를 읽을 때는 단순 assertion 이 아니라 값 검사가 우선시 되어야한다.
- assertion 은 버그 추적의 최후 수단이기 때문에, 역설적으로, 절대 실패해서는 안될 때, 소통을 위해 사용한다
