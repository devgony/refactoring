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
        if (!aDate.isBefore(plan.summerStart) && !aDate.isAfter(plan.summerEnd))
            charge = quantity * plan.summerRate;
        else
            charge = quantity * plan.regularRate + plan.regularServiceCharge;

        return charge;
    }

}
