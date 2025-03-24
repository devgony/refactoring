package chapter10;

import org.junit.jupiter.api.Test;

import chapter10.ConsolidateConditionalExpression.Employee;
import static chapter10.ConsolidateConditionalExpression.*;

import static org.assertj.core.api.Assertions.assertThat;

class ConsolidateConditionalExpressionTest {
    @Test
    void client1() {
        Employee employee = new Employee(1, 13, false, false);
        double actual = disabilityAmount(employee);
        assertThat(actual).isEqualTo(0);
    }

    @Test
    void client2() {
        Employee employee = new Employee(1, 13, false, false);
        double actual = usingAnds(employee);
        assertThat(actual).isEqualTo(0.5);
    }
}
