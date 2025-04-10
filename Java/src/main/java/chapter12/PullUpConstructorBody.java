package chapter12;

class PullUpConstructorBody {
    static class Party {
        Party() {
        }
    }

    static class Employee extends Party {
        String _id;
        String _name;
        double _monthlyCost;

        Employee(String name, String id, double monthlyCost) {
            super();
            this._id = id;
            this._name = name;
            this._monthlyCost = monthlyCost;
        }
        // rest of class...
    }

    static class Department extends Party {
        String _name;
        Employee _staff;

        Department(String name, Employee staff) {
            super();
            this._name = name;
            this._staff = staff;
        }
        // rest of class...
    }

}
