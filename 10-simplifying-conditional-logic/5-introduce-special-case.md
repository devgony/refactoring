> Introduce Special Case

```js
if (aCustomer === "unknown") customerName = "occupant";
```

👇

```js
class UnknownCustomer {
    get name() {return "occupant";}
```

# Motivation

- 특정 경우에 동일한 동작을 한다면 하나로 묶을 수 있다
- special-case 처리가 필요한 일반 값의 대표적인 예는 null 이다
  - special-case of special case: Null Object Pattern

# Mechanics

- refactoring 대상인 property 를 포함하는 container data structure 혹은class로 시작
- container 의 client 는 subject prop 을 special-case value 와 비교한다
- subject 에 special-case check popr 을 추가하고 false 를 return 한다
- special-case object 를 생성하고 true 를 return 하는 special-case check prop 만 할당
- `Extract Function`: special-case 비교 부분을 추출하고 caller 도 모두 변경
- 함수 호출 혹은 transform 함수를 적용하여 새 special-case subject 를 추가
- special-case 비교 함수의 body 를 special-case check prop 을 사용하도록 변경
- Test
- `Combine Functions into Class` 혹은 ``Combine Functions into Transform` 을 사용하여 공통 special-case 행동을 새 요소로 옮긴다
  - 단순한 요청에 대해서는 고정된 값을 리턴하므로 literal record를 사용한다
- 필요한 경우에는 `Inline Function` 으로 special-case 비교 구문을 남겨둔다

# Example

- 유틸 회사가 사이트에 서비스를 설치하는 예제

```js
class Site…
  get customer() {return this._customer;}
```

```js

class Customer…

  get name()            {...}
  get billingPlan()     {...}
  set billingPlan(arg)  {...}
  get paymentHistory()  {...}

```

- 고객이 떠나면 unknown 으로 바뀔텐데 어떻게 처리할 것인가?

```js
client 1…
  const aCustomer = site.customer;
  // ... lots of intervening code ...
  let customerName;
  if (aCustomer === "unknown") customerName = "occupant";
  else customerName = aCustomer.name;

client 2…
  const plan = (aCustomer === "unknown") ?
        registry.billingPlans.basic
        : aCustomer.billingPlan;

client 3…
  if (aCustomer !== "unknown") aCustomer.billingPlan = newPlan;

client 4…
  const weeksDelinquent = (aCustomer === "unknown") ?
        0
        : aCustomer.paymentHistory.weeksDelinquentInLastYear;
```

- unknown 에 대한 동작들이 정해져있다
- `isUnknown` 함수 추가

```js
class Customer…
  get isUnknown() {return false;}
```

- `UnknownCustomer` class 추가
  - js 의 동적 typing 때문에, Customer 의 subclass 로는 만들지 않겠다

```js
class UnknownCustomer {
  get isUnknown() {
    return true;
  }
}
```

- 수많은 client 의 호출 방식을 바꾸기 어려우니 `Extract Function` 활용

```js
function isUnknown(arg) {
  if (!(arg instanceof Customer || arg === "unknown"))
    throw new Error(`investigate bad value: <${arg}>`);
  return arg === "unknown";
}
```

- 각 client 의 호출 방식 변경

```js
client 1…
  let customerName;
  if (isUnknown(aCustomer)) customerName = "occupant";
  else customerName = aCustomer.name;

client 2…

  const plan = (isUnknown(aCustomer)) ?
        registry.billingPlans.basic
        : aCustomer.billingPlan;
client 3…
  if (!isUnknown(aCustomer)) aCustomer.billingPlan = newPlan;

client 4…
  const weeksDelinquent = isUnknown(aCustomer) ?
        0
        : aCustomer.paymentHistory.weeksDelinquentInLastYear;
```

- get customer 가 unknown 인 경우에 `UnknownCustomer` 를 return

```js
class Site…
  get customer() {
    return (this._customer === "unknown") ? new UnknownCustomer() :  this._customer;
  }
```

- isUnknown 내부에서도 UnknownCustomer.isUnknown 을 사용하도록 변경

```js
client 1…
  function isUnknown(arg) {
    if (!(arg instanceof Customer || arg instanceof UnknownCustomer))
      throw new Error(`investigate bad value: <${arg}>`);
    return arg.isUnknown;
  }
```

- `Combine Functions into Class` 를 사용하여 `isUnknown` 일 때 occupant 로 지정하는 부분을 이동

```diff
client 1…
  let customerName;
- if (isUnknown(aCustomer)) customerName = "occupant";
  else customerName = aCustomer.name;

class UnknownCustomer…
+ get name() {return "occupant";}
```

- client1 의 조건문 제거 가능

```js
const customerName = aCustomer.name;
```

- `Inline Variable` 도 사용 가능하다

- 다음은 override billingPlan
  - getter 의 경우에는 값을 리턴하지만 setter의 경우 일단 body를 비워둔다

```js
class UnknownCustomer…
  get billingPlan()    {return registry.billingPlans.basic;}
  set billingPlan(arg) { /* ignore */ }

client reader…
  const plan = aCustomer.billingPlan;

client writer…
  aCustomer.billingPlan = newPlan;
```

- Special-case object 는 VO 이므로 immutable 이어야 한다

```js
client…
  const weeksDelinquent = isUnknown(aCustomer) ?
        0
        : aCustomer.paymentHistory.weeksDelinquentInLastYear;
```

- 위 경우에서 Special-case object 가 관련된 객체를 리턴할 필요가있는 경우, 그 역시도 special-case object 이다
  - null payment history 를 만든다

```js
class UnknownCustomer…
  get paymentHistory() {return new NullPaymentHistory();}

class NullPaymentHistory…
  get weeksDelinquentInLastYear() {return 0;}

client…
  const weeksDelinquent = aCustomer.paymentHistory.weeksDelinquentInLastYear;
```

- 수많은 client 중에 다르게 동작하고 싶은 client 가 있다고 가정 해보자

```js
client…
  const name =  ! isUnknown(aCustomer) ? aCustomer.name : "unknown occupant";
```

- 이 경우에는 special-case check 을 유지한다. `Inline Function`: isUnkknown

```js
client…
  const name =  aCustomer.isUnknown ? "unknown occupant" : aCustomer.name;
```

- 모든 client 에 대해 완료되면 `Remove Dead Code` 로 사용하지 않는 `isPresent` 함수 제거

# Example: Using an Object Literal

- customer 가 updated 되어야 하는 경우에는 literal object 를 사용한다

```js
class Site…
  get customer() {return this._customer;}

class Customer…
  get name()            {...}
  get billingPlan()     {...}
  set billingPlan(arg)  {...}
  get paymentHistory()  {...}

client 1…
  const aCustomer = site.customer;
  // ... lots of intervening code ...
  let customerName;
  if (aCustomer === "unknown") customerName = "occupant";
  else customerName = aCustomer.name;

client 2…
  const plan = (aCustomer === "unknown") ?
        registry.billingPlans.basic
        : aCustomer.billingPlan;

client 3…
  const weeksDelinquent = (aCustomer === "unknown") ?
        0
        : aCustomer.paymentHistory.weeksDelinquentInLastYear;
```

- 기존 예제와 동일하게 `isUnknown` 추가 하지만 이번에는 special- case 가 literal이다

```js
class Customer…
  get isUnknown() {return false;}

top level…
  function createUnknownCustomer() {
    return {
      isUnknown: true,
    };
  }
```

- `Extract Function`: speical-case check 를 함수로 추출

```js
function isUnknown(arg) {
  return (arg === "unknown");
}
client 1…

  let customerName;
  if (isUnknown(aCustomer)) customerName = "occupant";
  else customerName = aCustomer.name;
client 2…

  const plan = isUnknown(aCustomer) ?
        registry.billingPlans.basic
        : aCustomer.billingPlan;
client 3…

  const weeksDelinquent = isUnknown(aCustomer) ?
        0
        : aCustomer.paymentHistory.weeksDelinquentInLastYear;
```

- site class 와 조건절 을 special case 에 맞게 변경

```js
class Site…
  get customer() {
    return (this._customer === "unknown") ?  createUnknownCustomer() :  this._customer;
  }

top level…
  function isUnknown(arg) {
    return arg.isUnknown;
  }

```

- 각 응답을 적절한 literal value 로 변경
  - name

```js
function createUnknownCustomer() {
  return {
    isUnknown: true,
    name: "occupant",
  };
}

client 1…
  const customerName = aCustomer.name;
```

- biling plan

```js
function createUnknownCustomer() {
  return {
    isUnknown: true,
    name: "occupant",
    billingPlan: registry.billingPlans.basic,
  };
}

client 2…
  const plan = aCustomer.billingPlan;
```

- null payment history 를 literal 로 생성

```js
function createUnknownCustomer() {
  return {
    isUnknown: true,
    name: "occupant",
    billingPlan: registry.billingPlans.basic,
    paymentHistory: {
      weeksDelinquentInLastYear: 0,
    },
  };
}

client 3…
  const weeksDelinquent = aCustomer.paymentHistory.weeksDelinquentInLastYear;
```

# Example: Using a Transform

- transform 과 record 를 활용해서도 special-case 를 처리할 수 있다

```js
{
  name: "Acme Boston",
  location: "Malden MA",
  // more site details
  customer: {
    name: "Acme Industries",
    billingPlan: "plan-451",
    paymentHistory: {
      weeksDelinquentInLastYear: 7
      //more
    },
    // more
  }
}
```

- 특정 경우에 customer 는 unknown 이 된다

```js
{
  name: "Warehouse Unit 15",
  location: "Malden MA",
  // more site details
  customer: "unknown",
}
```

- unknown customer 를 확인하는 유사한 client code

```js
client 1…
  const site = acquireSiteData();
  const aCustomer = site.customer;
  // ... lots of intervening code ...
  let customerName;
  if (aCustomer === "unknown") customerName = "occupant";
  else customerName = aCustomer.name;

client 2…
  const plan = (aCustomer === "unknown") ?
        registry.billingPlans.basic
        : aCustomer.billingPlan;

client 3…
  const weeksDelinquent = (aCustomer === "unknown") ?
        0
        : aCustomer.paymentHistory.weeksDelinquentInLastYear;
```

- 처음 transform 은 deep copy 만 수행

```js
client 1…
  const rawSite = acquireSiteData();
  const site = enrichSite(rawSite);
  const aCustomer = site.customer;
  // ... lots of intervening code ...
  let customerName;
  if (aCustomer === "unknown") customerName = "occupant";
  else customerName = aCustomer.name;

function enrichSite(inputSite) {
  return _.cloneDeep(inputSite);
}
```

- `Extract Function`: special-case check 를 함수로 추출

```diff
+function isUnknown(aCustomer) {
+  return aCustomer === "unknown";
+}

client 1…
  const rawSite = acquireSiteData();
  const site = enrichSite(rawSite);
  const aCustomer = site.customer;
  // ... lots of intervening code ...
  let customerName;
+ if (isUnknown(aCustomer)) customerName = "occupant";
  else customerName = aCustomer.name;

client 2…
+ const plan = (isUnknown(aCustomer)) ?
        registry.billingPlans.basic
        : aCustomer.billingPlan;

client 3…
+ const weeksDelinquent = (isUnknown(aCustomer)) ?
        0
        : aCustomer.paymentHistory.weeksDelinquentInLastYear;
```

- `isUnknown` 필드를 transform 에서 채운다

```js
function enrichSite(aSite) {
  const result = _.cloneDeep(aSite);
  const unknownCustomer = {
    isUnknown: true,
  };

  if (isUnknown(result.customer)) result.customer = unknownCustomer;
  else result.customer.isUnknown = false;
  return result;
}
```

- `isUnknown` 를 갱신. 기존 조건절도 남겨놓아 호환성 유지

```diff
function isUnknown(aCustomer) {
+ if (aCustomer === "unknown") return true;
+ else return aCustomer.isUnknown;
}
```

- `Combine Functions into Transform`: name

```diff
function enrichSite(aSite) {
  const result = _.cloneDeep(aSite);
  const unknownCustomer = {
    isUnknown: true,
+   name: "occupant",
  };

  if (isUnknown(result.customer)) result.customer = unknownCustomer;
  else result.customer.isUnknown = false;
  return result;
}

client 1…
  const rawSite = acquireSiteData();
  const site = enrichSite(rawSite);
  const aCustomer = site.customer;
  // ... lots of intervening code ...
+ const customerName = aCustomer.name;
```

- billingPlan

```js
function enrichSite(aSite) {
  const result = _.cloneDeep(aSite);
  const unknownCustomer = {
    isUnknown: true,
    name: "occupant",
+   billingPlan: registry.billingPlans.basic,
  };

  if (isUnknown(result.customer)) result.customer = unknownCustomer;
  else result.customer.isUnknown = false;
  return result;
}

client 2…
+ const plan = aCustomer.billingPlan;
```

- paymentHistory

```js
function enrichSite(aSite) {
  const result = _.cloneDeep(aSite);
  const unknownCustomer = {
    isUnknown: true,
    name: "occupant",
    billingPlan: registry.billingPlans.basic,
+   paymentHistory: {
+     weeksDelinquentInLastYear: 0,
+   }
  };

  if (isUnknown(result.customer)) result.customer = unknownCustomer;
  else result.customer.isUnknown = false;
  return result;
}

client 3…
+ const weeksDelinquent = aCustomer.paymentHistory.weeksDelinquentInLastYear;
```
