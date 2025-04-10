package chapter12;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

import chapter12.RemoveSubclass.Female;
import chapter12.RemoveSubclass.Male;
import chapter12.RemoveSubclass.Person;
import static chapter12.RemoveSubclass.*;

class RemoveSubclassTest {
    @Test
    void client1() {
        Male person1 = createMale("John");
        Female person2 = createFemale("Jane");
        List<Person> people = Arrays.asList(person1, person2);
        int numberOfMales = people.stream().filter(p -> p instanceof Male).mapToInt(p -> 1).sum();
        assertThat(numberOfMales).isEqualTo(1);
    }

    @Test
    void client2() {
        List<Map<String, String>> data = Arrays.asList(
                new HashMap<String, String>() {
                    {
                        put("name", "John");
                        put("gender", "M");
                    }
                },
                new HashMap<String, String>() {
                    {
                        put("name", "Jane");
                        put("gender", "F");
                    }
                },
                new HashMap<String, String>() {
                    {
                        put("name", "Don");
                        put("gender", "X");
                    }
                }

        );
        List<Person> people = loadFromInput(data);
        assertThat(people).hasSize(3);
        assertThat(people.get(0).name()).isEqualTo("John");
        assertThat(people.get(1).name()).isEqualTo("Jane");
        assertThat(people.get(2).name()).isEqualTo("Don");

        assertThat(people.get(0).genderCode()).isEqualTo("M");
        assertThat(people.get(1).genderCode()).isEqualTo("F");
        assertThat(people.get(2).genderCode()).isEqualTo("X");

        int numberOfMales = people.stream().filter(p -> isMale(p)).mapToInt(p -> 1).sum();
        assertThat(numberOfMales).isEqualTo(1);
    }

    boolean isMale(Person aPerson) {
        return aPerson instanceof Male;
    }
}
