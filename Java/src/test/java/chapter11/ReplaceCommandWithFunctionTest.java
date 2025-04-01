package chapter11;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import chapter11.ReplaceCommandWithFunction.*;

class ReplaceCommandWithFunctionTest {
    @Test
    void client1() {
        Customer customer = new Customer(10);
        int usage = 50;
        Provider provider = new Provider(20);
        double monthCharge = new ChargeCalculator(customer, usage, provider).charge();
        assertThat(monthCharge).isEqualTo(520);
    }
}
