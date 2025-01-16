# 1. Refactoring: A First Example

- 예제를 통해 설명을 시작하는 것이 이해하기 좋다.
- 너무 큰 프로그램은 이해하기에 어렵고 너무 작으면 refactoring 의 가치를 느끼기 어렵다.

## The Starting Point

- 다양한 행사에서 공연을 하는 연극단 예제
- tragedy, comedy 두가지 장르의 연극을 공연한다.
- 공연료를 계산하는 프로그램
- 할인을 위한 volume credit 존재

- 각 공연 목록

```json
// plays.json
{
  "hamlet": { "name": "Hamlet", "type": "tragedy" },
  "as-like": { "name": "As You Like It", "type": "comedy" },
  "othello": { "name": "Othello", "type": "tragedy" }
}
```

- 빌링에 대한 정보

```json
// invoices.json
[
  {
    "customer": "BigCo",
    "performances": [
      {
        "playID": "hamlet",
        "audience": 55
      },
      {
        "playID": "as-like",
        "audience": 35
      },
      {
        "playID": "othello",
        "audience": 40
      }
    ]
  }
]
```

- 공연료 계산을 위한 코드

```javascript
function statement(invoice, plays) {
  let totalAmount = 0;
  let volumeCredits = 0;

  let result = `Statement for ${invoice.customer}\n`;
  const format = new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: 2,
  }).format;
  for (let perf of invoice.performances) {
    const play = plays[perf.playID];
    let thisAmount = 0;

    switch (play.type) {
      case "tragedy":
        thisAmount = 40000;
        if (perf.audience > 30) {
          thisAmount += 1000 * (perf.audience - 30);
        }
        break;
      case "comedy":
        thisAmount = 30000;
        if (perf.audience > 20) {
          thisAmount += 10000 + 500 * (perf.audience - 20);
        }
        thisAmount += 300 * perf.audience;
        break;
      default:
        throw new Error(`unknown type: ${play.type}`);
    }

    // add volume credits
    volumeCredits += Math.max(perf.audience - 30, 0);
    // add extra credit for every ten comedy attendees
    if ("comedy" === play.type) volumeCredits += Math.floor(perf.audience / 5);

    // print line for this order
    result += `  ${play.name}: ${format(thisAmount / 100)} (${perf.audience} seats)\n`;
    totalAmount += thisAmount;
  }
  result += `Amount owed is ${format(totalAmount / 100)}\n`;
  result += `You earned ${volumeCredits} credits\n`;
  return result;
}
```

- 최종 공연료 계산 결과

```text
Statement for BigCo
  Hamlet: $650.00 (55 seats)
  As You Like It: $580.00 (35 seats)
  Othello: $500.00 (40 seats)
Amount owed is $1,730.00
You earned 47 credits
```

## Comments on the Starting Program

- 컴파일러는 코드가 더럽다는것을 신경쓰지 않는다.
- 하지만 사람이 개입해서 코드를 수정하는 시점부터는 가독성이 떨어지면 실수하거나 버그를 만들 가능성이 높아진다.
- 해당 프로그램에 구조화 되어있지 않은 경우 refactoring 을 통해 쉬운 구조를 만들고나서 기능을 추가하거나 수정하는 것이 좋다.
- 복사해서 htmlStatement 를 만드는것이 당장은 나쁘지 않아보이지만 미래에 변경이 일어날 가능성이 있다면 유지보수의 복잡도는 점점 높아진다.

## The First Step in Refactoring

- refactoring 을 시작하기 전에 반드시 테스트를 작성한다 이 테스트 들은 자가검사가 가능해야한다.

## Decomposing the statement Function

- 긴 코드를 리팩토링 하는 경우 전체 동작을 작은 part 단위로 나눈다
- 가장 눈에띄는 중앙부의 switch statement 를 `Extract Function` 한다

```javascript
function amountFor(perf, play) {
  let thisAmount = 0;
  switch (play.type) {
    case "tragedy":
      thisAmount = 40000;
      if (perf.audience > 30) {
        thisAmount += 1000 * (perf.audience - 30);
      }
      break;
    case "comedy":
      thisAmount = 30000;
      if (perf.audience > 20) {
        thisAmount += 10000 + 500 * (perf.audience - 20);
      }
      thisAmount += 300 * perf.audience;
      break;
    default:
      throw new Error(`unknown type: ${play.type}`);
  }
  return thisAmount;
}
```

```javascript
function statement(invoice, plays) {
  let totalAmount = 0;
  let volumeCredits = 0;
  let result = `Statement for ${invoice.customer}\n`;
  const format = new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: 2,
  }).format;
  for (let perf of invoice.performances) {
    const play = plays[perf.playID];
    let thisAmount = amountFor(perf, play);

    // add volume credits
    volumeCredits += Math.max(perf.audience - 30, 0);
    // add extra credit for every ten comedy attendees
    if ("comedy" === play.type) volumeCredits += Math.floor(perf.audience / 5);
    // print line for this order
    result += `  ${play.name}: ${format(thisAmount / 100)} (${perf.audience} seats)\n`;
    totalAmount += thisAmount;
  }
  result += `Amount owed is ${format(totalAmount / 100)}\n`;
  result += `You earned ${volumeCredits} credits\n`;
  return result;
}
```

- 조금 변경했어도 테스트를 통과하는지 확인한다.
- Refactoring 은 점진적으로 진행되어야 실수를 만들었을때 쉽게 찾을 수 있다.
- 로컬에서는 변경을 하고 테스트를 할때마다 commit 을 쌓고 remote 로 push 하기 전에는 의미있는 단위로 모아 squash 한다.
- `Extract Function` 이 자동으로 수행되더라도, rename 등 추가 처리를 해줄 사항이 있는지 확인한다.

```javascript
function amountFor(perf, play) {
  let result = 0;
  ..
  return result;
}
```

- 마틴 파울러는 return 할 변수명은 항상 result 로 정의한다고 한다.

```javascript
function amountFor(aPerformance, play) {
    ..
}
```

- perf => aPerformance 변경: parameter의 이름은 type 과 복수형 구분을 포함하는것이 좋다

> Any fool can write code that a computer can understand. Good programmers write code that humans can understand.

## Removing the play Variable

- amountFor 의 param 중 play 는 aPerformance 를 기반으로 계산 될 수 있으므로, param이 아니라 내부에서 playFor 수행하여 얻는다
- `Replace Temp with Query`: `Extract Function` + `Inlinie Variable`
  - `Extract Function`: playFor computation
  - `Inline Variable`: local var 제거

```javascript
function playFor(aPerformance) {
  return plays[aPerformance.playID];
}
```

- `Change Function Declaration` 으로 play 를 제거

```javascript
function amountFor(aPerformance) {
  // ..
  switch (playFor(aPerformance).type) {
    // ..
    default:
      throw new Error(`unknown type: ${playFor(aPerformance).type}`);
  }
  return result;
}
```

- playFor 의 수행 횟수가 3배 많아 졌지만 interplay of refactoring and performance 는 추후에 다룰 예정
- Extract 전에는 local var 를 최대한 줄이는 것이 좋다.
- thisAmount 는 다시 update 되지 않는 temp variable 이므로 `Inline Variable` 통해 제거

```javascript
function statement(invoice, plays) {
  // ..
  for (let perf of invoice.performances) {
    // ..
    if ("comedy" === playFor(perf).type)
      volumeCredits += Math.floor(perf.audience / 5);

    result += `  ${playFor(perf).name}: ${format(amountFor(perf) / 100)} (${perf.audience} seats)\n`;
    // ..
  }
  // ..
}
```

## Extracting Volume Credits

- volumeCredits 는 accumulate 되고 있으므로 volumeCreditsFor 함수 내에서 shadowing 한다

```javascript
function volumeCreditsFor(perf) {
  let volumeCredits = 0;
  volumeCredits += Math.max(perf.audience - 30, 0);
  if ("comedy" === playFor(perf).type)
    volumeCredits += Math.floor(perf.audience / 5);
  return volumeCredits;
}
```

```javascript
function statement(invoice, plays) {
  // ..
  for (let perf of invoice.performances) {
    volumeCredits += volumeCreditsFor(perf);
  }
}
```

- result, parameter 에 type 및 단복수 구분 위한 rename

```javascript
function volumeCreditsFor(aPerformance) {
  let result = 0;
  result += Math.max(aPerformance.audience - 30, 0);
  if ("comedy" === playFor(aPerformance).type)
    result += Math.floor(aPerformance.audience / 5);
  return result;
}
```

## Removing the format Variable

- format 과 같은 temp var 는 특정한 routine 에만 유용하기 때문에 길고 복잡한 로직을 유도하는 경우가 많다. => function 으로 추출
- 하는김에 명확하게 역할을 표현하도록 이름도 usd 로 변경

```javascript
function usd(aNumber) {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: 2,
  }).format(aNumber / 100);
}
```

## Removing Total Volume Credits

- `Split Loop` 를 통해 volumeCredits 를 계산하는 부분을 분리한다.

```javascript
function statement(invoice, plays) {
  let volumeCredits = 0;
  //..
  for (let perf of invoice.performances) {
    // print line for this order
    result += `  ${playFor(perf).name}: ${usd(amountFor(perf))} (${perf.audience} seats)\n`;
    totalAmount += amountFor(perf);
  }
  for (let perf of invoice.performances) {
    volumeCredits += volumeCreditsFor(perf);
  }
  //..
}
```

- `Slide Statements`: volumeCredits 초기화 부분을 두 loop 사이로 이동

```javascript
function statement(invoice, plays) {
  //..
  for (let perf of invoice.performances) {
    // print line for this order
    result += `  ${playFor(perf).name}: ${usd(amountFor(perf))} (${perf.audience} seats)\n`;
    totalAmount += amountFor(perf);
  }
  let volumeCredits = 0;
  for (let perf of invoice.performances) {
    volumeCredits += volumeCreditsFor(perf);
  }
  //..
}
```

- `Replace Temp with Query`: `Extract Function` + `Inlinie Variable`
- `Extract Function`: volumeCredits 선언부와 volumeCreditsFor 를 totalVolumeCredits 로 추출

```javascript
function totalVolumeCredits() {
  let volumeCredits = 0;
  for (let perf of invoice.performances) {
    volumeCredits += volumeCreditsFor(perf);
  }
  return volumeCredits;
}
```

- `Inline Variable`: totalVolumeCredits 를 사용하는 부분을 inline

```javascript
function statement(invoice, plays) {
  //..
  result += `You earned ${totalVolumeCredits()} credits\n`;
  return result;
}
```

- 위 변화에 의해 loop 가 두 배로 늘었지만, 직관적으로 느릴거라고 생각하는 점은 대부분 캐싱과같은 컴파일러의 최적화 방식에 따라 해소되는 경우가 많다
- 성능문제는 대부분 극소수의 병목이 원인이 되며 이외의 부분은 대세에 지장이 없다.
- 성능 개선을 해야하는 경우라도, 일단은 코드를 리팩토링 한 후에 하는것이 더 효과적이다.

- totalAmount 에 대해서도 `Replace Temp with Query` 수행

  - `Extract Function` 시에 변수와 이름이 겹치는 경우 `applceSauce` 같은 랜덤한 이름을 임시로 사용해도 좋다.

```js
function totalAmount() {
  let result = 0;
  for (let perf of invoice.performances) {
    result += amountFor(perf);
  }
  return result;
}
```

```js
function statement(invoice, plays) {
  //..
  result += `Amount owed is ${usd(totalAmount())}\n`;
  //..
}
```

## Status: Lots of Nested Functions

- overall state of the code

```js
function statement(invoice, plays) {
  let result = `Statement for ${invoice.customer}\n`;
  for (let perf of invoice.performances) {
    result += `  ${playFor(perf).name}: ${usd(amountFor(perf))} (${perf.audience} seats)\n`;
  }
  result += `Amount owed is ${usd(totalAmount())}\n`;
  result += `You earned ${totalVolumeCredits()} credits\n`;
  return result;

  function totalAmount() {
    let result = 0;
    for (let perf of invoice.performances) {
      result += amountFor(perf);
    }
    return result;
  }

  function totalVolumeCredits() {
    let result = 0;
    for (let perf of invoice.performances) {
      result += volumeCreditsFor(perf);
    }
    return result;
  }
  function usd(aNumber) {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
      minimumFractionDigits: 2,
    }).format(aNumber / 100);
  }
  function volumeCreditsFor(aPerformance) {
    let result = 0;
    result += Math.max(aPerformance.audience - 30, 0);
    if ("comedy" === playFor(aPerformance).type)
      result += Math.floor(aPerformance.audience / 5);
    return result;
  }
  function playFor(aPerformance) {
    return plays[aPerformance.playID];
  }
  function amountFor(aPerformance) {
    let result = 0;
    switch (playFor(aPerformance).type) {
      case "tragedy":
        result = 40000;
        if (aPerformance.audience > 30) {
          result += 1000 * (aPerformance.audience - 30);
        }
        break;
      case "comedy":
        result = 30000;
        if (aPerformance.audience > 20) {
          result += 10000 + 500 * (aPerformance.audience - 20);
        }
        result += 300 * aPerformance.audience;
        break;
      default:
        throw new Error(`unknown type: ${playFor(aPerformance).type}`);
    }
    return result;
  }
}
```

- 모든 계산 로직은 nested function으로 이동되었고 statement 함수는 하위 함수를 호출하고 print 를 수행하는 7 line 에 불과하다

## Splitting the Phases of Calculation and Formatting

- 이제 structure 가 구성 되었으니 HTML render 기능을 추가한다.
- `Split Phase`: 현재는 textual statement method 내에 calculation 이 섞여 있으므로 각 로직들을 재활용 하기 쉽도록 Calculation과 Rendering을 분리한다.
  1. `Extract Function` 으로 rendering 부분을 분리한다 => statement 함수 전체에 해당

```js
function statement (invoice, plays) {
  return renderPlainText(invoice, plays);
}

function renderPlainText(invoice, plays) {
  let result = `Statement for ${invoice.customer}\n`;
  for (let perf of invoice.performances) {
    result += `  ${playFor(perf).name}: ${usd(amountFor(perf))} (${perf.audience} seats)\n`;
  }
  result += `Amount owed is ${usd(totalAmount())}\n`;
  result += `You earned ${totalVolumeCredits()} credits\n`;
  return result;

  function totalAmount() {...}
  function totalVolumeCredits() {...}
  function usd(aNumber) {...}
  function volumeCreditsFor(aPerformance) {...}
  function playFor(aPerformance) {...}
  function amountFor(aPerformance) {...}
}
```

```js
function statement(invoice, plays) {
  const statementData = {}; // 중간 데이터 구조 생성
  return renderPlainText(statementData, invoice, plays);
}

function renderPlainText(data, invoice, plays) {
  //..
}
```

- customer 의 필드를 data 로 이동

```js
function statement(invoice, plays) {
  const statementData = {};
  statementData.customer = invoice.customer;
  statementData.performances = invoice.performances;
  return renderPlainText(statementData, plays);
}
```

```js
function renderPlainText(data, plays) {
  //..
  for (let perf of data.performances) {
    //..
  }
  //..
}
```

```js
function statement(invoice, plays) {
  //..
  statementData.performances = invoice.performances.map(enrichPerformance);
  return renderPlainText(statementData, plays);

  function enrichPerformance(aPerformance) {
    const result = Object.assign({}, aPerformance); // immutable 를 위한 shallow copy
    result.play = playFor(result); // 중간 데이터 구조에 play 추가 하여 향후 재 활용

    return result;
  }
  //..
}
```

- replace all the references to playFor in renderPlainText to use `perf.play`
- `amountFor`, `volumeCreditsFor` 함수에 대해서도 동일 적용

```js
function enrichPerformance(aPerformance) {
  //..
  result.amount = amountFor(result);
  result.volumeCredits = volumeCreditsFor(result);
  return result;
}
```

- move the two calculations of the totals.

```js
//..
statementData.performances = invoice.performances.map(enrichPerformance);
statementData.totalAmount = totalAmount(statementData);
statementData.totalVolumeCredits = totalVolumeCredits(statementData);
return renderPlainText(statementData, plays);

function totalAmount(data) {...}
function totalVolumeCredits(data) {...}
```

```js
// function renderPlainText…
result += `Amount owed is ${usd(data.totalAmount)}\n`;
result += `You earned ${data.totalVolumeCredits} credits\n`;
return result;
```

- 외부의 statementData 를 수정할 수도 있지만 명시적으로 parameter를 pass 하는 것이 더 좋다.

  - side effect 를 줄이기 위함?

- `Replace Loop with Pipeline`: loop 를 iterator method 로 변경

```js
// function renderPlainText…
function totalAmount(data) {
  return data.performances.reduce((total, p) => total + p.amount, 0);
}
function totalVolumeCredits(data) {
  return data.performances.reduce((total, p) => total + p.volumeCredits, 0);
}
```

- extract all the first-phase code into `createStatementData`

```js
// top level…
function statement(invoice, plays) {
  return renderPlainText(createStatementData(invoice, plays));
}
```

- 드디어 HTML rednering 을 추가 가능

```js
// statement.js…
function htmlStatement(invoice, plays) {
  return renderHtml(createStatementData(invoice, plays));
}
function renderHtml(data) {
  let result = `<h1>Statement for ${data.customer}</h1>\n`;
  result += "<table>\n";
  result += "<tr><th>play</th><th>seats</th><th>cost</th></tr>";
  for (let perf of data.performances) {
    result += `  <tr><td>${perf.play.name}</td><td>${perf.audience}</td>`;
    result += `<td>${usd(perf.amount)}</td></tr>\n`;
  }
  result += "</table>\n";
  result += `<p>Amount owed is <em>${usd(data.totalAmount)}</em></p>\n`;
  result += `<p>You earned <em>${data.totalVolumeCredits}</em> credits</p>\n`;
  return result;
}
```

## Status: Separated into Two Files (and Phases)

```js
// statement.js
import createStatementData from "./createStatementData.js";
function statement(invoice, plays) {
  return renderPlainText(createStatementData(invoice, plays));
}
function renderPlainText(data, plays) {
  let result = `Statement for ${data.customer}\n`;
  for (let perf of data.performances) {
    result += `  ${perf.play.name}: ${usd(perf.amount)} (${perf.audience} seats)\n`;
  }
  result += `Amount owed is ${usd(data.totalAmount)}\n`;
  result += `You earned ${data.totalVolumeCredits} credits\n`;
  return result;
}
function htmlStatement(invoice, plays) {
  return renderHtml(createStatementData(invoice, plays));
}
function renderHtml(data) {
  let result = `<h1>Statement for ${data.customer}</h1>\n`;
  result += "<table>\n";
  result += "<tr><th>play</th><th>seats</th><th>cost</th></tr>";
  for (let perf of data.performances) {
    result += `  <tr><td>${perf.play.name}</td><td>${perf.audience}</td>`;
    result += `<td>${usd(perf.amount)}</td></tr>\n`;
  }
  result += "</table>\n";
  result += `<p>Amount owed is <em>${usd(data.totalAmount)}</em></p>\n`;
  result += `<p>You earned <em>${data.totalVolumeCredits}</em> credits</p>\n`;
  return result;
}
function usd(aNumber) {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: 2,
  }).format(aNumber / 100);
}
```

```js
// createStatementData.js
export default function createStatementData(invoice, plays) {
  const result = {};
  result.customer = invoice.customer;
  result.performances = invoice.performances.map(enrichPerformance);
  result.totalAmount = totalAmount(result);
  result.totalVolumeCredits = totalVolumeCredits(result);
  return result;

  function enrichPerformance(aPerformance) {
    const result = Object.assign({}, aPerformance);
    result.play = playFor(result);
    result.amount = amountFor(result);
    result.volumeCredits = volumeCreditsFor(result);
    return result;
  }
  function playFor(aPerformance) {
    return plays[aPerformance.playID];
  }
  function amountFor(aPerformance) {
    let result = 0;
    switch (aPerformance.play.type) {
      case "tragedy":
        result = 40000;
        if (aPerformance.audience > 30) {
          result += 1000 * (aPerformance.audience - 30);
        }
        break;
      case "comedy":
        result = 30000;
        if (aPerformance.audience > 20) {
          result += 10000 + 500 * (aPerformance.audience - 20);
        }
        result += 300 * aPerformance.audience;
        break;
      default:
        throw new Error(`unknown type: ${aPerformance.play.type}`);
    }
    return result;
  }
  function volumeCreditsFor(aPerformance) {
    let result = 0;
    result += Math.max(aPerformance.audience - 30, 0);
    if ("comedy" === aPerformance.play.type)
      result += Math.floor(aPerformance.audience / 5);
    return result;
  }
  function totalAmount(data) {
    return data.performances.reduce((total, p) => total + p.amount, 0);
  }
  function totalVolumeCredits(data) {
    return data.performances.reduce((total, p) => total + p.volumeCredits, 0);
  }
}
```

- Brevity is the soul of wit, but clarity is the soul of evolvable software
- When programming, follow the camping rule: Always leave the code base healthier than when you found it.

## Reorganizing the Calculations by Type

- 현재의 amountFor 처럼 조건문 분기로 처리되면 점점 복잡도가 올라가므로,

- `Replace Conditional with Polymorphism`: 조건문을 객체로 변경

## Creating a Performance Calculator

- `Change Function Declaration`: play 필드를 PerformanceCalculator 내에 저장한다.
  - 이는 polymorphism 과는 상관없지만 하나의 class에 모아 consistency 를 높힌다.

```js
class PerformanceCalculator {
  constructor(aPerformance, aPlay) {
    this.performance = aPerformance;
    this.play = aPlay;
  }
}
```

```js
function enrichPerformance(aPerformance) {
  const calculator = new PerformanceCalculator(
    aPerformance,
    playFor(aPerformance),
  );
  //..
}
```

## Moving Functions into the Calculator

-`Move Function`: amountFor, volumeCredits 를 PerformanceCalculator 로 이동

```js
class PerformanceCalculator {
  //..
  get amount() {
    let result = 0;
    switch (this.play.type) {
      case "tragedy":
        result = 40000;
        if (this.performance.audience > 30) {
          result += 1000 * (this.performance.audience - 30);
        }
        break;
      case "comedy":
        result = 30000;
        if (this.performance.audience > 20) {
          result += 10000 + 500 * (this.performance.audience - 20);
        }
        result += 300 * this.performance.audience;
        break;
      default:
        throw new Error(`unknown type: ${this.play.type}`);
    }
    return result;
  }

  get volumeCredits() {
    let result = 0;
    result += Math.max(this.performance.audience - 30, 0);
    if ("comedy" === this.play.type)
      result += Math.floor(this.performance.audience / 5);
    return result;
  }
}
```

- What is this?

```js
// function createStatementData…
function amountFor(aPerformance) {
  return new PerformanceCalculator(aPerformance, playFor(aPerformance)).amount;
}
```

```js
// function createStatementData…
function enrichPerformance(aPerformance) {
  //..
  result.amount = calculator.amount;
  result.volumeCredits = calculator.volumeCredits;
}
```

## Making the Performance Calculator Polymorphic

- `Replace Type Code with Subclasses`:
- `Replace Constructor with Factory Function`: js의 constructor 는 subclass 를 return 할 수 없음

```js
// top level…
function createPerformanceCalculator(aPerformance, aPlay) {
  switch (aPlay.type) {
    case "tragedy":
      return new TragedyCalculator(aPerformance, aPlay);
    case "comedy":
      return new ComedyCalculator(aPerformance, aPlay);
    default:
      throw new Error(`unknown type: ${aPlay.type}`);
  }
}

class TragedyCalculator extends PerformanceCalculator {}
class ComedyCalculator extends PerformanceCalculator {}
```

```js
// function createStatementData…
function enrichPerformance(aPerformance) {
  const calculator = createPerformanceCalculator(
    aPerformance,
    playFor(aPerformance),
  );
  const result = Object.assign({}, aPerformance);
  result.play = calculator.play;
  result.amount = calculator.amount;
  result.volumeCredits = calculator.volumeCredits;
  return result;
}
```

- `Replace Conditional with Polymorphism`:

```js
// class TragedyCalculator…
  get amount() {
    let result = 40000;
    if (this.performance.audience > 30) {
      result += 1000 * (this.performance.audience - 30);
    }
    return result;
  }
```

```js
// class ComedyCalculator…
  get amount() {
    let result = 30000;
    if (this.performance.audience > 20) {
      result += 10000 + 500 * (this.performance.audience - 20);
    }
    result += 300 * this.performance.audience;
    return result;
  }
```

```js
// class PerformanceCalculator…
  get amount() {
    throw new Error('subclass responsibility');
  }
```

- volumeCredits 의 경우에는 variation 이 적으므로 superclass 의 메서드를 남겨둔다.

```js
// class PerformanceCalculator…
  get volumeCredits() {
    return Math.max(this.performance.audience - 30, 0);
  }
```

```js
// class ComedyCalculator…
  get volumeCredits() {
    return super.volumeCredits + Math.floor(this.performance.audience / 5);
  }
```

## Status: Creating the Data with the Polymorphic Calculator

- 최종 코드

```js
// createStatementData.js;
export default function createStatementData(invoice, plays) {
  const result = {};
  result.customer = invoice.customer;
  result.performances = invoice.performances.map(enrichPerformance);
  result.totalAmount = totalAmount(result);
  result.totalVolumeCredits = totalVolumeCredits(result);
  return result;

  function enrichPerformance(aPerformance) {
    const calculator = createPerformanceCalculator(
      aPerformance,
      playFor(aPerformance),
    );
    const result = Object.assign({}, aPerformance);
    result.play = calculator.play;
    result.amount = calculator.amount;
    result.volumeCredits = calculator.volumeCredits;
    return result;
  }
  function playFor(aPerformance) {
    return plays[aPerformance.playID];
  }
  function totalAmount(data) {
    return data.performances.reduce((total, p) => total + p.amount, 0);
  }
  function totalVolumeCredits(data) {
    return data.performances.reduce((total, p) => total + p.volumeCredits, 0);
  }
}

function createPerformanceCalculator(aPerformance, aPlay) {
  switch (aPlay.type) {
    case "tragedy":
      return new TragedyCalculator(aPerformance, aPlay);
    case "comedy":
      return new ComedyCalculator(aPerformance, aPlay);
    default:
      throw new Error(`unknown type: ${aPlay.type}`);
  }
}

class PerformanceCalculator {
  constructor(aPerformance, aPlay) {
    this.performance = aPerformance;
    this.play = aPlay;
  }
  get amount() {
    throw new Error("subclass responsibility");
  }
  get volumeCredits() {
    return Math.max(this.performance.audience - 30, 0);
  }
}

class TragedyCalculator extends PerformanceCalculator {
  get amount() {
    let result = 40000;
    if (this.performance.audience > 30) {
      result += 1000 * (this.performance.audience - 30);
    }
    return result;
  }
}

class ComedyCalculator extends PerformanceCalculator {
  get amount() {
    let result = 30000;
    if (this.performance.audience > 20) {
      result += 10000 + 500 * (this.performance.audience - 20);
    }
    result += 300 * this.performance.audience;
    return result;
  }
  get volumeCredits() {
    return super.volumeCredits + Math.floor(this.performance.audience / 5);
  }
}
```

- 코드 양은 비록 증가했지만,
- 각 play type 에 대한 계산이 그룹화 되었다
- 새로운 play type 이 추가되더라도 subclass 만 추가하면 된다.
- 두 개의 함수에 대한 conditional lookup 을 하나의 constructor function 으로 대체하였다.

## Final Thoughts

- The true test of good code is how easy it is to change it.
- The rhythm of refactoring is a steady beat of small changes.
