package chapter08;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import chapter08.SplitLoop.Person;

class SplitLoopTest {
    @Test
    void client1() {
        List<Person> people = Arrays.asList(new Person(20, 100), new Person(30, 200), new Person(40, 300));
        SplitLoop.ageAndSalary(people);

        assertThat(SplitLoop.ageAndSalary(people)).isEqualTo("youngestAge: 20, totalSalary: 600");

        assertThat(SplitLoop.ageAndSalary(new ArrayList<>())).isEqualTo("youngestAge: 2147483647, totalSalary: 0");
    }
}
