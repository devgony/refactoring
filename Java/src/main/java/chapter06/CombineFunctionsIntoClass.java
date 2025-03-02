package chapter06;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

class CombineFunctionsIntoClass {
    @AllArgsConstructor
    @EqualsAndHashCode
    static class Reading {
        String _customer;
        int _quantity;
        int _month;
        int _year;

        String customer() {
            return _customer;
        }

        int quantity() {
            return _quantity;
        }

        int month() {
            return _month;
        }

        int year() {
            return _year;
        }

        int baseCharge() {
            return baseRate(this._month, this._year) * this._quantity;
        }

        int taxableCharge() {
            return Math.max(0, this.baseCharge() - taxThreshold(this._year));
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
        int baseCharge = reading.baseCharge();

        return baseCharge;
    }

    int client2() {
        Reading reading = acquireReading();
        int taxableCharge = reading.taxableCharge();

        return taxableCharge;
    }

    int client3() {
        Reading aReading = acquireReading();
        int basicChargeAmount = aReading.baseCharge();

        return basicChargeAmount;
    }

}
