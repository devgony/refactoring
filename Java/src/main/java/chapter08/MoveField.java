package chapter08;

import java.util.Date;

class MoveField {
    static class CustomerContract {
        Date _startDate;
        double _discountRate;

        CustomerContract(Date startDate, double discountRate) {
            this._startDate = startDate;
            this._discountRate = discountRate;
        }

        double discountRate() {
            return this._discountRate;
        }

        void setDiscountRate(double discountRate) {
            this._discountRate = discountRate;
        }
    }

    static class Customer {
        String _name;
        CustomerContract _contract;

        Customer(String name, double discountRate) {
            this._name = name;
            this._contract = new CustomerContract(new Date(), discountRate);
            this._setDiscountRate(discountRate);
        }

        double discountRate() {
            return this._contract.discountRate();
        }

        void _setDiscountRate(double discountRate) {
            this._contract.setDiscountRate(discountRate);
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
        double _interestRate;

        AccountType(String nameString, double interestRate) {
            this._name = nameString;
            this._interestRate = interestRate;
        }

        double interestRate() {
            return this._interestRate;
        }
    }
}
