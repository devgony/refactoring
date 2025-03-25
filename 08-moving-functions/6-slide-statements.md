> Slide Statements

```js
const pricingPlan = retrievePricingPlan();
const order = retreiveOrder();
let charge;
const chargePerUnit = pricingPlan.unit;
```

👇

```js
const pricingPlan = retrievePricingPlan();
const chargePerUnit = pricingPlan.unit;
const order = retreiveOrder();
let charge;
```

# Motivation

- 관계 있는 코드가 함께 나타나면 이해하기 쉽다
- `Slide Statements` 를 통해 함께 모일수 있도록 변경
- 저자의 경우는 변수를 첫사용 직전에 선언하는 것을 선호한다

# Mechanics

- 코드 조각을 옮길 target 식별
  - source 와 target 간에 간섭이 있는지 확인
    - 참조하는 변수의 선언보다 위로 선언 불가
    - 참조하는 변수보다 하단으로 이동 불가?
    - 참조하는 요소를 수정하는 구문은 이동 불가
    - 요소를 수정하는 코드는 수정된 요소를 참조하는 다른 요소쪽으로 이동 불가
- source 에서 target 으로 복붙
- Test
  - 실패할 경우 이동 하려는 구문의 단위를 더 작게 줄여 재 시도

# Example

- 무엇을 옮길지
  - 선언부를 사용처 주변으로 이동
  - `Extract Function`
- 옮길 수 있을지
  - 프로그램의 외부 행동을 변경할 만한 간섭이 생기는지 확인

```js
 1 const pricingPlan = retrievePricingPlan();
 2 const order = retreiveOrder();
 3 const baseCharge = pricingPlan.base;
 4 let charge;
 5 const chargePerUnit = pricingPlan.unit;
 6 const units = order.units;
 7 let discount;
 8 charge = baseCharge + units * chargePerUnit;
 9 let discountableUnits = Math.max(units - pricingPlan.discountThreshold, 0);
10 discount = discountableUnits * pricingPlan.discountFactor;
11 if (order.isRepeat) discount += 20;
12 charge = charge - discount;
13 chargeOrder(charge);
```

- discount 와 관련된 것들은 하나로 모으고 싶다
  - `Extract Function` 으로 discount 관련 로직을 추출하기 위해서는 선언부를 이동해야 함
  - 7 line 을 10 line 위로 이동
- Line 2 를 Line 6 위로 이동
- Line 2 에 대해서는 retrieveOrder 내부 로직 확인 필요

- `Command-Query Separation`: value 를 return 하는 함수는 side effect 가 없다

  - `Commands(modifiers, mutators)`: system 의 상태를 변경하지만 value 를 return 하지 않음

  - `Queries`: 결과를 return 하고 system 의 상태를 변화시키지 않음

- side effect 가 있는 코드를 옮기거나, 특정 코드를 side effect 가 존재하는 곳으로 옮길 경우 최대한 주의

  - Line 11 을 Line 12 보다 아래로 내릴 수 없다
  - Line 13 을 Line 12 보다 위로 올릴 수 없다
  - Line 8 은 9-11 사이 어딘가로는 이동 가능하다

- 각각이 모두 mutation 을 하고 참조하는 경우 이동 불가능
- mutation 을 최대한 줄이는 것이 좋으므로 slide 전에 `Split Variable` 수행

# Example: Sliding with Conditionals

- 중복 코드를 가지는 조건절

```js
let result;
if (availableResources.length === 0) {
  result = createResource();
  allocatedResources.push(result);
} else {
  result = availableResources.pop();
  allocatedResources.push(result);
}
return result;
```

- Slide it outside of condiitional block

```js
let result;
if (availableResources.length === 0) {
  result = createResource();
} else {
  result = availableResources.pop();
}
allocatedResources.push(result);
return result;
```

# Future Reading

- `Swap Statement`: 단일 문장 이동
- `Silde Statements`: 복수 문장
