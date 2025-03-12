package chapter08;

import java.util.Date;

class MoveField {
    static class CustomerContract {
        Date _startDate;

        CustomerContract(Date startDate) {
            this._startDate = startDate;
        }
    }

    static class Customer {
        String _name;
        double _discountRate;
        CustomerContract _contract;

        Customer(String name, double discountRate) {
            this._name = name;
            this._setDiscountRate(discountRate);
            this._contract = new CustomerContract(new Date());
        }

        double discountRate() {
            return this._discountRate;
        }

        void _setDiscountRate(double discountRate) {
            this._discountRate = discountRate;
        }

        void becomePreferred() {
            this._setDiscountRate(this.discountRate() + 0.03);
            // other nice things
        }

        double applyDiscount(int amount) {
            return amount - amount * this.discountRate();
        }
    }

    // Example: Moving to a Shared Object
    static class Account {
        int _number;
        AccountType _type;
        double _interestRate;

        Account(int number, AccountType type, double interestRate) {
            this._number = number;
            this._type = type;
            this._interestRate = interestRate;
        }

        double interestRate() {
            return this._interestRate;
        }
    }

    static class AccountType {
        String _name;

        AccountType(String nameString) {
            this._name = nameString;
        }
    }
}
