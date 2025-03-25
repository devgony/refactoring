> Replace Magic Literal

```js
function potentialEnergy(mass, height) {
  return mass * 9.81 * height;
}
```

👇

```js
const STANDARD_GRAVITY = 9.81;
function potentialEnergy(mass, height) {
  return mass * STANDARD_GRAVITY * height;
}
```

# Motivation

- constant 이름으로 literal 의 의미를 명확히 표현
- 모든 참조를 constant 로 바꾸는것도 좋지만 constant 가 비교 테스트에 쓰인 경우 boolean return 하는 함수로 변환
- `ONE = 1` 처럼 추가의미가 없는데 과도한 사용은 좋지 않다
  - 단일 함수에서 쓰이는 값인 경우 굳이 추출 필요없다

# Mechanics

- constant 를 선언하고, literal 을 할당한다
- literal 을 참조하는 모든 곳을 찾는다
- 각각에 대해 constant 의미에 부합한다면 변경한다
- constant 의 값을 바꿔서 테스트가 실패하는지도 확인해본
