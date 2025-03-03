package chapter07;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import chapter07.ReplacePrimitiveWithObject.Data;
import chapter07.ReplacePrimitiveWithObject.Order;

class ReplacePrimitiveWithObjectTest {
    @Test
    void client() {
        List<Order> orders = Arrays.asList(new Order(new Data("low")), new Order(new Data("high")),
                new Order(new Data("rush")));
        long highPriorityCount = orders.stream()
                .filter(order -> order.priority.equals("high") || order.priority.equals("rush")).count();

        assertThat(highPriorityCount).isEqualTo(2);
    }
}
