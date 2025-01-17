package chapter01;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import chapter01.Statement.Invoice;
import chapter01.Statement.Play;
import chapter01.Statement.StatementData;

import java.io.InputStream;
import static org.assertj.core.api.Assertions.assertThat;

class StatementTest {
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void test() throws Exception {
        String expected = "Statement for BigCo\nHamlet: $650.00 (55 seats)\nAs You Like It: $580.00 (35 seats)\nOthello: $500.00 (40 seats)\nAmount owed is $1,730.00\nYou earned 47 credits\n";

        InputStream invoicesStream = getClass().getClassLoader().getResourceAsStream("invoices.json");
        List<Invoice> invoices = objectMapper.readValue(invoicesStream,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Invoice.class));

        InputStream playsStream = getClass().getClassLoader().getResourceAsStream("plays.json");
        Map<String, Play> plays = objectMapper.readValue(playsStream,
                objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Play.class));

        Statement app = new Statement(plays);
        String actual = app.statement(invoices.get(0));
        assertThat(actual).isEqualTo(expected);
    }
}