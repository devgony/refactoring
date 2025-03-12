package chapter08;

import java.util.ArrayList;
import java.util.List;

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
        boolean firstLine = true;
        List<CityPhoneData> result = new ArrayList<>();
        for (String line : lines) {
            if (firstLine) {
                firstLine = false;
                continue;
            }
            if (line.trim().isEmpty())
                continue;
            String[] record = line.split(",");
            if (record[1].trim().equals("India")) {
                result.add(new CityPhoneData(record[0].trim(), record[2].trim()));
            }
        }

        return result;
    }

}
