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

    String format(int number) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);

        return "$" + formatter.format(number);
    }

    String statement() {
        int totalAmount = 0;
        int volumeCredits = 0;

        String result = "Statement for " + invoice.customer + "\n";

        for (Performance perf : invoice.performances) {
            Play play = plays.get(perf.playID);
            int thisAmount = amountFor(perf, play);

            // add volume credits
            volumeCredits += Math.max(perf.audience - 30, 0);
            // add extra credit for every ten comedy attendees
            if ("comedy".equals(play.type))
                volumeCredits += Math.floor(perf.audience / 5);

            // print line for this order
            result += play.name + ": " + format(thisAmount / 100) + " (" + perf.audience + " seats)\n";
            totalAmount += thisAmount;
        }
        result += "Amount owed is " + format(totalAmount / 100) + "\n";
        result += "You earned " + volumeCredits + " credits\n";

        return result;
    }

    private int amountFor(Performance aPerformance, Play play) throws Error {
        int result = 0;
        switch (play.type) {
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
                throw new Error("unknown type: " + play.type);
        }
        return result;
    }
}
