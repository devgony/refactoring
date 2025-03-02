package chapter06;

import org.junit.jupiter.api.Test;

import chapter06.ExtractVariable.Order;
import static org.assertj.core.api.Assertions.assertThat;

class ExtractVariableTest {
    @Test
    void testPrice() {
        Order order = new Order(100, 10);
        double actual = ExtractVariable.price(order);

        assertThat(actual).isEqualTo(1100);
    }
}
