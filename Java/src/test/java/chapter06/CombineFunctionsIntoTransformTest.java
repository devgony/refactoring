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
        assertThat(baseCharge).isEqualTo(0);

        int texThreshold = new CombineFunctionsIntoTransform().taxThreshold(aReading.year);
        assertThat(texThreshold).isEqualTo(0);

    }
}
