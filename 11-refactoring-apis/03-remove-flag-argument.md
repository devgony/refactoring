> Remove Flag Argument

```js
function setDimension(name, value) {
  if (name === "height") {
    this._height = value;
    return;
  }
  if (name === "width") {
    this._width = value;
    return;
  }
}
```

👇

```js
function setHeight(value) {
  this._height = value;
}

function setWidth(value) {
  this._width = value;
}
```

# Motivation

- flag arguement: 인자를 bool 로 사용 + 제어 흐름에 영향을 주는 용도로 활용
- flag argument는 함수를 어떻게 사용해야할지 이해하기 어렵게 만든다
- flag 를 제거하므로서 중요한 로직과 일반 로직의 구분이 용이해진다
  만든다
- 함수에 flag 가 두 개 이상 있는 경우는 모두 함수로 나누면 경우의 수가 너무 많으므로 어쩔 수 없이 flag 를 사용할 수도 있다.
  - 하지만 설계 자체를 바꾸거나 더 간단한 함수를 구현할 수는 없는지 점검 해보아야한다

# Mechanics

- 각 인자에 대한 명시적인 함수를 생성 한다
- 기존에 literal 값은 인자로 넘겨주던 호출자들을 신규 함수 호출로 변경

# Example

- 배송일 계산하는 예제

```js
aShipment.deliveryDate = deliveryDate(anOrder, true);
//..
aShipment.deliveryDate = deliveryDate(anOrder, false);
//..

function deliveryDate(anOrder, isRush) {
  if (isRush) {
    let deliveryTime;
    if (["MA", "CT"].includes(anOrder.deliveryState)) deliveryTime = 1;
    else if (["NY", "NH"].includes(anOrder.deliveryState)) deliveryTime = 2;
    else deliveryTime = 3;
    return anOrder.placedOn.plusDays(1 + deliveryTime);
  } else {
    let deliveryTime;
    if (["MA", "CT", "NY"].includes(anOrder.deliveryState)) deliveryTime = 2;
    else if (["ME", "NH"].includes(anOrder.deliveryState)) deliveryTime = 3;
    else deliveryTime = 4;
    return anOrder.placedOn.plusDays(2 + deliveryTime);
  }
}
```

- 함수 사용의 요점은 호출자의 지시를 따르는 것이므로 명시적 함수를 사용하여 의도를 명확히 한다

- `Decompose Conditional`

```js
function deliveryDate(anOrder, isRush) {
  if (isRush) return rushDeliveryDate(anOrder);
  else return regularDeliveryDate(anOrder);
}
function rushDeliveryDate(anOrder) {
  let deliveryTime;
  if (["MA", "CT"].includes(anOrder.deliveryState)) deliveryTime = 1;
  else if (["NY", "NH"].includes(anOrder.deliveryState)) deliveryTime = 2;
  else deliveryTime = 3;
  return anOrder.placedOn.plusDays(1 + deliveryTime);
}
function regularDeliveryDate(anOrder) {
  let deliveryTime;
  if (["MA", "CT", "NY"].includes(anOrder.deliveryState)) deliveryTime = 2;
  else if (["ME", "NH"].includes(anOrder.deliveryState)) deliveryTime = 3;
  else deliveryTime = 4;
  return anOrder.placedOn.plusDays(2 + deliveryTime);
}
```

- 각 함수를 직접 부르게 수정

```diff
aShipment.deliveryDate = deliveryDate(anOrder, true);
+aShipment.deliveryDate = rushDeliveryDate(anOrder);
```

- 모든 요청자를 수정했으면 `deliveryDate` 삭제
- flag 를 쓰거나 안 쓰는 두 가지 경우가 모두 필요 할 수 있다
  - isRush 를 top level 에서 필요한 경우마다 쓰도록 내린다

```js
function deliveryDate(anOrder, isRush) {
  let result;
  let deliveryTime;
  if (anOrder.deliveryState === "MA" || anOrder.deliveryState === "CT")
    deliveryTime = isRush ? 1 : 2;
  else if (anOrder.deliveryState === "NY" || anOrder.deliveryState === "NH") {
    deliveryTime = 2;
    if (anOrder.deliveryState === "NH" && !isRush) deliveryTime = 3;
  } else if (isRush) deliveryTime = 3;
  else if (anOrder.deliveryState === "ME") deliveryTime = 3;
  else deliveryTime = 4;
  result = anOrder.placedOn.plusDays(2 + deliveryTime);
  if (isRush) result = result.minusDays(1);
  return result;
}
```

- 이 경우 단점은 isRush 가 분해되어 있어 제거 할 수 없으므로 래핑 함수를 만들어 경우를 구분

```js
function rushDeliveryDate(anOrder) {
  return deliveryDate(anOrder, true);
}
function regularDeliveryDate(anOrder) {
  return deliveryDate(anOrder, false);
}
```

- 인자를 데이터로 사용하는 호출자가 없는 경우에는 접근 제어자를 제한한다
  - TODO: 함수인데 어떻게 제어를 하라는것일까?
