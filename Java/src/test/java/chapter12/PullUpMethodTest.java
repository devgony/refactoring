package chapter12;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import chapter12.PullUpMethod.Department;
import chapter12.PullUpMethod.Employee;

class PullUpMethodTest {
    @Test
    void client1() {
        Employee employee = new Employee(1000);
        Department department = new Department(2000);

        assertThat(employee.annualCost()).isEqualTo(12000);
        assertThat(department.annualCost()).isEqualTo(24000);
    }
}
