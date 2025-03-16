package chapter09;

class ReplaceMagicLiteral {
    private static final double STANDARD_GRAVITY = 9.81;

    static double potentialEnergy(double mass, double height) {
        return mass * STANDARD_GRAVITY * height;
    }
}
