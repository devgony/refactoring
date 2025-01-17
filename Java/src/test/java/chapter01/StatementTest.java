package chapter01;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import chapter01.CreateStatementData.Invoice;
import chapter01.CreateStatementData.Play;
import java.io.InputStream;
import static org.assertj.core.api.Assertions.assertThat;

class StatementTest {
    ObjectMapper objectMapper = new ObjectMapper();
    List<Invoice> invoices;
    Map<String, Play> plays;
    Statement app = new Statement();

    @BeforeEach
    void setUp() throws Exception {
        InputStream invoicesStream = getClass().getClassLoader().getResourceAsStream("invoices.json");
        invoices = objectMapper.readValue(invoicesStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Invoice.class));

        InputStream playsStream = getClass().getClassLoader().getResourceAsStream("plays.json");
        plays = objectMapper.readValue(playsStream,
                objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Play.class));
    }

    @Test
    void renderPlainText() throws Exception {
        String expected = "Statement for BigCo\nHamlet: $650.00 (55 seats)\nAs You Like It: $580.00 (35 seats)\nOthello: $500.00 (40 seats)\nAmount owed is $1,730.00\nYou earned 47 credits\n";
        String actual = app.statement(invoices.get(0), plays);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void renderHtml() throws Exception {
        String expected = "<h1>Statement for ${data.customer}</h1>\n<table>\n<tr><th>play</th><th>seats</th><th>cost</th></tr>  <tr><td>${perf.play.name}</td><td>${perf.audience}</td><td>${usd(perf.amount)}</td></tr>\n  <tr><td>${perf.play.name}</td><td>${perf.audience}</td><td>${usd(perf.amount)}</td></tr>\n  <tr><td>${perf.play.name}</td><td>${perf.audience}</td><td>${usd(perf.amount)}</td></tr>\n</table>\n<p>Amount owed is <em>${usd(data.totalAmount)}</em></p>\n<p>You earned <em>${data.totalVolumeCredits}</em> credits</p>\n";
        String actual = app.htmlStatement(invoices.get(0), plays);
        assertThat(actual).isEqualTo(expected);
    }
}
