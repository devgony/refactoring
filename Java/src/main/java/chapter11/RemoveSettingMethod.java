package chapter11;

class RemoveSettingMethod {
    static class Person {
        String _id;
        String _name;

        Person(String id, String name) {
            this._id = id;
            this._name = name;
        }

        String name() {
            return this._name;
        }

        String id() {
            return this._id;
        }
    }
}
