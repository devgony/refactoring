package chapter11;

import lombok.AllArgsConstructor;

class ReplaceQueryWithParameter {
    @AllArgsConstructor
    static class Thermostat {
        double selectedTemperature;
        double currentTemperature;
    }

    @AllArgsConstructor
    static class HeatingPlan {
        Thermostat thermostat;
        int _min;
        int _max;

        private double targetTemperature(double selectedTemperature) {
            if (selectedTemperature > this._max)
                return this._max;
            else if (selectedTemperature < this._min)
                return this._min;
            else
                return selectedTemperature;
        }

        String client1() {
            if (this.targetTemperature(this.thermostat.selectedTemperature) > thermostat.currentTemperature)
                return "setToHeat";
            else if (this.targetTemperature(this.thermostat.selectedTemperature) < thermostat.currentTemperature)
                return "setToCool";
            else
                return "setOff";
        }

    }
}
