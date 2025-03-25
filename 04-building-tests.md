> 4. Building Tests

# The Value of Self-Testing Code

- 개발자는 대부분의 시간을 debugging에 사용한다
- 버그를 수정하는건 쉽지만, 찾는게 어렵다
- Self-Testing: actual과 expected 를 컴퓨터가 알아서 비교하고 통과 여부만 알려주게 하는 것

  > Make sure all tests are fully automatic and that they check their own results.

- 테스트를 자주 돌리는 것은 강력한 버그 탐지기가 될 수 있다

  > A suite of tests is a powerful bug detector that decapitates the time it takes to find bugs.

- 테스트를 구현에 앞서 미리 작성하면

  - 스스로에게 무엇이 필요한지 질문하게된다
  - 구현체보다 인터페이스에 집중하게 해준다
  - 동작하는 테스트를 통해 완료된 작업들을 확인할 수 있다

- 리팩토링을 위해서는 반드시 테스트가 있어야 한다. 없으면 테스트 먼저 작성해야한다.

# Sample Code to Test

- shortfall 계산기

1. 단일 생산자
2. 전체 주

   - JS object 를 통해 테스트를 작성한다

```js
// class Province…
  constructor(doc) {
    this._name = doc.name;
    this._producers = [];
    this._totalProduction = 0;
    this._demand = doc.demand;
    this._price = doc.price;
    doc.producers.forEach(d => this.addProducer(new Producer(this, d)));
  }
  addProducer(arg) {
    this._producers.push(arg);
    this._totalProduction += arg.production;
  }
```

```js
function sampleProvinceData() {
  return {
    name: "Asia",
    producers: [
      { name: "Byzantium", cost: 10, production: 9 },
      { name: "Attalia", cost: 12, production: 10 },
      { name: "Sinope", cost: 10, production: 6 },
    ],
    demand: 30,
    price: 20,
  };
}
```

```js
// class Province…
  get name()    {return this._name;}
  get producers() {return this._producers.slice();}
  get totalProduction()    {return this._totalProduction;}
  set totalProduction(arg) {this._totalProduction = arg;}
  get demand()    {return this._demand;}
  set demand(arg) {this._demand = parseInt(arg);}
  get price()    {return this._price;}
  set price(arg) {this._price = parseInt(arg);}
```

- UI 에서는 숫자를 포함한 string 으로 호출하므로 parseInt 필요

```js
// class Producer…
  constructor(aProvince, data) {
    this._province = aProvince;
    this._cost = data.cost;
    this._name = data.name;
    this._production = data.production || 0;
  }
  get name() {return this._name;}
  get cost()    {return this._cost;}
  set cost(arg) {this._cost = parseInt(arg);}

  get production() {return this._production;}
  set production(amountStr) {
    const amount =  parseInt(amountStr);
    const newProduction = Number.isNaN(amount) ? 0 : amount;
    this._province.totalProduction += newProduction - this._production;
    this._production = newProduction;
  }
```

- set production 이 냄새가 나지만 일단 테스트를 작성할때까지 참자

```js
// class Province…
  get shortfall() {
    return this._demand - this.totalProduction;
  }
```

```js
// class Province…
  get profit() {
    return this.demandValue - this.demandCost;
  }
  get demandCost() {
    let remainingDemand = this.demand;
    let result = 0;
    this.producers
      .sort((a,b) => a.cost - b.cost)
      .forEach(p => {
        const contribution = Math.min(remainingDemand, p.production);
          remainingDemand -= contribution;
          result += contribution * p.cost;
      });
    return result;
  }
  get demandValue() {
    return this.satisfiedDemand * this.price;
  }
  get satisfiedDemand() {
    return Math.min(this._demand, this.totalProduction);
  }
```

# A First Test

- 여기서는 test framework 로 mocha 를 사용한다

```js
describe("province", function () {
  it("shortfall", function () {
    const asia = new Province(sampleProvinceData());
    assert.equal(asia.shortfall, 5);
  });
});
```

> leave them empty, arguing that the descriptive sentence is just duplicating the code in the same way a comment does. I like to put in just enough to identify which test is which when I get failures.

- describe 는 식별 가능한 한 간단하게 작성하라. 코멘트와 같은 원리

  > Always make sure a test will fail when it should.

- 다 성공해버리면 오히려 제대로테스트를 하지 않은건지 알수 없기 때문에 차라리 실패하는 테스트가 더 의미있다
  - 구현부를 일부러 바꿔서 실패하게 해보기

```sh
  0 passing (72ms)
  1 failing

  1) province shortfall:
     AssertionError: expected -20 to equal 5
      at Context.<anonymous> (src/tester.js:10:12)
```

> Run tests frequently. Run those exercising the code you're working on at least every few minutes; run all tests at least daily.

- Chai supports validations in both of assert and expect style

```js
// assert style
describe("province", function () {
  it("shortfall", function () {
    const asia = new Province(sampleProvinceData());
    assert.equal(asia.shortfall, 5);
  });
});
```

```js
// expect style
describe("province", function () {
  it("shortfall", function () {
    const asia = new Province(sampleProvinceData());
    expect(asia.shortfall).equal(5);
  });
});
```

## Never refactor on a red bar

- 실패한 테스트에 대해서는 리팩토링을 하지 말자

## Revert to green

- 현재 변경을 철회하고 테스트가 모두 통과하던 시점으로 되돌리다

# Add Another Test

- 모든 public method 를 테스트 하라는 말이 아니다
- Testing 은 risk-driven 이여야 한다
- 현재 혹은 미래에 있을 버그를 찾는 것이 중요하다
  - 필드 read/write 와 같이 단순한 것들은 버그가 일어날 가능성이 적으니 테스트 하지 않는다
- 너무 많은 테스트를 작성하면 테스트가 느려지고, 유지보수가 어려워진다
- 가장 잘못될까봐 걱정이 많이 되는 부분 부터 테스트하라

> It is better to write and run incomplete tests than not to run complete tests.

- main output 부터 test 를 시작한다

```js
describe("province", function () {
  it("shortfall", function () {
    const asia = new Province(sampleProvinceData());
    expect(asia.shortfall).equal(5);
  });
  it("profit", function () {
    const asia = new Province(sampleProvinceData());
    expect(asia.profit).equal(230);
  });
});
```

## Common testing pattern

- expected 는 임시 placeholder 로 할당
- 테스트 수행한 actual 값을 복사하여 expected 에 할당
- fault 를 의도적으로 삽입 하여 테스트 실패 확인
- fault 제거

## beforeEach

```js
describe("province", function () {
  const asia = new Province(sampleProvinceData()); // DON'T DO THIS
  it("shortfall", function () {
    expect(asia.shortfall).equal(5);
  });
  it("profit", function () {
    expect(asia.profit).equal(230);
  });
});
```

- shared fixture 는 test 간의 의존성을 만드므로 피해야한다
  - js 의 경우 const 를 쓰더라도 reference 가 const 인거지 object 의 conent 가 일관되게 유지되지 않는다
  - 잘해봐야 디버깅 시간을 늘릴 뿐이고 최악의 경우에는 테스트의 신뢰성을 떨어뜨린다
  - test 순서에 따라 fixture 가 변경될 수 있기 때문에 beforeEach 를 사용한다

```js
describe("province", function () {
  let asia;
  beforeEach(function () {
    asia = new Province(sampleProvinceData());
  });
  it("shortfall", function () {
    expect(asia.shortfall).equal(5);
  });
  it("profit", function () {
    expect(asia.profit).equal(230);
  });
});
```

- beforeEach 는 각 테스트 전에 실행되며 fixture 를 초기화하여 각 테스트가 독립적으로 실행되도록 한다
- 성능걱정 ? 대부분의 경우에는 무시해도 된다. 정말 문제가 된다면 shared fixture 쓸 수도 있겠지만 반드시 immutable 하게 만들어야한다
  - shread fixture 를 사용하다가 디버깅 오해걸리는 비용이 fresh fixture 의 성능 저하 비용보다 크다

# Modifying the Fixture

- 실제 환경에서는 fixture 는 유저에 의해 변경될 수 있다
- 복잡한 형태의 update 는 test 할 가치가 있다

```js
describe('province'…
  it('change production', function() {
      asia.producers[0].production = 20;
      expect(asia.shortfall).equal(-6);
      expect(asia.profit).equal(292);
  });
```

- setup-exercise-verify 패턴
- 위에서는 두 가지를 하나의 it block 에서 테스트 했지만, 하나의 테스트에는 하나의 assertion 만 하는게 좋다
- 단, 각 테스트가 연관성이 깊을 경우 일단은 하나에서 진행하고 나중에 분리할 수도 있겠다

# Probing the Boundaries

- 현재까지는 성공하는 경우만 테스트 했지만, 조건의 경계를 확인하고, 실패하는 경우도 테스트 해야한다
- 예를 들어, producers 가 없는 경우

```js
describe('no producers', function() {
  let noProducers;
  beforeEach(function() {
    const data = {
      name: "No proudcers",
      producers: [],
      demand: 30,
      price: 20
    };
    noProducers = new Province(data);
  });
  it('shortfall', function() {
    expect(noProducers.shortfall).equal(30);
  });
  it('profit', function() {
    expect(noProducers.profit).equal(0);
  });
```

```js
describe('province'…
    it("zero demand", function () {
    asia.demand = 0;
    expect(asia.shortfall).equal(-25);
    expect(asia.profit).equal(0);
    });
```

```js
describe('province'…
it('negative demand', function() {
    asia.demand = -1;
    expect(asia.shortfall).equal(-26);
    expect(asia.profit).equal(-10);
});
```

- demand 라는 특정 필드가 업무적으로 음수가 될 수 있는지 확인 필요
  - 불가능한 상황이라면 해당 필드의 setter 에 validation 을 추가해야한다
- 이렇듯 테스트는 경계 조건에 대해 어떻게 동작할지 고려하는 것을 도와준다

> Think of the boundary conditions under which things might go wrong and concentrate your tests there.

- UI 에서 숫자란에 공백이 있을 경우를 테스트

```js
describe('province'…
  it('empty string demand', function() {
    asia.demand = "";
    expect(asia.shortfall).NaN;
    expect(asia.profit).NaN;
  });
```

## error vs failure

```js
describe("string for producers", function () {
  it("", function () {
    const data = {
      name: "String producers",
      producers: "",
      demand: 30,
      price: 20,
    };
    const prov = new Province(data);
    expect(prov.shortfall).equal(0);
  });
});
```

```text
  9 passing (74ms)
  1 failing

  1) string for producers :
     TypeError: doc.producers.forEach is not a function
      at new Province (src/main.js:22:19)
      at Context.<anonymous> (src/tester.js:86:18)
```

- 해결법

  - 더 나은 에러 메시지를 추가하거나
  - producers 를 빈 배열로 초기화하거나
  - 특별한 이유가 있어서 에러가 나게 그대로 두거나
    - 같은 모듈에 있을 경우 validation 이 중복될 수 있으므로 에러를 내게 두는것이 좋을 수도 있다

- 리팩토링 전에 위와같은 테스트를 작성했다면, 테스트를 폐기하는 것이 좋다
  - 리팩토링은 관찰 가능한 동작을 유지해야하는데 위 테스트의 에러는 관찰 가능한 범위를 벗어나기 때문이다

> Don’t let the fear that testing can’t catch all bugs stop you from writing tests that catch most bugs.

# Much More Than This

> When you get a bug report, start by writing a unit test that exposes the bug.

## How much testing is enough?

- coverage 보다는 `누군가 코드를 변경했을때 일부 테스트가 실패할 것이라고  얼마나 확신하는가`가 중요하다
- 구현부보다 테스트에 더 많은 시간을 들이고 있다면 과도한게 맞다.
  - 하지만 과잉 테스트의 경우는 거의 없으며 대부분은 과소 테스트이다
