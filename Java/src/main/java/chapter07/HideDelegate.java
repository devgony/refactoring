package chapter07;

class HideDelegate {
    static class Person {
        String _name;
        Department _department;

        Person(String name) {
            this._name = name;
        }

        String name() {
            return this._name;
        }

        Department department() {
            return this._department;
        }

        void department(Department arg) {
            this._department = arg;
        }

        Person manager() {
            return this.department().manager();
        }
    }

    static class Department {
        private String _chargeCode;
        private Person _manager;

        Department(String chargeCode, Person manager) {
            this._chargeCode = chargeCode;
            this._manager = manager;
        }

        String chargeCode() {
            return this._chargeCode;
        }

        void chargeCode(String arg) {
            this._chargeCode = arg;
        }

        Person manager() {
            return this._manager;
        }

        void manager(Person arg) {
            this._manager = arg;
        }
    }

}
