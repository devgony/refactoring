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
            return rushDeliveryDate(anOrder);
        } else {
            return regularDeliveryDate(anOrder);
        }
    }

    static int rushDeliveryDate(Order anOrder) {
        int deliveryTime;
        if (Arrays.asList("MA", "CT").contains(anOrder.deliveryState))
            deliveryTime = 1;
        else if (Arrays.asList("NY", "NH").contains(anOrder.deliveryState))
            deliveryTime = 2;
        else
            deliveryTime = 3;
        return anOrder.placedOn.plusDays(1 + deliveryTime);
    }

    static int regularDeliveryDate(Order anOrder) {
        int deliveryTime;
        if (Arrays.asList("MA", "CT", "NY").contains(anOrder.deliveryState))
            deliveryTime = 2;
        else if (Arrays.asList("ME", "NH").contains(anOrder.deliveryState))
            deliveryTime = 3;
        else
            deliveryTime = 4;
        return anOrder.placedOn.plusDays(2 + deliveryTime);
    }

    static int deliveryDate2(Order anOrder, boolean isRush) {
        int result;
        int deliveryTime;
        if (anOrder.deliveryState == "MA" || anOrder.deliveryState == "CT")
            deliveryTime = isRush ? 1 : 2;
        else if (anOrder.deliveryState == "NY" || anOrder.deliveryState == "NH") {
            deliveryTime = 2;
            if (anOrder.deliveryState == "NH" && !isRush)
                deliveryTime = 3;
        } else if (isRush)
            deliveryTime = 3;
        else if (anOrder.deliveryState == "ME")
            deliveryTime = 3;
        else
            deliveryTime = 4;
        result = anOrder.placedOn.plusDays(2 + deliveryTime);
        if (isRush)
            result = minusDays(result, 1);
        return result;
    }

    private static int minusDays(int result, int i) {
        return result - i;
    }

    static int rushDeliveryDate2(Order anOrder) {
        return deliveryDate2(anOrder, true);
    }

    static int regularDeliveryDate2(Order anOrder) {
        return deliveryDate2(anOrder, false);
    }
}
