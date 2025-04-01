package chapter11;

import lombok.AllArgsConstructor;

class ParameterizeFunction {
    @AllArgsConstructor
    static class Person {
        Salary salary;
    }

    static class Salary {
        private double amount;

        public Salary(double amount) {
            this.amount = amount;
        }

        public Salary multiply(double multiplier) {
            return new Salary(amount * multiplier);
        }
    }

    static double tenPercentRaise(Person aPerson) {
        aPerson.salary = aPerson.salary.multiply(1.1);

        return aPerson.salary.amount;
    }

    static double fivePercentRaise(Person aPerson) {
        aPerson.salary = aPerson.salary.multiply(1.05);

        return aPerson.salary.amount;
    }

    static double raise(Person aPerson, double factor) {
        aPerson.salary = aPerson.salary.multiply(1 + factor);

        return aPerson.salary.amount;
    }
}
