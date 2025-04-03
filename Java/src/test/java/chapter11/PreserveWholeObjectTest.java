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
        HeatingPlan aPlan = new HeatingPlan(range);
        boolean actual = aPlan.withinRange(aRoom.daysTempRange);
        assertTrue(actual);

        range = new Range(10, 21);
        actual = aPlan.withinRange(range);
        assertFalse(actual);
        // if (!aPlan.withinRange(low, high))
        // System.out.println("room temperature went outside range");
    }

    @Test
    void client2() {
        Range range = new Range(10, 20);
        HeatingPlan aPlan = new HeatingPlan(range);
        Range tempRange = (new Room(range)).daysTempRange;
        boolean isWithinRange = aPlan.withinRange2(tempRange);
        assertTrue(isWithinRange);

        range = new Range(10, 20 + 1);
        tempRange = (new Room(range)).daysTempRange;
        isWithinRange = aPlan.withinRange2(tempRange);
        assertFalse(isWithinRange);
    }

}
