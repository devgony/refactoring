package chapter08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

class ReplaceLoopWithPipeline {
    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    static class CityPhoneData {
        String city;
        String phone;
    }

    static List<CityPhoneData> acquireData(String input) {
        String[] lines = input.split("\n");

        return Arrays.asList(lines).stream()
                .skip(1)
                .filter(line -> !line.trim().isEmpty())
                .map(line -> line.split(","))
                .filter(fields -> fields[1].trim().equals("India"))
                .map(fields -> new CityPhoneData(fields[0].trim(), fields[2].trim()))
                .collect(Collectors.toList());
    }
}
