package chapter11;

import org.junit.jupiter.api.Test;
import chapter11.ReplaceParameterWithQuery.Order;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReplaceParameterWithQueryTest {
    @Test
    void client1() {
        Order order = new Order(10, 1000);
        assertThat(order.finalPrice()).isEqualTo(9500);
        assertThat(order.discountedPrice(9500, 1)).isEqualTo(9025);
        assertThat(order.discountedPrice(9500, 2)).isEqualTo(8550);
        assertThatThrownBy(() -> order.discountedPrice(9500, 3)).isInstanceOf(IllegalArgumentException.class);
    }

}
