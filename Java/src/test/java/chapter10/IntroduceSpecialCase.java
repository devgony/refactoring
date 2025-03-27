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
    Customer unknownCustomer;
    Customer customer;

    @BeforeEach
    void setUp() {
        PaymentHistory paymentHistory = new PaymentHistory(10);
        site = new Site(new Customer("unknown", "originalBillingPlan", paymentHistory));
        unknownCustomer = site.customer();
        customer = new Customer("x", "realPlan", paymentHistory);
    }

    @Test
    void client1Test() {
        assertThat(client1(unknownCustomer)).isEqualTo("occupant");
        assertThat(client1(customer)).isEqualTo("x");
    }

    @Test
    void client2Test() {
        assertThat(client2(unknownCustomer)).isEqualTo("basic");
        assertThat(client2(customer)).isEqualTo("realPlan");
    }

    @Test
    void client3Test() {
        assertThat(client3(unknownCustomer)).isEqualTo("originalBillingPlan");
        assertThat(client3(customer)).isEqualTo("newPlan");
    }

    @Test
    void client4Test() {
        assertThat(client4(unknownCustomer)).isEqualTo(0);
        assertThat(client4(customer)).isEqualTo(10);
    }
}
