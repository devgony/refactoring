package chapter07;

import java.util.Arrays;
import java.util.List;

class SubstituteAlgorithm {
    static String foundPerson(String[] people) {
        for (String person : people) {
            if (person.equals("Don")) {
                return "Don";
            }
            if (person.equals("John")) {
                return "John";
            }
            if (person.equals("Kent")) {
                return "Kent";
            }
        }
        return "";
    }

    static String foundPerson2(List<String> people) {
        final List<String> candidates = Arrays.asList("Don", "John", "Kent");
        return people.stream().filter(candidates::contains).findFirst().orElse("");
    }
}
