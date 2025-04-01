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

        boolean withinRange2(int bottom, int top) {
            return (bottom >= this._temperatureRange.low) && (top <= this._temperatureRange.high);
        }

        boolean xxNEWwithinRange(Range tempRange) {
            int low = tempRange.low;
            int high = tempRange.high;
            boolean isWithinRange = this.withinRange2(low, high);

            return isWithinRange;
        }

        boolean withinRange(Range aNumberRange) {
            return (aNumberRange.low >= this._temperatureRange.low)
                    && (aNumberRange.high <= this._temperatureRange.high);

        }
    }

}
