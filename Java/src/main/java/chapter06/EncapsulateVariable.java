package chapter06;

class EncapsulateVariable {
    static class Owner {
        private String firstName;
        private String lastName;

        static Owner _defaultOwner = new Owner("Martin", "Fowler");

        public Owner(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String arg) {
            firstName = arg;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String arg) {
            lastName = arg;
        }

        static Owner defaultOwner() {
            return new Owner(_defaultOwner.getFirstName(), _defaultOwner.getLastName());
        }

    }

    static class Spaceship {
        Owner owner;

        void setDefaultOwner(Owner arg) {
            this.owner = arg;
        }
    }

}
