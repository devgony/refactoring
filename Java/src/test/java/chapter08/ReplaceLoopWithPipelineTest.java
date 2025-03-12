package chapter08;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import chapter08.ReplaceLoopWithPipeline.CityPhoneData;
import static org.assertj.core.api.Assertions.assertThat;

class ReplaceLoopWithPipelineTest {
    @Test
    void client1() {
        String csvString = "office, country, telephone\nChicago, USA, +1 312 373 1000\nBeijing, China, +86 4008 900 505\nBangalore, India, +91 80 4064 9570\nPorto Alegre, Brazil, +55 51 3079 3550\nChennai, India, +91 44 660 44766";
        List<CityPhoneData> actual = ReplaceLoopWithPipeline.acquireData(csvString);
        List<CityPhoneData> expected = Arrays.asList(
                new CityPhoneData("Bangalore", "+91 80 4064 9570"),
                new CityPhoneData("Chennai", "+91 44 660 44766"));

        assertThat(actual).containsExactlyElementsOf(expected);
    }
}
