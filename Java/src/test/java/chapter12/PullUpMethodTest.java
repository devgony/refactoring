package chapter12;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

import chapter12.PullUpMethod.Department;
import chapter12.PullUpMethod.Employee;
import chapter12.PullUpMethod.NotImpl;

class PullUpMethodTest {
    @Test
    void client1() {
        Employee employee = new Employee();
        Department department = new Department();
        NotImpl notImpl = new NotImpl();

        assertThat(employee.annualCost()).isEqualTo(12000);
        assertThat(department.annualCost()).isEqualTo(24000);
        assertThatThrownBy(notImpl::annualCost)
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessage("Not implemented");
    }
}
