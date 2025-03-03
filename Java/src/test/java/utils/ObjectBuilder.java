package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ObjectBuilder {
    public static ObjectMapper mapper = new ObjectMapper();

    public static ObjectNode readValue(String jsonString) {
        try {
            return mapper.readValue(jsonString, ObjectNode.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode readTree(String jsonString) {
        try {
            return mapper.readTree(jsonString);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
