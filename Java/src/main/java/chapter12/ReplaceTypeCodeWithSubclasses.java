package chapter12;

class ReplaceTypeCodeWithSubclasses {
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

        public String toString() {
            return this._name + " (" + this._type + ")";
        }
    }

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
