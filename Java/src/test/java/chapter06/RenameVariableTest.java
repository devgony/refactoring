package chapter06;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class RenameVariableTest {
    @Test
    void test() {
        RenameVariable renameVariable = new RenameVariable();
        String actual = renameVariable.headingOne();
        assertThat(actual).isEqualTo("<h1>untitled</h1>");

        renameVariable.mutateTpHd("new title");
        assertThat(renameVariable._title).isEqualTo("new title");
    }
}
