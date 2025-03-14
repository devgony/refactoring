package chapter06;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

class IntroductionParameterObject {
    @AllArgsConstructor
    static class Data {
        int min, max;
    }

    static class NumberRange {
        Data _data;

        NumberRange(int min, int max) {
            this._data = new Data(min, max);
        }

        int min() {
            return this._data.min;
        }

        int max() {
            return this._data.max;
        }

        boolean contains(int arg) {
            return arg >= min() && arg <= max();
        }
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    static class Reading {
        int temp;
        String time;
    }

    @AllArgsConstructor
    static class Station {
        String name;
        List<Reading> readings;
    }

    @AllArgsConstructor
    static class OperatingPlan {
        int temperatureFloor;
        int temperatureCeiling;
    }

    static List<Reading> readingsOutsideRange(Station station, NumberRange range) {
        return station.readings.stream().filter(r -> !range.contains(r.temp)).collect(Collectors.toList());
    }
}
