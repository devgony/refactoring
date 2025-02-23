package chapter04;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StringForProducersTest {
    @Test
    void emptyListHandledByJackson() {
        String data = "{"
                + "\"name\": \"String producers\","
                + "\"producers\": \"\","
                + "\"demand\": 30,"
                + "\"price\": 20"
                + "}";

        Province province = new Province(Serde.deserialize(data));
        assertThat(province.shortfall()).isEqualTo(30);

    }
}
