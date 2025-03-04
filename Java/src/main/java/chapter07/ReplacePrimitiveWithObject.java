package chapter07;

import lombok.AllArgsConstructor;

class ReplacePrimitiveWithObject {
    @AllArgsConstructor
    static class Data {
        String priority;
    }

    static class Order {
        String priority;

        Order(Data data) {
            this.priority = data.priority;
        }
    }
}
