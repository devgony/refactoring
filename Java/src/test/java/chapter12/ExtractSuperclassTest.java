package chapter12;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import chapter12.ExtractSuperclass.Department;
import chapter12.ExtractSuperclass.Employee;

class ExtractSuperclassTest {
    @Test
    void client1() {
        Employee employee1 = new Employee("John", "123", 1000);
        Employee employee2 = new Employee("Jane", "456", 2000);
        Department department = new Department("HR", Arrays.asList(employee1, employee2));

        assertThat(department.name()).isEqualTo("HR");
        assertThat(department.headCount()).isEqualTo(2);
        assertThat(department.monthlyCost()).isEqualTo(3000);
        assertThat(department.staff()).containsExactly(employee1, employee2);
        assertThat(department.staff().get(0).name()).isEqualTo("John");
        assertThat(department.staff().get(0).id()).isEqualTo("123");
        assertThat(department.staff().get(0).monthlyCost()).isEqualTo(1000);
        assertThat(department.staff().get(0).annualCost()).isEqualTo(12000);
    }
}
