package chapter10;

import lombok.AllArgsConstructor;

class ConsolidateConditionalExpression {
    @AllArgsConstructor
    static class Employee {
        int seniority;
        int monthsDisabled;
        boolean isPartTime;
        boolean onVacation;
    }

    static int disabilityAmount(Employee anEmployee) {
        if (isNotEligableForDisability(anEmployee))
            return 0;
        // compute the disability amount
        //
        return 1;
    }

    private static boolean isNotEligableForDisability(Employee anEmployee) {
        return anEmployee.seniority < 2 || anEmployee.monthsDisabled > 12 || anEmployee.isPartTime;
    }

    static double usingAnds(Employee anEmployee) {
        if (anEmployee.onVacation) {
            if (anEmployee.seniority > 10)
                return 1;
        }
        return 0.5;
    }
}
