package chapter10;

import lombok.AllArgsConstructor;

class DecomposeConditional {
    @AllArgsConstructor
    static class Date {
        int currentDate;

        boolean isBefore(int adate) {
            return currentDate < adate;
        }

        boolean isAfter(int adate) {
            return currentDate > adate;
        }
    }

    @AllArgsConstructor
    static class Plan {
        int summerStart;
        int summerEnd;
        double summerRate;
        double regularRate;
        double regularServiceCharge;
    }

    static double client1(Date aDate, Plan plan, int quantity) {
        double charge;
        if (summer(aDate, plan))
            charge = quantity * plan.summerRate;
        else
            charge = quantity * plan.regularRate + plan.regularServiceCharge;

        return charge;
    }

    private static boolean summer(Date aDate, Plan plan) {
        return !aDate.isBefore(plan.summerStart) && !aDate.isAfter(plan.summerEnd);
    }

}
