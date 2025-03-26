package chapter10;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.ToString;

class ReplaceConditionalWithPolymorphism {
    @AllArgsConstructor
    @ToString
    static class Bird {
        String name;
        String type;
        int numberOfCoconuts;
        int voltage;
        boolean isNailed;

        String name() {
            return name;
        }

        String plumage() {
            return "unknown";
        }

        Integer airSpeedVelocity() {
            return null;
        }
    }

    static class EuropeanSwallow extends Bird {
        EuropeanSwallow(
                String name,
                String type,
                int numberOfCoconuts,
                int voltage,
                boolean isNailed) {
            super(name, type, numberOfCoconuts, voltage, isNailed);
        }

        @Override
        String plumage() {
            return "average";
        }

        @Override
        Integer airSpeedVelocity() {
            return 35;
        }
    }

    static class AfricanSwallow extends Bird {
        AfricanSwallow(
                String name,
                String type,
                int numberOfCoconuts,
                int voltage,
                boolean isNailed) {
            super(name, type, numberOfCoconuts, voltage, isNailed);
        }

        @Override
        String plumage() {
            return this.numberOfCoconuts > 2 ? "tired" : "average";
        }

        @Override
        Integer airSpeedVelocity() {
            return 40 - 2 * this.numberOfCoconuts;
        }
    }

    static class NorwegianBlueParrot extends Bird {
        NorwegianBlueParrot(
                String name,
                String type,
                int numberOfCoconuts,
                int voltage,
                boolean isNailed) {
            super(name, type, numberOfCoconuts, voltage, isNailed);
        }

        @Override
        String plumage() {
            return this.voltage > 100 ? "scorched" : "beautiful";
        }

        @Override
        Integer airSpeedVelocity() {
            return this.isNailed ? 0 : 10 + this.voltage / 10;
        }
    }

    static Bird createBird(
            String name,
            String type,
            int numberOfCoconuts,
            int voltage,
            boolean isNailed) {
        switch (type) {
            case "EuropeanSwallow":
                return new EuropeanSwallow(name, type, numberOfCoconuts, voltage, isNailed);
            case "AfricanSwallow":
                return new AfricanSwallow(name, type, numberOfCoconuts, voltage, isNailed);
            case "NorwegianBlueParrot":
                return new NorwegianBlueParrot(name, type, numberOfCoconuts, voltage, isNailed);
            default:
                return new Bird(name, type, numberOfCoconuts, voltage, isNailed);
        }
    }

    static Map<String, String> plumages(List<Bird> birds) {
        return birds.stream().collect(Collectors.toMap(b -> b.name(), b -> plumage(b)));
    }

    static Map<String, Integer> speeds(List<Bird> birds) {
        return birds.stream()
                .collect(HashMap::new,
                        (map, bird) -> map.put(bird.name(), airSpeedVelocity(bird)),
                        HashMap::putAll);
    }

    static String plumage(Bird bird) {
        return bird.plumage();
    }

    static Integer airSpeedVelocity(Bird bird) {
        return bird.airSpeedVelocity();
    }

    // ----

    @AllArgsConstructor
    static class Voyage {
        String zone;
        int length;
        int profit;
    }

    String rating(Voyage voyage, List<Voyage> history) {
        return createRating(voyage, history).value();
    }

    int voyageRisk(Voyage voyage) {
        int result = 1;
        if (voyage.length > 4)
            result += 2;
        if (voyage.length > 8)
            result += voyage.length - 8;
        if (Arrays.asList("china", "east-indies").contains(voyage.zone))
            result += 4;
        return Math.max(result, 0);
    }

    int captainHistoryRisk(Voyage voyage, List<Voyage> history) {
        int result = 1;
        if (history.size() < 5)
            result += 4;
        result += history.stream().filter((v) -> v.profit < 0).collect(Collectors.toList()).size();
        if (voyage.zone == "china" && hasChina(history))
            result -= 2;
        return Math.max(result, 0);
    }

    boolean hasChina(List<Voyage> history) {
        return history.stream().anyMatch((v) -> "china" == v.zone);
    }

    int voyageProfitFactor(Voyage voyage, List<Voyage> history) {
        int result = 2;
        if (voyage.zone == "china")
            result += 1;
        if (voyage.zone == "east-indies")
            result += 1;
        if (voyage.zone == "china" && hasChina(history)) {
            result += 3;
            if (history.size() > 10)
                result += 1;
            if (voyage.length > 12)
                result += 1;
            if (voyage.length > 18)
                result -= 1;
        } else {
            if (history.size() > 8)
                result += 1;
            if (voyage.length > 14)
                result -= 1;
        }
        return result;
    }

    @AllArgsConstructor
    static class Rating {
        Voyage voyage;
        List<Voyage> history;

        String value() {
            int vpf = voyageProfitFactor();
            int vr = voyageRisk();
            int chr = captainHistoryRisk();
            if (vpf * 3 > vr + chr * 2)
                return "A";
            else
                return "B";
        }

        int voyageRisk() {
            int result = 1;
            if (voyage.length > 4)
                result += 2;
            if (voyage.length > 8)
                result += voyage.length - 8;
            if (Arrays.asList("china", "east-indies").contains(voyage.zone))
                result += 4;
            return Math.max(result, 0);
        }

        int captainHistoryRisk() {
            int result = 1;
            if (history.size() < 5)
                result += 4;
            result += history.stream().filter((v) -> v.profit < 0).collect(Collectors.toList()).size();

            return Math.max(result, 0);
        }

        boolean hasChinaHistory() {
            return history.stream().anyMatch((v) -> "china" == v.zone);
        }

        int voyageProfitFactor() {
            int result = 2;
            if (voyage.zone == "china")
                result += 1;
            if (voyage.zone == "east-indies")
                result += 1;
            result += voyageAndHistoryLengthFactor();

            return result;
        }

        int voyageAndHistoryLengthFactor() {
            int result = 0;
            if (voyage.zone == "china" && hasChinaHistory()) {
                result += 3;
                if (history.size() > 10)
                    result += 1;
                if (voyage.length > 12)
                    result += 1;
                if (voyage.length > 18)
                    result -= 1;
            } else {
                if (history.size() > 8)
                    result += 1;
                if (voyage.length > 14)
                    result -= 1;
            }
            return result;
        }
    }

    Rating createRating(Voyage voyage, List<Voyage> history) {
        if (voyage.zone == "china" && history.stream().anyMatch((v) -> "china" == v.zone))
            return new ExperiencedChinaRating(voyage, history);
        else
            return new Rating(voyage, history);
    }

    class ExperiencedChinaRating extends Rating {
        ExperiencedChinaRating(
                Voyage voyage,
                List<Voyage> history) {
            super(voyage, history);
        }

        int captainHistoryRisk() {
            int result = super.captainHistoryRisk() - 2;
            return Math.max(result, 0);
        }
    }

}
