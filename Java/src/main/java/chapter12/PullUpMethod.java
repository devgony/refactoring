package chapter12;

class PullUpMethod {
    static abstract class Party {
        protected double monthlyCost;

        public Party(double monthlyCost) {
            this.monthlyCost = monthlyCost;
        }
    }

    static class Employee extends Party {
        public Employee(double monthlyCost) {
            super(monthlyCost);
        }

        double annualCost() {
            return this.monthlyCost * 12;
        }
    }

    static class Department extends Party {
        public Department(double monthlyCost) {
            super(monthlyCost);
        }

        double annualCost() {
            return this.monthlyCost * 12;
        }
    }
}
