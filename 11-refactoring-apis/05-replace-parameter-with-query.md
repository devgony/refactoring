> Replace Parameter with Query

```js
availableVacation(anEmployee, anEmployee.grade);

function availableVacation(anEmployee, grade) {
  // calculate vacation...
```

👇

```js
availableVacation(anEmployee)

function availableVacation(anEmployee) {
  const grade = anEmployee.grade;
  // calculate vacation...
```

# Motivation

- 그 때 마다 derived value 를 구하는 것
- 함수가 자체적으로 쉽게 결정할 수 있는 값이라면 인자보다는 body 에서 직접 구하는게 복잡성을 줄인다
- 인자를 제거하면서 값결정에 대한 책임을 요청자에서 함수 body 로 넘기는 것이기 때문에
  - 책임을 함수 내부 구현으로 넘기는게 적합할 경우만 사용
- 적용예외
  - 인자 제거로 인해 함수body 에 원치않는 종속성이 추가되는 경우
  - 나중에 제거하려는 수신자 객체 내의 무언가에 엑세스 해야 할 때
- 좋은경우
  - 다른 인자만으로 쿼리하여 얻을 수 있는 값일 때
- 현재 함수가 참조 투명성(동일 인자로 항상 동일한 결과를 내는지)을 가지는지 확인 필요
  - 추론하고 테스트하기가 쉬움
  - 이를 위해 매개 변수를 mutable global var 에 대한 접근으로 대체하지 않아야 함

# Example

- 더이상 사용되지 않는 인자에 대해 `Replace Parameter with Query` 를 적용 하는 예시

```js
class Order…
  get finalPrice() {
    const basePrice = this.quantity * this.itemPrice;
    let discountLevel;
    if (this.quantity > 100) discountLevel = 2;
    else discountLevel = 1;
    return this.discountedPrice(basePrice, discountLevel);
  }

  discountedPrice(basePrice, discountLevel) {
    switch (discountLevel) {
      case 1: return basePrice * 0.95;
      case 2: return basePrice * 0.9;
    }
  }
```

- `Replace Temp with Query` 를 통해 단순화

```diff
class Order…
  get finalPrice() {
    const basePrice = this.quantity * this.itemPrice;
+   return this.discountedPrice(basePrice, this.discountLevel);
  }

+ get discountLevel() {
    return (this.quantity > 100) ? 2 : 1;
  }
```

- 함수 추출이 잘 되었으므로 요청시에 실행해서 인자로 보낼거면 body 안에서 수행하는게 낫다

```diff
class Order…
  get finalPrice() {
    const basePrice = this.quantity * this.itemPrice;
-   return this.discountedPrice(basePrice, this.discountLevel);
+   return this.discountedPrice(basePrice);
  }

- discountedPrice(basePrice, discountLevel) {
+ discountedPrice(basePrice) {
    switch (this.discountLevel) {
      case 1: return basePrice * 0.95;
      case 2: return basePrice * 0.9;
    }
  }
```
