> Separate Query from Modifier

```js
function getTotalOutstandingAndSendBill() {
  const result = customer.invoices.reduce(
    (total, each) => each.amount + total,
    0,
  );
  sendBill();
  return result;
}
```

👇

```js
function totalOutstanding() {
  return customer.invoices.reduce((total, each) => each.amount + total, 0);
}
function sendBill() {
  emailGateway.send(formatBill(customer));
}
```

# Motivation

- CQS(Command Query Separation): value 를 return 하는 함수는 side effect 가 없어야 한다
  - 이 룰을 100% 지키는 사람도 있다.
  - TODO: 100% 지키는게 많이 어렵나?
- 값을 반환하지만 side effect 가 있는 함수를 발견하면 query 와 modifier 를 분리한다
- 최적화를 위해 query 의 결과를 캐싱했다면 변화를 관측할 수 없다 -> TODO: 무얼 말하고싶나?

# Mechanics

- 함수를 복사하여 query 로 명명
  - 함수의 리턴값으로 할당하려 했던 변수로부터 힌트를 얻는다
- 새 query 함수의 side effect 가 있으면 제거한다
- static check
- 원본 메서드 호출에서 return 값을 사용하는 경우 원본 호출을 query 호출로 바꾸고 그 아래에 원본 메서드 호출을 추가. Test.
- 원본의 return 값 제거

# Example

```js
function alertForMiscreant(people) {
  for (const p of people) {
    if (p === "Don") {
      setOffAlarms();
      return "Don";
    }
    if (p === "John") {
      setOffAlarms();
      return "John";
    }
  }
  return "";
}
```

- query 에 적합한 이름으로 바꾸면서 복제

````js
function findMiscreant (people) {
    for (const p of people) {
      if (p === "Don") {
        setOffAlarms();
        return "Don";
      }
      if (p === "John") {
        setOffAlarms();
        return "John";
      }
    }
    return "";
  }```
````

- side effect 제거

```diff
function findMiscreant(people) {
  for (const p of people) {
    if (p === "Don") {
-     setOffAlarms();
      return "Don";
    }
    if (p === "John") {
-     setOffAlarms();
      return "John";
    }
  }
  return "";
}
```

- 호출자 변경

```diff
-const found = alertForMiscreant(people);
+const found = findMiscreant(people);
+alertForMiscreant(people);
```

- modifier 에서 return 제거
  - TODO: return 제거 하면 setOffAlarms() 가 두 번 호출되는데?

```diff
  function alertForMiscreant (people) {
    for (const p of people) {
      if (p === "Don") {
        setOffAlarms();
-       return;
      }
      if (p === "John") {
        setOffAlarms();
-       return;
      }
    }
-   return;
  }
```

- 중복 제거를 위해 `Substitute Algorithm`

```js
function alertForMiscreant(people) {
  if (findMiscreant(people) !== "") setOffAlarms();
}
```

- TODO: return value 로 조건문을 사용해 처리하는 리팩토링이 참신하다
