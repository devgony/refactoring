package chapter12;

import static org.assertj.core.api.Assertions.assertThat;
import static chapter12.ReplaceTypeCodeWithSubclasses.createEmployee;
import org.junit.jupiter.api.Test;
import chapter12.ReplaceTypeCodeWithSubclasses.Employee;
import chapter12.ReplaceTypeCodeWithSubclasses.Employee2;

class ReplaceTypeCodeWithSubclassesTest {
    @Test
    void client1() {
        Employee employee = createEmployee("John Doe", "engineer");
        assertThat(employee.toString()).isEqualTo("John Doe (engineer)");

        employee = createEmployee("Sam Smith", "salesman");
        assertThat(employee.toString()).isEqualTo("Sam Smith (salesman)");
    }

    @Test
    void client2() {
        Employee2 employee = new Employee2("Jane Doe", "engineer");
        assertThat(employee.toString()).isEqualTo("Jane Doe (Engineer)");
    }
}
