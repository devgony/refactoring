package chapter10;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import chapter10.IntroduceSpecialCase.Customer;
import chapter10.IntroduceSpecialCase.PaymentHistory;
import chapter10.IntroduceSpecialCase.Site;
import static chapter10.IntroduceSpecialCase.*;

class IntroduceSpecialCaseTest {
    Site site;
    Customer aCustomer;

    @BeforeEach
    void setUp() {
        PaymentHistory paymentHistory = new PaymentHistory(10);
        site = new Site(new Customer("unknown", "originalBillingPlan", paymentHistory));
        aCustomer = site.customer();
    }

    @Test
    void client1Test() {
        assertThat(client1(aCustomer)).isEqualTo("occupant");
        aCustomer.name("x");
        assertThat(client1(aCustomer)).isEqualTo("x");
    }

    @Test
    void client2Test() {
        assertThat(client2(aCustomer)).isEqualTo("basic");
        aCustomer.name("x");
        assertThat(client2(aCustomer)).isEqualTo("originalBillingPlan");
    }

    @Test
    void client3Test() {
        assertThat(client3(aCustomer)).isEqualTo("originalBillingPlan");
        aCustomer.name("x");
        assertThat(client3(aCustomer)).isEqualTo("newPlan");
    }

    @Test
    void client4Test() {
        assertThat(client4(aCustomer)).isEqualTo(0);
        aCustomer.name("x");
        assertThat(client4(aCustomer)).isEqualTo(10);
    }
}
