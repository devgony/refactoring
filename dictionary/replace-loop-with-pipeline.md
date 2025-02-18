```js
const names = [];
for (const i of input) {
  if (i.job === "programmer") names.push(i.name);
}
```

👇

```js
const names = input.filter((i) => i.job === "programmer").map((i) => i.name);
```
