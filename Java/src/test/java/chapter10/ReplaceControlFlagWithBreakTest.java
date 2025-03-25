package chapter10;

import java.util.Arrays;
import java.util.List;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;

class ReplaceControlFlagWithBreakTest {
    @Test
    void client1() {
        List<String> people = Arrays.asList("Don", "John", "Kent");
        ReplaceControlFlagWithBreak spyObj = spy(new ReplaceControlFlagWithBreak());
        spyObj.client1(people);
        verify(spyObj, times(1)).sendAlert();
    }
}
