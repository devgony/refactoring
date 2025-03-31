> Remove Setting Method

```js
class Person {
  get name() {...}
  set name(aString) {...}
```

👇

```js
class Person {
  get name() {...}
```

# Motivation

- immutable 을 위해 setter 제거
- 생성자만 남겨둠
- 객체를 생성 스크립트를 통해 생성하고, 이후에는 setter를 사용하지 않는다면 제거하는것이 좋다

# Mechanics

- setter 의 대상이 생성자 없는경우 추가한다: `Change Function Declaration`
- 생성자 이외의 setter 를 생성자제 사용하도록 변경
- `Inline Function` 으로 생성자 내부의 setter사용 및 정의 제거
- Test

# Example

```js
class Person…
  get name()    {return this._name;}
  set name(arg) {this._name = arg;}
  get id()    {return this._id;}
  set id(arg) {this._id = arg;}
```

- 새 객체 생성

```js
const martin = new Person();
martin.name = "martin";
martin.id = "1234";
```

- name 은 바뀔수 있지만 id 는 바뀔 일이 없으므로 생성자에서 입력: `Change Function Declaration`

```js
class Person…
  constructor(id) {
    this.id = id;
  }
```

- 생성 스크립트를 생성자 방식으로 변경

```diff
+ const martin = new Person("1234");
  martin.name = "martin";
- martin.id = "1234";
```

- 모든 생성부에 반복 수행 후 `Inline Function`: 으로 setter 제거

```diff
Class Person…
  constructor(id) {
+   this._id = id;
  }
  get name()    {return this._name;}
  set name(arg) {this._name = arg;}
  get id()    {return this._id;}
- set id(arg) {this._id = arg;}
```
