package chapter06;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

class CombineFunctionsIntoClass {
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
        // 예시 로직: 월과 연도에 따라 기본 요율을 계산
        int rate = 100; // 기본 요율
        if (month == 12) { // 12월은 특별 할인
            rate -= 20;
        }
        if (year > 2020) { // 2020년 이후는 요율 증가
            rate += 10;
        }
        return rate;
    }

    int taxThreshold(int year) {
        // 예시 로직: 연도에 따라 세금 기준을 계산
        int threshold = 50; // 기본 세금 기준
        if (year > 2020) { // 2020년 이후는 기준 증가
            threshold += 10;
        }
        return threshold;
    }

    int client1() {
        Reading reading = acquireReading();
        int baseCharge = baseRate(reading.month, reading.year) * reading.quantity;

        return baseCharge;
    }

    int client2() {
        Reading reading = acquireReading();
        int baseCharge = baseRate(reading.month, reading.year) * reading.quantity;
        int taxableCharge = Math.max(0, baseCharge - taxThreshold(reading.year));

        return taxableCharge;
    }

    int client3() {
        Reading aReading = acquireReading();
        int basicChargeAmount = calculateBaseCharge(aReading);

        return basicChargeAmount;
    }

    int calculateBaseCharge(Reading aReading) {
        return baseRate(aReading.month, aReading.year) * aReading.quantity;
    }

}
