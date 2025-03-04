package chapter07;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SubstituteAlgorithmTest {
    @Test
    void testFound() {
        String[] people = { "Don", "John", "Kent" };
        String actual = SubstituteAlgorithm.foundPerson(people);
        assertThat(actual).contains("Don");
    }
}
