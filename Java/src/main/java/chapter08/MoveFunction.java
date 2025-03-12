package chapter08;

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

        int totalTime = calculateTime.get();
        double pace = totalTime / 60 / totalDistance(points);

        return new Summary(totalTime, totalDistance(points), pace);
    }

    static double totalDistance(Point[] points) {
        double result = 0;
        for (int i = 1; i < points.length; i++) {
            result += distance(points[i - 1], points[i]);
        }
        return result;
    };

    static double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    // function radians(degrees) { ... }

    // Example: Moving between Classes
    @AllArgsConstructor
    static class AccountType {
        boolean isPremium;

        double overdraftCharge(int daysOverdrawn) {
            if (this.isPremium) {
                double baseCharge = 10;
                if (daysOverdrawn <= 7)
                    return baseCharge;
                else
                    return baseCharge + (daysOverdrawn - 7) * 0.85;
            } else
                return daysOverdrawn * 1.75;
        }
    }

    @AllArgsConstructor
    static class Account {
        AccountType type;
        int _daysOverdrawn;

        double bankCharge() {
            double result = 4.5;
            if (this._daysOverdrawn > 0)
                result += this.type.overdraftCharge(this._daysOverdrawn);
            return result;
        }
    }

}
