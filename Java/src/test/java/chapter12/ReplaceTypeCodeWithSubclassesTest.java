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

        employee = createEmployee("Kate Brown", "manager");
        assertThat(employee.toString()).isEqualTo("Kate Brown (manager)");
    }

    @Test
    void client2() {
        Employee2 employee = new Employee2("Jane Doe", "engineer");
        assertThat(employee.toString()).isEqualTo("Jane Doe (Engineer)");

        employee = new Employee2("Tom Brown", "salesman");
        assertThat(employee.toString()).isEqualTo("Tom Brown (Salesman)");

        employee = new Employee2("Alice Green", "manager");
        assertThat(employee.toString()).isEqualTo("Alice Green (Manager)");
    }
}
