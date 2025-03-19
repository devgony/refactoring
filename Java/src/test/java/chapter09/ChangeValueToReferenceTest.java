package chapter09;

import org.junit.jupiter.api.Test;

import chapter09.ChangeValueToReference.Customer;
import chapter09.ChangeValueToReference.Data;
import chapter09.ChangeValueToReference.Order;
import chapter09.ChangeValueToReference.Repository;

import static org.assertj.core.api.Assertions.assertThat;

class ChangeValueToReferenceTest {
    @Test
    void client1() {
        Repository repository = new Repository();
        Order order = new Order(new Data(1, 1), repository);
        assertThat(order.customer()).isEqualTo(new Customer(1));
    }

}
