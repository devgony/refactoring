package chapter11;

import org.junit.jupiter.api.Test;

import chapter11.ParameterizeFunction.Person;
import chapter11.ParameterizeFunction.Salary;
import static org.assertj.core.api.Assertions.assertThat;
import static chapter11.ParameterizeFunction.*;
import static org.assertj.core.api.Assertions.offset;

class ParameterizeFunctionTest {
    @Test
    void client1() {
        Salary salary = new Salary(100);
        Person person = new Person(salary);
        double ten = raise(person, 0.1);
        assertThat(ten).isCloseTo(110.0, offset(0.0001));

        double five = raise(person, 0.05);
        assertThat(five).isCloseTo(115.5, offset(0.0001));
    }

    @Test
    void client2() {
        double actual = baseCharge(150);
        assertThat(actual).isEqualTo(5500);
    }
}
