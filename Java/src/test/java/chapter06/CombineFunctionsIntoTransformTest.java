package chapter06;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import chapter06.CombineFunctionsIntoTransform.Reading;

class CombineFunctionsIntoTransformTest {
    @Test
    void test() {
        Reading aReading = CombineFunctionsIntoTransform.acquireReading();
        assertThat(aReading).isEqualTo(new Reading("ivan", 10, 5, 2017));

        int baseCharge = CombineFunctionsIntoTransform.baseRate(aReading.month, aReading.year) * aReading.quantity;
        assertThat(baseCharge).isEqualTo(1000);

        int texThreshold = CombineFunctionsIntoTransform.taxThreshold(aReading.year);
        assertThat(texThreshold).isEqualTo(50);
    }

    @Test
    void clientTest() {
        CombineFunctionsIntoTransform c = new CombineFunctionsIntoTransform();
        assertThat(c.client1()).isEqualTo(1000);
        assertThat(c.client2()).isEqualTo(950);
        assertThat(c.client3()).isEqualTo(1000);
    }
}
