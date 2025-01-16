package chapter01;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class App {
    Invoice invoice;
    Map<String, Play> plays;

    @Data
    static class Invoice {
        String customer;
        List<Performance> performances;
    }

    @Data
    static class Performance {
        String playID;
        int audience;
    }

    @Data
    static class Play {
        String name;
        String type;
    }

    String usd(int number) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);

        return "$" + formatter.format(number);
    }

    String statement() {
        int totalAmount = 0;
        int volumeCredits = 0;

        String result = "Statement for " + invoice.customer + "\n";

        for (Performance perf : invoice.performances) {
            // add volume credits
            volumeCredits += volumeCreditsFor(perf);

            // print line for this order
            result += playFor(perf).name + ": " + usd(amountFor(perf) / 100) + " (" + perf.audience + " seats)\n";
            totalAmount += amountFor(perf);
        }
        result += "Amount owed is " + usd(totalAmount / 100) + "\n";
        result += "You earned " + volumeCredits + " credits\n";

        return result;
    }

    private int volumeCreditsFor(Performance aPerformance) {
        int result = 0;
        result += Math.max(aPerformance.audience - 30, 0);
        if ("comedy".equals(playFor(aPerformance).type))
            result += Math.floor(aPerformance.audience / 5);
        return result;
    }

    private Play playFor(Performance aPerformance) {
        return plays.get(aPerformance.playID);
    }

    private int amountFor(Performance aPerformance) throws Error {
        int result = 0;
        switch (playFor(aPerformance).type) {
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
                throw new Error("unknown type: " + playFor(aPerformance).type);
        }
        return result;
    }
}
