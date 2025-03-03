package chapter06;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

class SplitPhaseTransformerTest {
    @Test
    void countOrdersTest() throws StreamReadException, DatabindException, IOException {
        String path = getClass().getClassLoader().getResource("split-phase.json").getPath();
        long actual = SplitPhaseTransformer.run(new String[] { path });
        assertThat(actual).isEqualTo(3);

        actual = SplitPhaseTransformer.run(new String[] { "-r", path, });
        assertThat(actual).isEqualTo(1);
    }
}
