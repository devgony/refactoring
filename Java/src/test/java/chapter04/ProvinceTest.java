package chapter04;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.assertj.core.api.Assertions.assertThat;

class ProvinceTest {
    Province asia;

    @BeforeEach
    void setUp() {
        asia = new Province(sampleProvinceData());
    }

    JsonNode sampleProvinceData() {
        String jsonString = "{"
                + "\"name\": \"Asia\","
                + "\"producers\": ["
                + "{\"name\": \"Byzantium\", \"cost\": 10, \"production\": 9},"
                + "{\"name\": \"Attalia\", \"cost\": 12, \"production\": 10},"
                + "{\"name\": \"Sinope\", \"cost\": 10, \"production\": 6}"
                + "],"
                + "\"demand\": 30,"
                + "\"price\": 20"
                + "}";

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shortfall() {
        assertThat(asia.shortfall()).isEqualTo(5);
    }

    @Test
    void profit() {
        assertThat(asia.profit()).isEqualTo(230);
    }
}
