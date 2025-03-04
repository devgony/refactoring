package chapter07;

import lombok.AllArgsConstructor;

class ReplacePrimitiveWithObject {
    @AllArgsConstructor
    static class RawData {
        String priority;
    }

    static class Order {
        Priority _priority;

        String priority() {
            return this._priority.toString();
        }

        void priority(String arg) {
            this._priority = new Priority(arg);
        }

        Order(RawData data) {
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
