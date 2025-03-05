> Encapsulate Collection

```js
class Person {
  get courses() {return this._courses;}
  set courses(aList) {this._courses = aList;}
```

👇

```js
class Person {
  get courses() {return this._courses.slice();}
  addCourse(aCourse)    { ... }
  removeCourse(aCourse) { ... }
```

# Motivation

- mutable data 는 모두 encapsulate 하는 것이 좋다.
- collection 의 경우 그냥 reutnr 할 경우 class 가 개입하기 전에 수정을 허락하게 된다
- 방지하기 위해 `add`, `remove` method 를 제공
- 완벽히 mutation 을 차단하기 위해서는 collection 의 복사본을 return
  - collection 을 아예 반환하지 않고 indexes 혹은 keys 만 제공하는 법도 있지만 사용성이 떨어지므로 비추
- source data 의 수정 사항이 proxy 에는 표시되지만 복사본에는 반영되지 않는다
  - 이런 방식의 엑세스는 짧은 시간동안만 유지되므로 대게 문제가 되지 않는다

# Mechanics

- collection 에 대한 참조 변수가 encapsulate 되지 않았다면 `Encapsulate Variable` 을 수행
- add, remove method 를 추가
  - collection 에 setter 가 있다면 `Remove Setting Method` 수행
  - 없다면 collection 의 복제본을 제공
- static check
- collection 에 대해 mutate 하는 모든 부분에 대해 add, remove method 를 사용하도록 변경
- getter 가 read-only proxy 혹은 copy 를 return 하도록 수정
- test

# Example

```js
// class Person…
  constructor (name) {
    this._name = name;
    this._courses = [];
  }
  get name() {return this._name;}
  get courses() {return this._courses;}
  set courses(aList) {this._courses = aList;}

// class Course…
  constructor(name, isAdvanced) {
    this._name = name;
    this._isAdvanced = isAdvanced;
  }
  get name()       {return this._name;}
  get isAdvanced() {return this._isAdvanced;}

// client…
numAdvancedCourses = aPerson.courses
  .filter(c => c.isAdvanced)
  .length;
```

- collection 전체를 set 하는건 그나마 괜찮다.

```js
// client code…
const basicCourseNames = readBasicCourseNames(filename);
aPerson.courses = basicCourseNames.map((name) => new Course(name, false));

- 하지만 collection 일부를 수정하는 것은 위험하다.

// client code…
for (const name of readBasicCourseNames(filename)) {
  aPerson.courses.push(new Course(name, false));
}
```

- add 와 remove method 를 추가

```js
// class Person…
  addCourse(aCourse) {
    this._courses.push(aCourse);
  }
  removeCourse(aCourse, fnIfAbsent = () => {throw new RangeError();}) {
    const index = this._courses.indexOf(aCourse);
    if (index === -1) fnIfAbsent();
    else this._courses.splice(index, 1);
  }
```

- caller 에 적용

```js
// client code…
for (const name of readBasicCourseNames(filename)) {
  aPerson.addCourse(new Course(name, false));
}
```

- courses 전체를 수정할 일이 없다면 `Remove Setting Method` 를 수행
  - 특별한 이유로 해당 기능이 필요하다면 collection 을 복제해서 전달하도록 수정

```js
// class Person…
set courses(aList) {this._courses = aList.slice();} // TODO: why slice?
```

- 주어진 modifier 이외에는 수정 못하도록 getter 에서 복제본을 return

```js
// class Person…
  get courses() {return this._courses.slice();}
```
