> Ch9. Organizaing Data - QA

# ReplaceDerivedVariableWithQuery

- <https://github.com/devgony/refactoring/pull/17/commits/52ddc6bd290899dce12a0eeee3179d4cacfdbba3>
- 책에서 누락된 것인지, applyAdjustment 내부 로직을 변경 해주어야 테스트 성공함

```diff
void applyAdjustment(Adjustment anAdjustment) {
    this._adjustments.add(anAdjustment);
-   this._initialProduction += anAdjustment.amount();
+   _productionAccumulator += anAdjustment.amount();
}
```

# Inject Repository 추가 수행

- <https://github.com/devgony/refactoring/pull/17/commits/8d54d616ba66b37e02e4ad72c4aa72e2b06a5188>

```java
static class Repository {
    Map<String, Map<Integer, Customer>> _repositoryData;

    Repository() {
        _repositoryData = new HashMap<>();
        _repositoryData.put("customers", new HashMap<>());
    }

    Customer registerCustomer(int id) {
        if (!_repositoryData.get("customers").containsKey(id)) {
            _repositoryData.get("customers").put(id, new Customer(id));
        }

        return findCustomer(id);
    }

    Customer findCustomer(int id) {
        return _repositoryData.get("customers").get(id);
    }
}
```

```java
@Test
void client1() {
    Repository repository = new Repository();
    Order order = new Order(new Data(1, 1), repository);
    assertThat(order.customer()).isEqualTo(new Customer(1));
}
```
