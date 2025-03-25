> Encapsulate Record

```javascript
organization = { name: "Acme Gooseberries", country: "GB" };
```

👇

```js
class Organization {
  constructor(data) {
    this._name = data.name;
    this._country = data.country;
  }
  get name() {
    return this._name;
  }
  set name(arg) {
    this._name = arg;
  }
  get country() {
    return this._country;
  }
  set country(arg) {
    this._country = arg;
  }
}
```

# Motivation

- Record 는 관련 데이터를 그룹화하여 의미있는 데이터 단위를 전달
  - 단점: 저장된 내용과 계산된 값을 명확히 구분해야 한다
- mutable Record 보다 Object 가 저장된 내용을 숨기고 종류별 값에 대한 메서드를 제공하기 용이하다
  - immutable 이라면 Record 를 써도 충분하다
- Record 는 필드이름을 선언하는것과 동적 필드 두 가지가 있는데, 후자는 hashmap 등을 통해 구현된다

  - 범위가 작은경우 써도 되겠지만 넓어질수록 복잡도도 올라가므로 이를 명시적 Record 로 리팩토링 할수도 있겠지만 class 로 만드는게 가장 좋다

- json 혹은 xml 로 serde 하려는 경우 단순한 list 혹은 hashmap 을 쓸 수도 있겠지만 추후 변경되는 스키마의 추적을 위해서라도 class 쓰는게 좋겠다

# Mechanics

- record 를 할당한 변수에 대해 `Encapsulate Variable` 를 수행
  - 변수를 감싸는 helper function 이므로 찾기 쉬운이름으로 설정
- redcord 를 감싸는 단순한 class 로 교체.
  - class 내부 raw record 에 대한 accessor 정의
- Test
- raw record 대신에 object 를 return 하는 함수 제공
- record 의 각 client 에 대해 record 대신 object 를 return 하는 함수로 변경
  - nested 와 같은 복잡한 구조인경우 데이터를 먼저 업데이트하는 client 에 집중해야 한다
- 데이터를 읽기만 하는 client 를 위해서는 데이터의 복제본을 제공하는게 좋다
- class 내 raw record 와 초기에 만든 helper function 을 삭제한다
- Test
- record 의 필드 자체가 구조화 되어있는 경우 `Encapsulate Record` 와 `Encapsulate Collection` 을 recursive 하게 적용

# Example

```js
const organization = { name: "Acme Gooseberries", country: "GB" };

result += `<h1>${organization.name}</h1>`;
organization.name = newName;
```

- `Encapsulate Variable` 수행
  - 어차피 곧 사라질 함수이므로 못생기고 긴 이름을 사용

```js
function getRawDataOfOrganization() {
  return organization;
}

// example reader…
result += `<h1>${getRawDataOfOrganization().name}</h1>`;

// example writer…
getRawDataOfOrganization().name = newName;
```

- Encapsulate Record

```js
// class Organization…
class Organization {
  constructor(data) {
    this._data = data;
  }
}
```

```js
// top level
const organization = new Organization({
  name: "Acme Gooseberries",
  country: "GB",
});
function getRawDataOfOrganization() {
  return organization._data;
}
function getOrganization() {
  return organization;
}
```

- record 에는 getter, setter 로만 접근하도록 변경

```js
// class Organization…
  set name(aString) {this._data.name = aString;}

// client…
  getOrganization().name = newName;

// class Organization…
  get name()    {return this._data.name;}

// client…
  result += `<h1>${getOrganization().name}</h1>`;
```

- ugly helper function 제거

```diff
-function getRawDataOfOrganization() {return organization._data;}
+function getOrganization() {return organization;}
```

- `_data` 대신 필드를 직접 접근하도록 변경
  - 캡슐화를 깨트리는 참조가 있는지 검사하는 역할도 함
  - 개별 필드로 두지 않고 rawRecord 유지 하려면 복제본을 리턴 해야할 것임

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
```

## Example: Encapsulating a Nested Record

- 위는 shallow record 였지만 nested record 는 어떻게 할까?

```json
"1920": {
  name: "martin",
  id: "1920",
  usages: {
    "2016": {
      "1": 50,
      "2": 55,
      // remaining months of the year
    },
    "2015": {
      "1": 70,
      "2": 63,
      // remaining months of the year
    }
  }
},
"38673": {
  name: "neal",
  id: "38673",
  // more customers in a similar form
```

```js
// sample update…
customerData[customerID].usages[year][month] = amount;

// sample read…
function compareUsage(customerID, laterYear, month) {
  const later = customerData[customerID].usages[laterYear][month];
  const earlier = customerData[customerID].usages[laterYear - 1][month];
  return { laterAmount: later, change: later - earlier };
}
```

- 역시 일단은 `Encapsulate Variable` 수행 으로 시작

```js
function getRawDataOfCustomers() {
  return customerData;
}
function setRawDataOfCustomers(arg) {
  customerData = arg;
}

// sample update…
getRawDataOfCustomers()[customerID].usages[year][month] = amount;

// sample read…
function compareUsage(customerID, laterYear, month) {
  const later = getRawDataOfCustomers()[customerID].usages[laterYear][month];
  const earlier =
    getRawDataOfCustomers()[customerID].usages[laterYear - 1][month];
  return { laterAmount: later, change: later - earlier };
}
```

- class 생성

```js
class CustomerData {
  constructor(data) {
    this._data = data;
  }
}

// top level…
function getCustomerData() {
  return customerData;
}
function getRawDataOfCustomers() {
  return customerData._data;
}
function setRawDataOfCustomers(arg) {
  customerData = new CustomerData(arg);
}
```

- update 하는 부분을 먼저 집중하라
  - Extract Function 으로 setter 추출

```js
// sample update…
setUsage(customerID, year, month, amount);

// top level…
function setUsage(customerID, year, month, amount) {
  getRawDataOfCustomers()[customerID].usages[year][month] = amount;
}
```

- `Move Function` 으로 setter 를 class 로 이동

```js
// sample update…
  getCustomerData().setUsage(customerID, year, month, amount);

// class CustomerData…
  setUsage(customerID, year, month, amount) {
    this._data[customerID].usages[year][month] = amount;
  }
```

- update 에 대한 처리를 잘 했는지 테스트 하는법: getter 에서 복제본을 리턴하여 테스트 깨지는지 확인
  - 방법만 제시하는것. 아직 바꾸지는 마라 밑에서 바꿀꺼다

```js
// top level…
  function getCustomerData() {return customerData;}
  function getRawDataOfCustomers()    {return customerData.rawData;}
  function setRawDataOfCustomers(arg) {customerData = new CustomerData(arg);}

// class CustomerData…
// 방법만 제시하는것. 아직 바꾸지는 마라 밑에서 바꿀꺼다
  get rawData() {
    return _.cloneDeep(this._data);
  }
```

- 그렇다면 getter 들은 어떻게 처리할까?

```js
// class CustomerData…
  usage(customerID, year, month) {
    return this._data[customerID].usages[year][month];
  }

// top level…
  function compareUsage (customerID, laterYear, month) {
    const later   = getCustomerData().usage(customerID, laterYear, month);
    const earlier = getCustomerData().usage(customerID, laterYear - 1, month);
    return {laterAmount: later, change: later - earlier};
  }
```

- client 가 data 를 바로 수정하는 것을 막기 위해서는 복제본을 return 해야 한다

```js
// class CustomerData…
  get rawData() {
    return _.cloneDeep(this._data);
  }

// top level…
  function compareUsage (customerID, laterYear, month) {
    const later   = getCustomerData().rawData[customerID].usages[laterYear][month];
    const earlier = getCustomerData().rawData[customerID].usages[laterYear - 1][month];
    return {laterAmount: later, change: later - earlier};
  }
```
