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

    static class ChargeCalculator {
        Customer _customer;
        int _usage;
        Provider _provider;

        ChargeCalculator(Customer customer, int usage, Provider provider) {
            this._customer = customer;
            this._usage = usage;
            this._provider = provider;
        }

        double baseCharge() {
            return this._customer.baseRate * this._usage;
        }

        double charge() {
            return this.baseCharge() + this._provider.connectionCharge;
        }
    }

}
