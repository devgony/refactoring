package chapter08;

import java.util.List;
import lombok.AllArgsConstructor;

class SplitLoop {
    @AllArgsConstructor
    static class Person {
        int age;
        int salary;
    }

    static String ageAndSalary(List<Person> people) {
        return "youngestAge: " + youngestAge(people) + ", totalSalary: " + totalSalary(people);
    }

    private static int youngestAge(List<Person> people) {
        return people.stream().mapToInt(p -> p.age).min().orElse(Integer.MAX_VALUE);
    }

    private static int totalSalary(List<Person> people) {
        return people.stream().mapToInt(p -> p.salary).sum();
    }
}
