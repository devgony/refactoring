package chapter06;

import org.junit.jupiter.api.Test;

import chapter06.InlineFunction.Customer;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

class InlineFunctionTest {

    @Test
    void testRating() {
        InlineFunction.Driver driver = new InlineFunction.Driver(6);
        InlineFunction inlineFunction = new InlineFunction();
        int actual = inlineFunction.rating(driver);
        int expected = 2;
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void testReportLines() {
        Customer customer = new Customer("John Doe", "1234");

        List<String> actual = InlineFunction.reportLines(customer);
        assertThat(actual).containsExactly("name: John Doe", "id: 1234");
    }

}
