package chapter09;

import lombok.AllArgsConstructor;

class SplitVariable {
    @AllArgsConstructor
    static class Scenario {
        double primaryForce;
        double mass;
        double delay;
        double secondaryForce;
    }

    static double distanceTravelled(Scenario scenario, int time) {
        double result;
        final double primaryAcceleration = scenario.primaryForce / scenario.mass;
        double primaryTime = Math.min(time, scenario.delay);
        result = 0.5 * primaryAcceleration * primaryTime * primaryTime;
        double secondaryTime = time - scenario.delay;
        if (secondaryTime > 0) {
            double primaryVelocity = primaryAcceleration * scenario.delay;
            final double secondaryAcceleration = (scenario.primaryForce + scenario.secondaryForce) / scenario.mass;
            result += primaryVelocity * secondaryTime +
                    0.5 * secondaryAcceleration * secondaryTime * secondaryTime;
        }
        return result;
    }

    static double discount(double inputValue, int quantity) {
        double result = inputValue;
        if (result > 50)
            result = result - 2;
        if (quantity > 100)
            result = result - 1;

        return result;
    }
}
