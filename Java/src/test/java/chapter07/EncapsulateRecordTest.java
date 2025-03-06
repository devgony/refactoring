package chapter07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.ObjectBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static chapter07.EncapsulateRecord.getOrganization;

class EncapsulateRecordTest {
    JsonNode customerData;

    @BeforeEach
    void setUp() {
        String nestedRecord = "[{\"name\": \"martin\", \"id\": \"1\", \"usages\": {\"2016\": {\"1\": 50, \"2\": 55}, \"2015\": {\"1\": 70, \"2\": 63}}}, {\"name\": \"neal\", \"id\": \"2\", \"usages\": {\"2016\": {\"1\": 50, \"2\": 55}, \"2015\": {\"1\": 70, \"2\": 63}}}]";
        customerData = ObjectBuilder.readTree(nestedRecord);
    }

    @Test
    void testMain() throws JsonMappingException, JsonProcessingException {
        String result = "<h1>" + getOrganization().name() + "</h1>";
        assertThat(result).isEqualTo("<h1>Acme Gooseberries</h1>");
        getOrganization().name("newName");

        assertThat(getOrganization().name()).isEqualTo("newName");
    }

    @Test
    void nestedRecordUpdate() {
        int customerID = 1;
        String year = "2016";
        String month = "1";
        int amount = 10;
        ObjectNode yearNode = (ObjectNode) customerData.get(customerID).get("usages").get(year);
        yearNode.put(month, amount);
        assertThat(customerData.get(customerID).get("usages").get(year).get(month).asInt()).isEqualTo(10);
    }

    @Test
    void nestedRecordRead() {
        ObjectNode actual = compareUsage(1, "2016", "1");
        assertThat(actual.get("laterAmount").asInt()).isEqualTo(50);
        assertThat(actual.get("change").asInt()).isEqualTo(-20);
    }

    ObjectNode compareUsage(int customerID, String laterYear, String month) {
        int later = customerData.get(customerID).get("usages").get(laterYear).get(month).asInt();
        int earlierYear = Integer.parseInt(laterYear) - 1;
        int earlier = customerData.get(customerID).get("usages").get(String.valueOf(earlierYear)).get(month).asInt();
        return ObjectBuilder.mapper.createObjectNode()
                .put("laterAmount", later)
                .put("change", later - earlier);
    }
}
