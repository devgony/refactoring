package chapter12;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import chapter12.RemoveSubclass.Female;
import chapter12.RemoveSubclass.Male;
import chapter12.RemoveSubclass.Person;

class RemoveSubclassTest {
    @Test
    void client1() {

        Male person1 = new Male("John");
        Female person2 = new Female("Jane");
        List<Person> people = Arrays.asList(person1, person2);
        int numberOfMales = people.stream().filter(p -> p instanceof Male).mapToInt(p -> 1).sum();
        assertThat(numberOfMales).isEqualTo(1);
    }
}
