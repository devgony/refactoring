> Ch9. Organizaing Data - QA

# ReplaceDerivedVariableWithQuery

- [Example One More Source](9-organizing-data/3-replace-derived-variable-with-query.md#example-more-than-one-source)
- 책에서 누락된 것인지, applyAdjustment 내부 로직을 변경 해주어야 테스트 성공함

```diff
void applyAdjustment(Adjustment anAdjustment) {
    this._adjustments.add(anAdjustment);
-   this._initialProduction += anAdjustment.amount();
+   _productionAccumulator += anAdjustment.amount();
}
```

# Inject Repository 추가 수행

- [Inject Repository](/9-organizing-data/5-change-value-to-reference.md/inject-repository)

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
