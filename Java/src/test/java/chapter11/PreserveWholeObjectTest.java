package chapter11;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import chapter11.PreserveWholeObject.HeatingPlan;
import chapter11.PreserveWholeObject.Range;
import chapter11.PreserveWholeObject.Room;

class PreserveWholeObjectTest {
    @Test
    void client1() {
        Range range = new Range(10, 20);
        Room aRoom = new Room(range);
        int low = aRoom.daysTempRange.low;
        int high = aRoom.daysTempRange.high;
        HeatingPlan aPlan = new HeatingPlan(range);
        boolean actual = aPlan.xxNEWwithinRange(aRoom.daysTempRange);
        assertTrue(actual);

        actual = aPlan.withinRange(low, high + 1);
        assertFalse(actual);
        // if (!aPlan.withinRange(low, high))
        // System.out.println("room temperature went outside range");
    }

    @Test
    void client2() {
        Range range = new Range(10, 20);
        Room aRoom = new Room(range);
        int low = aRoom.daysTempRange.low;
        int high = aRoom.daysTempRange.high;
        HeatingPlan aPlan = new HeatingPlan(range);
        boolean actual = aPlan.withinRange2(low, high);
        assertTrue(actual);

        actual = aPlan.withinRange2(low, high + 1);
        assertFalse(actual);
    }
}
