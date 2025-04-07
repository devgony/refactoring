> Push Down Field

```js
class Employee {        // Java
  private String quota;
}

class Engineer extends Employee {...}
class Salesman extends Employee {...}
```

👇

```js
class Employee {...}
class Engineer extends Employee {...}

class Salesman extends Employee {
  protected String quota;
}
```

# Motivation

- 필드가 특정 자식클래스에서만 사용되는 경우, 부모에서 자식으로 옮긴다

# Mechanics

- 자식클래스에서 필요한 모든 필드를 선언한다
- 부모클래스에서 필드 제거
- Test
- 추가로 불필요한 필드 있으면 반복
- Test
