package chapter07;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import chapter07.ReplaceTempWithQuery.Order;

class ReplaceTempWithQueryTest {
    @Test
    void client() {
        Order order = new Order(10, 100);
        double actual = order.price();
        assertThat(actual).isEqualTo(980);
    }
}
