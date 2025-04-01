package chapter11;

import lombok.AllArgsConstructor;

class ReplaceParameterWithQuery {
    @AllArgsConstructor
    static class Order {
        int quantity;
        double itemPrice;

        double finalPrice() {
            double basePrice = this.quantity * this.itemPrice;
            return this.discountedPrice(basePrice, discountLevel());
        }

        private int discountLevel() {
            return (this.quantity > 100) ? 2 : 1;
        }

        double discountedPrice(double basePrice, int discountLevel) {
            switch (discountLevel) {
                case 1:
                    return basePrice * 0.95;
                case 2:
                    return basePrice * 0.9;
                default:
                    throw new IllegalArgumentException("invalid discountLevel");
            }
        }
    }
}
