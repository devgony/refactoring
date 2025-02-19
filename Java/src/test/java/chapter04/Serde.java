package chapter04;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Serde {
    static JsonNode deserialize(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static JsonNode sampleProvinceData() {
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

        return deserialize(jsonString);
    }

    static JsonNode noProducers() {
        String jsonString = "{"
                + "\"name\": \"No Producers\","
                + "\"producers\": [],"
                + "\"demand\": 30,"
                + "\"price\": 20"
                + "}";
        return deserialize(jsonString);
    }

}
