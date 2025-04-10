package chapter12;

class PullUpConstructorBody {
    static class Party {
        String _name;

        Party(String name) {
            this._name = name;
        }
    }

    static class Employee extends Party {
        String _id;
        double _monthlyCost;

        Employee(String name, String id, double monthlyCost) {
            super(name);
            this._id = id;
            this._name = name;
            this._monthlyCost = monthlyCost;
        }
        // rest of class...
    }

    static class Department extends Party {
        Employee _staff;

        Department(String name, Employee staff) {
            super(name);
            this._name = name;
            this._staff = staff;
        }
        // rest of class...
    }

}
