package chapter10;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.ObjectBuilder;

import static chapter10.IntroduceSpecialCaseUsingTransform.*;
import com.fasterxml.jackson.databind.node.ObjectNode;

class IntroduceSpecialCaseUsingTransformTest {
    ObjectNode record;
    ObjectNode unknownRecord;

    @BeforeEach
    void setUp() {
        record = ObjectBuilder.readValue("{" +
                "\"name\": \"Acme Boston\"," +
                "\"location\": \"Malden MA\"," +
                "\"customer\": {" +
                "  \"name\": \"Acme Industries\"," +
                "  \"billingPlan\": \"plan-451\"," +
                "  \"paymentHistory\": {" +
                "    \"weeksDelinquentInLastYear\": 7" +
                "  }" +
                "}" +
                "}");
        unknownRecord = ObjectBuilder.readValue("{" +
                "\"name\": \"Warehouse Unit 15\"," +
                "\"location\": \"Malden MA\"," +
                "\"customer\": \"unknown\"" +
                "}");
    }

    @Test
    void client1Test() {
        assertThat(client1(unknownRecord.get("customer"))).isEqualTo("occupant");
        assertThat(client1(record.get("customer"))).isEqualTo("Acme Industries");
    }

    @Test
    void client2Test() {
        assertThat(client2(unknownRecord.get("customer"))).isEqualTo("basic");
        assertThat(client2(record.get("customer"))).isEqualTo("plan-451");
    }

    @Test
    void client3Test() {
        assertThat(client3(unknownRecord.get("customer"))).isEqualTo(0);
        assertThat(client3(record.get("customer"))).isEqualTo(7);
    }
}
