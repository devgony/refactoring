package chapter06;

class ExtractVariable {
    static class Order {
        double quantity;
        double itemPrice;

        public Order(double quantity, double itemPrice) {
            this.quantity = quantity;
            this.itemPrice = itemPrice;
        }
    }

    static double price(Order order) {
        return order.quantity * order.itemPrice -
                Math.max(0, order.quantity - 500) * order.itemPrice * 0.05 +
                Math.min(order.quantity * order.itemPrice * 0.1, 100);

    }
}
