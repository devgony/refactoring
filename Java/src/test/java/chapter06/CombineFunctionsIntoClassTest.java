package chapter06;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import chapter06.CombineFunctionsIntoClass.Reading;

class CombineFunctionsIntoClassTest {
    @Test
    void test() {
        Reading aReading = CombineFunctionsIntoClass.acquireReading();
        assertThat(aReading).isEqualTo(new Reading("ivan", 10, 5, 2017));

        int baseCharge = CombineFunctionsIntoClass.baseRate(aReading.month, aReading.year) * aReading.quantity;
        assertThat(baseCharge).isEqualTo(1000);

        int texThreshold = new CombineFunctionsIntoClass().taxThreshold(aReading.year);
        assertThat(texThreshold).isEqualTo(50);

    }

    @Test
    void clientTest() {
        CombineFunctionsIntoClass c = new CombineFunctionsIntoClass();
        assertThat(c.client1()).isEqualTo(1000);
        assertThat(c.client2()).isEqualTo(950);
        assertThat(c.client3()).isEqualTo(1000);
    }

}
