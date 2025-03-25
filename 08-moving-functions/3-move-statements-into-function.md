> Move Statements into Function

```js
result.push(`<p>title: ${person.photo.title}</p>`);
result.concat(photoData(person.photo));

function photoData(aPhoto) {
  return [
    `<p>location: ${aPhoto.location}</p>`,
    `<p>date: ${aPhoto.date.toDateString()}</p>`,
  ];
}
```

👇

```js
result.concat(photoData(person.photo));

function photoData(aPhoto) {
  return [
    `<p>title: ${aPhoto.title}</p>`,
    `<p>location: ${aPhoto.location}</p>`,
    `<p>date: ${aPhoto.date.toDateString()}</p>`,
  ];
}
```

# Motivation

- 특정 함수를 호출할 때마다 수행되는 중복이 있다면 그 코드를 함수 그 자체에 합친다.
- 함수 안에 합쳐져 있으냐 수정이 용이하고 미래에 다양성을 가지게 된 다면 `Move Statements To Callers` 로 손쉽게 이동
- 옮길 대상에 대해 완전히 이해한 경우에만 함수를 옮긴다.
  - 호출된 함수의 특정 부분이 이해가 안가지만 호출자를 변경하고 싶은 경우 `Exatract Function`

# Mechanics

- 반복적인 코드가 target 함수에 인접하지 않은 경우, `Slide Statements` 를 통해 이동
- target 함수가 source 함수에 의해서만 호출되는 경우 전체를 붙여넣고 Test. 이하 절차는 무시한다
- 더 많은 호출자가 있는 경우, `Extract Function` 을 통해 target 함수 혹은 statement 를 감싼다.
  - 찾기 쉬운 임시 이름으로 지정
- 모든 요청이 새 함수를 사용하도록 변경
- 모두 변경되면 `Inline Function` 으로 기존 함수 제거
- `Rename Function` 으로 새 함수의 임시 이름을 기존 함수 이름으로 변경 하거나 더 나은 이름 부여

# Example

```js
function renderPerson(outStream, person) {
  const result = [];
  result.push(`<p>${person.name}</p>`);
  result.push(renderPhoto(person.photo));
  result.push(`<p>title: ${person.photo.title}</p>`);
  result.push(emitPhotoData(person.photo));
  return result.join("\n");
}

function photoDiv(p) {
  return ["<div>", `<p>title: ${p.title}</p>`, emitPhotoData(p), "</div>"].join(
    "\n",
  );
}

function emitPhotoData(aPhoto) {
  const result = [];
  result.push(`<p>location: ${aPhoto.location}</p>`);
  result.push(`<p>date: ${aPhoto.date.toDateString()}</p>`);
  return result.join("\n");
}
```

- title print 하는 부분을 emitPhotoData 로 이동
  - 두 개 이상의 호출이 있으므로 `Extract Function` 부터 수행

```js
function photoDiv(p) {
  return ["<div>", zznew(p), "</div>"].join("\n");
}
function zznew(p) {
  return [`<p>title: ${p.title}</p>`, emitPhotoData(p)].join("\n");
}
```

- editPhotoData 를 호출하는 renderPerson 에도 zznew 적용

```js
function renderPerson(outStream, person) {
  const result = [];
  result.push(`<p>${person.name}</p>`);
  result.push(renderPhoto(person.photo));
  result.push(zznew(person.photo));
  return result.join("\n");
}
```

- `Inline Function` 으로 `emitPhotoData` 함수 제거

```js
function zznew(p) {
  return [
    `<p>title: ${p.title}</p>`,
    `<p>location: ${p.location}</p>`,
    `<p>date: ${p.date.toDateString()}</p>`,
  ].join("\n");
}
```

- `Rename Function` 으로 zznew 를 emitPhotoData 로 변경

```js
function renderPerson(outStream, person) {
  const result = [];
  result.push(`<p>${person.name}</p>`);
  result.push(renderPhoto(person.photo));
  result.push(emitPhotoData(person.photo));
  return result.join("\n");
}

function photoDiv(aPhoto) {
  return ["<div>", emitPhotoData(aPhoto), "</div>"].join("\n");
}

function emitPhotoData(aPhoto) {
  return [
    `<p>title: ${aPhoto.title}</p>`,
    `<p>location: ${aPhoto.location}</p>`,
    `<p>date: ${aPhoto.date.toDateString()}</p>`,
  ].join("\n");
}
```
