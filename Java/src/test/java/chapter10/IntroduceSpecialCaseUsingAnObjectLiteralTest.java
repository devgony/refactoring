package chapter10;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import chapter10.IntroduceSpecialCaseUsingAnObjectLiteral.Site;
import utils.ObjectBuilder;

import static chapter10.IntroduceSpecialCaseUsingAnObjectLiteral.*;

class IntroduceSpecialCaseUsingAnObjectLiterlTest {
    ObjectNode record;
    ObjectNode aCustomer;
    ObjectNode unknownCustomer;

    @BeforeEach
    void setUp() {
        record = ObjectBuilder.readValue(
                "{\"isUnknown\": false, \"name\": \"x\", \"billingPlan\": \"realPlan\", \"paymentHistory\": {\"weeksDelinquentInLastYear\": 10}}");
        Site site = new Site(record);
        aCustomer = site.customer();
        Site unknownSite = new Site(ObjectBuilder.readValue("{\"name\": \"unknown\"}"));
        unknownCustomer = unknownSite.customer();
    }

    @Test
    void client1Test() {
        assertThat(client1(unknownCustomer)).isEqualTo("occupant");
        assertThat(client1(aCustomer)).isEqualTo("x");
    }

    @Test
    void client2Test() {
        assertThat(client2(unknownCustomer)).isEqualTo("basic");
        assertThat(client2(aCustomer)).isEqualTo("realPlan");
    }

    @Test
    void client3Test() {
        assertThat(client3(unknownCustomer)).isEqualTo(0);
        assertThat(client3(aCustomer)).isEqualTo(10);
    }
}
