package chapter07;

import lombok.AllArgsConstructor;

class ReplacePrimitiveWithObject {
    @AllArgsConstructor
    static class Data {
        String priority;
    }

    static class Order {
        String priority;

        String priority() {
            return this.priority;
        }

        void priority(String arg) {
            this.priority = arg;
        }

        Order(Data data) {
            priority(data.priority);
        }
    }

    static class Priority {
        private String _value;

        Priority(String value) {
            this._value = value;
        }

        public String toString() {
            return this._value;
        }
    }

}
