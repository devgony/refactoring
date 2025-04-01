package chapter11;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static chapter11.SeparateQueryFromModifier.alertForMiscreant;
import static chapter11.SeparateQueryFromModifier.findMiscreant;
import static org.assertj.core.api.Assertions.assertThat;

class SeparateQueryFromModifierTest {
    @Test
    void client1() {
        List<String> people = Arrays.asList("Don", "John");

        try (MockedStatic<SeparateQueryFromModifier> mockedStatic = Mockito
                .mockStatic(SeparateQueryFromModifier.class)) {
            mockedStatic.when(() -> SeparateQueryFromModifier.alertForMiscreant(people)).thenCallRealMethod();
            mockedStatic.when(() -> SeparateQueryFromModifier.findMiscreant(people)).thenCallRealMethod();
            String actual = findMiscreant(people);
            alertForMiscreant(people);

            assertThat(actual).isEqualTo("Don");
            mockedStatic.verify(() -> SeparateQueryFromModifier.setOffAlarms());
        }
    }
}
