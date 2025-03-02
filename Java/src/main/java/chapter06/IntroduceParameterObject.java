package chapter06;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

class IntroductionParameterObject {
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

    static List<Reading> readingsOutsideRange(Station station, int low, int high) {
        return station.readings.stream().filter(r -> r.temp < low || r.temp > high).collect(Collectors.toList());
    }
}
