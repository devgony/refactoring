> Refactoring APIs

- 모듈과 그 기능은 소프트웨어의 구성 요소이고 API 는 이를 서로 연결하는 관절이다
- API 를 쉽게 사용할 수 있도록 리팩터링 해야한다
- 좋은 API 는 immutable 과 mutable 을 구분. 만약 결합되어있다면 `Separate Query from Modifier`
- `Parameterize Function` 을 통해 값에 따라 달라지는 함수들을 통합
  - 하지만 일부 파라미터는 전혀 다른 동작에 대한것이므로 `Remove Flag Argument` 수행 필요
- 불필요하게 분산된 Data structure 는 `Preserve Whole Object` 로 통합
- 어느 부분을 파라미터로 받고 어느 부분을 함수 호출로 해결 할 지는 `Replace Parameter with Query` 와 `Replace Query with Parameter` 를 반복해보며 개선
- class 는 모듈의 가장 흔한 형태이고, 가능하면 `Remove Setting Method` 를 통해 최대한 immutable 하게 유지
- 단순 생성자보다 더 많은 유연성이 필요한 경우 `Replace Constructor with Factory Function`
- 많은 데이털르 전달하는 복잡한 함수 리팩토링 경우 `Replace Function with Command` 으로 `Extract Function` 을 좀더 쉽게 도움
- 함수가 충분히 단순화 되었다면: `Replace Command with Function`
