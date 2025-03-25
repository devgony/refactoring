> Rename Field

```js
class Organization {
  get name() {...}
}
```

👇

```js
class Organization {
  get title() {...}
}
```

# Motivation

- 레코드 구조의 필드 일므은 프로그램 전체에서 널리 사용되는 경우 특히 중요하다
- 테이블 스키마는 플로우차트가 필요 없을 정도로 유용하다
- 클래스의 필드 역시 이름이 중요하다

# Mechanics

- 레코드가 제한된 범위를 가지면, 참조하는 모든 곳에서 이름을 변경하고 test 수행.
  - 성공한다면 아래 단계는 수행 불필요
- 레코드가 캡슐화 되어있지 않다면, `Encapsulate Record`
- private 필드를 rename 하고 관련 내부 메서드들에도 적용
- Test
- constructor 가 해당 이름을 사용한다면 `Change Function Declaration`
- 각 accessor 에 `Rename Function` 수행

# Example: Renaming a Field

```js
const organization = { name: "Acme Gooseberries", country: "GB" };
```

- name 필드를 title 로 변경. title 에 대한 update 로직이 있으므로 먼저 `Encapsulate Record` 수행

```js
class Organization {
  constructor(data) {
    this._name = data.name;
    this._country = data.country;
  }
  get name() {
    return this._name;
  }
  set name(aString) {
    this._name = aString;
  }
  get country() {
    return this._country;
  }
  set country(aCountryCode) {
    this._country = aCountryCode;
  }
}

const organization = new Organization({
  name: "Acme Gooseberries",
  country: "GB",
});
```

- record 가 class 로 변경되는 단계이므로 일단은 class 의 field 만 `_name` 에서 `_title` 로 변경

```diff
class Organization {
  constructor(data) {
+   this._title = data.name;
    this._country = data.country;
  }
  get name() {
+   return this._title;
  }
  set name(aString) {
+   this._title = aString;
  }
  get country() {
    return this._country;
  }
  set country(aCountryCode) {
    this._country = aCountryCode;
  }
}
```

- backward compatibility 를 위해 name 을 title 로 변경해주는 로직 추가

```diff
  class Organization {
    constructor(data) {
+     this._title = (data.title !== undefined) ? data.title : data.name;
      this._country = data.country;
    }
    get name()    {return this._title;}
    set name(aString) {this._title = aString;}
    get country()    {return this._country;}
    set country(aCountryCode) {this._country = aCountryCode;}
  }
```

- 실제 record 에서도 name => title 수정

```diff
const organization = new Organization({
+ title: "Acme Gooseberries",
  country: "GB",
});
```

- 변경이 완료되었으니 backward compatibility 로직 제거

```diff
  class Organization {
    constructor(data) {
      this._title = data.title;
      this._country = data.country;
    }
    get name()    {return this._title;}
    set name(aString) {this._title = aString;}
    get country()    {return this._country;}
    set country(aCountryCode) {this._country = aCountryCode;}
  }
```

- `Rename Function` 으로 accessor 도 변경

```js
get title()    {return this._title;}
set title(aString) {this._title = aString;}
```

- class 로 감싸지 않고 값을 복제하여 순차적으로 rename 하는 방법도 있겠으나, 데이터의 복제는 immutable 이 아닌경우 위험함
