package chapter06;

import java.util.Date;

import org.junit.jupiter.api.Test;

import chapter06.ExtractFunction.Invoice;
import chapter06.ExtractFunction.Order;
import static org.assertj.core.api.Assertions.assertThat;

class ExtractFunctionTest {
    @Test
    public void testPrintOwing() {
        Invoice invoice = new Invoice("John Doe", new Date(), new Order[] { new Order(100), new Order(200) });
        String actual = ExtractFunction.printOwing(invoice);
        // **** Customer Owes ****
        // "***********************
        // ***********************
        // name: John Doeamount: 300due: Thu Feb 27 20:46:19 KST 2025"

        assertThat(actual).containsPattern("name: John Doe\namount: 300\ndue: .*");
    }
}
