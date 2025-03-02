package chapter06;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;

class ChangeFunctionDeclaration {
    static double circum(double radius) {
        return circumference(radius);
    }

    private static double circumference(double radius) {
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
            this._reservations.add(customer);
        }

    }

    static boolean inNewEngland(Customer aCustomer) {
        return Arrays.asList("MA", "CT", "ME", "VT", "NH", "RI").contains(aCustomer.address.state);
    }

}
