package chapter07;

import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.ObjectBuilder;

class EncapsulateRecord {
    static Organization organization = new Organization("{\"name\": \"Acme Gooseberries\", \"country\": \"GB\"}");

    static class Organization {
        private ObjectNode _data;

        Organization(String jsonString) {
            _data = ObjectBuilder.readValue(jsonString);
        }

        String name() {
            return _data.get("name").asText();
        }

        void name(String arg) {
            _data.set("name", ObjectBuilder.mapper.valueToTree(arg));
        }

    }

    static Organization getOrganization() {
        return organization;
    }

}
