package refactoring;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void shouldAddNumbersCorrectly() {
        int result = 1 + 1;
        Assertions.assertThat(result).isEqualTo(2);
    }

    @Test
    void shouldContainSpecificWord() {
        String message = "Hello, AssertJ!";
        Assertions.assertThat(message).contains("AssertJ");
    }
}
