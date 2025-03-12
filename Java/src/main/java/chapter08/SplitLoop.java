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
        int youngest = people.isEmpty() ? Integer.MAX_VALUE : people.get(0).age;
        for (Person p : people) {
            if (p.age < youngest)
                youngest = p.age;
        }
        return youngest;
    }

    private static int totalSalary(List<Person> people) {
        int totalSalary = 0;
        for (Person p : people) {
            totalSalary += p.salary;
        }
        return totalSalary;
    }

}
