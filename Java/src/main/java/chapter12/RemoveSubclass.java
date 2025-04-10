package chapter12;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class RemoveSubclass {
    static class Person {
        String _name;
        String _genderCode;

        Person(String name, String genderCode) {
            this._name = name;
            this._genderCode = genderCode == null ? "X" : genderCode;
        }

        String name() {
            return this._name;
        }

        String genderCode() {
            return this._genderCode;
        }

        // snip
        boolean isMale() {
            return "M".equals(this._genderCode);
        }
    }

    // client
    static List<Person> loadFromInput(List<Map<String, String>> data) {
        return data.stream().map(r -> createPerson(r)).collect(Collectors.toList());
    }

    private static Person createPerson(Map<String, String> aRecord) {
        switch (aRecord.get("gender")) {
            case "M":
                return new Person(aRecord.get("name"), "M");
            case "F":
                return new Person(aRecord.get("name"), "F");
            default:
                return new Person(aRecord.get("name"), "X");
        }
    }
}
