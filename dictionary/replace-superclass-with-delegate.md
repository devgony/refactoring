![replace-superclass-with-delegate](../assets/replace-superclass-with-delegate.png)

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
