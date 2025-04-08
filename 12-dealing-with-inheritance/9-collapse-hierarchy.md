> Collapse Hierarchy

```js
class Employee {...}
class Salesman extends Employee {...}
```

👇

```js
class Employee {...}
```

# Motivation

- 리팩터링 하다보면 부모를 분리할 필요가 없어지는 경우가 생긴다 -> 하나로 병합

# Mechanics

- 제거할 대상을 선택

  - 미래에 더 나은 이름을 가지는 것을 남겨둔다
  - 두 이름 모두 좋은 경우 아무거나 고른다

- `Pull Up Field`, `Push Down Field`, `Pull Up Method`, `Push Down Method` 로 모든 요소를 하나의 class 로 병합
- 사라진 클래스를 참조하던 client 수정
- 빈 class 제거
- Test
