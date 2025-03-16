> Replace Drived Variable with Query

```js
get discountedTotal() {return this._discountedTotal;}
set discount(aNumber) {
  const old = this._discount;
  this._discount = aNumber;
  this._discountedTotal += old - aNumber;
}
```

👇

```js
get discountedTotal() {return this._baseTotal - this._discount;}
set discount(aNumber) {this._discount = aNumber;}
```

# Motivation

- mutable data 는 코드의 여러 부분을 어색하게 결합하여 버그 발견을 어렵게 한다
- 모든 mutable data 를 없애는거 어렵지만 변경 가능한 데이터의 범위를 최소화 하는 것이 좋다

- 쉽게 계산할 수 있는 변수 하나를 제거 해보라
- 계산을 하면 데이터의 의미가 더 명확해지고, 소스 데이터가 변경될 때, 업데이트 하지 못해도 데이터가 손상되는 것을 방지할 수 있다.

- 쉽게 계산할 수 있는 변수는 Inline 으로 제거하는 게 낫다.
- 그 때마다 계산을 하기 때문에 최신 데이터를 기반으로 결과가 나오므로 mutable data 대응 가능
  - 원본이 immutable 이고 계산 결과도 immutable 이면 변수로 그대로 둬도 된다

# Mechanics

- 모든 변수의 update 되는 지점을 확인. 필요하다면 `Split Variable` 사용
- 변수의 값을 계산하는 함수 생성
- `Introduce Assertion` 으로 변수와 계산이 같은 결과를 가지도록 검증
- Test
- 변수를 조회하는 부분을 새 함수 호출로 변경
- Test
- `Remove Dead Code`

# Example

```js
// class ProductionPlan…
  get production() {return this._production;}
  applyAdjustment(anAdjustment) {
    this._adjustments.push(anAdjustment);
    this._production += anAdjustment.amount;
  }
```

- `Introduction Assertion` 으로 `_production` 과 `calculatedProduction` 이 같은 결과를 가지도록 검증

```js
// class ProductionPlan…
  get production() {
    assert(this._production === this.calculatedProduction);
    return this._production;
  }

  get calculatedProduction() {
    return this._adjustments
      .reduce((sum, a) => sum + a.amount, 0);
  }
```

- Test 이상 없다면 `productio()` 이 바로 return 하도록 `Inline Function`

```js
// class ProductionPlan…
  get production() {
    return this._adjustments
      .reduce((sum, a) => sum + a.amount, 0);
  }
```

- 과거 참조하던 변수 `Remove Dead Code`

```diff
// class ProductionPlan…
  applyAdjustment(anAdjustment) {
    this._adjustments.push(anAdjustment);
-   this._production += anAdjustment.amount;
  }
```

# Example: More Than One Source

```js
// class ProductionPlan…
  constructor (production) {
    this._production = production;
    this._adjustments = [];
  }
  get production() {return this._production;}
  applyAdjustment(anAdjustment) {
    this._adjustments.push(anAdjustment);
    this._production += anAdjustment.amount;
  }
```

- 위 예제에서 `_production` 의 초기값이 0 이 아닌 경우 assert 가 실패한다
- 먼저 `Split Variable` 로 초기값을 분리

```js
constructor (production) {
  this._initialProduction = production;
  this._productionAccumulator = 0;
  this._adjustments = [];
}
get production() {
  return this._initialProduction + this._productionAccumulator;
}
```

- 이후 `Introduced Assertion` 할 수 있다
  - 단 totalProductionAdjustments 는 inline 하지 않고 그대로 둔다
    - TODO: 그런 필드 없는데..

```js
  // class ProductionPlan…
  get production() {
    assert(this._productionAccumulator === this.calculatedProductionAccumulator);
    return this._initialProduction + this._productionAccumulator;
  }

  get calculatedProductionAccumulator() {
    return this._adjustments
        .reduce((sum, a) => sum + a.amount, 0);
  }
```
