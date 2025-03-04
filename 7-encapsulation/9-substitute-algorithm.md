> Substitute Algorithm

```js
function foundPerson(people) {
  for (let i = 0; i < people.length; i++) {
    if (people[i] === "Don") {
      return "Don";
    }
    if (people[i] === "John") {
      return "John";
    }
    if (people[i] === "Kent") {
      return "Kent";
    }
  }
  return "";
}
```

👇

```js
function foundPerson(people) {
  const candidates = ["Don", "John", "Kent"];
  return people.find((p) => candidates.includes(p)) || "";
}
```

# Motivation

- 때로는 전체 알고리즘을 제거하고 더 간단한 것으로 대체해야 하는 지점에 도달
- 이 단계를 수행해야 할 때는 가능한 한 메서드를 분해해야 한다

# Mechanics

- 함수를 완성시키는 원본 코드를 작성
- 해당 함수만을 위한 테스트를 작성, 행동을 캡처
- 대체 알고리즘 준비
- Static check
- 원본 알고리즘을 대체 알고리즘으로 교체하고 테스트 수행
  - 실패하면 디버깅하면서 비교
