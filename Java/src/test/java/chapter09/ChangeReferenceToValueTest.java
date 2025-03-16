package chapter09;

import org.junit.jupiter.api.Test;

import chapter09.ChangeReferenceToValue.Person;
import static org.assertj.core.api.Assertions.assertThat;

class ChangeReferenceToValueTest {
    @Test
    void client1() {
        Person kent = new Person();
        kent.officeAreaCode("781");
        kent.officeNumber("555-1212");
        assertThat(kent.officeAreaCode()).isEqualTo("781");
        assertThat(kent.officeNumber()).isEqualTo("555-1212");
    }

}
