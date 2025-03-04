package chapter07;

import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.ObjectBuilder;

class EncapsulateRecord {
    static ObjectNode organization = ObjectBuilder.readValue("{\"name\": \"Acme Gooseberries\", \"country\": \"GB\"}");

    static ObjectNode getRawDataOfOrganization() {
        return organization;
    }
}
