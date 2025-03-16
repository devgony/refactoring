package chapter09;

import org.junit.jupiter.api.Test;
import chapter09.ReplaceDerivedVariableWithQuery.ProductionPlan;
import chapter09.ReplaceDerivedVariableWithQuery.ProductionPlan2;
import chapter09.ReplaceDerivedVariableWithQuery.Adjustment;
import static org.assertj.core.api.Assertions.assertThat;

class ReplaceRerivedVariableWithQueryTest {
    @Test
    void client1() {
        ProductionPlan plan = new ProductionPlan(0);
        plan.applyAdjustment(new Adjustment(5, "increase"));
        double actual = plan.production();
        assertThat(actual).isEqualTo(5.0);
    }

    @Test
    void client2() {
        int wrongInit = 1;
        ProductionPlan2 plan = new ProductionPlan2(wrongInit);
        plan.applyAdjustment(new Adjustment(5, "increase"));
        double actual = plan.production();
        assertThat(actual).isEqualTo(6.0);
    }
}
