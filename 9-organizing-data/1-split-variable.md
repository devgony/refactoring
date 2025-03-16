> Split Variable

```js
let temp = 2 * (height + width);
console.log(temp);
temp = height * width;
console.log(temp);
```

👇

```js
const perimeter = 2 * (height + width);
console.log(perimeter);
const area = height * width;
console.log(area);
```

# Motivation

- 많은 변수들은 결과값을 들고 있다가 나중에 참조하기 위해 존재
  - 이러한 목적의 변수가 두 번 이상 할당 되는것은 목적도 두 개이상이라는 신호
- 목적당 변수 하나씩으로 분리 한다

# Mechanics

- 변수의 선언부와 첫 할당에서 이름을 변경한다
  - `i = i + something` 과 같은 collecting 변수인 경우에는 분리하면 안된다
- 가능하다면, 새 변수를 immutable 로 선언한다
- Test
- 최종 할당이 나올 때 까지 위 단계를 반복한다

# Example

- haggis 의 이동 거리 계산

```js
function distanceTravelled(scenario, time) {
  let result;
  let acc = scenario.primaryForce / scenario.mass;
  let primaryTime = Math.min(time, scenario.delay);
  result = 0.5 * acc * primaryTime * primaryTime;
  let secondaryTime = time - scenario.delay;
  if (secondaryTime > 0) {
    let primaryVelocity = acc * scenario.delay;
    acc = (scenario.primaryForce + scenario.secondaryForce) / scenario.mass;
    result +=
      primaryVelocity * secondaryTime +
      0.5 * acc * secondaryTime * secondaryTime;
  }
  return result;
}
```

- acc 가 두 번 할당 되고 있다. 분리 필요
- 변수의 첫 정의에 대해 이름을 변경하고, 참조하는 모든 ref 도 변경
- let -> const 로 immutable 변경

```diff
function distanceTravelled(scenario, time) {
  let result;
+ const primaryAcceleration = scenario.primaryForce / scenario.mass;
  let primaryTime = Math.min(time, scenario.delay);
+ result = 0.5 * primaryAcceleration * primaryTime * primaryTime;
  let secondaryTime = time - scenario.delay;
  if (secondaryTime > 0) {
+   let primaryVelocity = primaryAcceleration * scenario.delay;
+   let acc = (scenario.primaryForce + scenario.secondaryForce) / scenario.mass;
    result +=
      primaryVelocity * secondaryTime +
      0.5 * acc * secondaryTime * secondaryTime;
  }
  return result;
}
```

- 두 번째 변수도 적합한 이름으로 변경

```diff
function distanceTravelled (scenario, time) {
    let result;
    const primaryAcceleration = scenario.primaryForce / scenario.mass;
    let primaryTime = Math.min(time, scenario.delay);
    result = 0.5 * primaryAcceleration * primaryTime * primaryTime;
    let secondaryTime = time - scenario.delay;
    if (secondaryTime > 0) {
      let primaryVelocity = primaryAcceleration * scenario.delay;
+     const secondaryAcceleration = (scenario.primaryForce + scenario.secondaryForce) / scenario.mass;
      result += primaryVelocity * secondaryTime +
+       0.5 * secondaryAcceleration * secondaryTime * secondaryTime;
    }
    return result;
  }
```

# Example: Assigning to an Input Parameter

- 변수가 인자로 정의된 경우

```js
function discount(inputValue, quantity) {
  if (inputValue > 50) inputValue = inputValue - 2;
  if (quantity > 100) inputValue = inputValue - 1;
  return inputValue;
}
```

- `Split Variable` 로 값을 복제

```diff
+function discount (originalInputValue, quantity) {
+   let inputValue = originalInputValue;
    if (inputValue > 50) inputValue = inputValue - 2;
    if (quantity > 100) inputValue = inputValue - 1;
    return inputValue;
  }
```

- `Rename Variable` 로 더 나은 이름 변경

```js
function discount(inputValue, quantity) {
  let result = inputValue;
  if (inputValue > 50) result = result - 2;
  if (quantity > 100) result = result - 1;
  return result;
}
```

`let result = inputValue;` 구문이 의미 없게 보일지라도, result 는 inputValue를 기반으로 변경을 누적하는 것이라고 보면 된다.
