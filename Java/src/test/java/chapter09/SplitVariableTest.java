package chapter09;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import static chapter09.SplitVariable.*;

class SplitVariableTest {
    @Test
    void client1() {
        double actual = distanceTravelled(new Scenario(10, 5, 2, 20), 5);
        assertThat(actual).isEqualTo(43.0);

    }
}
