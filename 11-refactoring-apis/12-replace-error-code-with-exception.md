> Replace Error Code with Exception

```js
if (data) return new ShippingRules(data);
else return -23;
```

👇

```js
if (data) return new ShippingRules(data);
else throw new OrderProcessingError(-23);
```

# Motivation

- 에러코드를 외우거나 콜스택에 넣어서 처리하는 복잡함 없이 Exception 을 던지는 것으로 쉽게 해결할 수 있다.
  - 핸들러를 찾을때 까지 콜스택을 쌓아주며, default 행동도 정의된다
- 엄격하게 예외적인 상황일 때만 쓰여야 한다
- Exception 던지는 부분들 프로그램 종료라고 생각했을때, 여전히 동작해야하는 상황이라면 사용하지 말아야 한다
  - error detection 을 별도로 만들고 정규 workflow 중 하나로 다뤄야한다

# Mechanics

- Exception handler 생성
  - 일단은 모든 exception 을 rethrow 한다
  - 이미 handler 가 있는 경우, extend 하여 새 call chain 을 커버한다
- Test
- 다른 Exception 과 구별할 수 있는 marker 선택
  - 보통은 subclass 를 만듬
- Static check
- catch 절이 적합한 exception 으로 호출되었으면 error 에 대한 action 을 수행하고, 그렇지 않으면 rethrow 하도록 수정
- Test
- error code 를 return 하는 모든 부분이 새 exception 를 던지도록 수정하고 Test
- 모든 error code 가 교체되면, error code 를 호출 스택위로 전달하는 코드 제거

# Example

```js
function localShippingRules(country) {
  const data = countryData.shippingRules[country];
  if (data) return new ShippingRules(data);
  else return -23;
}

caller...
function calculateShippingCosts(anOrder) {
  // irrelevent code
  const shippingRules = localShippingRules(anOrder.country);
  if (shippingRules < 0) return shippingRules;
  // more irrelevent code..

top level…
  const status = calculateShippingCosts(orderData);
  if (status < 0) errorList.push({order: orderData, errorCode: status});
```

- error 가 예상된 값인지 확인
  - localShippingRules 는 shippingRules 가 countryData 에 적절하게 로드 되었을것이라고 가정하는가?
  - country argument 가 global data 에 존재하는 key 인가?
    - validation step 을 거쳤는가?
- top level 에 exception handler 를 추가
  - localShippingRules 호출부를 try block 으로 감싸고 싶지만 status 가 catch 에서도 쓰여야하므로 let 변수 미리 선언 하여 분리

```diff
top level…
+ let status;
+ status = calculateShippingCosts(orderData);
  if (status < 0) errorList.push({order: orderData, errorCode: status});
```

- try/catch 로 감쌈
  - catch 한 에러는 뭉개지않고 다시 던진다

```diff
top level…
  let status;
+ try {
+   status = calculateShippingCosts(orderData);
+ } catch (e){
+   throw e;
+ }
  if (status < 0) errorList.push({order: orderData, errorCode: status});
```

- 호출 코드의 또다른 부분에 대한 오류 목록에 주문을 추가하는 핸들러가 이미 있다면, try block 에서 `calculateShippingCosts` 호출을 포함하도록 변경

- 이 리팩토링을 통해 도입하는 예외를 다른 예외와 구별할 방법을 마련
  - 다른 클래스로 만듬
  - 특정 속성값을 부여
  - 서브클래스로 정의

```js
class OrderProcessingError extends Error {
  constructor(errorCode) {
    super(`Order processing error ${errorCode}`);
    this.code = errorCode;
  }
  get name() {
    return "OrderProcessingError";
  }
}
```

- 해당 Error 를 처리하는 로직 작성

```diff
top level…

  let status;
  try {
    status = calculateShippingCosts(orderData);
  } catch (e){
+   if (e instanceof OrderProcessingError)
+     errorList.push({order: orderData, errorCode: e.code});
+   else
      throw e;
  }
  if (status < 0) errorList.push({order: orderData, errorCode: status});
```

- 에러 코드 대신 에러 객체를 던치도록 수정

```js
function localShippingRules(country) {
  const data = countryData.shippingRules[country];
  if (data) return new ShippingRules(data);
  else throw new OrderProcessingError(-23);
}
```

- trap code 를 통해 삭제 전 test 검증

```diff
function calculateShippingCosts(anOrder) {
  // irrelevent code
  const shippingRules = localShippingRules(anOrder.country);
+ if (shippingRules < 0) throw new Error("error code not dead yet");
  // more irrelevent code
```

- error code 처리 부분 삭제

```diff
function calculateShippingCosts(anOrder) {
  // irrelevent code
  const shippingRules = localShippingRules(anOrder.country);
- if (shippingRules < 0) throw new Error("error code not dead yet");
  // more irrelevent code
```

```diff
top level…
  let status;
  try {
    status = calculateShippingCosts(orderData);
  } catch (e){
    if (e instanceof OrderProcessingError)
      errorList.push({order: orderData, errorCode: e.code});
    else
      throw e;
  }
- if (status < 0) errorList.push({order: orderData, errorCode: status});
```

- 불필요한 status 변수 제거

```diff
top level…
- let status;
  try {
-   status = calculateShippingCosts(orderData);
+   calculateShippingCosts(orderData);
  } catch (e){
    if (e instanceof OrderProcessingError)
      errorList.push({order: orderData, errorCode: e.code});
    else
      throw e;
  }
```
