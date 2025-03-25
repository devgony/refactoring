> Move Statements to Callers

```js
emitPhotoData(outStream, person.photo);

function emitPhotoData(outStream, photo) {
  outStream.write(`<p>title: ${photo.title}</p>\n`);
  outStream.write(`<p>location: ${photo.location}</p>\n`);
}
```

👇

```js
emitPhotoData(outStream, person.photo);
outStream.write(`<p>location: ${person.photo.location}</p>\n`);

function emitPhotoData(outStream, photo) {
  outStream.write(`<p>title: ${photo.title}</p>\n`);
}
```

# Motivation

- 함수는 추상화의 기본적인 블록이지만 경계를 정하기 어렵다
- 코드가 바뀌면서 함수의 경계도 바뀐다

  - 한 때는 응집력있는 원자적 행동 단위였지만, 두 가지 이상 서로 다른것의 혼합물이 됌

- 여러 곳에서 사용되는 공통 동작이 일부 호출에서 달라져야 할 때
- `Move Statements to Callers` 는 작은 변경에는 잘 동작하지만, 호출자와 호출되는 함수의 경계를 재 설정해야 하는 경우도 있다
  - 일단 `Inline Function` 한 후 slide 하고 다시 extract 하는 것이 올바른 경계 설정에 도움을 준다

# Mechanics

- 두 개 이하 호출자가 있는 단순한 구조라면 전체를 잘라서 붙여넣고 Test 수행
- 더 많은 호출자가 있는 경우, `Extract Function` 으로 이동하지 않고 고정할 부분을 추출. 찾기쉬운 임시 이름 부여
  - 함수가 subclass 에 의해 override 되는 경우, 대상을 모두 추출하여 rename 한 메서드가 모든 class 에서 같게 한다. 이후 subclass 메서드는 삭제
- `Inline Function` 으로 원본 함수 제거
- `Change Function Declaration` 으로 새 함수 이름을 원본 함수 이름으로 변경

# Example

- 두 개의 호출자를 가진 예시

```js
function renderPerson(outStream, person) {
  outStream.write(`<p>${person.name}</p>\n`);
  renderPhoto(outStream, person.photo);
  emitPhotoData(outStream, person.photo);
}

function listRecentPhotos(outStream, photos) {
  photos
    .filter((p) => p.date > recentDateCutoff())
    .forEach((p) => {
      outStream.write("<div>\n");
      emitPhotoData(outStream, p);
      outStream.write("</div>\n");
    });
}

function emitPhotoData(outStream, photo) {
  outStream.write(`<p>title: ${photo.title}</p>\n`);
  outStream.write(`<p>date: ${photo.date.toDateString()}</p>\n`);
  outStream.write(`<p>location: ${photo.location}</p>\n`);
}
```

- `listRecentPhotos` 가 `renderPerson` 과는 다르게 렌더링을 하게 하고싶다
  - 마지막 줄을 `Move Statements to Callers`
- 호출자가 두 개 이하이므로 바로 복붙 해도 되지만 설명을 위해 정석으로 접근
- `Extract Function`: `emitPhotoData` 의 고정할 부분을 함수로 추출

```js
function emitPhotoData(outStream, photo) {
  zztmp(outStream, photo);
  outStream.write(`<p>location: ${photo.location}</p>\n`);
}

function zztmp(outStream, photo) {
  outStream.write(`<p>title: ${photo.title}</p>\n`);
  outStream.write(`<p>date: ${photo.date.toDateString()}</p>\n`);
}
```

- `Inline Function`: `emitPhotoData`을 해체한다. `renderPerson` 내 에서 먼저 수행

```js
function renderPerson(outStream, person) {
  outStream.write(`<p>${person.name}</p>\n`);
  renderPhoto(outStream, person.photo);
  zztmp(outStream, person.photo);
  outStream.write(`<p>location: ${person.photo.location}</p>\n`);
}
```

- `listRecentPhotos` 에도 적용

```js
function listRecentPhotos(outStream, photos) {
  photos
    .filter((p) => p.date > recentDateCutoff())
    .forEach((p) => {
      outStream.write("<div>\n");
      zztmp(outStream, p);
      outStream.write(`<p>location: ${p.location}</p>\n`);
      outStream.write("</div>\n");
    });
}
```

- `emitPhotoData` 함수 제거

- `zztmp` 를 원래 함수 이름(`emitPhotoData`)으로 변경
