package chapter11;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class ReturnModifiedValueTest {
    @Test
    void client1() {
        ReturnModifiedValue returnModifiedValue = new ReturnModifiedValue();
        double actual = returnModifiedValue.client1();
        assertThat(actual).isEqualTo(27.0);
    }
}
