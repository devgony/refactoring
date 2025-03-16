package chapter09;

import com.fasterxml.jackson.databind.node.ObjectNode;

class RenameField {
    static class Organization {
        String _title;
        String _country;

        Organization(ObjectNode data) {
            this._title = data.get("title") != null ? data.get("title").asText() : data.get("name").asText();
            this._country = data.get("country").asText();
        }

        String name() {
            return this._title;
        }

        void name(String aString) {
            this._title = aString;
        }

        String country() {
            return this._country;
        }

        void country(String aCountryCode) {
            this._country = aCountryCode;
        }
    }

}
