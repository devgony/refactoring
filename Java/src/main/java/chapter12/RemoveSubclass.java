package chapter12;

class RemoveSubclass {
    static class Person {
        String _name;

        Person(String name) {
            this._name = name;
        }

        String name() {
            return this._name;
        }

        String genderCode() {
            return "X";
        }
        // snip
    }

    static class Male extends Person {
        Male(String name) {
            super(name);
        }

        String genderCode() {
            return "M";
        }
    }

    static class Female extends Person {
        Female(String name) {
            super(name);
        }

        String genderCode() {
            return "F";
        }
    }
}
