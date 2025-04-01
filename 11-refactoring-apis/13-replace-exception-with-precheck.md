> Replace Exception with Precheck

```js
double getValueForPeriod (int periodNumber) {
  try {
    return values[periodNumber];
  } catch (ArrayIndexOutOfBoundsException e) {
    return 0;
  }
}
```

👇

```js
double getValueForPeriod (int periodNumber) {
  return (periodNumber >= values.length) ? 0 : values[periodNumber];
```

# Motivation

- 과도한 Exception 은 자제해야함
- 호출자가 호출 전에 조건을 확인하여 합리적으로 처리할 수 있다면 예외를 던지는 대신 호출자가 사용할 테스트를 제공해야한다

# Mechanics

- Exception을 발생시킬 경우를 test 하는 조건을 추가
  - catch block 을 조건절의 하나로 옮기고 나머지로직은 try block 에 남겨둔다
- assertion 을 catch block 에 추가후 Test
- try/catch 구문 제거
- Test

# Example (Java)

```java
class ResourcePool…

  public Resource get() {
    Resource result;
    try {
      result = available.pop();
      allocated.add(result);
     } catch (NoSuchElementException e) {
      result = Resource.create();
      allocated.add(result);
    }
    return result;
  }
private Deque<Resource> available;
private List<Resource> allocated;
```

- resource 를 모두 소비하는 것은 예상 가능한 조건이므로 Exception 을 사전에 test 하는 구문으로 바꿔야한다
  - catch 로직 -> if 문 이동

```java
class ResourcePool…
  public Resource get() {
    Resource result;
+   if (available.isEmpty()) {
+     result = Resource.create();
+     allocated.add(result);
+   }
+   else {
      try {
        result = available.pop();
        allocated.add(result);
      } catch (NoSuchElementException e) {
      }
    }
    return result;
  }
```

- catch 에 unreachable assertion 추가
  - 확실한 break 를 위해 assert 구문보다 throw AssertionError 를 사용하는 것이 좋다

```diff
class ResourcePool…
  public Resource get() {
    Resource result;
    if (available.isEmpty()) {
      result = Resource.create();
      allocated.add(result);
    }
    else {
      try {
        result = available.pop();
        allocated.add(result);
      } catch (NoSuchElementException e) {
+       throw new AssertionError("unreachable");
      }
    }
    return result;
  }
```

- try/catch 제거

```java
class ResourcePool…
  public Resource get() {
    Resource result;
    if (available.isEmpty()) {
      result = Resource.create();
      allocated.add(result);
    } else {
      result = available.pop();
      allocated.add(result);
    }
    return result;
  }
```

- `Slide Statements`: 마지막에 add 를 한번만 호출하도록 변경

```diff
class ResourcePool…
  public Resource get() {
    Resource result;
    if (available.isEmpty()) {
      result = Resource.create();
-     allocated.add(result);
    } else {
      result = available.pop();
-   allocated.add(result);
    }
+   allocated.add(result);
    return result;
  }

```

- if/else 를 ternary 로 변경

```diff
class ResourcePool…
  public Resource get() {
+   Resource result = available.isEmpty() ? Resource.create() : available.pop();
    allocated.add(result);
    return result;
  }
```
