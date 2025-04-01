package chapter11;

import lombok.AllArgsConstructor;

class PreserveWholeObject {
    @AllArgsConstructor
    static class Room {
        Range daysTempRange;
    }

    @AllArgsConstructor
    static class Range {
        int low;
        int high;
    }

    @AllArgsConstructor
    static class HeatingPlan {
        Range _temperatureRange;

        boolean withinRange(int bottom, int top) {
            return (bottom >= this._temperatureRange.low) && (top <= this._temperatureRange.high);
        }

        boolean withinRange2(int bottom, int top) {
            return (bottom >= this._temperatureRange.low) && (top <= this._temperatureRange.high);
        }

        boolean xxNEWwithinRange(Range aNumberRange) {
            return withinRange(aNumberRange.low, aNumberRange.high);

        }
    }

}
