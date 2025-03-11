> Split Loop

```js
let averageAge = 0;
let totalSalary = 0;
for (const p of people) {
  averageAge += p.age;
  totalSalary += p.salary;
}
averageAge = averageAge / people.length;
```

👇

```js
let totalSalary = 0;
for (const p of people) {
  totalSalary += p.salary;
}

let averageAge = 0;
for (const p of people) {
  averageAge += p.age;
}
averageAge = averageAge / people.length;
```

# Motivation

- 같은 루프에서 두가지의 일을 하면 수정할 때마다 두 가지 모두를 이해해야 한다
- 루프를 분리하면 각각의 루프를 이해하기 쉬워진다
- 루프를 분리하면 다양하게 사용할 수 있다
  - 값을 return
  - 많은것을 struct 에 담아 return
  - 특히, `Split Loop` 직후에 `Extract Function` 주로 활용
- 성능은 일단 코드가 깔끔해진 이후에 개선해도 되며, 분할 된 루프가 미치는 영향은 대부분 미미하다

# Mechanics

- 루프를 복사하여 붙여넣기
- 중복 sdie effect 를 확인하여 해결
- Test
- Extract Function 과 함께 사용 권장

# Example

- 급여 합계와 가장 어린 나이 계산기

```js
let youngest = people[0] ? people[0].age : Infinity;
let totalSalary = 0;
for (const p of people) {
  if (p.age < youngest) youngest = p.age;
  totalSalary += p.salary;
}

return `youngestAge: ${youngest}, totalSalary: ${totalSalary}`;
```

- 단순하지만 두 가지 일이 하나의 루프에 섞여있으므로 분리한다
  - totalSalary loop 복제

```js
let youngest = people[0] ? people[0].age : Infinity;
let totalSalary = 0;
for (const p of people) {
  if (p.age < youngest) youngest = p.age;
  totalSalary += p.salary;
}

for (const p of people) {
  if (p.age < youngest) youngest = p.age;
  totalSalary += p.salary;
}
```

- 현재 경우는 중복 side effect 가 있으므로 제거

```diff
..
for (const p of people) {
- if (p.age < youngest) youngest = p.age;
  totalSalary += p.salary;
}

for (const p of people) {
  if (p.age < youngest) youngest = p.age;
- totalSalary += p.salary;
}
```

- `Slide Statements` 로 추출할 함수 주변에 변수를 보낸다

```js
let totalSalary = 0;
for (const p of people) {
  totalSalary += p.salary;
}

let youngest = people[0] ? people[0].age : Infinity;
for (const p of people) {
  if (p.age < youngest) youngest = p.age;
}
return `youngestAge: ${youngest}, totalSalary: ${totalSalary}`;
```

- 두 개의 루프를 각각 `Extract Function`

```js
return `youngestAge: ${youngestAge()}, totalSalary: ${totalSalary()}`;

function totalSalary() {
  let totalSalary = 0;
  for (const p of people) {
    totalSalary += p.salary;
  }
  return totalSalary;
}

function youngestAge() {
  let youngest = people[0] ? people[0].age : Infinity;
  for (const p of people) {
    if (p.age < youngest) youngest = p.age;
  }
  return youngest;
}
```

- 필요한 경우 추가 작업 수행
  - `Replace Loop With Pipleline`: totalSalary
  - `Substitute Algorithm`: youngestAge

```js
return `youngestAge: ${youngestAge()}, totalSalary: ${totalSalary()}`;

function totalSalary() {
  return people.reduce((total, p) => total + p.salary, 0);
}
function youngestAge() {
  return Math.min(...people.map((p) => p.age));
}
```
