> Rename Variable

```js
let a = height * width;
```

👇

```js
let area = height * width;
```

# Motivation

- 이름의 중요성은 얼마나 널리 쓰이냐에 달려있다
  - 짧은 함수 내에서 잠깐 쓰이는 변수의 경우 한글자로 해도 괜찮다
  - 반면에 전역변수 같은 경우 최대한 신중하게 이름을 정하는 것이 좋다

# Mechanics

- 변수가 넓은 범위에 쓰이는 경우 `Encapsulate Variable` 고려
- 모든 참조에 대해 하나씩 변경 수행
  - 다른 코드 베이스에서 참조하고 있거나, 배포된 변수인 경우는 리팩토링 할 수 없다
  - 변수를 변경할 수 없는 경우 새로운 이름에 복제만 해두고 점차적으로 변경한다
- 테스트 수행

# Example

- codebase 전체에 걸쳐 참조되고 있는 경우 rename이 어렵다
- read ref

```js
let tpHd = "untitled";
```

- mut ref

```js
tpHd = obj["articleTitle"];
```

- `Encapsulate Variable` 고려

```js
setTitle(obj["articleTitle"]);

function title() {
  return tpHd;
}
function setTitle(arg) {
  tpHd = arg;
}
```

- rename tpHd to `_title`
  - inline 으로 처리할 수도 있겠지만, 이름을 바꾸기 위해 캡슐화 할 정도로 널리 쓰이는 변수라면, 미래를 위해 캡슐화 해두는게 좋다

```js
let _title = "untitled";
function title() {
  return _title;
}
function setTitle(arg) {
  _title = arg;
}
```

## Renaming a Constant

- constant 의 이름을 변경할 때는 캡슐화 없이 새로운 이름에 대한 복제로 해결한다

  - 새 이름을 선언하고 이전 이름을 복사하여 할당하는 것이 롤백하기 쉽다

```js
const cpyNm = "Acme Gooseberries";
```

```js
const companyName = "Acme Gooseberries";
const cpyNm = companyName;
```
