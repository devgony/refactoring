package chapter10;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

class ReplaceConditionalWithPolymorphism {
    @AllArgsConstructor
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
            switch (this.type) {
                case "EuropeanSwallow":
                    return "average";
                case "AfricanSwallow":
                    return this.numberOfCoconuts > 2 ? "tired" : "average";
                case "NorwegianBlueParrot":
                    return this.voltage > 100 ? "scorched" : "beautiful";
                default:
                    return "unknown";
            }
        }

        Integer airSpeedVelocity() {
            switch (this.type) {
                case "EuropeanSwallow":
                    return 35;
                case "AfricanSwallow":
                    return 40 - 2 * this.numberOfCoconuts;
                case "NorwegianBlueParrot":
                    return this.isNailed ? 0 : 10 + this.voltage / 10;
                default:
                    return null;
            }
        }
    }

    class EuropeanSwallow extends Bird {
        EuropeanSwallow(Bird bird) {
            super(bird.name, bird.type, bird.numberOfCoconuts, bird.voltage, bird.isNailed);
        }
    }

    class AfricanSwallow extends Bird {
        AfricanSwallow(Bird bird) {
            super(bird.name, bird.type, bird.numberOfCoconuts, bird.voltage, bird.isNailed);
        }
    }

    class NorwegianBlueParrot extends Bird {
        NorwegianBlueParrot(Bird bird) {
            super(bird.name, bird.type, bird.numberOfCoconuts, bird.voltage, bird.isNailed);
        }
    }

    Bird createBird(Bird bird) {
        switch (bird.type) {
            case "EuropeanSwallow":
                return new EuropeanSwallow(bird);
            case "AfricanSwallow":
                return new AfricanSwallow(bird);
            case "NorweigianBlueParrot":
                return new NorwegianBlueParrot(bird);
            default:
                return bird;
        }
    }

    static Map<String, String> plumages(List<Bird> birds) {
        return birds.stream().collect(Collectors.toMap(b -> b.name(), b -> plumage(b)));
    }

    static Map<String, Integer> speeds(List<Bird> birds) {
        return birds.stream().collect(Collectors.toMap(b -> b.name(), b -> airSpeedVelocity(b)));
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
        int vpf = voyageProfitFactor(voyage, history);
        int vr = voyageRisk(voyage);
        int chr = captainHistoryRisk(voyage, history);
        if (vpf * 3 > vr + chr * 2)
            return "A";
        else
            return "B";
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

}
