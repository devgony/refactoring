> Return Modified Value

```js
let totalAscent = 0;
calculateAscent();

function calculateAscent() {
  for (let i = 1; i < points.length; i++) {
    const verticalChange = points[i].elevation - points[i - 1].elevation;
    totalAscent += verticalChange > 0 ? verticalChange : 0;
  }
}
```

👇

```js
const totalAscent = calculateAscent();

function calculateAscent() {
  let result = 0;
  for (let i = 1; i < points.length; i++) {
    const verticalChange = points[i].elevation - points[i - 1].elevation;
    result += verticalChange > 0 ? verticalChange : 0;
  }
  return result;
}
```

# Motivation

- mutate 의 흐름을 명확하게 보여주는 것이 좋다
- 명확한 단일 값 리턴하는 함수 에 적용하기 좋다
- mutate 를 여러번 하지 않아야한다
- `Move Function` 의 사전 단계로 유용하다

# Mechanics

- 수정된 변수를 리턴하고 호출자에 변수로 할당한다
- Test
- 수정된 함수 안에서 value 선언
  - 호출자 쪽에서 value init 가능하면 생략
- Test
- 선언부와 계산부를 병합
  - 가능하면 변수를 immutable 로 선언
- Test
- 새로운 역할 반영을 위해 서브함수 내에서 변수명 변경
- Test

# Example

```js
let totalAscent = 0;
let totalTime = 0;
let totalDistance = 0;
calculateAscent();
calculateTime();
calculateDistance();
const pace = totalTime / 60 / totalDistance;
```

- totalAscent 에 대한 업데이트가 calculateAscent 내에 가려져 있으므로, return 값을 totalAscent 에 할당 한다

```diff
let totalAscent = 0;
let totalTime = 0;
let totalDistance = 0;
+totalAscent = calculateAscent();
calculateTime();
calculateDistance();
const pace = totalTime / 60 /  totalDistance ;
function calculateAscent() {
  for (let i = 1; i < points.length; i++) {
    const verticalChange = points[i].elevation - points[i-1].elevation;
    totalAscent += (verticalChange > 0) ? verticalChange : 0;
  }
+ return totalAscent;
}
```

- calculateAscent 내에서 totalAscent 를 쉐도잉 한다

```diff
function calculateAscent() {
+ let totalAscent = 0;
  for (let i = 1; i < points.length; i++) {
    const verticalChange = points[i].elevation - points[i-1].elevation;
    totalAscent += (verticalChange > 0) ? verticalChange : 0;
  }
  return totalAscent;
}
```

- convention 에 맞춰 이름을 result 로 변경

```diff
function calculateAscent() {
+ let result = 0;
  for (let i = 1; i < points.length; i++) {
    const verticalChange = points[i].elevation - points[i-1].elevation;
+   result += (verticalChange > 0) ? verticalChange : 0;
  }
+ return result;
}
```

- totalAscent 에 대한 선언과 할당을 병합. const 로 변경

```diff
+const totalAscent = calculateAscent();
let totalTime = 0;
let totalDistance = 0;
calculateTime();
calculateDistance();
const pace = totalTime / 60 /  totalDistance ;
```

- 다른 함수에 대해서도 반복 수행

```diff
const totalAscent = calculateAscent();
+const totalTime = calculateTime();
+const totalDistance = calculateDistance();
const pace = totalTime / 60 /  totalDistance ;
```
