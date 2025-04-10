package chapter12;

class PullUpMethod {
    static abstract class Party {
        int annualCost() {
            return this.monthlyCost() * 12;
        }

        int monthlyCost() {
            throw new UnsupportedOperationException("Not implemented");
        }
    }

    static class Employee extends Party {
        @Override
        int monthlyCost() {
            return 1000;
        }
    }

    static class Department extends Party {
        @Override
        int monthlyCost() {
            return 2000;
        }
    }

    static class NotImpl extends Party {
    }
}
