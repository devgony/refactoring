```js
const low = aRoom.daysTempRange.low;
const high = aRoom.daysTempRange.high;
if (aPlan.withinRange(low, high))
```

👇

```js
if (aPlan.withinRange(aRoom.daysTempRange))
```
