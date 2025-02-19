package chapter04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class ProvinceTest {
    Province asia;

    @BeforeEach
    void setUp() {
        asia = new Province(Serde.sampleProvinceData());
    }

    @Test
    void shortfall() {
        assertThat(asia.shortfall()).isEqualTo(5);
    }

    @Test
    @DisplayName("Add Another Test using BeforeEach")
    void profit() {
        assertThat(asia.profit()).isEqualTo(230);
    }

    @Test
    @DisplayName("Modifying the Fixture")
    void changeProduction() {
        asia.producers().get(0).production("20");
        assertThat(asia.shortfall()).isEqualTo(-6);
        assertThat(asia.profit()).isEqualTo(292);
    }
}
