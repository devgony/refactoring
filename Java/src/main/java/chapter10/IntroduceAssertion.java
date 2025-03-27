package chapter10;

class IntroduceAssertion {
    static class Customer {
        Double discountRate;

        void setDiscountRate(Double discountRate) {
            assert (discountRate == null || discountRate >= 0);
            this.discountRate = discountRate;
        }

        double applyDiscount(double aNumber) {
            if (discountRate == null)
                return aNumber;
            else {
                return aNumber - (this.discountRate * aNumber);
            }
        }
    }

}
