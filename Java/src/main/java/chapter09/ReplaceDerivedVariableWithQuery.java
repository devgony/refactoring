package chapter09;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;

class ReplaceDerivedVariableWithQuery {
    @AllArgsConstructor
    static class Adjustment {
        double _amount;
        String _type;

        double amount() {
            return this._amount;
        }

        String type() {
            return this._type;
        }
    }

    static class ProductionPlan {
        List<Adjustment> _adjustments;

        ProductionPlan(double production) {
            this._adjustments = new ArrayList<>();
        }

        double production() {
            return this._adjustments.stream().mapToDouble(Adjustment::amount).sum();
        }

        void applyAdjustment(Adjustment anAdjustment) {
            this._adjustments.add(anAdjustment);
        }
    }

    static class ProductionPlan2 {
        double _initialProduction;
        List<Adjustment> _adjustments;
        double _productionAccumulator;

        ProductionPlan2(double production) {
            this._initialProduction = production;
            this._productionAccumulator = 0;
            this._adjustments = new ArrayList<>();
        }

        double production() {
            return this._initialProduction + this._productionAccumulator;
        }

        void applyAdjustment(Adjustment anAdjustment) {
            this._adjustments.add(anAdjustment);
            this._initialProduction += anAdjustment.amount();
        }
    }
}
