package chapter07;

import org.junit.jupiter.api.Test;

import chapter07.RemoveMiddleMan.Department;
import chapter07.RemoveMiddleMan.Person;
import static org.assertj.core.api.Assertions.assertThat;

class RemoveMiddleManTest {
    @Test
    void client() {
        Person aPerson = new Person("John");
        Person manager = new Person("Man");
        Department department = new Department("charcode", manager);
        aPerson.department(department);
        Person actual = aPerson.department().manager();
        assertThat(actual).isEqualTo(manager);
    }
}
