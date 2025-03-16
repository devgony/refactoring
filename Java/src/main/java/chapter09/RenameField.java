package chapter09;

import com.fasterxml.jackson.databind.node.ObjectNode;

class RenameField {
    static class Organization {
        String _name;
        String _country;

        Organization(ObjectNode data) {
            this._name = data.get("name").asText();
            this._country = data.get("country").asText();
        }

        String name() {
            return this._name;
        }

        void name(String aString) {
            this._name = aString;
        }

        String country() {
            return this._country;
        }

        void country(String aCountryCode) {
            this._country = aCountryCode;
        }
    }

}
