> Extract Function

```js
function printOwing(invoice) {
  printBanner();
  let outstanding = calculateOutstanding();

  //print details
  console.log(`name: ${invoice.customer}`);
  console.log(`amount: ${outstanding}`);
}
```

👇

```js
function printOwing(invoice) {
  printBanner();
  let outstanding = calculateOutstanding();
  printDetails(outstanding);

  function printDetails(outstanding) {
    console.log(`name: ${invoice.customer}`);
    console.log(`amount: ${outstanding}`);
  }
}
```

# Motivation

- 가장 흔하게 사용되는 리팩터링 중 하나
- 코드 조각을 보고, 무엇을 하는지 파악한 다음, 목적에 따라 이름을 붙인 자체 함수로 추출
- intention 과 implementation 을 분리하라
  - 코드 조각을 보고 무엇을 하는지 파악하는데 시간을 소비해야한다면, 함수로 추출하고 intention에 대한 이름을 부여
  - 다음번에 읽을 때는 intention 파악이 바로 가능하고, 함수 body 의 implementation 은 신경쓸 필요가 없다
- 저자는 보통 6줄 이하의 함수를 사용하고 심지어 한 줄인 경우도 있다.

  - eg) function highlight(v) {v.reverse()}
    - 함수 이름이 코드 자체보다 더 많은 정보를 제공하는 경우 더 길어지더라도 함수로 추출하는 것이 좋다

- 하지만 짧은 함수는 이름이 좋아야 실효성을 가진다.
- 긴 함수에 달려있는 주석 역시 Extract function 의 좋은 힌트이다

# Mechanics

- 새 함수를 만들고 함수의 intention 에 맞는 이름을 붙인다

  - 언어가 nested function 지원한다면 일단 그렇게 하고 나중에 옮기는 것이 scope 고민을 줄인다

- 추출할 코드 조각을 새 함수로 복사한다

- 참조하고 있던 변수들을 새 함수의 인자로 정의한다

  - 변수가 추출된 코드 안에서만 쓰인다면 추출된 코드 안으로 옮긴다
  - 값으로 전달되는 변수는 하나만 있는 경우, 추출된 코드를 쿼리로 처리하고 결과를 해당 변수에 할당한다
    - TODO: 예시 필요
  - 너무 많은 인자가 생긴다면 추출을 취소하고 `Split Variable`, `Replace Temp with Query` 등을 통해 리팩토링을 먼저 수행한 수 다시 추출한다

- 모든 변수가 인자로 전달되면, compile check 수행한다
- 추출할 코드에 대한 부분을 새 함수 호출로 변경한다
- 테스트 수행
- 같거나 유사한 코드블럭을 새 함수도 더 대체할 수 있는지 확인한다. `Replace Inline Code with Function Call`

# Example: No Variables Out of Scope

```js
function printOwing(invoice) {
  let outstanding = 0;

  console.log("***********************");
  console.log("**** Customer Owes ****");
  console.log("***********************");

  // calculate outstanding
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  // record due date
  const today = Clock.today;
  invoice.dueDate = new Date(
    today.getFullYear(),
    today.getMonth(),
    today.getDate() + 30,
  );

  //print details
  console.log(`name: ${invoice.customer}`);
  console.log(`amount: ${outstanding}`);
  console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
}
```

```js
function printOwing(invoice) {
  let outstanding = 0;

  printBanner();

  // calculate outstanding
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  // record due date
  const today = Clock.today;
  invoice.dueDate = new Date(
    today.getFullYear(),
    today.getMonth(),
    today.getDate() + 30,
  );

  //print details
  console.log(`name: ${invoice.customer}`);
  console.log(`amount: ${outstanding}`);
  console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
}
function printBanner() {
  console.log("***********************");
  console.log("**** Customer Owes ****");
  console.log("***********************");
}
```

```js
function printOwing(invoice) {
  let outstanding = 0;

  printBanner();

  // calculate outstanding
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  // record due date
  const today = Clock.today;
  invoice.dueDate = new Date(
    today.getFullYear(),
    today.getMonth(),
    today.getDate() + 30,
  );

  printDetails();

  function printDetails() {
    console.log(`name: ${invoice.customer}`);
    console.log(`amount: ${outstanding}`);
    console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
  }
  //..
}
```

# Example: Using Local Variables

- 변수가 사용된 이후로 재할당이 되지 않는다면 인자로 전달하는것이 좋다

```js
function printOwing(invoice) {
  let outstanding = 0;

  printBanner();

  // calculate outstanding
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  // record due date
  const today = Clock.today;
  invoice.dueDate = new Date(
    today.getFullYear(),
    today.getMonth(),
    today.getDate() + 30,
  );

  //print details
  console.log(`name: ${invoice.customer}`);
  console.log(`amount: ${outstanding}`);
  console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
}
```

```js
function printOwing(invoice) {
  let outstanding = 0;

  printBanner();

  // calculate outstanding
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  // record due date
  const today = Clock.today;
  invoice.dueDate = new Date(
    today.getFullYear(),
    today.getMonth(),
    today.getDate() + 30,
  );

  printDetails(invoice, outstanding);
}
function printDetails(invoice, outstanding) {
  console.log(`name: ${invoice.customer}`);
  console.log(`amount: ${outstanding}`);
  console.log(`due: ${invoice.dueDate.toLocaleDateString()}`);
}
```

- 날짜 생성 역시 함수로 추출

```js
function printOwing(invoice) {
  let outstanding = 0;

  printBanner();

  // calculate outstanding
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  recordDueDate(invoice);
  printDetails(invoice, outstanding);
}
function recordDueDate(invoice) {
  const today = Clock.today;
  invoice.dueDate = new Date( // mutate
    today.getFullYear(),
    today.getMonth(),
    today.getDate() + 30,
  );
}
```

# Example: Reassigning a Local Variable

- temp 에 대해서만 해당
- 인자에 대한 할당이 있으면 `Split Variable` 을 먼저 수행하여 temp로 변경
  - mutable 선언 후 변환이 아니라 두 개의 immutable 변수로 선언하여 사용
- 추출된 함수 내에서만 temp 가 사용되는 경우는 쉽지만
- 추출된 함수 밖에서도 사용되는 경우는 temp를 return 하도록 변경

```js
function printOwing(invoice) {
  let outstanding = 0;

  printBanner();

  // calculate outstanding
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  recordDueDate(invoice);
  printDetails(invoice, outstanding);
}
```

- outstanding 를 사용처 주위로 이동

```js
function printOwing(invoice) {
  printBanner();

  // calculate outstanding
  let outstanding = 0;
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  recordDueDate(invoice);
  printDetails(invoice, outstanding);
}
```

- 한꺼번에 추출

```js
function printOwing(invoice) {
  printBanner();

  // calculate outstanding
  let outstanding = 0;
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }

  recordDueDate(invoice);
  printDetails(invoice, outstanding);
}
function calculateOutstanding(invoice) {
  let outstanding = 0;
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }
  return outstanding;
}
```

- 추출된 함수로 변경

```js
function printOwing(invoice) {
  printBanner();
  let outstanding = calculateOutstanding(invoice);
  recordDueDate(invoice);
  printDetails(invoice, outstanding);
}
function calculateOutstanding(invoice) {
  let outstanding = 0;
  for (const o of invoice.orders) {
    outstanding += o.amount;
  }
  return outstanding;
}
```

- return 할 변수는 result 로 rename 하고 outstanding은 const 로 변경

```js
function printOwing(invoice) {
  printBanner();
  const outstanding = calculateOutstanding(invoice);
  recordDueDate(invoice);
  printDetails(invoice, outstanding);
}
function calculateOutstanding(invoice) {
  let result = 0;
  for (const o of invoice.orders) {
    result += o.amount;
  }
  return result;
}
```

- 리턴할 변수 가 두개 이상인 경우?

  - 함수당 하나의 값을 리턴하는 것을 권장한다
  - 여러 함수를 만들어 복수의 리턴값을 나눠 처리할 수 있도록 시도한다
  - 정말 필요한 경우, 객체를 리턴하도록 변경한다
  - 하지만 사전에 `Replaca Temp with Query`, `Split Variable` 만 사용해도 하나만 리턴하게 되는 경우가 많다
    - TODO: 예시 필요

- 작은 스텝으로 일단 nested function 추출하는게 좋긴 하지만, 나중에 옮기기 전까지는 score 문제를 알 수 없기때문에 첫 추출 때 적어도 sibling level 로 추출하는 것도 좋은 방법이다
