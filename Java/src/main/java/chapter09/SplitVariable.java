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
        double primaryAcceleration = scenario.primaryForce / scenario.mass;
        double primaryTime = Math.min(time, scenario.delay);
        result = 0.5 * primaryAcceleration * primaryTime * primaryTime;
        double secondaryTime = time - scenario.delay;
        if (secondaryTime > 0) {
            double primaryVelocity = primaryAcceleration * scenario.delay;
            primaryAcceleration = (scenario.primaryForce + scenario.secondaryForce) / scenario.mass;
            result += primaryVelocity * secondaryTime +
                    0.5 * primaryAcceleration * secondaryTime * secondaryTime;
        }
        return result;
    }
}
