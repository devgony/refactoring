package chapter12;

import java.util.ArrayList;
import java.util.List;

class ExtractSuperclass {
    static class Employee {
        String _id;
        String _name;
        double _monthlyCost;

        Employee(String name, String id, double monthlyCost) {
            this._id = id;
            this._name = name;
            this._monthlyCost = monthlyCost;
        }

        double monthlyCost() {
            return this._monthlyCost;
        }

        String name() {
            return this._name;
        }

        String id() {
            return this._id;
        }

        double annualCost() {
            return this.monthlyCost() * 12;
        }
    }

    static class Department {
        String _name;
        List<Employee> _staff;

        Department(String name, List<Employee> staff) {
            this._name = name;
            this._staff = staff;
        }

        List<Employee> staff() {
            return new ArrayList<>(this._staff);
        }

        String name() {
            return this._name;
        }

        double totalMonthlyCost() {
            return this.staff().stream()
                    .map((e) -> e.monthlyCost())
                    .reduce(0.0, (sum, cost) -> sum + cost);
        }

        int headCount() {
            return this.staff().size();
        }

        double totalAnnualCost() {
            return this.totalMonthlyCost() * 12;
        }
    }
}
