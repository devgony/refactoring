package chapter07;

import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.ObjectBuilder;

class EncapsulateRecord {
    static Organization organization = new Organization("{\"name\": \"Acme Gooseberries\", \"country\": \"GB\"}");

    static class Organization {
        ObjectNode _data;

        Organization(String jsonString) {
            _data = ObjectBuilder.readValue(jsonString);
        }
    }

    static ObjectNode getRawDataOfOrganization() {
        return organization._data;
    }

    static Organization getOrganization() {
        return organization;
    }
}
