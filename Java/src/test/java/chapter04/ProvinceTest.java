package chapter04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void zeroDemand() {
        asia.demand("0");
        assertThat(asia.shortfall()).isEqualTo(-25);
        assertThat(asia.profit()).isEqualTo(0);
    }

    @Test
    void negativeDemand() {
        asia.demand("-1");
        assertThat(asia.shortfall()).isEqualTo(-26);
        assertThat(asia.profit()).isEqualTo(-10);
    }

    @Test
    @DisplayName("Empty String Demand should get error")
    void emptyStringDemand() {
        assertThatThrownBy(() -> asia.demand("")).isInstanceOf(NumberFormatException.class);
    }
}
