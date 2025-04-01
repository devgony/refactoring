package chapter11;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import static chapter11.SeparateQueryFromModifier.alertForMiscreant;
import static org.assertj.core.api.Assertions.assertThat;

class SeparateQueryFromModifierTest {
    @Test
    void client1() {
        List<String> people = Arrays.asList("Don", "John");
        String actual = alertForMiscreant(people);
        assertThat(actual).isEqualTo("Don");
    }
}
