package chapter04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class NoProducersTest {
    Province noProducers;

    @BeforeEach
    void setUp() {
        noProducers = new Province(Serde.noProducers());
    }

    @Test
    void shortfall() {
        assertThat(noProducers.shortfall()).isEqualTo(30);
    }

    @Test
    void profit() {
        assertThat(noProducers.profit()).isEqualTo(0);
    }
}
