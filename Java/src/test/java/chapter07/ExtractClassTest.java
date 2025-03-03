package chapter07;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import chapter07.ExtractClass.Person;

class ExtractClassTest {
    @Test
    void client() {
        Person person = new Person();
        person.name("John Doe");
        person.officeAreaCode("123");
        person.officeNumber("456");

        assertThat(person.telephoneNumber()).isEqualTo("(123) 456");
        assertThat(person.name()).isEqualTo("John Doe");
        assertThat(person.officeAreaCode()).isEqualTo("123");
        assertThat(person.officeNumber()).isEqualTo("456");
    }
}
