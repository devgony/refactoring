package chapter10;

class IntroduceAssertion {
    static class Customer {
        Double discountRate;

        double applyDiscount(double aNumber) {
            return (this.discountRate != null)
                    ? aNumber - (this.discountRate * aNumber)
                    : aNumber;
        }
    }

}
