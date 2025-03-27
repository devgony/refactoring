package chapter10;

class IntroduceAssertion {
    static class Customer {
        Double discountRate;

        double applyDiscount(double aNumber) {
            if (discountRate == null)
                return aNumber;
            else {
                assert discountRate >= 0;
                return aNumber - (this.discountRate * aNumber);
            }
        }
    }

}
