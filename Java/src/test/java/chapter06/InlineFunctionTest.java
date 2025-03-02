package chapter06;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class InlineFunctionTest {

    @Test
    void testRating() {
        InlineFunction.Driver driver = new InlineFunction.Driver(6);
        InlineFunction inlineFunction = new InlineFunction();
        int actual = inlineFunction.rating(driver);
        int expected = 2;
        assertThat(actual).isEqualTo(expected);
    }
}
