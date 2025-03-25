package chapter10;

import java.time.ZoneId;

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
        Result result;
        if (employee.isSeparated) {
            result = new Result(0, "SEP");
        } else {
            if (employee.isRetired) {
                result = new Result(0, "RET");
            } else {
                result = someFinalComputation();
            }
        }
        return result;
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
        if (anInstrument.capital > 0) {
            if (anInstrument.interestRate > 0 && anInstrument.duration > 0) {
                result = (anInstrument.income / anInstrument.duration) *
                        anInstrument.adjustmentFactor;
            }
        }
        return result;
    }
}
