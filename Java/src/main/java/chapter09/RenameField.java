package chapter09;

import com.fasterxml.jackson.databind.node.ObjectNode;

class RenameField {
    static class Organization {
        String _title;
        String _country;

        Organization(ObjectNode data) {
            this._title = data.get("title").asText();
            this._country = data.get("country").asText();
        }

        String title() {
            return this._title;
        }

        void title(String aString) {
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
