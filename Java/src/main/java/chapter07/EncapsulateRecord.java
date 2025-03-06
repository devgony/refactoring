package chapter07;

class EncapsulateRecord {
    static Organization organization = new Organization("Acme Gooseberries", "GB");

    static class Organization {
        private String _name;
        private String _country;

        Organization(String name, String country) {
            _name = name;
            _country = country;

        }

        String name() {
            return _name;
        }

        void name(String arg) {
            _name = arg;
        }

    }

    static Organization getOrganization() {
        return organization;
    }

}
