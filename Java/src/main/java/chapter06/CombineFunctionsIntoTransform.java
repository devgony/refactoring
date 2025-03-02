package chapter06;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

class CombineFunctionsIntoTransform {
    @EqualsAndHashCode
    static class Reading {
        String customer;
        int quantity;
        int month;
        int year;
        Optional<Integer> baseCharge = Optional.empty();
        Optional<Integer> taxableCharge = Optional.empty();

        Reading(String customer, int quantity, int month, int year) {
            this.customer = customer;
            this.quantity = quantity;
            this.month = month;
            this.year = year;
        }
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

    static int taxThreshold(int year) {
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
        reading = enrichReading(reading);
        int taxableCharge = reading.taxableCharge.get();

        return taxableCharge;
    }

    int client3() {
        Reading reading = acquireReading();
        reading = enrichReading(reading);
        int basicChargeAmount = reading.baseCharge.get();

        return basicChargeAmount;
    }

    int calculateBaseCharge(Reading reading) {
        return baseRate(reading.month, reading.year) * reading.quantity;
    }

    Reading enrichReading(Reading original) {
        Reading result = new Reading(original.customer, original.quantity, original.month, original.year);
        result.baseCharge = Optional.of(calculateBaseCharge(result));
        result.taxableCharge = Optional.of(Math.max(0, result.baseCharge.get() - taxThreshold(result.year)));

        return result;

    }

}
