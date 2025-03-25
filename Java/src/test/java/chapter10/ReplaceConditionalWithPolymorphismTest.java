package chapter10;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import chapter10.ReplaceConditionalWithPolymorphism.Bird;
import static chapter10.ReplaceConditionalWithPolymorphism.*;
import static org.assertj.core.api.Assertions.assertThat;

class ReplaceConditionalWithPolymorphismTest {
    @Test
    void client1() {
        List<Bird> birds = Arrays.asList(
                createBird("bird1", "EuropeanSwallow", 0, 0, false),
                createBird("bird2", "AfricanSwallow", 3, 0, false),
                createBird("bird3", "NorwegianBlueParrot", 0, 101, false));

        Map<String, String> plumages = plumages(birds);
        assertThat(plumages).containsExactlyInAnyOrderEntriesOf(new HashMap<String, String>() {
            {
                put("bird1", "average");
                put("bird2", "tired");
                put("bird3", "scorched");
            }
        });

        Map<String, Integer> speeds = speeds(birds);
        assertThat(speeds).containsExactlyInAnyOrderEntriesOf(new HashMap<String, Integer>() {
            {
                put("bird1", 35);
                put("bird2", 34);
                put("bird3", 20);
            }
        });
    }

    @Test
    void client2() {
        ReplaceConditionalWithPolymorphism r = new ReplaceConditionalWithPolymorphism();
        Voyage voyage = new Voyage("china", 10, 1);
        List<Voyage> history = Arrays.asList(
                new Voyage("australia", 10, 1),
                new Voyage("brazil", 10, 1));
        String actual = r.rating(voyage, history);
        assertThat(actual).isEqualTo("B");
    }

    @Test
    void client3() {
        ReplaceConditionalWithPolymorphism r = new ReplaceConditionalWithPolymorphism();
        Voyage voyage = new Voyage("China", 10, 1);
        List<Voyage> history = Arrays.asList(
                new Voyage("china", 5, 5),
                new Voyage("west-indies", 5, 15),
                new Voyage("china", 5, 7),
                new Voyage("west-indies", 5, 5),
                new Voyage("china", 5, 20),
                new Voyage("east-indies", 5, 14),
                new Voyage("west-africa", 5, 8),
                new Voyage("china", 5, 10),
                new Voyage("west-indies", 5, 9),
                new Voyage("china", 5, 12),
                new Voyage("east-indies", 5, 7));
        String actual = r.rating(voyage, history);
        assertThat(actual).isEqualTo("A");
    }
}
