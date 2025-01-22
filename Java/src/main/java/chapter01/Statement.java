package chapter01;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import chapter01.CreateStatementData.Invoice;
import chapter01.CreateStatementData.Performance;
import chapter01.CreateStatementData.Play;
import chapter01.CreateStatementData.StatementData;

public class Statement {
    String statement(Invoice invoice, Map<String, Play> plays) {
        CreateStatementData csd = new CreateStatementData(plays);

        return renderPlainText(csd.createStatementData(invoice, plays));
    }

    private String renderPlainText(StatementData data) throws Error {
        String result = "Statement for " + data.customer + "\n";

        for (Performance perf : data.performances) {
            result += perf.play.name + ": " + usd(perf.amount / 100) + " (" + perf.audience + " seats)\n";
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

    String htmlStatement(Invoice invoice, Map<String, Play> plays) {
        CreateStatementData csd = new CreateStatementData(plays);
        return renderHtml(csd.createStatementData(invoice, plays));
    }

    String renderHtml(StatementData data) {
        String result = "<h1>Statement for ${data.customer}</h1>\n";
        result += "<table>\n";
        result += "<tr><th>play</th><th>seats</th><th>cost</th></tr>";
        for (Performance perf : data.performances) {
            result += "  <tr><td>${perf.play.name}</td><td>${perf.audience}</td>";
            result += "<td>${usd(perf.amount)}</td></tr>\n";
        }
        result += "</table>\n";
        result += "<p>Amount owed is <em>${usd(data.totalAmount)}</em></p>\n";
        result += "<p>You earned <em>${data.totalVolumeCredits}</em> credits</p>\n";

        return result;
    }
}
