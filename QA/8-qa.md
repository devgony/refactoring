[move-statements-to-callers.md](/8-moving-functions/4-move-statements-to-callers.md)

- 코드가 바뀌면서 함수의 경계도 바뀐다
  - 한 때는 응집력있는 원자적 행동 단위였지만, 두 가지 이상 서로 다른것의 혼합물이 됌

[replace-inline-code-with-function-call](/8-moving-functions/5-replace-inline-code-with-function-call.md)

- - 우연의 일치로 유사하게 보이지만 함수 본문을 변경해도 Inline 코드의 동작이 변경되지 않을 것으로 예상되는 경우는 예외이다
    - TODO: 예시필요
    - 예시가 잘 와닿지 않음

[slide-statements.md](/8-moving-functions/6-slide-statements.md)

- `Command-Query Separation`: value 를 return 하는 함수는 side effect 가 없다
  - `Commands(modifiers, mutators)`: system 의 상태를 변경하지만 value 를 return 하지 않음
  - `Queries`: 결과를 return 하고 system 의 상태를 변화시키지 않음
