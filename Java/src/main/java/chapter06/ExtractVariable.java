package chapter06;

import lombok.AllArgsConstructor;

class ExtractVariable {
    @AllArgsConstructor
    static class OrderData {
        double quantity;
        double itemPrice;
    }

    static class Order {
        OrderData _data;

        public Order(OrderData aRecord) {
            this._data = aRecord;
        }

        double quantity() {
            return this._data.quantity;
        }

        double itemPrice() {
            return this._data.itemPrice;
        }

        double price2() {
            return (this.quantity() * this.itemPrice() -
                    Math.max(0, this.quantity() - 500) * this.itemPrice() * 0.05 +
                    Math.min(this.quantity() * this.itemPrice() * 0.1, 100));
        }
    }

    static double price(OrderData orderData) {
        double basePrice = orderData.quantity * orderData.itemPrice;
        double quantityDiscount = Math.max(0, orderData.quantity - 500) * orderData.itemPrice * 0.05;
        double shipping = Math.min(basePrice * 0.1, 100);
        return basePrice -
                quantityDiscount +
                shipping;

    }
}
