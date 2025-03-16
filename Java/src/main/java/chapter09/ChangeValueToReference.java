package chapter09;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

class ChangeValueToReference {
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
