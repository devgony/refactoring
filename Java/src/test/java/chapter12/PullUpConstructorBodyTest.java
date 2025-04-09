package chapter12;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import chapter12.PullUpConstructorBody.Department;
import chapter12.PullUpConstructorBody.Employee;

class PullUpConstructorBodyTest {
    @Test
    void client1() {
        Employee employee = new Employee("John Doe", "123", 1000);
        assertThat(employee._id).isEqualTo("123");

        Department department = new Department("HR", employee);
        assertThat(department._name).isEqualTo("HR");
    }
}
