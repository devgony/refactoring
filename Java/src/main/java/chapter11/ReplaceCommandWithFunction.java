package chapter11;

class ReplaceCommandWithFunction {
    static class Customer {
        private int baseRate;

        public Customer(int baseRate) {
            this.baseRate = baseRate;
        }

        public int getBaseRate() {
            return baseRate;
        }
    }

    static class Provider {
        private int connectionCharge;

        public Provider(int connectionCharge) {
            this.connectionCharge = connectionCharge;
        }

        public int getConnectionCharge() {
            return connectionCharge;
        }
    }

    static double charge(Customer customer, int usage, Provider provider) {
        double baseCharge = customer.baseRate * usage;

        return baseCharge + provider.connectionCharge;
    }
}
