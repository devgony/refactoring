> Pull Up Method

```js
class Employee {...}

class Salesman extends Employee {
  get name() {...}
}

class Engineer extends Employee {
  get name() {...}
}
```

👇

```js
class Employee {
  get name() {...}
}

class Salesman extends Employee {...}
class Engineer extends Employee {...}
```

# Motivation

- 공통 구현을 부모에서 묶어서 처리하는 것
- 두 개의 중복된 코드가 멈춰있다면 그대로 내버려 둬도 되지만, 코드의 변경이 공유되지 않으면서 미래에는 버그를 가져온다
- 다른 단계 뒤에 Pull Up Method 가 오는 경우, 서로 다른 클래스에 두 메서드가 본질적으로는 같은 메서드로 끝나는 방식으로 매개변수화 될 수 있다

  - Parameterize Function 을 별도로 적용한 다음 Pull Up Method 적용한다

- 부모에 존재 하지 않는 메서드를 묶고 싶은 경우 `Pull Up Field` 먼저 수행 하고 `Pull Up Method` 수행
- 대부분 같지만 약간 다른 메서드들의 경우 `Form Template Method` 수행

# Mechanics

- 두 메서드의 동등성 검사
  - 약간 다르면 같아질 때까지 refactor
- 모든 메서드 호출이 부모에서 가능한지 확인
- 각 메서드의 시그니처가 다른 경우 `Change Function Declaration`
- 부모에 새 메서드 생성 후 자식의 body 로 채우기
- static check
- 자식 메서드 하나 지우기
- Test
- 남은 자식 메서드 하나씩 지우기 반복

# Example

```js
class Employee extends Party…
  get annualCost() {
    return this.monthlyCost * 12;
  }

class Department extends Party…
  get totalAnnualCost() {
    return this.monthlyCost * 12;
  }
```

- 정적인 언어의 경우 monthlyCost 를 위해 abstract method 를 Party 에 정의 해주어야 한다
- 이름이 다르므로 `Ch`ange Function Declaration` 을 통해 이름을 바꿔준다

```diff
class Department…
+ get annualCost() {
    return this.monthlyCost * 12;
  }
```

- 부모로 메서드 복제

```diff
+class Party…
+
+  get annualCost() {
+    return this.monthlyCost * 12;
+  }
```

- annualCost 를 Employee 에서 제거
- annualCost 를 Department 에서 제거
- monthlyCost 를 구현하지않아 조회 못하는 자식을 대비해 에러를 던지도록 기본 구현 설정

```js
class Party…
  get monthlyCost() {
    throw new SubclassResponsibilityError();
  }
```
