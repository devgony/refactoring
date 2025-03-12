> Replace Loop with Pipeline

```js
const names = [];
for (const i of input) {
  if (i.job === "programmer") names.push(i.name);
}
```

👇

```js
const names = input.filter((i) => i.job === "programmer").map((i) => i.name);
```

# Motivation

- Collection Pipeline 은 consume, edit 하는 일련의 작업을 나열할 수 있다
  - object 의 흐름을 순차적으로 이해하기 쉽다

# Mechanics

- loop 의 collection 에 대한 새 변수를 생성
  - 기존 변수의 단순 복제여도 좋다
- 위에서부터 각 루프를 collection pipeline 으로 변환
- 모든 행동들이 루프에서 제거되면, 루프를 제거
  - accumulator 를 사용하는 경우 pipline의 결과를 accumulator에 할당 한다

# Example

- office 에 대한 CSV file 예시

```csv
office, country, telephone
Chicago, USA, +1 312 373 1000
Beijing, China, +86 4008 900 505
Bangalore, India, +91 80 4064 9570
Porto Alegre, Brazil, +55 51 3079 3550
Chennai, India, +91 44 660 44766

... (more data follows)
```

- India 의 office 가 있는 도시와 전화번호 들을 리턴

```js
function acquireData(input) {
  const lines = input.split("\n");
  let firstLine = true;
  const result = [];
  for (const line of lines) {
    if (firstLine) {
      firstLine = false;
      continue;
    }
    if (line.trim() === "") continue;
    const record = line.split(",");
    if (record[1].trim() === "India") {
      result.push({ city: record[0].trim(), phone: record[2].trim() });
    }
  }
  return result;
}
```

- loop 를 pipeline 으로 변경
  - 편의를 위해 변수를 먼저 분리

```js
function acquireData(input) {
  const lines = input.split("\n");
  let firstLine = true;
  const result = [];
  const loopItems = lines;
  for (const line of loopItems) {
    if (firstLine) {
      firstLine = false;
      continue;
    }
    if (line.trim() === "") continue;
    const record = line.split(",");
    if (record[1].trim() === "India") {
      result.push({ city: record[0].trim(), phone: record[2].trim() });
    }
  }
  return result;
}
```

- firstLine 을 필터하는 로직을 slice 로 분리

```diff
function acquireData(input) {
  const lines = input.split("\n");
- let firstLine = true;
  const result = [];
  const loopItems = lines
+   .slice(1);
  for (const line of loopItems) {
-   if (firstLine) {
-     firstLine = false;
-     continue;
-   }
    if (line.trim() === "") continue;
    const record = line.split(",");
    if (record[1].trim() === "India") {
      result.push({ city: record[0].trim(), phone: record[2].trim() });
    }
  }
  return result;
}
```

- 공백 필터 조건 -> filter operation 으로 변경

```diff
function acquireData(input) {
  const lines = input.split("\n");
  const result = [];
  const loopItems = lines
    .slice(1)
+   .filter((line) => line.trim() !== "");
  for (const line of loopItems) {
-   if (line.trim() === "") continue;
    const record = line.split(",");
    if (record[1].trim() === "India") {
      result.push({ city: record[0].trim(), phone: record[2].trim() });
    }
  }
  return result;
}
```

- split 을 map 내부로 변경

```diff
function acquireData(input) {
  const lines = input.split("\n");
  const result = [];
  const loopItems = lines
        .slice(1)
        .filter(line => line.trim() !== "")
+       .map(line => line.split(","))
        ;
  for (const line of loopItems) {
-   const record = line.split(",");
+   const record = line;
    if (record[1].trim() === "India") {
      result.push({city: record[0].trim(), phone: record[2].trim()});
    }
  }
  return result;
}
```

- India 에 대한 조건문을 filter 로 변경

```diff
function acquireData(input) {
  const lines = input.split("\n");
  const result = [];
  const loopItems = lines
        .slice(1)
        .filter(line => line.trim() !== "")
        .map(line => line.split(","))
+       .filter(record => record[1].trim() === "India")
        ;
  for (const line of loopItems) {
    const record = line;
-   if (record[1].trim() === "India") {
      result.push({city: record[0].trim(), phone: record[2].trim()});
-   }
  }
  return result;
}
```

- Output record 형태로 변경

```diff
function acquireData(input) {
  const lines = input.split("\n");
  const result = [];
  const loopItems = lines
        .slice(1)
        .filter(line => line.trim() !== "")
        .map(line => line.split(","))
        .filter(record => record[1].trim() === "India")
+       .map(record => ({city: record[0].trim(), phone: record[2].trim()}))
        ;
  for (const line of loopItems) {
    const record = line;
+   result.push(line);
  }
  return result;
}
```

- pipeline 을 result accumulator 에 할당

```diff
function acquireData(input) {
  const lines = input.split("\n");
+ const result = lines
        .slice(1)
        .filter(line => line.trim() !== "")
        .map(line => line.split(","))
        .filter(record => record[1].trim() === "India")
        .map(record => ({city: record[0].trim(), phone: record[2].trim()}))
        ;
- for (const line of loopItems) {
-   const record = line;
-   result.push(line);
- }
  return result;
}
```

- result 를 Inline 으로 제거
- lambda 변수를 rename

```js
function acquireData(input) {
  const lines = input.split("\n");
  return lines
    .slice(1)
    .filter((line) => line.trim() !== "")
    .map((line) => line.split(","))
    .filter((fields) => fields[1].trim() === "India")
    .map((fields) => ({ city: fields[0].trim(), phone: fields[2].trim() }));
}
```
