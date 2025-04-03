package chapter11;

import org.junit.jupiter.api.Test;
import chapter11.ReplaceParameterWithQuery.Order;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReplaceParameterWithQueryTest {
    @Test
    void client1() {
        Order order = new Order(100, 100);
        assertThat(order.finalPrice()).isEqualTo(9500);
        assertThat(order.discountedPrice(9500)).isEqualTo(9025);
        order = new Order(101, 100);
        assertThat(order.discountedPrice(9500)).isEqualTo(8550);
    }

}
