> Inilne Variable

```js
let basePrice = anOrder.basePrice;
return basePrice > 1000;
```

👇

```js
return anOrder.basePrice > 1000;
```

# Motivation

- 때로는 변수가 인접한 코드를 리팩토링 하는데 방해가 될 수 있다 -> line variable

# Mechanics

- 변수 할당의 우변이 영향도가 없는지 확인한다
- 기존 변수가 immutable 이 아니였을 경우 변경하고 테스트를 수행해본다
- 처음 참조하는 곳을 찾아서 변수를 inline 한다
- 테스트를 수행한다
- 다음 참조에 대해서 inline 과 테스트 수행을 반복한다
- 변수 선언과 할당부를 제거한다
- 테스트 수행
