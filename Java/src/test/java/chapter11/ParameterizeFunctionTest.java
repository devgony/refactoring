package chapter11;

import org.junit.jupiter.api.Test;

import chapter11.ParameterizeFunction.Person;
import chapter11.ParameterizeFunction.Salary;
import static chapter11.ParameterizeFunction.tenPercentRaise;
import static org.assertj.core.api.Assertions.assertThat;
import static chapter11.ParameterizeFunction.fivePercentRaise;
import static org.assertj.core.api.Assertions.offset;

class ParameterizeFunctionTest {
    @Test
    void client1() {
        Salary salary = new Salary(100);
        Person person = new Person(salary);
        double ten = tenPercentRaise(person);
        assertThat(ten).isCloseTo(110.0, offset(0.0001));

        double five = fivePercentRaise(person);
        assertThat(five).isCloseTo(115.5, offset(0.0001));
    }
}
