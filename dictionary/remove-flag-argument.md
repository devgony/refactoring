```js
function setDimension(name, value) {
  if (name === "height") {
    this._height = value;
    return;
  }
  if (name === "width") {
    this._width = value;
    return;
  }
}
```

👇

```js
function setHeight(value) {
  this._height = value;
}
function setWidth(value) {
  this._width = value;
}
```
