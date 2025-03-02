package chapter06;

class InlineFunction {
    static class Driver {
        int numberOfLateDeliveries;

        public Driver(int numberOfLateDeliveries) {
            this.numberOfLateDeliveries = numberOfLateDeliveries;
        }
    }

    int rating(Driver aDriver) {
        return moreThanFiveLateDeliveries(aDriver) ? 2 : 1;
    }

    boolean moreThanFiveLateDeliveries(Driver dvr) {
        return dvr.numberOfLateDeliveries > 5;
    }

}
