> Pull Up Field

```js
class Employee {...} // Java

class Salesman extends Employee {
  private String name;
}

class Engineer extends Employee {
  private String name;
}
```

👇

```js
class Employee {
  protected String name;
}

class Salesman extends Employee {...}
class Engineer extends Employee {...}
```

# Motivation

- 서브클래스를 독립적으로 개발하거나 리팩토링으로 결합하는 경우 기능이 중복될 수 있다.
- 특히 특정 필드가 중복되는데 이름이 비슷할 수도 있고 아닐수도 있다.
- 중복을 식별할 유일한 방법은 필드가 어떻게 사용되는지 살펴보는 것
- 비슷하게 사용되고 있다면 수퍼클래스로 끌어 올린다
  - 중복데이터 선언을 제거
  - 해당 필드를 사용하는 동작을 서브 -> 슈퍼로 옮김
- 많은 동적 언어는 필드를 클래스 정의의 일부로 정의하지 않는다
  - 대신 필드가 처음 할당될 때 나타난다
  - 필드를 슈퍼로 끌어 올리는 것은 생성자 본문을 끌어 올리는것과 같다

# Mechanics

- 필드를 사용하는 모든 후보군을 조사하여 유사한 사용을 검사한다
- 해당 필드들이 서로 다른이름을 사용한다면 `Rename Field` 를 통해 통일시킨다
- 슈퍼클래스에 새 필드 생성
  - 새 필드는 서브클래스에서 접근 가능해야한다 (protected)
- 서브클래스의 필드를 제거
- Test
