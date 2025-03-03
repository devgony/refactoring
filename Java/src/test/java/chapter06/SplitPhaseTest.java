package chapter06;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

import chapter06.SplitPhase.Product;
import chapter06.SplitPhase.ShippingMethod;

class SplitPhaseTest {
    @Test
    void testSplitPhase() {
        Product product = new Product(100, 0.05, 10);
        ShippingMethod shippingMethod = new ShippingMethod(1000, 10, 20);

        double actual = SplitPhase.priceOrder(product, 50, shippingMethod);
        assertThat(actual).isEqualTo(5300);
    }

    @Test
    void countOrdersTest() throws StreamReadException, DatabindException, IOException {
        String path = getClass().getClassLoader().getResource("split-phase.json").getPath();
        long actual = SplitPhase.run(new String[] { path });
        assertThat(actual).isEqualTo(3);

        actual = SplitPhase.run(new String[] { "-r", path, });
        assertThat(actual).isEqualTo(1);
    }
}
