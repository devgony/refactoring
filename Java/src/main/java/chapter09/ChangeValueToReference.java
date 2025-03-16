package chapter09;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

class ChangeValueToReference {
    Map<String, Map<Integer, Customer>> _repositoryData;

    void initialize() {
        _repositoryData = new HashMap<>();
        this._repositoryData.put("customers", new HashMap<>());
    }

    Customer registerCustomer(int id) {
        if (!this._repositoryData.get("customers").containsKey(id)) {
            this._repositoryData.get("customers").put(id, new Customer(id));
        }

        return findCustomer(id);
    }

    Customer findCustomer(int id) {
        return this._repositoryData.get("customers").get(id);
    }

    @AllArgsConstructor
    static class Data {
        int number;
        int customerId;
    }

    static class Order {
        int _number;
        Customer _customer;

        Order(Data data) {
            this._number = data.number;
            this._customer = new Customer(data.customerId);
            // load other data
        }

        Customer customer() {
            return this._customer;
        }
    }

    @EqualsAndHashCode
    static class Customer {
        int _id;

        Customer(int id) {
            this._id = id;
        }

        int id() {
            return this._id;
        }
    }
}
