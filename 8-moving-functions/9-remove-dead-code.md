> Remove Dead Code

```js
if (false) {
  doSomethingThatUsedToMatter();
}
```

👇

```
// removed
```

# Motivation

- 사용되지 않은 코드는 컴파일러에게는 큰 짐이 아니지만 소프트웨어 동작을 이해하려는 사람에게는 큰 짐이 된다
- 일단 코드가 더 이상 사용되지 않으면, 과감히 삭제한다.
  - 혹시 모르니 주석으로 남겨두는 것은 버전 관리 시스템이 없던 시절의 악습
  - 미래에 필요하게 되면, 버전 관리 시스템에서 다시 찾아오면 된다.

# Mechanics

- 함수와 같이 호출이 가능한 경우에는 참조하는 caller 가 있는지 조사한다
- Dead code 제거
- Test
