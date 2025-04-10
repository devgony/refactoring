package chapter12;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.HashMap;
import java.util.Map;
import static chapter12.ReplaceSubclassWithDelegate.*;
import org.junit.jupiter.api.Test;
import chapter12.ReplaceSubclassWithDelegate.Booking;
import chapter12.ReplaceSubclassWithDelegate.Extras;
import chapter12.ReplaceSubclassWithDelegate.Show;

class ReplaceSubclassWithDelegateTest {
    @Test
    void client1() {
        Booking booking = createBooking(new Show("The Lion King", 100, null), "Saturday");
        assertThat(booking.basePrice()).isEqualTo(115);
        assertThat(booking.hasTalkback()).isFalse();
    }

    @Test
    void client2() {
        Extras extras = new Extras(20, "Dinner");
        Booking booking = createPremiumBooking(
                new Show("The Lion King", 100, "SomeTalkBack"), "Saturday", extras);
        assertThat(booking.basePrice()).isEqualTo(135);
        assertThat(booking.hasTalkback()).isTrue();
    }

    @Test
    void client3() {
        Map<String, Object> data = new HashMap<String, Object>() {
            {
                put("type", "EuropeanSwallow");
                put("name", "euro-swallow");
                put("plumage", "blue");
            }
        };
        Bird bird = createBird(data);
        assertThat(bird.name()).isEqualTo("euro-swallow");
        assertThat(bird.plumage()).isEqualTo("blue");
        assertThat(bird.airSpeedVelocity()).isEqualTo(35.0);

        data = new HashMap<String, Object>() {
            {
                put("type", "AfricanSwallow");
                put("name", "african-swallow");
                put("plumage", "red");
                put("numberOfCoconuts", 2);
            }
        };
        bird = createBird(data);
        assertThat(bird.name()).isEqualTo("african-swallow");
        assertThat(bird.plumage()).isEqualTo("red");
        assertThat(bird.airSpeedVelocity()).isEqualTo(36.0);

        data = new HashMap<String, Object>() {
            {
                put("type", "NorweigianBlueParrot");
                put("name", "norwegian-blue-parrot");
                put("plumage", "green");
                put("voltage", 10.5);
                put("isNailed", true);
            }
        };
        bird = createBird(data);
        assertThat(bird.name()).isEqualTo("norwegian-blue-parrot");
        assertThat(bird.plumage()).isEqualTo("green");
        assertThat(bird.airSpeedVelocity()).isEqualTo(0.0);

        data = new HashMap<String, Object>() {
            {
                put("type", "Bird");
                put("name", "generic-bird");
                put("plumage", "yellow");
            }
        };
        bird = createBird(data);
        assertThat(bird.name()).isEqualTo("generic-bird");
        assertThat(bird.plumage()).isEqualTo("yellow");
        assertThat(bird instanceof Bird).isTrue();
        assertThat(bird.airSpeedVelocity()).isNull();
    }
}
