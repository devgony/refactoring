package chapter07;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

class SubstituteAlgorithmTest {
    @Test
    void testFound() {
        // String[] people = { "Don", "John", "Kent" };
        List<String> people = Arrays.asList("Don", "John", "Kent");
        String actual = SubstituteAlgorithm.foundPerson2(people);
        assertThat(actual).contains("Don");
    }
}
