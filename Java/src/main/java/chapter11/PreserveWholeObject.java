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

        boolean withinRange2(Range tempRange) {
            int low = tempRange.low;
            int high = tempRange.high;
            boolean isWithinRange = (low >= this._temperatureRange.low) && (high <= this._temperatureRange.high);

            return isWithinRange;
        }

        boolean withinRange(Range aNumberRange) {
            return (aNumberRange.low >= this._temperatureRange.low)
                    && (aNumberRange.high <= this._temperatureRange.high);

        }
    }

}
