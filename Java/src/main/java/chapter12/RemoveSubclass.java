package chapter12;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    static Person createPerson(String name) {
        return new Person(name);
    }

    static Male createMale(String name) {
        return new Male(name);
    }

    static Female createFemale(String name) {
        return new Female(name);
    }

    // client
    static List<Person> loadFromInput(List<Map<String, String>> data) {
        List<Person> result = new ArrayList<>();
        data.stream().forEach((aRecord) -> {
            result.add(createPerson(aRecord));
        });

        return result;
    }

    private static Person createPerson(Map<String, String> aRecord) {
        switch (aRecord.get("gender")) {
            case "M":
                return new Male(aRecord.get("name"));
            case "F":
                return new Female(aRecord.get("name"));
            default:
                return new Person(aRecord.get("name"));
        }
    }
}
