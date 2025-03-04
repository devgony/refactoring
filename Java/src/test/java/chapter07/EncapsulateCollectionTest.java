package chapter07;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import chapter07.EncapsulateCollection.Course;
import chapter07.EncapsulateCollection.Person;

class EncapsulateCollectionTest {
    @Test
    void client() {
        Person aPerson = new Person("John Doe");
        aPerson.courses().add(new Course("Math", true));
        long numberAdvancedCourses = aPerson.courses().stream().filter(Course::isAdvanced).count();
        assertThat(numberAdvancedCourses).isEqualTo(1);
    }
}
