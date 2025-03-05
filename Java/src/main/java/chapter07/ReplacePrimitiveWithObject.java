package chapter07;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        int _index() {
            return Priority.legalValues().indexOf(this._value);
        }

        static List<String> legalValues() {
            return Arrays.asList("low", "normal", "high", "rush");
        }

        public boolean equals(Priority other) {
            return this._index() == other._index();
        }

        boolean higherThan(Priority arg) {
            return this._index() > arg._index();
        }

        boolean lowerThan(Priority arg) {
            return this._index() < arg._index();
        }
    }

}
