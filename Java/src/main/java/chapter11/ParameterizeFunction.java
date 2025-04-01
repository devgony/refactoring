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

    static double baseCharge(int usage) {
        if (usage < 0)
            return usd(0);
        double amount = bottomBand(usage) * 0.03 + middleBand(usage) * 0.05 + topBand(usage) * 0.07;

        return usd(amount);
    }

    static int bottomBand(int usage) {
        return Math.min(usage, 100);
    }

    static int middleBand(int usage) {
        return usage > 100 ? Math.min(usage, 200) - 100 : 0;
    }

    static int topBand(int usage) {
        return usage > 200 ? usage - 200 : 0;
    }

    private static double usd(double amount) {
        return amount * 1000;
    }
}
