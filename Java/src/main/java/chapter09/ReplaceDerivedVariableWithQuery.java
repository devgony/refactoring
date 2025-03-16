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
        double _production;
        List<Adjustment> _adjustments;

        ProductionPlan(double production) {
            this._production = production;
            this._adjustments = new ArrayList<>();
        }

        double production() {
            return this._production;
        }

        void applyAdjustment(Adjustment anAdjustment) {
            this._adjustments.add(anAdjustment);
            this._production += anAdjustment.amount();
        }
    }
}
