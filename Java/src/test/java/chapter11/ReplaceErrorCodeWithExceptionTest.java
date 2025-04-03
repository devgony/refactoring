package chapter11;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import chapter11.ReplaceErrorCodeWithException.CountryData;
import chapter11.ReplaceErrorCodeWithException.Order;
import static org.assertj.core.api.Assertions.assertThat;

class ReplaceErrorCodeWithExceptionTest {
    @Test
    void client1() {
        Map<String, Integer> shippingRules = new HashMap<String, Integer>() {
            {
                put("US", 10);
                put("FR", 20);
                put("IT", 30);
            }
        };
        CountryData countryData = new CountryData(shippingRules);
        ReplaceErrorCodeWithException r = new ReplaceErrorCodeWithException(countryData);
        int status;
        try {
            status = r.calculateShippingCosts(new Order("XX"));
        } catch (Exception e) {
            throw new RuntimeException("Error in test", e);
        }
        assertThat(status).isEqualTo(-23); // error
    }
}
