package chapter12;

class PullUpConstructorBody {
    // Ex1
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

    // Ex2
    static class Employee2 {
        String _name;
        String _car;

        Employee2(String name) {
            this._name = name;
        }

        boolean isPrivileged() {
            throw new UnsupportedOperationException("Not implemented");
        }

        void assignCar() {
            this._car = "Luxury Car";
        }

        String car() {
            return this._car;
        }
    }

    static class Manager extends Employee2 {
        int _grade;

        Manager(String name, int grade) {
            super(name);
            this._grade = grade;
            finishConstruction();
        }

        private void finishConstruction() {
            if (this.isPrivileged())
                this.assignCar(); // every subclass does this
        }

        boolean isPrivileged() {
            return this._grade > 4;
        }
    }
}
