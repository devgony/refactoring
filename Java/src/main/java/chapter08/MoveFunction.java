package chapter08;

import java.util.function.BiFunction;
import java.util.function.Supplier;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

class MoveFunction {
    @AllArgsConstructor
    static class Point {
        private int x;
        private int y;
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    static class Summary {
        private int _time;
        private double _distance;
        private double _pace;
    }

    static Summary trackSummary(Point[] points) {
        Supplier<Integer> calculateTime = () -> {
            return 600;
        };

        Supplier<Double> calculateDistance = () -> {
            return topCalculateDistance(points);
        };
        int totalTime = calculateTime.get();
        double totalDistance = calculateDistance.get();
        double pace = totalTime / 60 / totalDistance;

        return new Summary(totalTime, totalDistance, pace);
    }

    static double topCalculateDistance(Point[] points) {
        BiFunction<Point, Point, Double> distance = (p1, p2) -> {
            return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
        };
        // function radians(degrees) { ... }
        //
        double result = 0;
        for (int i = 1; i < points.length; i++) {
            result += distance.apply(points[i - 1], points[i]);
        }
        return result;
    };

    // Example: Moving between Classes
    @AllArgsConstructor
    static class AccountType {
        boolean isPremium;
    }

    @AllArgsConstructor
    static class Account {
        AccountType type;
        int _daysOverdrawn;

        double bankCharge() {
            double result = 4.5;
            if (this._daysOverdrawn > 0)
                result += this.overdraftCharge();
            return result;
        }

        double overdraftCharge() {
            if (this.type.isPremium) {
                double baseCharge = 10;
                if (this._daysOverdrawn <= 7)
                    return baseCharge;
                else
                    return baseCharge + (this._daysOverdrawn - 7) * 0.85;
            } else
                return this._daysOverdrawn * 1.75;
        }
    }

}
