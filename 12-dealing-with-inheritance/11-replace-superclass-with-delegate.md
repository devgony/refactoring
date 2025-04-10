> Replace Superclass with Delegate

```js
class List {...}
class Stack extends List {...}
```

👇

```js
class Stack {
  constructor() {
    this._storage = new List();
  }
}
class List {...}
```

# Motivation

- OOP 에서 subclassing 은 혼란과 복잡을 초래할 수 있다.
- 오용 사례
  - 연속된 subclass 의 stack 을 쌓음
    - data 에 대한 모든 연산은 stact 된 interface 에 존재 하겠지만 적용 못하는 경우가 대부분이다
    - List 를 stack 의 field 로 만들고 필요한 연산을 위임하는게 좋다

## Replace Superclass with Delegate

- 함수의 superclass 가 subclass 에서 의미가 없다면 상속을 하지 말아야 한다는 신호
- superclass 모든 함수를 사용하는것 뿐만아니라 모든 subclass 의 instance 들은 superclass 의 instance 이므로 superclass 가 사용되는 모든 경우에 유효한 object 여야 한다
- `type-instance-homonym`
  - Car class 가 이름과 엔진크기 를 가지면 VIN 번호와 공정일자를 추가하여 물리적인 Car 를 만들수 있을 거라 생각하는 일반적인 실수
- 상속을 위임으로 변경하여 객체를 분리하면 쉽게 해결된다
- subclass 가 합리적을 모델링 되었어도 `Replace Superclass with Delegate` 는 유용하다
  - sub 와 super 의 관계가 너무 의존적이기 때문에
  - 단점은 host 와 delegate 모두에 같은 forwarding 함수를 작성해야 하는 것
- 하지만 상속을 아예 피하라는건 아니다
  - 상속을 먼저 하고 문제가 생기면 `Replace Superclass with Delegate` 를 고려하라

# Mechanics

- subclass 에 superclass 를 참조할 필드를 생성
  - 새 instance 만들 때 이 위임 참조를 추가
- superclass 의 각 요소에 subclass 의 forwarding function 생성하여 위임 ref 전달
  - 각 요소별로 Test
  - 단 get/set 의 경우, 페어로 옮겨야 Test 가능
- 모든 superclass 요소들이 forwarer 로 override 되면, 상속 link 제거

# Example

- 고대 두루마리(Scroll) 도서관 예제

```js
class CatalogItem…
  constructor(id, title, tags) {
    this._id = id;
    this._title = title;
    this._tags = tags;
  }

  get id() {return this._id;}
  get title() {return this._title;}
  hasTag(arg) {return this._tags.includes(arg);}
```

- 두루마리(Scroll)에 필요한 것 중 하나는 정기적인 정리
  - Catalog item 을 청소에 필요한 데이터로 확장

```js
class Scroll extends CatalogItem…
  constructor(id,  title, tags, dateLastCleaned) {
    super(id, title, tags);
    this._lastCleaned = dateLastCleaned;
  }

  needsCleaning(targetDate) {
    const threshold =  this.hasTag("revered") ? 700 : 1500;
    return this.daysSinceLastCleaning(targetDate) > threshold ;
  }
  daysSinceLastCleaning(targetDate) {
    return this._lastCleaned.until(targetDate, ChronoUnit.DAYS);
  }
```

- 하지만 실제 Scroll과 Catalog item 에는 차이가 있다
  - 회색조 질환에 대한 치료법 Scroll과 여러장 이지만 Catalog item은 하나
  - immutable 이라면 복제해서 써도 상관 없지만 mutable 이라면 동기화에 주의해야 한다
- 위 이슈가 아니더라도 관계는 변경하자
  - 미래의 개발자들이 혼동될 수 있다
- Scroll과 CatalogItem 을 참조하는 필드 추가, 생성자에서 주입

```diff
class Scroll extends CatalogItem…
  constructor(id,  title, tags, dateLastCleaned) {
    super(id, title, tags);
+   this._catalogItem = new CatalogItem(id, title, tags);
    this._lastCleaned = dateLastCleaned;
  }
```

- superclass 의 모든 필드에 대한 forwarding 메서드 생성

```diff
class Scroll…
+ get id() {return this._catalogItem.id;}
+ get title() {return this._catalogItem.title;}
+ hasTag(aString) {return this._catalogItem.hasTag(aString);}
```

- 상속 제거

```diff
-class Scroll extends CatalogItem{
+class Scroll {
  constructor(id,  title, tags, dateLastCleaned) {
-   super(id, title, tags);
    this._catalogItem = new CatalogItem(id, title, tags);
    this._lastCleaned = dateLastCleaned;
  }
```

- 기본적인 refactoring 은 끝
- refactoring 결과로 CatalogItem 의 역할이 Scroll의 컴포넌트로 바뀌었다
- 각 Scroll은 unique 한 catalogItem 을 가지게 된다
- 하지만 복수(여섯 장)의 Scroll이 같은 catalogItem 을 참조하게 하면 더 좋다: `Change Value to Reference`

  - 하지만 기존 상속 구조에서 Scroll은 catalogItem의 ID 를 본인의 ID 로서 필드에 저장했다
  - 참조로 변경하기 위해서는 Scroll ID 가 아니라 catalogItem ID 를 저장해야 한다

```diff
class Scroll…

  constructor(id,  title, tags, dateLastCleaned) {
+   this._id = id;
    this._catalogItem = new CatalogItem(null, title, tags);
    this._lastCleaned = dateLastCleaned;
  }

+ get id() {return this._id;}
```

- 당장은 id 가 null 인 catalogItem 이 에러를 내겠지만, 모양은 갖춰졌다
- Scroll 을 load 하는 client

```js
load routine…
  const scrolls = aDocument
        .map(record => new Scroll(record.id,
                                  record.catalogData.title,
                                  record.catalogData.tags,
                                  LocalDate.parse(record.lastCleaned)));
```

1. repository 를 찾거나 생성한다
2. ID 를 Scorll의 생성자에 전달한다
   - 다행히 입력데이터에 존재하는데 상속시에는 사용 안되고 있었다
   - `Change Function Declaration` 으로 catalog 와 catalogItem ID 를 전달

```diff
load routine…
  const scrolls = aDocument
        .map(record => new Scroll(record.id,
                                  record.catalogData.title,
                                  record.catalogData.tags,
                                  LocalDate.parse(record.lastCleaned),
+                                 record.catalogData.id,
+                                 catalog));

class Scroll…
+ constructor(id,  title, tags, dateLastCleaned, catalogID, catalog) {
    this._id = id;
    this._catalogItem = new CatalogItem(null, title, tags);
    this._lastCleaned = dateLastCleaned;
  }
```

- 생성자에서 새로 생성이 아니라 id 를 lookup

```diff
class Scroll…
  constructor(id,  title, tags, dateLastCleaned, catalogID, catalog) {
    this._id = id;
+   this._catalogItem = catalog.get(catalogID);
    this._lastCleaned = dateLastCleaned;
  }
```

- title 과 tags 를 생성자에서 제거

```diff
load routine…
  const scrolls = aDocument
        .map(record => new Scroll(record.id,
+                                 record.catalogData.title,
+                                 record.catalogData.tags,
                                  LocalDate.parse(record.lastCleaned),
                                  record.catalogData.id,
                                  catalog));

class Scroll…
- constructor(id, title, tags, dateLastCleaned, catalogID, catalog) {
+ constructor(id, dateLastCleaned, catalogID, catalog) {
    this._id = id;
    this._catalogItem = catalog.get(catalogID);
    this._lastCleaned = dateLastCleaned;
  }
```
