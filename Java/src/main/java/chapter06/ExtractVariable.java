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
            return (basePrice() - quantityDiscount() + shipping());
        }

        double basePrice() {
            return this._data.quantity * this._data.itemPrice;
        }

        double quantityDiscount() {
            return Math.max(0, this._data.quantity - 500) * this._data.itemPrice * 0.05;
        }

        double shipping() {
            return Math.min(this.basePrice() * 0.1, 100);
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
