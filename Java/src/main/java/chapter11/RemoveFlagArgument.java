package chapter11;

import java.util.Arrays;

import lombok.AllArgsConstructor;

class RemoveFlagArgument {
    @AllArgsConstructor
    static class Order {
        String deliveryState;
        PlaceOn placedOn;
    }

    static class PlaceOn {
        public PlaceOn() {
        }

        public int plusDays(int i) {
            return i;
        }
    }

    static int deliveryDate(Order anOrder, boolean isRush) {
        if (isRush) {
            int deliveryTime;
            if (Arrays.asList("MA", "CT").contains(anOrder.deliveryState))
                deliveryTime = 1;
            else if (Arrays.asList("NY", "NH").contains(anOrder.deliveryState))
                deliveryTime = 2;
            else
                deliveryTime = 3;
            return anOrder.placedOn.plusDays(1 + deliveryTime);
        } else {
            int deliveryTime;
            if (Arrays.asList("MA", "CT", "NY").contains(anOrder.deliveryState))
                deliveryTime = 2;
            else if (Arrays.asList("ME", "NH").contains(anOrder.deliveryState))
                deliveryTime = 3;
            else
                deliveryTime = 4;
            return anOrder.placedOn.plusDays(2 + deliveryTime);
        }
    }
}
