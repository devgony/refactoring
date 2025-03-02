package chapter06;

import org.junit.jupiter.api.Test;

import chapter06.ExtractVariable.Order;
import chapter06.ExtractVariable.OrderData;

import static org.assertj.core.api.Assertions.assertThat;

class ExtractVariableTest {
    @Test
    void testPrice() {
        OrderData data = new OrderData(100, 10);
        double actual = ExtractVariable.price(data);

        assertThat(actual).isEqualTo(1100);
    }

    @Test
    void withAClass() {
        Order order = new Order(new OrderData(100, 10));
        double actual = order.price2();
        assertThat(actual).isEqualTo(1100);
    }
}
