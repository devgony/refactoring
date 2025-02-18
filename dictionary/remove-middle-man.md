```js
manager = aPerson.manager;

class Person {
  get manager() {
    return this.department.manager;
  }
}
```

👇

```js
manager = aPerson.department.manager;
```
