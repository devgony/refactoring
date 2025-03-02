package chapter06;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;

class ChangeFunctionDeclaration {
    static double circumference(double radius) {
        return 2 * Math.PI * radius;
    }

    @AllArgsConstructor
    static class Address {
        String state;
    }

    @AllArgsConstructor
    static class Customer {
        Address address;

    }

    @AllArgsConstructor
    static class Book {
        List<Customer> _reservations;

        void addReservation(Customer customer) {
            zz_addReservation(customer, false);
        }

        private void zz_addReservation(Customer customer, boolean isPriority) {
            // do something with isPriority
            this._reservations.add(customer);
        }

    }

    static boolean inNewEngland(Customer aCustomer) {
        return Arrays.asList("MA", "CT", "ME", "VT", "NH", "RI").contains(aCustomer.address.state);
    }

}
