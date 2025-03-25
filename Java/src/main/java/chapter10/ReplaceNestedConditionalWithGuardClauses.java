package chapter10;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

class ReplaceNestedConditionalWithGuardClauses {
    @AllArgsConstructor
    static class Employee {
        boolean isSeparated;
        boolean isRetired;
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    static class Result {
        double amount;
        String reasonCode;
    }

    static Result payAmount(Employee employee) {
        if (employee.isSeparated) {
            return new Result(0, "SEP");
        }
        if (employee.isRetired) {
            return new Result(0, "RET");
        }

        return someFinalComputation();
    }

    static private Result someFinalComputation() {
        return new Result(1, "OK");
    }

    @AllArgsConstructor
    static class Instrument {
        double capital;
        double income;
        double duration;
        double interestRate;
        double adjustmentFactor;
    }

    static double adjustedCapital(Instrument anInstrument) {
        double result = 0;
        if (anInstrument.capital <= 0 || anInstrument.interestRate <= 0 || anInstrument.duration <= 0) {
            return result;
        }

        return (anInstrument.income / anInstrument.duration) *
                anInstrument.adjustmentFactor;
    }
}
