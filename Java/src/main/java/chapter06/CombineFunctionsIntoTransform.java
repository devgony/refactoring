package chapter06;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

class CombineFunctionsIntoTransform {
    @AllArgsConstructor
    @EqualsAndHashCode
    static class Reading {
        String customer;
        int quantity;
        int month;
        int year;
    }

    static Reading acquireReading() {
        return new Reading("ivan", 10, 5, 2017);
    }

    static int baseRate(int month, int year) {
        return 0;
    }

    int taxThreshold(int year) {
        return 0;
    }

    void client1() {
        Reading reading = acquireReading();
        int baseCharge = baseRate(reading.month, reading.year) * reading.quantity;
    }

    void client2() {
        Reading reading = acquireReading();
        int baseCharge = baseRate(reading.month, reading.year) * reading.quantity;
        int taxableCharge = Math.max(0, baseCharge - taxThreshold(reading.year));
    }

}
