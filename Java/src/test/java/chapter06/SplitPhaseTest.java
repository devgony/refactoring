package chapter06;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

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
    void jsonTest() {
        // TODO:
    }
}
