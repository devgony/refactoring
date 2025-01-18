package chapter01;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
class CreateStatementData {
    Map<String, Play> plays;

    @Data
    static class Invoice {
        String customer;
        List<Performance> performances;
    }

    @Data
    @NoArgsConstructor
    static class Performance {
        public Performance(String playID, int audience) {
            this.playID = playID;
            this.audience = audience;
        }

        String playID;
        int audience;
        Play play;
        int amount;
        int volumeCredits;
    }

    @Data
    static class Play {
        String name;
        String type;
    }

    @Data
    static class StatementData {
        String customer;
        List<Performance> performances;
        int totalAmount;
        int totalVolumeCredits;
    }

    static class TragedyCalculator extends PerformanceCalculator {
        public TragedyCalculator(Performance aPerformance, Play aPlay) {
            super(aPerformance, aPlay);
        }

        @Override
        int amount() throws Error {
            int result = 40000;
            if (performance.audience > 30) {
                result += 1000 * (performance.audience - 30);
            }

            return result;
        }
    }

    static class ComedyCalculator extends PerformanceCalculator {
        public ComedyCalculator(Performance aPerformance, Play aPlay) {
            super(aPerformance, aPlay);
        }

        @Override
        int amount() throws Error {
            int result = 30000;
            if (performance.audience > 20) {
                result += 10000 + 500 * (performance.audience - 20);
            }
            result += 300 * performance.audience;

            return result;
        }
    }

    static abstract class PerformanceCalculator {
        Performance performance;
        Play play;

        public PerformanceCalculator(Performance aPerformance, Play aPlay) {
            this.performance = aPerformance;
            this.play = aPlay;
        }

        static PerformanceCalculator createPerformanceCalculator(Performance aPerformance, Play aPlay) {
            switch (aPlay.type) {
                case "tragedy":
                    return new TragedyCalculator(aPerformance, aPlay);
                case "comedy":
                    return new ComedyCalculator(aPerformance, aPlay);
                default:
                    throw new Error("unknown type: " + aPlay.type);
            }
        }

        int amount() throws Error {
            throw new Error("subclass responsibility");
        }

        int volumeCredits() {
            int result = 0;
            result += Math.max(performance.audience - 30, 0);
            if ("comedy".equals(play.type))
                result += Math.floor(performance.audience / 5);

            return result;
        }
    }

    public StatementData createStatementData(Invoice invoice, Map<String, Play> plays) throws Error {
        StatementData data = new StatementData();
        data.customer = invoice.customer;
        data.performances = invoice.performances.stream().map(this::enrichPerformance).collect(Collectors.toList());
        data.totalAmount = totalAmount(data);
        data.totalVolumeCredits = totalVolumeCredits(data);

        return data;
    }

    private Performance enrichPerformance(Performance aPerformance) {
        PerformanceCalculator calculator = PerformanceCalculator.createPerformanceCalculator(aPerformance,
                playFor(aPerformance));
        Performance result = new Performance(aPerformance.playID, aPerformance.audience);
        result.play = calculator.play;
        result.amount = calculator.amount();
        result.volumeCredits = calculator.volumeCredits();

        return result;
    };

    private int totalAmount(StatementData data) throws Error {
        return data.performances.stream().mapToInt(Performance::getAmount).sum();
    }

    private int totalVolumeCredits(StatementData data) {
        return data.performances.stream().mapToInt(Performance::getVolumeCredits).sum();
    }

    private Play playFor(Performance aPerformance) {
        return plays.get(aPerformance.playID);
    }
}
