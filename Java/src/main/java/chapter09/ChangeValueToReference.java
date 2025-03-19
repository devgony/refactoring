package chapter09;

import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

class ChangeValueToReference {
    @AllArgsConstructor
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

    @AllArgsConstructor
    static class Data {
        int number;
        int customerId;
    }

    static class Order {
        int _number;
        Customer _customer;
        Repository _repository;

        Order(Data data, Repository repository) {
            this._number = data.number;
            this._repository = repository;
            this._customer = repository.registerCustomer(data.customerId);
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
