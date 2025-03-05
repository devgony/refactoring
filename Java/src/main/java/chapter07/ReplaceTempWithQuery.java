package chapter07;

class ReplaceTempWithQuery {
    static class Order {
        double _quantity;
        double _item;

        Order(double quantity, double item) {
            this._quantity = quantity;
            this._item = item;
        }

        double price() {
            double discountFactor = discountFactor();
            return basePrice() * discountFactor;
        }

        private double discountFactor() {
            double discountFactor = 0.98;
            if (basePrice() > 1000) {
                discountFactor -= 0.03;
            }
            return discountFactor;
        }

        private double basePrice() {
            return _quantity * _item;
        }

    }
}
