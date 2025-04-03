package chapter11;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import chapter11.ReplaceCommandWithFunction.*;
import static chapter11.ReplaceCommandWithFunction.charge;

class ReplaceCommandWithFunctionTest {
    @Test
    void client1() {
        Customer customer = new Customer(10);
        int usage = 50;
        Provider provider = new Provider(20);
        double monthCharge = charge(customer, usage, provider);
        assertThat(monthCharge).isEqualTo(520);
    }
}
