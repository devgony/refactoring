package chapter11;

import lombok.AllArgsConstructor;

class ReplaceParameterWithQuery {
    @AllArgsConstructor
    static class Order {
        int quantity;
        double itemPrice;

        double finalPrice() {
            double basePrice = this.quantity * this.itemPrice;
            int discountLevel;
            if (this.quantity > 100)
                discountLevel = 2;
            else
                discountLevel = 1;
            return this.discountedPrice(basePrice, discountLevel);
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
