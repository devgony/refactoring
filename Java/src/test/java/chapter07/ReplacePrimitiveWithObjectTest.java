package chapter07;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import chapter07.ReplacePrimitiveWithObject.RawData;
import chapter07.ReplacePrimitiveWithObject.Order;

class ReplacePrimitiveWithObjectTest {
    @Test
    void client() {
        List<Order> orders = Arrays.asList(new Order(new RawData("low")), new Order(new RawData("high")),
                new Order(new RawData("rush")));
        long highPriorityCount = orders.stream()
                .filter(order -> order._priority.toString().equals("high") || order._priority.toString().equals("rush"))
                .count();

        assertThat(highPriorityCount).isEqualTo(2);
    }
}
