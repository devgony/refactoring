> Push Down Method

```js
class Employee {
  get quota {...}
}

class Engineer extends Employee {...}
class Salesman extends Employee {...}
```

👇

```js
class Employee {...}
class Engineer extends Employee {...}
class Salesman extends Employee {
  get quota {...}
}
```

# Motivation

- 메서드가 하나 혹은 일부분의 자식 클래스에서만 사용되는 경우
- 부모에서 필요한 자식으로 옮겨 준다
- 호출자는 특정 서브클래스에서만 작동한다는 사실을 인지하고 있을때 가능한 리팩토링
- 혹은 `Replace Conditional with Polymorphism` 적용

# Mechanics

- 메서드가 필요한 자식클래스에만 메서드를 복제한다
- 부모의 메서드 제거
- Test
- 필요 없는 메서드가 부모에 더 있다면 제거
- Test
