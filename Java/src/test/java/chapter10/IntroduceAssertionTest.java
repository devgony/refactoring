package chapter10;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import chapter10.IntroduceAssertion.Customer;

class IntroduceAssertionTest {
    @Test
    void client1() {
        Customer customer = new Customer();
        customer.discountRate = 0.1;
        assertThat(customer.applyDiscount(100)).isEqualTo(90);
    }
}
