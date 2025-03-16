> Change Value to Reference

```js
let customer = new Customer(customerData);
```

👇

```js
let customer = customerRepository.get(customerData.id);
```

# Motivation

- 동일한 논리 데이터 구조에 여러개의 record 들이 연결될 수 있다
- reference 를 사용하면 하나의 record 를 여러 곳에서 참조할 수 있다
- 고객이 데이터를 절대 변경하지 않는다면 reference 와 copied value 모두 합리적
- 하지만 변경이 필요한 경우, copied value 는 모두 찾아야 하기 때문에 reference 가 좋다
- 보통 해당 object 에 접근할 repository 가 필요하다
  - entity 에 대한 object 는 하나만 만들고, repository 통해 조회

# Mechanics

- 관련 instance 들을 위한 repository 를 만든다
- constructor 가 올바른 instance 를 찾는지 확인
- host object 의 constructor 가 repository 를 통해 관련 object를 얻도록 수정
- Test

# Example

```js
// class Order…
  constructor(data) {
    this._number = data.number;
    this._customer = new Customer(data.customer);
    // load other data
  }
  get customer() {return this._customer;}

// class Customer…
  constructor(id) {
    this._id = id;
  }
  get id() {return this._id;}
```

- 현재 방식은 copied value

  - 5개의 order 가 customer ID 123 을 참조한다면, 5개 의 customer object 가 생성된다

- 같은 customer object 를 사용하기 위해서 `repository object` 사용

```js
let _repositoryData;

export function initialize() {
  _repositoryData = {};
  _repositoryData.customers = new Map();
}

export function registerCustomer(id) {
  if (!_repositoryData.customers.has(id))
    _repositoryData.customers.set(id, new Customer(id));
  return findCustomer(id);
}

export function findCustomer(id) {
  return _repositoryData.customers.get(id);
}
```

- Order constructor 에서 registerCustomer

```js
// class Order…
  constructor(data) {
    this._number = data.number;
    this._customer = registerCustomer(data.customer);
    // load other data
  }
  get customer() {return this._customer;}
```

- 현재 예제 에서는 첫 번째 주문 시 참조하는 새 고객 개체를 만들었지만

  - 미리 Customer 목록을 가져와서 repository 를 채우고 주문을 읽을 때 Customer 에 연결하는 방식도 있다

    - repository 에 없는 고객 ID 가 포함된 주문은 오류 발생

- 현재 방식의 문제는 global repository 에 의존되어있다는것.
  - 이를 해결하기 위해 constructor 에 repository 를 주입하는 방식도 있다
