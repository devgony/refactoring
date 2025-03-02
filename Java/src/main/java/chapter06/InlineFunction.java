package chapter06;

class InlineFunction {
    static class Driver {
        int numberOfLateDeliveries;

        public Driver(int numberOfLateDeliveries) {
            this.numberOfLateDeliveries = numberOfLateDeliveries;
        }
    }

    int rating(Driver aDriver) {
        return aDriver.numberOfLateDeliveries > 5 ? 2 : 1;
    }

}
