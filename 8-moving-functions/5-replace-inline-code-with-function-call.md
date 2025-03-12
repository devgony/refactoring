> Replace Inline Code with Function Call

```js
let appliesToMass = false;
for (const s of states) {
  if (s === "MA") appliesToMass = true;
}
```

👇

```js
appliesToMass = states.includes("MA");
```

# Motivation

- 함수는 행동을 포장할 수 있게 한다
- 이름이 부여된 함수는 동작방식이 아닌 코드의 목적을 설명할 수 있다
- 중복 제거에 효과적
- 수정해야 하는 경우 유사 코드를 일일히 찾을 필요가 없다
- 기존 함수와 동일한 작업을 보면 대부분 Inline 을 함수 호출로 대체한다
  - 우연의 일치로 유사하게 보이지만 함수 본문을 변경해도 Inline 코드의 동작이 변경되지 않을 것으로 예상되는 경우는 예외이다
    - TODO: 예시필요

```js
// We have a function that checks if a user is from Massachusetts
function isUserFromMassachusetts(user) {
  return user.state === "MA";
}

// Later in the code:
// Case where we shouldn't replace with function call
if (stateCodes.indexOf("MA") !== -1) {
  applySpecialTaxRules();
}
```

```text
In this example, although both pieces of code check for "MA", they have different purposes:

- The function checks if a specific user is from Massachusetts
- The inline code checks if Massachusetts is in a list of states for tax processing

Even though the string "MA" appears in both places, replacing the inline code with `isUserFromMassachusetts()` would be incorrect because:

1. They have different semantic meanings
2. If we later change the function to `user.state === "Massachusetts"` or `user.stateCode === "MA"`, the inline code shouldn't change
3. The function operates on a user object, while the inline code checks a collection
```

- 이를 쉽게 식별하기 위해서는 함수 이름을 잘 지어야 한다

# Mechanics

- Inline 코드를 함수 호출로 대체
- Test
