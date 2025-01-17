package chapter01;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
public class App {
    Invoice invoice;
    Map<String, Play> plays;
    StatementData data;

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

    String statement() {
        data.customer = invoice.customer;
        data.performances = invoice.performances.stream().map(this::enrichPerformance).collect(Collectors.toList());
        data.totalAmount = totalAmount(data);
        data.totalVolumeCredits = totalVolumeCredits(data);

        return renderPlainText(data);
    }

    private Performance enrichPerformance(Performance aPerformance) {
        Performance result = new Performance(aPerformance.playID, aPerformance.audience);
        result.play = playFor(result);
        result.amount = amountFor(result);
        result.volumeCredits = volumeCreditsFor(result);

        return result;
    };

    private String renderPlainText(StatementData data) throws Error {
        String result = "Statement for " + invoice.customer + "\n";

        for (Performance perf : data.performances) {
            result += perf.play.name + ": " + usd(amountFor(perf) / 100) + " (" + perf.audience + " seats)\n";
        }
        result += "Amount owed is " + usd(data.totalAmount / 100) + "\n";
        result += "You earned " + data.totalVolumeCredits + " credits\n";

        return result;
    }

    String usd(int number) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);

        return "$" + formatter.format(number);
    }

    private int totalAmount(StatementData data) throws Error {
        return data.performances.stream().mapToInt(Performance::getAmount).sum();
    }

    private int totalVolumeCredits(StatementData data) {
        return data.performances.stream().mapToInt(Performance::getVolumeCredits).sum();
    }

    private int volumeCreditsFor(Performance aPerformance) {
        int result = 0;
        result += Math.max(aPerformance.audience - 30, 0);
        if ("comedy".equals(aPerformance.play.type))
            result += Math.floor(aPerformance.audience / 5);
        return result;
    }

    private Play playFor(Performance aPerformance) {
        return plays.get(aPerformance.playID);
    }

    private int amountFor(Performance aPerformance) throws Error {
        int result = 0;
        switch (aPerformance.play.type) {
            case "tragedy":
                result = 40000;
                if (aPerformance.audience > 30) {
                    result += 1000 * (aPerformance.audience - 30);
                }
                break;
            case "comedy":
                result = 30000;
                if (aPerformance.audience > 20) {
                    result += 10000 + 500 * (aPerformance.audience - 20);
                }
                result += 300 * aPerformance.audience;
                break;
            default:
                throw new Error("unknown type: " + aPerformance.play.type);
        }
        return result;
    }
}
