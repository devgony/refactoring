package chapter12;

class ReplaceTypeCodeWithSubclasses {
    // Ex1
    static Employee createEmployee(String name, String type) {
        switch (type) {
            case "engineer":
                return new Engineer(name);
            case "salesman":
                return new Salesman(name);
            case "manager":
                return new Manager(name);
            default:
                throw new IllegalArgumentException("Employee cannot be of type " + type);
        }
    }

    static class Employee {
        String _name;
        String _type;

        Employee(String name) {
            this._name = name;
        }

        String type() {
            throw new UnsupportedOperationException("Not implemented");
        }

        public String toString() {
            return this._name + " (" + this.type() + ")";
        }
    }

    static class Engineer extends Employee {
        Engineer(String name) {
            super(name);
        }

        @Override
        String type() {
            return "engineer";
        }
    }

    static class Salesman extends Employee {
        Salesman(String name) {
            super(name);
        }

        @Override
        String type() {
            return "salesman";
        }
    }

    static class Manager extends Employee {
        Manager(String name) {
            super(name);
        }

        @Override
        String type() {
            return "manager";
        }
    }

    // Ex2
    static class Employee2 {
        String _name;
        EmployeeType _type;

        Employee2(String name, String type) {
            this.validateType(type);
            this._name = name;
            this._type = createEmployeeType(type);
        }

        void validateType(String arg) {
            if (!arg.equals("engineer") && !arg.equals("manager") && !arg.equals("salesman")) {
                throw new IllegalArgumentException("Employee cannot be of type " + arg);
            }
        }

        String typeString() {
            return this._type.toString();
        }

        EmployeeType type() {
            return this._type;
        }

        void type(String arg) {
            this._type = createEmployeeType(arg);
        }

        String capitalizedType() {
            return this.typeString().substring(0, 1).toUpperCase() + this.typeString().substring(1).toLowerCase();
        }

        public String toString() {
            return this._name + " (" + this.capitalizedType() + ")";
        }

        static EmployeeType createEmployeeType(String type) {
            switch (type) {
                case "engineer":
                    return new Engineer2();
                case "salesman":
                    return new Salesman2();
                case "manager":
                    return new Manager2();
                default:
                    throw new IllegalArgumentException("Employee cannot be of type " + type);
            }
        }
    }

    static class EmployeeType {
    }

    static class Engineer2 extends EmployeeType {
        @Override
        public String toString() {
            return "engineer";
        }
    }

    static class Salesman2 extends EmployeeType {
        @Override
        public String toString() {
            return "salesman";
        }
    }

    static class Manager2 extends EmployeeType {
        @Override
        public String toString() {
            return "manager";
        }
    }
}
