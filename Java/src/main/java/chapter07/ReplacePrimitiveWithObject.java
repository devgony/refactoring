package chapter07;

import lombok.AllArgsConstructor;

class ReplacePrimitiveWithObject {
    @AllArgsConstructor
    static class RawData {
        String priority;
    }

    static class Order {
        Priority _priority;

        String priorityString() {
            return this._priority.toString();
        }

        Priority priority() {
            return this._priority;
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

        public static Priority of(Priority priority) {
            return priority;
        }

        public String toString() {
            return this._value;
        }
    }

}
