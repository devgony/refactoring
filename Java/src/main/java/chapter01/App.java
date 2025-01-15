package chapter01;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.Data;

public class App {
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

    static String format(int number) {
        NumberFormat formatter = NumberFormat.getInstance(Locale.US);
        formatter.setMinimumFractionDigits(2);

        return "$" + formatter.format(number);
    }

    static String statement(Invoice invoice, Map<String, Play> plays) {
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

    private static int amountFor(Performance perf, Play play) throws Error {
        int thisAmount = 0;
        switch (play.type) {
            case "tragedy":
                thisAmount = 40000;
                if (perf.audience > 30) {
                    thisAmount += 1000 * (perf.audience - 30);
                }
                break;
            case "comedy":
                thisAmount = 30000;
                if (perf.audience > 20) {
                    thisAmount += 10000 + 500 * (perf.audience - 20);
                }
                thisAmount += 300 * perf.audience;
                break;
            default:
                throw new Error("unknown type: " + play.type);
        }
        return thisAmount;
    }
}
