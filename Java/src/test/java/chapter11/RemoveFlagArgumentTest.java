package chapter11;

import org.junit.jupiter.api.Test;
import chapter11.RemoveFlagArgument.Order;
import chapter11.RemoveFlagArgument.PlaceOn;
import static chapter11.RemoveFlagArgument.*;
import static org.assertj.core.api.Assertions.assertThat;

class RemoveFlagArgumentTest {
    @Test
    void client1() {
        Order anOrder1 = new Order("MA", new PlaceOn());
        int date1 = rushDeliveryDate(anOrder1);
        assertThat(date1).isEqualTo(2);
        Order anOrder2 = new Order("NH", new PlaceOn());
        int date2 = regularDeliveryDate(anOrder2);
        assertThat(date2).isEqualTo(5);
    }

    @Test
    void client2() {
        Order anOrder1 = new Order("MA", new PlaceOn());
        int date1 = deliveryDate2(anOrder1, true);
        assertThat(date1).isEqualTo(2);
        Order anOrder2 = new Order("NH", new PlaceOn());
        int date2 = deliveryDate2(anOrder2, false);
        assertThat(date2).isEqualTo(5);
    }
}
