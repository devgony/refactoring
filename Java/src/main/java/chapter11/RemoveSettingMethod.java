package chapter11;

class RemoveSettingMethod {
    static class Person {
        String _id;
        String _name;

        Person(String id) {
            this._id = id;
        }

        String name() {
            return this._name;
        }

        void name(String arg) {
            this._name = arg;
        }

        String id() {
            return this._id;
        }

        void id(String arg) {
            this._id = arg;
        }
    }
}
