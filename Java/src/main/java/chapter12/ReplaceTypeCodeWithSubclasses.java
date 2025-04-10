package chapter12;

class ReplaceTypeCodeWithSubclasses {
    // Ex1
    static Employee createEmployee(String name, String type) {
        switch (type) {
            case "engineer":
                return new Engineer(name, type);
        }
        return new Employee(name, type);
    }

    static class Employee {
        String _name;
        String _type;

        Employee(String name, String type) {
            this.validateType(type);
            this._name = name;
            this._type = type;
        }

        void validateType(String arg) {
            if (!arg.equals("engineer") && !arg.equals("manager") && !arg.equals("salesman")) {
                throw new IllegalArgumentException("Employee cannot be of type " + arg);
            }
        }

        String type() {
            return this._type;
        }

        public String toString() {
            return this._name + " (" + this.type() + ")";
        }
    }

    static class Engineer extends Employee {
        Engineer(String name, String type) {
            super(name, type);
        }

        @Override
        String type() {
            return "engineer";
        }
    }

    // Ex2
    static class Employee2 {
        String _name;
        String _type;

        Employee2(String name, String type) {
            this.validateType(type);
            this._name = name;
            this._type = type;
        }

        void validateType(String arg) {
            if (!arg.equals("engineer") && !arg.equals("manager") && !arg.equals("salesman")) {
                throw new IllegalArgumentException("Employee cannot be of type " + arg);
            }
        }

        String type() {
            return this._type;
        }

        void type(String arg) {
            this._type = arg;
        }

        String capitalizedType() {
            return this._type.substring(0, 1).toUpperCase() + this._type.substring(1).toLowerCase();
        }

        public String toString() {
            return this._name + " (" + this.capitalizedType() + ")";
        }
    }
}
