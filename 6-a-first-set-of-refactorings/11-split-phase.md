> Split Phase

```js
const orderData = orderString.split(/\s+/);
const productPrice = priceList[orderData[0].split("-")[1]];
const orderPrice = parseInt(orderData[1]) * productPrice;
```

👇

```js
const orderRecord = parseOrder(order);
const orderPrice = price(orderRecord, priceList);

function parseOrder(aString) {
  const values = aString.split(/\s+/);
  return {
    productID: values[0].split("-")[1],
    quantity: parseInt(values[1]),
  };
}
function price(order, priceList) {
  return order.quantity * priceList[order.productID];
}
```

# Motivation

- 서로 다른 두 가지를 다루는 코드가 있을 때, 별도의 모듈로 분리한다
- 분할을 위해 가장 깔금한 방법은 동작을 두 개의 순차적인 단계로 나누는 것이다
  - 예시: 로직에 필요한 모델을 인풋이 반영하고 있지 않을때, 시작전에 미리 입력을 가공
- 수행 로직을 순차적인 단계로 나눈다. 각 단계마다 작업이 하는 일은 크게 다를 수 있다.
- 더 확실한 예: 컴파일러
  - 텍스트를 토큰화
  - 토큰을 파싱하여 AST 생성
  - 최적화
  - 객체코드 생성
  - 각 단계는 범위가 제한되어있고 다른 단계의 이해 없이는 진행할 수 없다
- 여러 단계로 나누면 각 단계를 별도로 리팩토링 할 수 있다
- 각 단계가 서로 다른 데이터와 함수 집합을 사용한다면 별도의 모듈로 전환

# Mechanics

- 2번재 단계를 함수로 추출
- 테스트 수행
- 중간 데이터 구조를 추출된 함수로 전달
- 테스트 수행
- 추출된 두번쨰 함수의 인자가 첫 단계에서도 쓰이는 경우, 중간 데이터 구조로 옮긴다
  - 옮기는 과정마다 테스트 수행
  - 두 번째 단계에서 인자를 사용하지 않는 경우는 각 인자의 사용 결과를 중간 데이터 구조의 필드로 추출하고 이를 채워넣는 부분을 첫 단계로 옮긴다

# Example

- 상품에 대한 주문 가격 책정 하는 코드

```js
function priceOrder(product, quantity, shippingMethod) {
  const basePrice = product.basePrice * quantity;
  const discount =
    Math.max(quantity - product.discountThreshold, 0) *
    product.basePrice *
    product.discountRate;
  const shippingPerCase =
    basePrice > shippingMethod.discountThreshold
      ? shippingMethod.discountedFee
      : shippingMethod.feePerCase;
  const shippingCost = quantity * shippingPerCase;
  const price = basePrice - discount + shippingCost;
  return price;
}
```

- 첫 단계: 제품 정보를 사용하여 제품 가격 계산
- 두 번째 단계: 배송 정보를 사용하여 배송비 결정
  - 이 부분을 `Extract Function` 으로 추출
  - 두 번째 단계에서 필요한 모든 데이터를 개별 인자로 전달. 나중에 줄일 것임.

```js
function priceOrder(product, quantity, shippingMethod) {
  const basePrice = product.basePrice * quantity;
  const discount =
    Math.max(quantity - product.discountThreshold, 0) *
    product.basePrice *
    product.discountRate;
  const price = applyShipping(basePrice, shippingMethod, quantity, discount);
  return price;
}
function applyShipping(basePrice, shippingMethod, quantity, discount) {
  const shippingPerCase =
    basePrice > shippingMethod.discountThreshold
      ? shippingMethod.discountedFee
      : shippingMethod.feePerCase;
  const shippingCost = quantity * shippingPerCase;
  const price = basePrice - discount + shippingCost;
  return price;
}
```

- 중간 데이터 구조 생성

```diff
function priceOrder(product, quantity, shippingMethod) {
  const basePrice = product.basePrice * quantity;
  const discount =
    Math.max(quantity - product.discountThreshold, 0) *
    product.basePrice *
    product.discountRate;
+ const priceData = {};
  const price = applyShipping(
+   priceData,
    basePrice,
    shippingMethod,
    quantity,
    discount,
  );
  return price;
}

function applyShipping(
+ priceData,
  basePrice,
  shippingMethod,
  quantity,
  discount,
) {
  const shippingPerCase =
    basePrice > shippingMethod.discountThreshold
      ? shippingMethod.discountedFee
      : shippingMethod.feePerCase;
  const shippingCost = quantity * shippingPerCase;
  const price = basePrice - discount + shippingCost;
  return price;
}
```

- basePrice 의 경우 첫 단계에서 사용되는 데이터이므로 중간 데이터 구조로 옮긴다

```diff
function priceOrder(product, quantity, shippingMethod) {
  const basePrice = product.basePrice * quantity;
  const discount =
    Math.max(quantity - product.discountThreshold, 0) *
    product.basePrice *
    product.discountRate;
- const priceData = {  };
+ const priceData = { basePrice: basePrice };
  const price = applyShipping(
    priceData,
-   basePrice,
    shippingMethod,
    quantity,
    discount,
  );
  return price;
}

function applyShipping(
  priceData,
- basePrice,
  shippingMethod,
  quantity,
  discount,
) {
  const shippingPerCase =
-   basePrice > shippingMethod.discountThreshold
+   priceData.basePrice > shippingMethod.discountThreshold
      ? shippingMethod.discountedFee
      : shippingMethod.feePerCase;
  const shippingCost = quantity * shippingPerCase;
- const price = basePrice - discount + shippingCost;
+ const price = priceData.basePrice - discount + shippingCost;
  return price;
}
```

- shippingMethod 의 경우 첫 단계에서 사용되지 않으므로 인자로 그대로 둔다

- quantity 는 첫 단계에서 사용되긴 하지만 그로인해 생성된건 아니므로 인자로 그대로 둬도 되지만, 저자는 옮길 수 있는건 최대한 옮기고 싶어한다

```diff
function priceOrder(product, quantity, shippingMethod) {
  const basePrice = product.basePrice * quantity;
  const discount =
    Math.max(quantity - product.discountThreshold, 0) *
    product.basePrice *
    product.discountRate;
  const priceData = { basePrice: basePrice,
+       quantity: quantity
    };
  const price = applyShipping(priceData, shippingMethod,
-   quantity,
    discount);
  return price;
}

function applyShipping(priceData, shippingMethod, quantity, discount) {
  const shippingPerCase =
    priceData.basePrice > shippingMethod.discountThreshold
      ? shippingMethod.discountedFee
      : shippingMethod.feePerCase;
  const shippingCost =
-   priceData.quantity
+   priceData.quantity
    * shippingPerCase;
  const price = priceData.basePrice - discount + shippingCost;
  return price;
}
```

- discount 역시 동일하므로 옮긴다

```diff
function priceOrder(product, quantity, shippingMethod) {
  const basePrice = product.basePrice * quantity;
  const discount =
    Math.max(quantity - product.discountThreshold, 0) *
    product.basePrice *
    product.discountRate;
  const priceData = {
    basePrice: basePrice,
    quantity: quantity,
    discount: discount,
  };
  const price = applyShipping(priceData, shippingMethod,
-   discount
    );
  return price;
}
function applyShipping(priceData, shippingMethod
-   , discount
) {
  const shippingPerCase =
    priceData.basePrice > shippingMethod.discountThreshold
      ? shippingMethod.discountedFee
      : shippingMethod.feePerCase;
  const shippingCost = priceData.quantity * shippingPerCase;
- const price = priceData.basePrice - discount + shippingCost;
+ const price = priceData.basePrice - priceData.discount + shippingCost;
  return price;
}
```

- 중간 데이터 구조가 모두 채워 졌으므로 채우는 부분을 함수로 추출한다

```diff
  function priceOrder(product, quantity, shippingMethod) {
+   const priceData = calculatePricingData(product, quantity);
    const price =  applyShipping(priceData, shippingMethod);
    return price;
  }
+ function calculatePricingData(product, quantity) {
+   const basePrice = product.basePrice * quantity;
+   const discount = Math.max(quantity - product.discountThreshold, 0)
+           * product.basePrice * product.discountRate;
+   return {basePrice: basePrice, quantity: quantity, discount:discount};
+ }
  function applyShipping(priceData, shippingMethod) {
    const shippingPerCase = (priceData.basePrice > shippingMethod.discountThreshold)
            ? shippingMethod.discountedFee : shippingMethod.feePerCase;
    const shippingCost = priceData.quantity * shippingPerCase;
    const price =  priceData.basePrice - priceData.discount + shippingCost;
    return price;
  }
```

- 마지막 변수는 inline 으로 바로 return 한다

```diff
  function priceOrder(product, quantity, shippingMethod) {
    const priceData = calculatePricingData(product, quantity);
+   return applyShipping(priceData, shippingMethod);
  }

  function calculatePricingData(product, quantity) {
    const basePrice = product.basePrice * quantity;
    const discount = Math.max(quantity - product.discountThreshold, 0)
            * product.basePrice * product.discountRate;
    return {basePrice: basePrice, quantity: quantity, discount:discount};
  }
  function applyShipping(priceData, shippingMethod) {
    const shippingPerCase = (priceData.basePrice > shippingMethod.discountThreshold)
            ? shippingMethod.discountedFee : shippingMethod.feePerCase;
    const shippingCost = priceData.quantity * shippingPerCase;
+   return priceData.basePrice - priceData.discount + shippingCost;
  }
```

## Example: Splitting a Command-Line Program (Java)

- json 에 있는 주문 개수를 세는 프로그램

```java
public static void main(String[] args) {
    try {
        if (args.length == 0) throw new RuntimeException("must supply a filename");
        String filename = args[args.length - 1];
        File input = Paths.get(filename).toFile();
        ObjectMapper mapper = new ObjectMapper();
        Order[] orders = mapper.readValue(input, Order[].class);
        if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
        System.out.println(Stream.of(orders).filter(o -> "ready".equals(o.status)).count());
        else
        System.out.println(orders.length);
    } catch (Exception e) {
        System.err.println(e);
        System.exit(1);
    }
}
```

- ready 만 필터 하려면 -r 옵션을 사용한다

- 첫 단계: 인자를 구문 분석
- 두번쨰 단계: 파일을 읽어서 Order 배열로 변환

- 테스트를 쉽게 하기 위해 메인 프로세스를 `Exatract Function`

```java
public static void main(String[] args) {
    try {
      run(args);
    } catch (Exception e) {
      System.err.println(e);
      System.exit(1);
    }
  }

  static void run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    String filename = args[args.length - 1];
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
      System.out.println(Stream.of(orders).filter(o -> "ready".equals(o.status)).count());
    else
      System.out.println(orders.length);
  }
```

- 테스트를 위해 run 의 결과를 stdout 출력

```java
  public static void main(String[] args) {
    try {
      System.out.println(run(args));
    } catch (Exception e) {
      System.err.println(e);
      System.exit(1);
    }
  }

  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    String filename = args[args.length - 1];
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
```

- `Humble Object` 패턴: 위 경우는 객체가 아니라 main method 이지만, 사이드 이펙트를 최소화 하기위해 가능한 많은 로직을 제거하는것을 말함

- 두번째 단계 를 자체 메서드로 추출

```java
static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    String filename = args[args.length - 1];
    return countOrders(args, filename);
  }

  private static long countOrders(String[] args, String filename) throws IOException {
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
```

- 중간 데이터 구조 생성

```java
static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine();
    String filename = args[args.length - 1];
    return countOrders(commandLine, args, filename);
  }

  private static long countOrders(CommandLine commandLine, String[] args, String filename) throws IOException {
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
  private static class CommandLine {}
```

- countOrders 의 인자를 조사: 1. args
  - 첫 단계에서 사용되긴 하지만 두번째 단계에서는 아예 참조 안하고싶다
    - 사용되는 부분을 추출한다

```diff
private static long countOrders(CommandLine commandLine, String[] args, String filename) throws IOException {
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
+   boolean onlyCountReady = Stream.of(args).anyMatch(arg -> "-r".equals(arg));
+   if (onlyCountReady)
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
```

- 중간 데이터 구조로 옮긴다

```diff
  private static long countOrders(CommandLine commandLine, String[] args, String filename) throws IOException {
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
+   commandLine.onlyCountReady = Stream.of(args).anyMatch(arg -> "-r".equals(arg));
+   if (commandLine.onlyCountReady)
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
  private static class CommandLine {
+   boolean onlyCountReady;
  }
```

- `Move Statements to Callers` 를 통해 onlyCountReady 를 run 으로 옮긴다

```diff
  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine();
    String filename = args[args.length - 1];
+   commandLine.onlyCountReady = Stream.of(args).anyMatch(arg -> "-r".equals(arg));
    return countOrders(commandLine, args, filename);
  }

  private static long countOrders(CommandLine commandLine, String[] args, String filename) throws IOException {
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
-   commandLine.onlyCountReady = Stream.of(args).anyMatch(arg -> "-r".equals(arg));
    if (commandLine.onlyCountReady)
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
  private static class CommandLine {
    boolean onlyCountReady;
  }
```

- filename 인자: CommandLine 으로 옮긴다

```diff
  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine();
+   commandLine.filename = args[args.length - 1];
    commandLine.onlyCountReady = Stream.of(args).anyMatch(arg -> "-r".equals(arg));
-   return countOrders(commandLine, filename);
+   return countOrders(commandLine, filename);
  }

- private static long countOrders(CommandLine commandLine, String filename) throws IOException {
+ private static long countOrders(CommandLine commandLine) throws IOException {
    File input = Paths.get(commandLine.filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (commandLine.onlyCountReady)
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
  private static class CommandLine {
    boolean onlyCountReady;
+   String filename;
  }
```

- 중간 구조체를 모두 채웠으므로 채우는 부분을 첫단계에서 함수로 추출

```diff
 static long run(String[] args) throws IOException {
+   CommandLine commandLine = parseCommandLine(args);
    return countOrders(commandLine);
  }

+ private static CommandLine parseCommandLine(String[] args) {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine();
    commandLine.filename = args[args.length - 1];
    commandLine.onlyCountReady = Stream.of(args).anyMatch(arg -> "-r".equals(arg));
    return commandLine;
  }

  private static long countOrders(CommandLine commandLine) throws IOException {
    File input = Paths.get(commandLine.filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (commandLine.onlyCountReady)
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
  private static class CommandLine {
    boolean onlyCountReady;
    String filename;
  }
```

- inline and rename

```diff
  static long run(String[] args) throws IOException {
+   return countOrders(parseCommandLine(args));
  }

  private static CommandLine parseCommandLine(String[] args) {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
+   CommandLine result = new CommandLine();
+   result.filename = args[args.length - 1];
+   result.onlyCountReady = Stream.of(args).anyMatch(arg -> "-r".equals(arg));
+   return result;
  }

  private static long countOrders(CommandLine commandLine) throws IOException {
    File input = Paths.get(commandLine.filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (commandLine.onlyCountReady)
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
  private static class CommandLine {
    boolean onlyCountReady;
    String filename;
  }
```

- 분리가 완벽히 되었으므로 필요한 경우에는 parseCommandLine 을 기능이 더 많은 lib 로 교체하는 등의 변화를 줄 수 있다

## Example: Using a Transformer for the First Phase (Java)

- 위의 예제에서 첫 단계를 Transformer 로 옮기는 방법

```java
  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine();
    String filename = args[args.length - 1];
    return countOrders(commandLine, args, filename);
  }

  private static long countOrders(CommandLine commandLine, String[] args, String filename) throws IOException {
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
  private static class CommandLine {}
```

- 현재 시나리오에서는 레코드 구조를 만들었기 때문에 inner class 로 public data 를 만들었다.
  - 하지만 top level 에서 더 동작이 풍부한 클래스를 만들 수도 있다

```java
// class App…
  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine(args);
    String filename = args[args.length - 1];
    return countOrders(commandLine, args, filename);
  }

  private static long countOrders(CommandLine commandLine, String[] args, String filename) throws IOException {
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }

public class CommandLine {
  String[] args;

  public CommandLine(String[] args) {
    this.args = args;
  }
}
```

- filename 의 경우 `Replace Temp with Query` 를 통해 메서드로 추출

```diff
// class App…
  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine(args);
+   return countOrders(commandLine, args, filename(args));
  }

+ private static String filename(String[] args) {
+   return args[args.length - 1];
+ }

  private static long countOrders(CommandLine commandLine, String[] args, String filename) throws IOException {
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }

```

- `Move Function`

```diff
# class App…
  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine(args);
+   return countOrders(commandLine, args, commandLine.filename());
  }

  private static long countOrders(CommandLine commandLine, String[] args, String filename) throws IOException {
    File input = Paths.get(filename).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }

# class CommandLine…
  String[] args;

  public CommandLine(String[] args) {
    this.args = args;
  }
+ String filename() {
+   return args[args.length - 1];
+ }
```

- `Change Function Decalaraion` 을 통해 filename 을 인자로 받지 않도록 변경

```diff
# class App…
  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine(args);
+   return countOrders(commandLine, args, commandLine.filename..);
  }

+ private static long countOrders(CommandLine commandLine, String[] args, String filename) throws IOException {
+   File input = Paths.get(commandLine.filename()).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }
```

- arg 를 제거하기 위해 condition 을 먼저 추출

```diff
# class App…
  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine(args);
    return countOrders(commandLine, args);
  }

  private static long countOrders(CommandLine commandLine, String[] args) throws IOException {
    File input = Paths.get(commandLine.filename()).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
+   if (onlyCountReady(args))
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }

+ private static boolean onlyCountReady(String[] args) {
+   return Stream.of(args).anyMatch(arg -> "-r".equals(arg));
+ }
```

- onlyCountReady 를 CommandLine 으로 옮기고 args 인자에서 제거

```diff
# class App…
  static long run(String[] args) throws IOException {
    if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine(args);
+   return countOrders(commandLine, args);
  }

+ private static long countOrders(CommandLine commandLine, String[] args) throws IOException {
    File input = Paths.get(commandLine.filename()).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
+   if (commandLine.onlyCountReady())
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }

# class CommandLine…
  String[] args;

  public CommandLine(String[] args) {
    this.args = args;
  }
  String filename() {
    return args[args.length - 1];
  }
+ boolean onlyCountReady() {
+   return Stream.of(args).anyMatch(arg -> "-r".equals(arg));
+ }
```

- empty cmd line validation 을 CommandLine 으로 옮긴다

```diff
# class App…
  static long run(String[] args) throws IOException {
-   if (args.length == 0) throw new RuntimeException("must supply a filename");
    CommandLine commandLine = new CommandLine(args);
    return countOrders(commandLine);
  }

  private static long countOrders(CommandLine commandLine) throws IOException {
    File input = Paths.get(commandLine.filename()).toFile();
    ObjectMapper mapper = new ObjectMapper();
    Order[] orders = mapper.readValue(input, Order[].class);
    if (commandLine.onlyCountReady())
      return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
    else
      return orders.length;
  }

# class CommandLine…
  String[] args;

  public CommandLine(String[] args) {
    this.args = args;
+   if (args.length == 0) throw new RuntimeException("must supply a filename");
  }
  String filename() {
+   return args[args.length - 1];
  }
  boolean onlyCountReady() {
    return Stream.of(args).anyMatch(arg -> "-r".equals(arg));
  }
```

- dumb data structure 이긴 하지만 순차적인 두 단계를 위한 통신 처럼 좁은 범위에서 사용되는 것은 괜찮다
- object 대신 trasnform 방식을 쓰는 것도 충분히 합리적이며, 핵심은 두 단계를 명확하게 구분하는 것
