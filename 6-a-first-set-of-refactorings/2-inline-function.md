> Inline Function

```js
function getRating(driver) {
  return moreThanFiveLateDeliveries(driver) ? 2 : 1;
}

function moreThanFiveLateDeliveries(driver) {
  return driver.numberOfLateDeliveries > 5;
}
```

👇

```js
function getRating(driver) {
  return driver.numberOfLateDeliveries > 5 ? 2 : 1;
}
```

# Motivation

- 가끔은 함수의 구현부가 이름만큼 명확한 경우도 있다
- 불필요한 간접성은 코드를 이해하기 어렵게 만든다
- 일부 함수들이 잘못 구성된 경우 일단 inline 으로 모두 바꾼후에 모아두고 다시 추출하는 경우도 있다
- 너무 많은 위임이 발생하고 있는 경우

# Mechanics

- 다형성을 가진 함수인지 확인해야 한다

  - 메서드가 클래스 안에 있고 서브클래스에서 오버라이드 되는 경우, inline 하기 어렵다

- 모든 호출자를 찾는다
- 각 호출을 함수 본문으로 교체한다
- 테스트 수행
  - 한번에 다 inline 할 필요는 없다. 항상 단계별로 수행
- 함수 선언을 삭제한다
- 단, recursion, mutiple reutrn, 접근자가 없는 object 에서의 inline 은 어렵다

# Example

```js
function rating(aDriver) {
  return moreThanFiveLateDeliveries(aDriver) ? 2 : 1;
}

function moreThanFiveLateDeliveries(dvr) {
  return dvr.numberOfLateDeliveries > 5;
}
```

- dvr 는 aDriver 로 이름 까지 변경

```js
function rating(aDriver) {
  return aDriver.numberOfLateDeliveries > 5 ? 2 : 1;
}
```

```js
function reportLines(aCustomer) {
  const lines = [];
  gatherCustomerData(lines, aCustomer);
  return lines;
}
function gatherCustomerData(out, aCustomer) {
  out.push(["name", aCustomer.name]);
  out.push(["location", aCustomer.location]);
}
```

## 한번에 하나씩 옮기기

- 항상 작은 단계로 수행

```diff
function reportLines(aCustomer) {
    const lines = [];
+   lines.push(["name", aCustomer.name]);
    gatherCustomerData(lines, aCustomer);
    return lines;
}
function gatherCustomerData(out, aCustomer) {
-   out.push(["name", aCustomer.name]);
    out.push(["location", aCustomer.location]);
}
```

```diff
function reportLines(aCustomer) {
    const lines = [];
    lines.push(["name", aCustomer.name]);
+   lines.push(["location", aCustomer.location]);
    return lines;
}
```
