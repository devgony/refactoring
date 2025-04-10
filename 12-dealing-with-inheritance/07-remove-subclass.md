> Remove Subclass

```js
class Person {
  get genderCode() {
    return "X";
  }
}
class Male extends Person {
  get genderCode() {
    return "M";
  }
}
class Female extends Person {
  get genderCode() {
    return "F";
  }
}
```

👇

```js
class Person {
  get genderCode() {
    return this._genderCode;
  }
}
```

# Motivation

- subclass 의 가치가 사라지고 이해하는데 추가 시간만 걸린다면 없애고 superclass 의 필드로 바꾸자

# Mechanics

- `Replace Constructor with Factory Function`: on subclass constructor
- subclass 의 type 에 대한 test 가 있는 경우 `Extract Function` 을 사용하고 `Move Function` 을 통해 superclass 로 이동
- subclass 의 type 을 나타낼 필드 생성
- subclass 를 참조하던 메서드가 필드를 바라보도록 변경
- subclss 제거
- Test

# Example

```js
class Person…
  constructor(name) {
    this._name = name;
  }
  get name()    {return this._name;}
  get genderCode() {return "X";}
  // snip
class Male extends Person {
  get genderCode() {return "M";}
}

class Female extends Person {
  get genderCode() {return "F";}
}

client…
  const numberOfMales = people.filter(p => p instanceof Male).length;
```

- 표현법을 변경할때, 영향을 최소화 하기 위해 캡슐화를 하는게 좋다
- 현재의 경우 `Replace Constructor with Factory Function`
  - 일단 직관적으로 각 생성자에 대해 factory 생성

```js
function createPerson(name) {
  return new Person(name);
}
function createMale(name) {
  return new Male(name);
}
function createFemale(name) {
  return new Female(name);
}
```

- client 의 경우 gender code 를 바로 사용하는 경우가 많다

```js
function loadFromInput(data) {
  const result = [];
  data.forEach((aRecord) => {
    let p;
    switch (aRecord.gender) {
      case "M":
        p = new Male(aRecord.name);
        break;
      case "F":
        p = new Female(aRecord.name);
        break;
      default:
        p = new Person(aRecord.name);
    }
    result.push(p);
  });
  return result;
}
```

- `Extract Function` 으로 selection logic 을 분리

```diff
+function createPerson(aRecord) {
  let p;
  switch (aRecord.gender) {
    case 'M': p = new Male(aRecord.name); break;
    case 'F': p = new Female(aRecord.name); break;
    default: p = new Person(aRecord.name);
  }
  return p;
}

function loadFromInput(data) {
  const result = [];
  data.forEach(aRecord => {
+   result.push(createPerson(aRecord));
  });
  return result;
}
```

- `Inline Variable` 및 early return 으로 정리

```diff
function createPerson(aRecord) {
  switch (aRecord.gender) {
+   case 'M': return new Male  (aRecord.name);
+   case 'F': return new Female(aRecord.name);
+   default:  return new Person(aRecord.name);
  }
}
```

- `Replace Loop with Pipeline`

```diff
function loadFromInput(data) {
+ return data.map(aRecord => createPerson(aRecord));
}
```

- `Extract Function`: isMale

```diff
client…
+ const numberOfMales = people.filter(p => isMale(p)).length;

+function isMale(aPerson) {return aPerson instanceof Male;}
```

- `Move Function`: into Person

```diff
class Person…
+ get isMale() {return this instanceof Male;}

client…
+ const numberOfMales = people.filter(p => p.isMale).length;
```

- 새로 만든 필드를 사용한다. 호환성을 위해 getter 는 그대로 사용
  - 없으면 default case ("X") 설정

```diff
class Person…
  constructor(name, genderCode) {
    this._name = name;
+   this._genderCode = genderCode || "X";
  }

+ get genderCode() {return this._genderCode;}
```

- M 인 경우부터 필드를 사용하도록 변경

```diff
function createPerson(aRecord) {
  switch (aRecord.gender) {
+   case 'M': return new Person(aRecord.name, "M");
    case 'F': return new Female(aRecord.name);
    default:  return new Person(aRecord.name);
  }
}

class Person…
+ get isMale() {return "M" === this._genderCode;}
```

- Male subclass 제거 후 Female 반복

```diff
function createPerson(aRecord) {
  switch (aRecord.gender) {
    case 'M': return new Person(aRecord.name, "M");
+   case 'F': return new Person(aRecord.name, "F");
    default:  return new Person(aRecord.name);
  }
}
```

- default 의 경우 factory 에서 처리
  - 이미 한거임..

```diff
function createPerson(aRecord) {
  switch (aRecord.gender) {
    case 'M': return new Person(aRecord.name, "M");
    case 'F': return new Person(aRecord.name, "F");
+   default:  return new Person(aRecord.name, "X");
  }
}

class Person…
  constructor(name, genderCode) {
    this._name = name;
+   this._genderCode = genderCode || "X";
  }
```
