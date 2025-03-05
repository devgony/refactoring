package chapter07;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import chapter07.HideDelegate.Department;
import chapter07.HideDelegate.Person;

class HideDelegateTest {
    @Test
    public void client() {
        Person aPerson = new Person("John");
        Person manager = new Person("Man");
        Department department = new Department("charcode", manager);
        aPerson.department(department);
        Person actual = aPerson.manager();
        assertThat(actual).isEqualTo(manager);
    }
}
