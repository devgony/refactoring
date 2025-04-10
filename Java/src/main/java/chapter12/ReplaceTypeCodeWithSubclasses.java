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
            this._type = new EmployeeType(type);
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
            this._type = new EmployeeType(arg);
        }

        String capitalizedType() {
            return this.typeString().substring(0, 1).toUpperCase() + this.typeString().substring(1).toLowerCase();
        }

        public String toString() {
            return this._name + " (" + this.capitalizedType() + ")";
        }
    }

    static class EmployeeType {
        String _value;

        EmployeeType(String aString) {
            this._value = aString;
        }

        public String toString() {
            return this._value;
        }
    }
}
