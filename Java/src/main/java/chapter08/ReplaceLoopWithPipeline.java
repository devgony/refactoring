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
        List<CityPhoneData> result = new ArrayList<>();
        List<String[]> loopItems = Arrays.asList(lines).stream()
                .skip(1)
                .filter(line -> !line.trim().isEmpty())
                .map(l -> l.split(","))
                .collect(Collectors.toList());
        for (String[] line : loopItems) {
            String[] record = line;
            if (record[1].trim().equals("India")) {
                result.add(new CityPhoneData(record[0].trim(), record[2].trim()));
            }
        }

        return result;
    }
}
