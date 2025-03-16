package chapter09;

import org.junit.jupiter.api.Test;
import chapter09.ReplaceDerivedVariableWithQuery.ProductionPlan;
import chapter09.ReplaceDerivedVariableWithQuery.Adjustment;
import static org.assertj.core.api.Assertions.assertThat;

class ReplaceRerivedVariableWithQuery {
    @Test
    void client1() {
        ProductionPlan plan = new ProductionPlan(10);
        plan.applyAdjustment(new Adjustment(5, "increase"));
        double actual = plan.production();
        assertThat(actual).isEqualTo(15.0);
    }
}
