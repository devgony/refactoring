> Replace Control Flag with Break

```js
for (const p of people) {
  if (!found) {
    if (p === "Don") {
      sendAlert();
      found = true;
    }
  }
}
```

👇

```js
for (const p of people) {
  if (p === "Don") {
    sendAlert();
    break;
  }
}
```

# Motivation

- control flag 는 코드를 이해하기 어렵게 만든다
- early return 을 사용하면 control flag 를 제거할 수 있다

# Mechanics

- control flag 가 사용된 부분을 `Extract Function`
- 각 control flag 업데이트에 대해 적절한 control statement 로 변경
  - eg) return, break or continue
- 모든 update 가 끝나면 control flag 제거

# Example

```js
// some unimportant code
let found = false;
for (const p of people) {
  if (!found) {
    if (p === "Don") {
      sendAlert();
      found = true;
    }
    if (p === "John") {
      sendAlert();
      found = true;
    }
  }
}
// more code
```

- control flag 를 사용하는 scope 모두를 `Extract Function`

```js
// some unimportant code
checkForMiscreants(people);
// more code
function checkForMiscreants(people) {
  let found = false;
  for (const p of people) {
    if (!found) {
      if (p === "Don") {
        sendAlert();
        found = true;
      }
      if (p === "John") {
        sendAlert();
        found = true;
      }
    }
  }
}
```

- found 가 true 가 되면 loop 가 끝나는 것이므로 break 혹은 return 으로 변경한다
  - 여기서 함수는 특별한 return 값이 없으므로 return 을 사용한다

```diff
function checkForMiscreants(people) {
  let found = false;
  for (const p of people) {
    if (!found) {
      if (p === "Don") {
        sendAlert();
+       return;
      }
      if (p === "John") {
        sendAlert();
+       return;
      }
    }
  }
}
```

- control flag 를 제거한다

```diff
function checkForMiscreants(people) {
- let found = false;
  for (const p of people) {
-   if (! found) {
      if ( p === "Don") {
        sendAlert();
        return;
      }
      if ( p === "John") {
        sendAlert();
        return;
      }
    }
  }
}
```

- 함수형 메서드로 더 단순하게 만들 수 있다

```js
function checkForMiscreants(people) {
  if (people.some((p) => ["Don", "John"].includes(p))) sendAlert();
}
```

- cf) 아래와 같은 함수가 있으면 좋겠다고 하는데 이미 [isDisjointFrom()](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Set/isDisjointFrom) 은 있는 것 같다

```js
["Don", "John"].isDisjointWith(people);
```
