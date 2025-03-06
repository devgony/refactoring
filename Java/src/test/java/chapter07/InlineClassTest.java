package chapter07;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import chapter07.InlineClass.Request;
import chapter07.InlineClass.Shipment;

class InlineClassTest {
    @Test
    void client() {
        Shipment aShipment = new Shipment("company", "1");
        assertThat(aShipment.trackingInfo()).isEqualTo("company: 1");

        Request request = new Request("vendor");
        aShipment.shippingCompany(request.vendor());
        assertThat(aShipment.trackingInfo()).isEqualTo("vendor: 1");
    }
}
