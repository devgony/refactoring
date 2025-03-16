package chapter09;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ReplaceMagicLiteralTest {
    @Test
    void client1() {
        double actual = ReplaceMagicLiteral.potentialEnergy(10, 5);
        assertThat(actual).isEqualTo(490.50000000000006);
    }
}
