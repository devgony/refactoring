package chapter07;

import java.util.Arrays;
import java.util.List;

class SubstituteAlgorithm {
    static String foundPerson(List<String> people) {
        final List<String> candidates = Arrays.asList("Don", "John", "Kent");
        return people.stream().filter(candidates::contains).findFirst().orElse("");
    }
}
