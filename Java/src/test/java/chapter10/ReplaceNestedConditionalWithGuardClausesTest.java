package chapter10;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static chapter10.ReplaceNestedConditionalWithGuardClauses.*;

class ReplaceNestedConditionalWithGuardClausesTest {
    @Test
    void client1() {
        Result actual = payAmount(new Employee(true, false));
        assertThat(actual).isEqualTo(new Result(0, "SEP"));

        actual = payAmount(new Employee(false, true));
        assertThat(actual).isEqualTo(new Result(0, "RET"));
    }

    @Test
    void client2() {
        double actual = adjustedCapital(new Instrument(2, 3, 4, 5, 6));
        assertThat(actual).isEqualTo(4.5);
    }
}
