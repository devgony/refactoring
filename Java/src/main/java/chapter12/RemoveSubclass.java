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
        Person p;
        switch (aRecord.get("gender")) {
            case "M":
                p = new Male(aRecord.get("name"));
                break;
            case "F":
                p = new Female(aRecord.get("name"));
                break;
            default:
                p = new Person(aRecord.get("name"));
        }
        return p;
    }
}
