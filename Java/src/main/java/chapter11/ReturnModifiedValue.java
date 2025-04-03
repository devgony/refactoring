package chapter11;

import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;

class ReturnModifiedValue {
    int totalAscent = 0;
    int totalTime = 20000;
    int totalDistance = 10;
    List<Point> points = Arrays.asList(new Point(10), new Point(30));

    @AllArgsConstructor
    static class Point {
        int elevation;
    }

    double client1() {
        calculateAscent();
        calculateTime();
        calculateDistance();
        double pace = totalTime / 60 / totalDistance;

        return pace;
    }

    private void calculateDistance() {
        totalDistance += points.size();
    }

    private void calculateTime() {
        totalTime += totalAscent;
    }

    private void calculateAscent() {
        for (int i = 1; i < points.size(); i++) {
            int verticalChange = points.get(i).elevation - points.get(i - 1).elevation;
            totalAscent += (verticalChange > 0) ? verticalChange : 0;
        }
    }
}
