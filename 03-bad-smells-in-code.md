> 3. Bad Smells in Code

- 리팩토링을 어떻게 하는지 알았다고 해서 언제 할 지/ 멈춰야 할 지 아는건 아니다
- 인간의 직관이 가장 뛰어난 지표이기 때문에, 책에서는 문제가 있다는 징후만 알려줄 것이며, 결국은 스스로 판단할 수 있어야 한다

# Mysterious Name

- function, moudle, variable, class 가 무엇인지 어떻게 사용하는지를 표현할 수 있는 일반적이고 명확한 이름을 사용해야 한다
- Renaming 은 단순히 이름을 바꾸는 것이 아니다. 좋은 이름을 짓기 힘들다면 디자인에 문제가 있는 신호인 경우가 많다. 이름을 짓다가 코드를 먼저 단순하게 리팩토링 하는 경우도 많다.

## 잘못된 예시 – 모호한 이름 사용

```java
public class MysteriousNameBad {
    public static void main(String[] args) {
        int d = calculate(2, 2024);
        System.out.println("Days in month: " + d);
    }

    public static int calculate(int m, int y) {
        if (m == 2) {
            if ((y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)) {
                return 29;
            } else {
                return 28;
            }
        }
        if (m == 4 || m == 6 || m == 9 || m == 11) {
            return 30;
        }
        return 31;
    }
}
```

👇

## 리팩토링 한 결과 – 명확한 이름 사용

```java
public class MysteriousNameGood {
    public static void main(String[] args) {
        int daysInMonth = getDaysInMonth(2, 2024);
        System.out.println("Days in month: " + daysInMonth);
    }

    public static int getDaysInMonth(int month, int year) {
        if (isFebruary(month)) {
            return isLeapYear(year) ? 29 : 28;
        }
        return isThirtyDayMonth(month) ? 30 : 31;
    }

    private static boolean isFebruary(int month) {
        return month == 2;
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static boolean isThirtyDayMonth(int month) {
        return month == 4 || month == 6 || month == 9 || month == 11;
    }
}
```

# Duplicated Code

- 코드 중복이 있다면 차이점이 무엇인지 비교하고, 변경할 때마다 모두를 변경해야 하는 번거로움이 생김
- 중복이 동등하다면: `Extract Function`
- 유사하지만 다른 부분이 있다면: `Slide Statements` 시도 후 `Extract Function`
- 중복 부분이 같은 공통 class 하위의 subclass 에 존재한다면 [Pull Up Method](./dictionary/pull-up-method.md)

## 잘못된 예시 – 중복된 코드 사용

```java
public class DuplicatedCodeBad {
    public void printUserDetails(User user) {
        System.out.println("User: " + user.getName());
        System.out.println("Email: " + user.getEmail());
    }

    public void printAdminDetails(Admin admin) {
        System.out.println("Admin: " + admin.getName());
        System.out.println("Email: " + admin.getEmail());
    }

    public static void main(String[] args) {
        User user = new User("John Doe", "john.doe@example.com");
        Admin admin = new Admin("Jane Smith", "jane.smith@example.com");
        DuplicatedCodeBad example = new DuplicatedCodeBad();
        example.printUserDetails(user);
        example.printAdminDetails(admin);
    }
}

class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

class Admin {
    private String name;
    private String email;

    public Admin(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
```

👇

## 리팩토링 한 결과 – 중복된 코드를 함수로 추출

```java
public class DuplicatedCodeGood {
    public void printDetails(Person person) {
        System.out.println(person.getRole() + ": " + person.getName());
        System.out.println("Email: " + person.getEmail());
    }

    public static void main(String[] args) {
        User user = new User("John Doe", "john.doe@example.com");
        Admin admin = new Admin("Jane Smith", "jane.smith@example.com");
        DuplicatedCodeGood example = new DuplicatedCodeGood();
        example.printDetails(user);
        example.printDetails(admin);
    }
}

abstract class Person {
    private String name;
    private String email;

    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public abstract String getRole();
}

class User extends Person {
    public User(String name, String email) {
        super(name, email);
    }

    @Override
    public String getRole() {
        return "User";
    }
}

class Admin extends Person {
    public Admin(String name, String email) {
        super(name, email);
    }

    @Override
    public String getRole() {
        return "Admin";
    }
}
```

# Long Function

- 설명 공유 선택 등 위임의 모든 이점은 작은 함수를 통해 지원된다
- 최신 언어에서는 in-process 호출에 대한 오버헤드를 거의 제거했다
- 유일하게 남은 오버헤드는 코드를 읽는 사람의 context-switch 인데, 이를 해결하는 것은 좋은 이름을 짓는 것이다
  - 함수의 이름이 좋으면 body 를 볼 필요가 없다
- 결국 우리는 함수를 작게 분해 하는데 더 공격적으로 임해야 한다
- 주석을 달 필요를 느낀다면 함수로 추출한다

  - 코드의 작동 방식이 아닌 코드의 의도에 따라 이름을 지정한다
  - 추출한 결과가 더 길어지어라도 추출 한다
  - 핵심은 함수의 길이가 아니라 함수가 무엇을 하는지와 어떻게 하는지의 의미적 거리이다

- 90% 의 경우에는 `Extract Fuction`
- 임시 변수가 많다면 `Replace Temp with Query`
- 추출된 함수에 너무 많은 인자를 전달하면 가독성이 더 떨어진다: [Introduce Parameter Object](./dictionary/introduce-parameter-object.md) and [Preserve Whole Object](./dictionary/preserve-whole-object.md)
- 조건문: [Decompose Conditional](./dictionary/decompose-conditional.md)
- 같은 조건에 대한 스위치문이 한 개 이상 있다면: [Replace Conditional with Polymorphism](./dictionary/replace-conditional-with-polymorphism.md)
- 반복문 역시 함수로 추출, 추출이 어려운 경우는 두 가지 이상의 일을 하고있기 때문: [Split Loop](./dictionary/split-loop.md)

## 잘못된 예시 – 긴 함수 사용

```java
public class LongFunctionBad {
    public static void main(String[] args) {
        printOrderDetails("John Doe", "123 Main St", "Widget", 5, 19.99);
    }

    public static void printOrderDetails(String customerName, String address, String productName, int quantity, double price) {
        System.out.println("Customer: " + customerName);
        System.out.println("Address: " + address);
        System.out.println("Product: " + productName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: $" + price);
        double total = quantity * price;
        System.out.println("Total: $" + total);
    }
}
```

👇

## 리팩토링 한 결과 – 작은 함수로 분리

```java
public class LongFunctionGood {
    public static void main(String[] args) {
        printOrderDetails("John Doe", "123 Main St", "Widget", 5, 19.99);
    }

    public static void printOrderDetails(String customerName, String address, String productName, int quantity, double price) {
        printCustomerDetails(customerName, address);
        printProductDetails(productName, quantity, price);
        printTotal(quantity, price);
    }

    private static void printCustomerDetails(String customerName, String address) {
        System.out.println("Customer: " + customerName);
        System.out.println("Address: " + address);
    }

    private static void printProductDetails(String productName, int quantity, double price) {
        System.out.println("Product: " + productName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: $" + price);
    }

    private static void printTotal(int quantity, double price) {
        double total = quantity * price;
        System.out.println("Total: $" + total);
    }
}
```

# Long Parameter List

> 과거에는 global data 를 쓰는것보다는 인자로 다 넘기는게 좋다고 했다. 하지만 인자가 너무 많아지면 그것 또한 문제가 된다.

- [Replace Parameter with Query](./dictionary/replace-parameter-with-query.md): 종속성이 있는 다른 인자를 제거하고 함수 내에서 계산
- [Preserve Whole Object](./dictionary/preserve-whole-object.md): 객체 전체를 전달하고 필요한 값만 추출
- [Introduce Parameter Object](./dictionary/introduce-parameter-object.md): 항상 같이 쓰이는 인자들을 객체로 묶어 전달
- [Remove Flag Argument](./dictionary/remove-flag-argument.md): boolean 인자를 제거하고 서로다른 행동을 두 함수로 분리
- [Combine Functions into Class](./dictionary/combine-functions-into-class.md): 여러 함수들이 같은 인자를 공유한다면 클래스로 묶는다
  - 함수형의 경우 [Partially applied function](./dictionary/partialy-applied-function.md) 을 활용할 수 있다

## 잘못된 예시 – 긴 매개변수 목록 사용

```java
public class LongParameterListBad {
    public static void main(String[] args) {
        printOrderDetails("John Doe", "123 Main St", "Widget", 5, 19.99);
    }

    public static void printOrderDetails(String customerName, String address, String productName, int quantity, double price) {
        System.out.println("Customer: " + customerName);
        System.out.println("Address: " + address);
        System.out.println("Product: " + productName);
        System.out.println("Quantity: " + quantity);
        System.out.println("Price: $" + price);
        double total = quantity * price;
        System.out.println("Total: $" + total);
    }
}
```

👇

## 리팩토링 한 결과 – 매개변수 객체로 묶기

```java
public class Order {
    private String customerName;
    private String address;
    private String productName;
    private int quantity;
    private double price;

    public Order(String customerName, String address, String productName, int quantity, double price) {
        this.customerName = customerName;
        this.address = address;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getAddress() {
        return address;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public double getTotal() {
        return quantity * price;
    }
}
```

```java
public class LongParameterListGood {
    public static void main(String[] args) {
        Order order = new Order("John Doe", "123 Main St", "Widget", 5, 19.99);
        printOrderDetails(order);
    }

    public static void printOrderDetails(Order order) {
        System.out.println("Customer: " + order.getCustomerName());
        System.out.println("Address: " + order.getAddress());
        System.out.println("Product: " + order.getProductName());
        System.out.println("Quantity: " + order.getQuantity());
        System.out.println("Price: $" + order.getPrice());
        System.out.println("Total: $" + order.getTotal());
    }
}
```

# Global Data

- 전역 변수는 어느 곳에서나 변경될 수 있어 버그가 어디서 발생했는지 찾기 어렵다
- 전역 변수 뿐만 아니라 클래스 변수, 싱글톤 역시 문제가 될 수 있다
- [Encapsulate Variable](./dictionary/encapsulate-variable.md): 변수를 감싸고, 변수를 변경하는 함수를 만들어 사용
  - 함수로 감싸져 있다면 어디에서 수정되었는지 확인하고 엑세스를 제어하기 시작할 수 있다
  - 감싼 함수를 해당 모듈의 코드만 볼 수 있는 클래스나 모듈 내로 이동하여 가능하다면 최대한 범위를 제한하는 것이 좋다
- 작은 global data 라도 캡슐화 해서 보관하는 건이 변화에 대처할 핵심이다

## 잘못된 예시 – 전역 변수를 직접 노출

```java
public class GlobalDataBad {
    // 전역 변수로 공개되어 있어 어디서든 변경 가능
    public static int counter = 0;

    public static void main(String[] args) {
        // 여러 곳에서 직접 접근하여 변경될 수 있음.
        GlobalDataBad.counter++;
        System.out.println("Counter: " + GlobalDataBad.counter);
    }
}
```

👇

## 리팩토링 한 결과 – 캡슐화된 변수 사용

```java
public class GlobalDataGood {
    // 변수는 private으로 감싸고, 접근 및 변경은 메서드를 통해 이루어짐
    private int counter = 0;

    public int getCounter() {
        return counter;
    }

    public void incrementCounter() {
        counter++;
    }

    public static void main(String[] args) {
        GlobalDataGood data = new GlobalDataGood();
        data.incrementCounter();
        System.out.println("Counter: " + data.getCounter());
    }
}
```

# Mutable Data

- 함수형 프로그래밍에서는 데이터의 변화가 있을 경우 복제본을 리턴한다
- [Encapsulate Variable](./dictionary/encapsulate-variable.md): 모든 변경이 추적 가능한 근접 함수에 의해서만 이루어지도록 감싼다
- 변수가 변경되어 저장되어야 할 경우 `Split Variable` 로 위험한 update 를 격리
- `Slide Statements`, `Extract Function`을 최대한 활용하여 action 과 calculation 을 분리한다
- API 에서는 [Separate Query from Modifier](./dictionary/separate-query-from-modifier.md) 를 통해 side effect 를 발생시키는 코드를 최대한 호출하지 않도록 한다
- 최대한 빨리 `Remove Setting Method` 를 활용하여 불필요한 setter 를 제거하면 변수의 scope 를 줄일 수 있다
- 모든 곳에서 사용할 수 있는 mutable data 는 불필요하다. [Replace Derived Variable with Query](./dictionary/replace-derived-variable-with-query.md) 로 제거 하자

- 변수의 scope 이 넓지 않으면 mutable data 자체가 큰 문제가 되지는 않는다. scope 이 증가하면서 위험성도 함께 증가
- [Combine Functions into Class](./dictionary/combine-functions-into-class.md), [Combine Functions into Transform](./dictionary/combine-functions-into-transform.md): update 에 필요한 범위 제한
- [Change Reference to Value](./dictionary/change-reference-to-value.md): 변수가 내부 구조를 가진 data 를 포함하는 경우, 일부 수정보다 전체 구조를 교체하는게 낫다

## 잘못된 예시 – 내부 mutable 데이터를 직접 노출

```java
import java.util.ArrayList;
import java.util.List;

public class MutableDataBad {
    private List<String> names = new ArrayList<>();

    public MutableDataBad() {
        names.add("Alice");
        names.add("Bob");
    }

    // 내부 리스트를 그대로 반환하여 외부에서 수정 가능함
    public List<String> getNames() {
        return names;
    }

    public static void main(String[] args) {
        MutableDataBad data = new MutableDataBad();
        // 외부에서 내부 상태 변경
        data.getNames().add("Charlie");
        System.out.println(data.getNames());
    }
}
```

👇

## 리팩토링 한 결과 – 내부 상태를 캡슐화하여 변경 불가능하게 제공

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MutableDataGood {
    private List<String> names = new ArrayList<>();

    public MutableDataGood() {
        names.add("Alice");
        names.add("Bob");
    }

    // 내부 리스트의 복사본을 반환하거나, 읽기 전용 뷰를 반환
    public List<String> getNames() {
        return Collections.unmodifiableList(new ArrayList<>(names));
    }

    public void addName(String name) {
        names.add(name);
    }

    public static void main(String[] args) {
        MutableDataGood data = new MutableDataGood();
        // 외부에서 리스트 수정 시도: UnsupportedOperationException 발생
        // data.getNames().add("Charlie");

        // 내부 메서드를 통해 안전하게 수정
        data.addName("Charlie");
        System.out.println(data.getNames());
    }
}
```

# Divergent Change

- 수정 가능한 명확한 포인트를 한번에 찾기 어렵다면, 냄새가 나는 코드이다
- 발산적 변화는 여러 가지 이유로 다른 방식으로 자주 변경될 때 발생
- 두 가지 측면이 순서를 형성하는 경우 명확한 데이터 구조로 [Split Phase](./dictionary/split-phase.md)
- 더 많은 상호 관계가 있는 경우 적절한 모듈을 만들고 `Move Function` 으로 프로세스를 분리
- 모듈이 Class 인 경우 `Extract Class` 로 분리

## 잘못된 예시 – 하나의 클래스가 할인 계산, 재고 업데이트, 인보이스 발송까지 담당

```java
public class OrderProcessorBad {
    public void processOrder(Order order) {
        // 할인 계산
        double discount = 0;
        for (Item item : order.getItems()) {
            if (item.isOnSale()) {
                discount += item.getPrice() * 0.1;
            }
        }
        order.setDiscount(discount);

        // 재고 업데이트
        for (Item item : order.getItems()) {
            Inventory.decrement(item.getId(), 1);
        }

        // 인보이스 발송
        EmailService.send(order.getCustomerEmail(), "Invoice", "Your invoice details...");
    }
}
```

👇

## 리팩토링 한 결과 – 각 책임을 별도의 클래스로 분리

```java
public class OrderProcessorGood {
    private DiscountCalculator discountCalculator = new DiscountCalculator();
    private InventoryUpdater inventoryUpdater = new InventoryUpdater();
    private InvoiceSender invoiceSender = new InvoiceSender();

    public void processOrder(Order order) {
        double discount = discountCalculator.calculate(order);
        order.setDiscount(discount);
        inventoryUpdater.update(order);
        invoiceSender.send(order);
    }
}

class DiscountCalculator {
    public double calculate(Order order) {
        double discount = 0;
        for (Item item : order.getItems()) {
            if (item.isOnSale()) {
                discount += item.getPrice() * 0.1;
            }
        }
        return discount;
    }
}

class InventoryUpdater {
    public void update(Order order) {
        for (Item item : order.getItems()) {
            Inventory.decrement(item.getId(), 1);
        }
    }
}

class InvoiceSender {
    public void send(Order order) {
        EmailService.send(order.getCustomerEmail(), "Invoice", "Your invoice details...");
    }
}
```

# Shotgun Surgery

- `Move Function` 과 `Move Field` 를 통해 여러 곳에서 변경되는 코드를 한 곳으로 모아라
- 여러 함수들이 유사한 데이터들에 대해 수행되고 있으면 [Combine Functions into Class](./dictionary/combine-functions-into-class.md)
- 특정 데이터 구조를 변화시키거나 enrich 하는 함수들이면 [Combine Functions into Transform](./dictionary/combine-functions-into-transform.md)
- `Inline Function` or `Inline Class` 를 사용하면 하나로 모으기 쉽다
- 나중에는 Function 혹은 Class 가 너무 커지게 될텐데, 이는 재구성을 위한 중간 단계일 뿐, 그때가 되면 중요 부분만 다시 추출하면 된다

## 잘못된 예시 – 세금 계산 로직이 여러 클래스에 분산되어 있음

```java
public class OrderProcessorBad {
    public void processOrder(Order order) {
        // 할인 적용 후 총액 계산
        double total = order.getAmount() - order.getDiscount();
        // 각 클래스에 흩어진 세금 계산 로직
        double tax = total * 0.1;
        order.setTax(tax);
        // ... 기타 주문 처리 로직
    }
}
```

```java
public class InvoiceGeneratorBad {
    public void generateInvoice(Order order) {
        // 주문 정보로부터 다시 세금 계산 로직이 사용됨
        double total = order.getAmount() - order.getDiscount();
        double tax = total * 0.1;
        // Invoice 생성 시 tax 값을 사용
        System.out.println("Invoice Tax: " + tax);
        // ... 기타 인보이스 생성 로직
    }
}
```

👇

## 리팩토링 한 결과 – 세금 계산 로직을 TaxCalculator 클래스로 통합

```java
public class TaxCalculator {
    // 세금 계산 시 한 군데에서 로직을 변경하면 모든 곳에 적용됨
    public double calculateTax(double amount, double discount) {
        double total = amount - discount;
        return total * 0.1;
    }
}
```

```java
public class OrderProcessorGood {
    private TaxCalculator taxCalculator = new TaxCalculator();

    public void processOrder(Order order) {
        double tax = taxCalculator.calculateTax(order.getAmount(), order.getDiscount());
        order.setTax(tax);
        // ... 기타 주문 처리 로직
    }
}
```

```java
public class InvoiceGeneratorGood {
    private TaxCalculator taxCalculator = new TaxCalculator();

    public void generateInvoice(Order order) {
        double tax = taxCalculator.calculateTax(order.getAmount(), order.getDiscount());
        System.out.println("Invoice Tax: " + tax);
        // ... 기타 인보이스 생성 로직
    }
}
```

# Feature Envy

- 프로그램을 모듈화 할 떄, 내부 영역 통신은 최대화 하고 바깥 영역 과의 통신은 최소화 한다
- 모듈 내 함수가 자체모듈 보다 외부 함수나 데이터와 통신하는데 더 많은 시간을 소비하면 기술질투가 발생한다

  - 해결하기 위해서 함수가 외부 데이터와 함께 있을 수 있도록 `Move Fuction` 으로 옮겨준다
  - 함수의 일부분만 질투를 당하는 경우, `Extract Function` 으로 분리 후 `Move Function` 으로 옮겨준다

- 위의 규칙에 해당 되지 않는 Strategy, Visitor, Self Delegation 등의 패턴 도 있다
- 하지만 기본적인 원칙은 함께 변화하는 것들을 한 곳에 모으는 것이다
- 데이터와 그것을 참조하는 행동은 대부분 함께 변화하지만 그렇지 않은 예외의 경우 행동을 옮겨서 한곳에 유지한다
  Strategy 와 Vistor 패턴은 추가 지시사항을 희생하는 대신, 재 정의해야하는 소량의 행동을 격리하기 때문에 행동을 쉽게 변경하도록 한다

## 잘못된 예시 – 외부 데이터(Address)에 과도하게 의존하는 메서드가 OrderProcessor에 위치

```java
public class OrderProcessor {
    public double calculateShippingCost(Order order) {
        Address addr = order.getCustomer().getAddress();
        double cost = 5.0; // 기본 배송비
        if ("NY".equals(addr.getState())) {
            cost += 2.0;
        }
        if (addr.getZipCode().startsWith("10")) {
            cost += 3.0;
        }
        return cost;
    }
}
```

👇

## 리팩토링 한 결과 – calculateShippingCost()를 Address 클래스로 이동

```java
public class Address {
    private String state;
    private String zipCode;

    // ... existing getters/setters 및 생성자

    public double calculateShippingCost() {
        double cost = 5.0; // 기본 배송비
        if ("NY".equals(this.getState())) {
            cost += 2.0;
        }
        if (this.getZipCode().startsWith("10")) {
            cost += 3.0;
        }
        return cost;
    }
}
```

```java
public class OrderProcessor {
    public double calculateShippingCost(Order order) {
        return order.getCustomer().getAddress().calculateShippingCost();
    }
}
```

# Data Clumps

- `Extract Class` 로 데이터를 하나의 객체로 묶어준다
- [Introduce Parameter Object](./dictionary/introduce-parameter-object.md) 나 [Preserve Whole Object](./dictionary/preserve-whole-object.md) 로 메서드 시그니처를 줄인다
- 새롭게 생긴 객체의 모든 필드를 사용하지는 않더라도, 두 개 이상의 필드를 새 객체로 바꿨다면 결국에는 이득이 될 것이다

## 잘못된 예시 – 여러 데이터가 개별 변수로 전달되는 경우

```java
public class DataClumpsBad {
    public void addContact(String firstName, String lastName, String phone, String email) {
        System.out.println("Adding contact: " + firstName + " " + lastName
                + ", " + phone + ", " + email);
    }

    public static void main(String[] args) {
        DataClumpsBad app = new DataClumpsBad();
        app.addContact("John", "Doe", "123-4567", "john.doe@example.com");
    }
}
```

👇

## 리팩토링 한 결과 – 데이터 관련 필드를 하나의 객체로 병합

```java
public class ContactInfo {
    private String firstName;
    private String lastName;
    private String phone;
    private String email;

    public ContactInfo(String firstName, String lastName, String phone, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return firstName + " " + lastName + ", " + phone + ", " + email;
    }
}
```

```java
public class DataClumpsGood {
    public void addContact(ContactInfo contact) {
        System.out.println("Adding contact: " + contact);
    }

    public static void main(String[] args) {
        DataClumpsGood app = new DataClumpsGood();
        ContactInfo contact = new ContactInfo("John", "Doe", "123-4567", "john.doe@example.com");
        app.addContact(contact);
    }
}
```

# Primitive Obsession

- 화폐단위, 전화번호 등을 primitive type 이 아닌 객체로 만들어 사용해야 한다: `Replace Primitive with Object`
- primitive type 이 조건을 제어한다면 [Replace Type Code With Subclasses](./dictionary/replace-type-code-with-subclasses.md) + [Replace Conditional with Polymorphism](./dictionary/replace-conditional-with-polymorphism.md)
- Data와 함께라면 `Extract Class`, [Introduce Parameter Object](./dictionary/introduce-parameter-object.md)

## 잘못된 예시 – primitive type을 사용하여 화폐 단위를 표현

```java
public class PaymentBad {
    private double amount;
    private String currency;

    public PaymentBad(double amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public static void main(String[] args) {
        PaymentBad payment = new PaymentBad(100.0, "USD");
        System.out.println("Paying " + payment.getAmount() + " " + payment.getCurrency());
    }
}
```

👇

## 리팩토링 한 결과 – Money 클래스로 primitive obsession 해결

```java
public class Money {
    private final double amount;
    private final String currency;

    public Money(double amount, String currency) {
        // 유효성 검증 로직 추가 가능
        this.amount = amount;
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
```

```java
public class PaymentGood {
    private Money money;

    public PaymentGood(Money money) {
        this.money = money;
    }

    public Money getMoney() {
        return money;
    }

    public static void main(String[] args) {
        Money money = new Money(100.0, "USD");
        PaymentGood payment = new PaymentGood(money);
        System.out.println("Paying " + payment.getMoney());
    }
}
```

# Repeated Switches

- switch 문이 두 번 이상 반복된다면 clause 를 추가 할 때마다 모든 switch 문을 업데이트 해주어야 함: [Replace Conditional with Polymorphism](./dictionary/replace-conditional-with-polymorphism.md)

## 잘못된 예시 – 반복되는 switch 문 사용

```java
public class RepeatedSwitchesBad {
    public double calculateDiscount(String customerType) {
        switch (customerType) {
            case "REGULAR": return 0.0;
            case "VIP": return 0.1;
            case "PREMIUM": return 0.2;
            default: return 0.0;
        }
    }

    public double calculateShipping(String customerType) {
        switch (customerType) {
            case "REGULAR": return 10.0;
            case "VIP": return 5.0;
            case "PREMIUM": return 0.0;
            default: return 10.0;
        }
    }

    public static void main(String[] args) {
        RepeatedSwitchesBad processor = new RepeatedSwitchesBad();
        String customerType = "VIP";
        System.out.println("Discount: " + processor.calculateDiscount(customerType));
        System.out.println("Shipping: " + processor.calculateShipping(customerType));
    }
}
```

👇

## 리팩토링 한 결과 – 폴리머피즘으로 중복 제거

```java
public interface CustomerStrategy {
    double getDiscount();
    double getShippingCost();
}
```

```java
public class RegularCustomer implements CustomerStrategy {
    public double getDiscount() {
        return 0.0;
    }
    public double getShippingCost() {
        return 10.0;
    }
}
```

```java
public class VipCustomer implements CustomerStrategy {
    public double getDiscount() {
        return 0.1;
    }
    public double getShippingCost() {
        return 5.0;
    }
}
```

```java
public class PremiumCustomer implements CustomerStrategy {
    public double getDiscount() {
        return 0.2;
    }
    public double getShippingCost() {
        return 0.0;
    }
}
```

```java
public class OrderProcessor {
    public void processOrder(CustomerStrategy customer) {
        System.out.println("Discount: " + customer.getDiscount());
        System.out.println("Shipping: " + customer.getShippingCost());
    }

    public static void main(String[] args) {
        OrderProcessor processor = new OrderProcessor();
        // 예시로 VIP 고객 사용 (클라이언트 로직에 따라 적절한 인스턴스를 생성)
        CustomerStrategy customer = new VipCustomer();
        processor.processOrder(customer);
    }
}
```

# Loops

- [Replace Loop with Pipeline](./dictionary/replace-loop-with-pipeline.md): 과거에는 반복문을 처리할 방법이 없었지만 현대에는 first-class function 들이 도입되면서 반복문을 함수로 추출할 수 있게 되었다

## 잘못된 예시 – 전통적인 for-loop 사용

```java
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoopBad {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> evenSquares = new ArrayList<>();
        for (Integer num : numbers) {
            if (num % 2 == 0) {
                evenSquares.add(num * num);
            }
        }
        System.out.println(evenSquares);
    }
}
```

👇

## 리팩토링 한 결과 – 스트림 파이프라인 사용

```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LoopGood {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> evenSquares = numbers.stream()
                                             .filter(num -> num % 2 == 0)
                                             .map(num -> num * num)
                                             .collect(Collectors.toList());
        System.out.println(evenSquares);
    }
}
```

# Lazy Element

- 별도의 구조가 불필요한 경우도 있다
- 본질적으로 단순한 함수/클래스 이거나, 로직이 복잡해질 것으로 예상했으나 실제로 그렇지 않은경우, 한 때는 제 역할을 했지만 리팩토링으로 규모가 축소된 경우에는 과감하게 없앨수도 있어야한다.
- `Inline Function`, `Inline Class`, [Callapse Hierarchy](./dictionary/collapse-hierarchy.md)

## 잘못된 예시 – Lazy Element (불필요한 별도 클래스 사용)

```java
public class LazyElementBad {
    // 단순 문자열 반환 기능만 존재하는 클래스 (과도한 분리)
    public String getGreeting() {
        return "Hello, world!";
    }

    public static void main(String[] args) {
        LazyElementBad greeter = new LazyElementBad();
        System.out.println(greeter.getGreeting());
    }
}
```

👇

## 리팩토링 한 결과 – 인라인 처리하여 불필요한 구조 제거

```java
public class LazyElementGood {
    public static void main(String[] args) {
        String greeting = "Hello, world!";
        System.out.println(greeting);
    }
}
```

# Speculative Generality

- 미래에 사용할 것이라고 추측하는 코드는 불필요하다
- abstract classs 가 하는 일이 없다면 [Collapse Hierarchy](./dictionary/collapse-hierarchy.md)
- 불필요한 위임을 제거: `Inline Function`, `Inline Class`
- 사용되지 않는 인자 제거: `Change Function Declaration`
- 유일한 사용처가 테스트 케이스라면 냄새가 나는것이다: `Remove Dead Code`

## 잘못된 예시 – 불필요한 추상 클래스 도입

```java
public abstract class Notification {
    // 미래 확장을 위해 추상으로 선언했지만 현재는 한 가지 역할만 함
    public abstract void notifyUser(String message);
}

public class EmailNotification extends Notification {
    @Override
    public void notifyUser(String message) {
        System.out.println("Sending email: " + message);
    }
}

public class NotificationServiceBad {
    public static void main(String[] args) {
        Notification notification = new EmailNotification();
        notification.notifyUser("Hello, world!");
    }
}
```

👇

## 리팩토링 한 결과 – 불필요한 계층 제거 및 인라인 처리

```java
public class NotificationServiceGood {
    public void notifyUser(String message) {
        System.out.println("Sending email: " + message);
    }

    public static void main(String[] args) {
        NotificationServiceGood service = new NotificationServiceGood();
        service.notifyUser("Hello, world!");
    }
}
```

# Temporary Field

- 특정 상황에서만 사용되는 필드는 이해하기 어렵다
- `Extract Class` 로 분리, 관련된 모든 코드를 `Move Function`
- [Introduce Sepacial Case](./dictionary/introduce-sepecial-case.md) 로 대체 클래스를 만들어 조건부 코드 제거 가능

## 잘못된 예시 – 특정 상황에서만 사용되는 임시 필드

```java
public class TemporaryFieldBad {
    private String tempData; // 특정 상황에서만 사용되는 임시 필드

    public void process(boolean condition) {
        if (condition) {
            tempData = "Temporary Data";
            System.out.println(tempData);
        }
    }

    public static void main(String[] args) {
        TemporaryFieldBad example = new TemporaryFieldBad();
        example.process(true);
    }
}
```

👇

## 리팩토링 한 결과 – 임시 필드를 별도의 클래스로 추출

```java
public class TemporaryFieldGood {
    public void process(boolean condition) {
        if (condition) {
            TempData tempData = new TempData("Temporary Data");
            tempData.print();
        }
    }

    public static void main(String[] args) {
        TemporaryFieldGood example = new TemporaryFieldGood();
        example.process(true);
    }
}

class TempData {
    private String data;

    public TempData(String data) {
        this.data = data;
    }

    public void print() {
        System.out.println(data);
    }
}
```

# Message Chains

- [Hide Delegate](./dictionary/hide-delegate.md): Middle man 역할의 함수를 만들어 위임으로 인한 chaining 을 숨긴다
  - 연속된 method chaining 이 발생하는 경우 chain 의 어느 부분에서든 수행할 수 있지만 그렇게 되면 모든 중간 객체가 middle man 으로 남게 된다.
- 객체가 어떤 용도로 사용되는지 확인하고, `Exatract Function` 으로 해당 객체를 사용하는 코드의 일부를 가져온 후, `Move Function` 으로 체인 아래에 밀어 넣는다.
- 체인에 있는 객체 중 하나의 클라이언트가 나머지 부분을 탐색하려는 경우, 이를 수행하는 메서드를 추가한다

## 잘못된 예시 – 연속된 메서드 체이닝 사용

```java
public class MessageChainsBad {
    public static void main(String[] args) {
        Order order = new Order();
        String customerCity = order.getCustomer().getAddress().getCity();
        System.out.println("Customer city: " + customerCity);
    }
}

class Order {
    private Customer customer = new Customer();

    public Customer getCustomer() {
        return customer;
    }
}

class Customer {
    private Address address = new Address();

    public Address getAddress() {
        return address;
    }
}

class Address {
    private String city = "New York";

    public String getCity() {
        return city;
    }
}
```

👇

## 리팩토링 한 결과 – 위임 메서드를 추가하여 체이닝 숨기기

```java
public class MessageChainsGood {
    public static void main(String[] args) {
        Order order = new Order();
        String customerCity = order.getCustomerCity();
        System.out.println("Customer city: " + customerCity);
    }
}

class Order {
    private Customer customer = new Customer();

    public String getCustomerCity() {
        return customer.getCity();
    }
}

class Customer {
    private Address address = new Address();

    public String getCity() {
        return address.getCity();
    }
}

class Address {
    private String city = "New York";

    public String getCity() {
        return city;
    }
}
```

# Middle Man

- 너무 많은 위임이 발생하는 경우 추적이 어려우므로: [Remove Middle Man](./dictionary/remove-middle-man.md)
- 일부 메서드만 위임하는 경우 `Inline Function` 으로 중간 객체를 제거한다
- 추가 로직이 있는 경우 [Replace Superclass with Delegate](./dictionary/replace-superclass-with-delegate.md) 혹은 [Replace Subclass with Delegate](./dictionary/replace-subclass-with-delegate.md) 를 통해 middle man 을 실제 Object 로 변경한다

## 잘못된 예시 – 너무 많은 위임 발생

```java
public class MiddleManBad {
    public static void main(String[] args) {
        Order order = new Order();
        String customerName = order.getCustomer().getName();
        System.out.println("Customer name: " + customerName);
    }
}

class Order {
    private Customer customer = new Customer();

    public Customer getCustomer() {
        return customer;
    }
}

class Customer {
    private String name = "John Doe";

    public String getName() {
        return name;
    }
}
```

👇

## 리팩토링 한 결과 – 중간 객체 제거

```java
public class MiddleManGood {
    public static void main(String[] args) {
        Order order = new Order();
        String customerName = order.getCustomerName();
        System.out.println("Customer name: " + customerName);
    }
}

class Order {
    private Customer customer = new Customer();

    public String getCustomerName() {
        return customer.getName();
    }
}

class Customer {
    private String name = "John Doe";

    public String getName() {
        return name;
    }
}
```

# Insider Trading

- 모듈 사이의 데이터 교환은 최소화 해야 한다
- 데이터 교환이 많은 모듈은 `Move Function`, `Move Field` 를 통해 분리해서 데이터 교환을 최소화 한다
- 모듈이 공통의 관심사를 가지고 있다면 세 번째 모듈을 만들어서 공통 관심사를 분리하거나, [Hide Delegate](./dictionary/hide-delegate.md) 를 통해 다른 모듈이 중개자 역할을 하도록 한다
- 상속은 담합으로 이어질 수 있으므로 [Replace Subclass with Delegate](./dictionary/replace-subclass-with-delegate.md) 혹은 [Replace Superclass with Delegate](./dictionary/replace-superclass-with-delegate.md) 를 통해 상속을 제거한다

## 잘못된 예시 – 모듈 간 데이터 교환이 많은 경우

```java
public class InsiderTradingBad {
    public static void main(String[] args) {
        Order order = new Order();
        Customer customer = order.getCustomer();
        Address address = customer.getAddress();
        String city = address.getCity();
        System.out.println("Customer city: " + city);
    }
}

class Order {
    private Customer customer = new Customer();

    public Customer getCustomer() {
        return customer;
    }
}

class Customer {
    private Address address = new Address();

    public Address getAddress() {
        return address;
    }
}

class Address {
    private String city = "New York";

    public String getCity() {
        return city;
    }
}
```

👇

## 리팩토링 한 결과 – 데이터 교환을 최소화

```java
public class InsiderTradingGood {
    public static void main(String[] args) {
        Order order = new Order();
        String city = order.getCustomerCity();
        System.out.println("Customer city: " + city);
    }
}

class Order {
    private Customer customer = new Customer();

    public String getCustomerCity() {
        return customer.getCity();
    }
}

class Customer {
    private Address address = new Address();

    public String getCity() {
        return address.getCity();
    }
}

class Address {
    private String city = "New York";

    public String getCity() {
        return city;
    }
}
```

# Large Class

- Class 가 너무 많은 것을 하려고하면 필드의 개수가 늘어난다. 필드가 많으면 코드가 중복될 수 밖에 없다
- `Exatract Class` 를 통해 여러 변수를 하나로 묶을 수 있다
- 클래스 변수의 하위 집합에 공통된 접두사나 접미어가 있으면 동일 컴포넌트에 속할 가능성이 높다
- 클래스가 모든필드를 사용하지 않는 경우도 있기 때문에 추출을 여러번 수행하여 해결할 수 있다
- 코드 가 긴 경우에는 클라이언트가 클래스의 하위 집합을 사용하는지 확인.
  - `Extract Class`, `Extract Superclass`, or [Replace Type Code with Subclasses](./dictionary/replace-type-code-with-subclasses.md) 사용하여 분리

## 잘못된 예시 – Large Class

```java
public class LargeClassBad {
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private double salary;
    private double bonus;

    public LargeClassBad(String name, String address, String phoneNumber, String email, double salary, double bonus) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.salary = salary;
        this.bonus = bonus;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

    public double calculateTotalCompensation() {
        return salary + bonus;
    }

    public static void main(String[] args) {
        LargeClassBad employee = new LargeClassBad("John Doe", "123 Main St", "555-1234", "john.doe@example.com", 50000, 5000);
        System.out.println("Total Compensation: " + employee.calculateTotalCompensation());
    }
}
```

👇

## 리팩토링 한 결과 – 클래스 분리

```java
public class Employee {
    private String name;
    private ContactInfo contactInfo;
    private Compensation compensation;

    public Employee(String name, ContactInfo contactInfo, Compensation compensation) {
        this.name = name;
        this.contactInfo = contactInfo;
        this.compensation = compensation;
    }

    public String getName() {
        return name;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public Compensation getCompensation() {
        return compensation;
    }

    public static void main(String[] args) {
        ContactInfo contactInfo = new ContactInfo("123 Main St", "555-1234", "john.doe@example.com");
        Compensation compensation = new Compensation(50000, 5000);
        Employee employee = new Employee("John Doe", contactInfo, compensation);
        System.out.println("Total Compensation: " + employee.getCompensation().calculateTotalCompensation());
    }
}
```

```java
public class ContactInfo {
    private String address;
    private String phoneNumber;
    private String email;

    public ContactInfo(String address, String phoneNumber, String email) {
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }
}
```

```java
public class Compensation {
    private double salary;
    private double bonus;

    public Compensation(double salary, double bonus) {
        this.salary = salary;
        this.bonus = bonus;
    }

    public double getSalary() {
        return salary;
    }

    public double getBonus() {
        return bonus;
    }

    public double calculateTotalCompensation() {
        return salary + bonus;
    }
}
```

# Alternative Classes with Different Interfaces

- 클래스의 장점 중 하나는 interface 만 같다면 필요할 때 언제든지 교체할 수 있다는 점이다
- `Change Function Declaration`, `Move Function` 으로 interface 를 통일
- 이 과정에서 중복이 발생하면, `Extract Superclass` 로 중복을 제거

## 잘못된 예시 – 서로 다른 인터페이스를 가진 클래스들

```java
public class EmailNotification {
    public void sendEmail(String message) {
        System.out.println("Sending email: " + message);
    }
}

public class SmsNotification {
    public void sendSms(String message) {
        System.out.println("Sending SMS: " + message);
    }
}

public class NotificationServiceBad {
    private EmailNotification emailNotification = new EmailNotification();
    private SmsNotification smsNotification = new SmsNotification();

    public void notifyByEmail(String message) {
        emailNotification.sendEmail(message);
    }

    public void notifyBySms(String message) {
        smsNotification.sendSms(message);
    }

    public static void main(String[] args) {
        NotificationServiceBad service = new NotificationServiceBad();
        service.notifyByEmail("Hello via Email");
        service.notifyBySms("Hello via SMS");
    }
}
```

👇

## 리팩토링 한 결과 – 통일된 인터페이스로 변경

```java
public interface Notification {
    void send(String message);
}
```

```java
public class EmailNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Sending email: " + message);
    }
}
```

```java
public class SmsNotification implements Notification {
    @Override
    public void send(String message) {
        System.out.println("Sending SMS: " + message);
    }
}
```

```java
public class NotificationServiceGood {
    private Notification emailNotification = new EmailNotification();
    private Notification smsNotification = new SmsNotification();

    public void notifyByEmail(String message) {
        emailNotification.send(message);
    }

    public void notifyBySms(String message) {
        smsNotification.send(message);
    }

    public static void main(String[] args) {
        NotificationServiceGood service = new NotificationServiceGood();
        service.notifyByEmail("Hello via Email");
        service.notifyBySms("Hello via SMS");
    }
}
```

# Data Class

- 데이터 클래스는 데이터를 set 하고 get 하는 field 만 가지고 있다
- public field를 가지고 있다면 즉시 [Encapsulate Record](./dictionary/encapsulate-record.md) 를 통해 getter, setter 를 만들어 사용
  - `Remove Setting Method`: immutable 한 경우 setter 를 제거하고, 생성자에서 값을 설정하도록 한다
- getter, setter 를 통해 데이터 클래스를 참조하는 부분을 찾고 해당 로직을 데이터 클래스 안으로 `Move Function`
- 옮길 수 없는 경우 `Extract Function` 으로 옮길 수 있는 함수로 추출
- Data class 는 잘못된 곳에 위치되는 경우가 많고 client 에서 Data class 로 옮기는 것만으로 많은 개선이 된다
  - 단, 별도 함수 호출에서 결과 레코드로 사용되는 경우는 예외이다
    - 이 경우의 특징은 데이터 클래스가 immutable 하다는 점이다
    - immutable field 는 캡슐화가 필요없으므로 메서드 대신 필드를 사용해도 된다

## 잘못된 예시 – getter와 setter만 있는 데이터 클래스

```java
public class DataClassBad {
    private String firstName;
    private String lastName;

    public DataClassBad(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

public class Client {
    public static void main(String[] args) {
        DataClassBad person = new DataClassBad("John", "Doe");
        String fullName = person.getFirstName() + " " + person.getLastName();
        System.out.println("Full Name: " + fullName);
    }
}
```

👇

## 리팩토링 한 결과 – 로직을 데이터 클래스 안으로 이동

```java
public class DataClassGood {
    private String firstName;
    private String lastName;

    public DataClassGood(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}

public class Client {
    public static void main(String[] args) {
        DataClassGood person = new DataClassGood("John", "Doe");
        System.out.println("Full Name: " + person.getFullName());
    }
}
```

# Refused Bequest

- subclass 가 superclass 의 일부 메서드나 데이터를 사용하지 않는 경우 전통적으로는 잘못된 설계이다
  - 새로운 sibling class 를 만들어서 쓰이지 않는 부분을 옮기고 superclass 에는 공통된 부분만 남긴다
    - [Push Down Method](./dictionary/push-down-method.md), [Push Down Field](./dictionary/push-down-field.md)
- 90% 의 경우에는 약간의 냄새가 나도 넘어갈 수 있는 수준
- subclass 가 행동을 재활용 하고있지만 superclass 의 interface 를 벗어나려하면 [Replace Subclass with Delegate](./dictionary/replace-subclass-with-delegate.md) or [Replace Superclass with Delegate](./dictionary/replace-superclass-with-delegate.md) 를 로 계층 구조를 완전히 바꾼다
  - 구현을 거부하는 것은 상관 없지만 인터페이스를 거부해서는 안된다

## 잘못된 예시 – 서브클래스가 슈퍼클래스의 일부 메서드나 데이터를 사용하지 않는 경우

```java
public class Employee {
    private String name;
    private double salary;

    public Employee(String name, double salary) {
        this.name = name;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public void work() {
        System.out.println(name + " is working.");
    }
}

public class Manager extends Employee {
    private int teamSize;

    public Manager(String name, double salary, int teamSize) {
        super(name, salary);
        this.teamSize = teamSize;
    }

    public int getTeamSize() {
        return teamSize;
    }

    // Manager는 work() 메서드를 사용하지 않음
}
```

👇

## 리팩토링 한 결과 – 공통 부분을 슈퍼클래스에 남기고, 사용되지 않는 부분을 서브클래스로 이동

```java
public class Employee {
    private String name;

    public Employee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void work() {
        System.out.println(name + " is working.");
    }
}

public class Manager extends Employee {
    private double salary;
    private int teamSize;

    public Manager(String name, double salary, int teamSize) {
        super(name);
        this.salary = salary;
        this.teamSize = teamSize;
    }

    public double getSalary() {
        return salary;
    }

    public int getTeamSize() {
        return teamSize;
    }

    // Manager는 work() 메서드를 사용하지 않음
}
```

# Comments

> When you feel the need to write a comment, first try to refactor the code so that any comment becomes superfluous.

- 코드에 대한 설명을 주석으로 달고싶으면 `Extract Function`
- 여전히 주석이 달고 싶으면 `Change Function Declaration` 으로 함수를 의미있는 이름으로 변경
- 특정 규칙에 대한 주석을 달고 싶으면 [Introduce Assertion](./dictionary/introduce-assertion.md)

## 주석을 작성해야 하는 경우

- 무엇을 해야할지 모를 때
- 무엇을 하는 코드인지를 적는게 아니라 확신이 없는 영역에 대해서만 작성
- 왜 이것을 하는지에 대해 작성

## 잘못된 예시 – 주석을 통해 코드 설명

```java
public class CommentsBad {
    public static void main(String[] args) {
        int d = daysInMonth(2, 2024); // 윤년이므로 2월은 29일
        System.out.println("Days in month: " + d);
    }

    // 주어진 월의 일수를 반환
    public static int daysInMonth(int month, int year) {
        if (month == 2) {
            // 윤년 계산
            if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
                return 29;
            } else {
                return 28;
            }
        }
        // 4, 6, 9, 11월은 30일
        if (month == 4 || month == 6 || month == 9 || month == 11) {
            return 30;
        }
        // 나머지 월은 31일
        return 31;
    }
}
```

👇

## 리팩토링 한 결과 – 주석을 제거하고 의미 있는 함수 이름 사용

```java
public class CommentsGood {
    public static void main(String[] args) {
        int days = getDaysInMonth(2, 2024);
        System.out.println("Days in month: " + days);
    }

    public static int getDaysInMonth(int month, int year) {
        if (isFebruary(month)) {
            return isLeapYear(year) ? 29 : 28;
        }
        return isThirtyDayMonth(month) ? 30 : 31;
    }

    private static boolean isFebruary(int month) {
        return month == 2;
    }

    private static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    private static boolean isThirtyDayMonth(int month) {
        return month == 4 || month == 6 || month == 9 || month == 11;
    }
}
```
