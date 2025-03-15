package chapter09;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;

import utils.ObjectBuilder;

class RenameFieldTest {
    @Test
    void client1() {
        String jsonStr = "{\"name\": \"Acme Gooseberries\", \"country\": \"GB\"}";
        ObjectNode organization = ObjectBuilder.readValue(jsonStr);
        assertThat(organization.get("name").asText()).isEqualTo("Acme Gooseberries");
        assertThat(organization.get("country").asText()).isEqualTo("GB");
    }
}
