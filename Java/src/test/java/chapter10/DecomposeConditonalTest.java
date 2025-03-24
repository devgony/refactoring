package chapter10;

import org.junit.jupiter.api.Test;
import static chapter10.DecomposeConditional.*;
import static org.assertj.core.api.Assertions.assertThat;

class DecomposeConditionalTest {
    @Test
    void cilent1() {
        Plan plan = new Plan(2021, 2022, 10, 20, 5);
        double actual = client1(new Date(2021), plan, 10);
        assertThat(actual).isEqualTo(100);
    }
}
