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

        double targetTemperature() {
            if (thermostat.selectedTemperature > this._max)
                return this._max;
            else if (thermostat.selectedTemperature < this._min)
                return this._min;
            else
                return thermostat.selectedTemperature;
        }

        String client1() {
            if (this.targetTemperature() > thermostat.currentTemperature)
                return "setToHeat";
            else if (this.targetTemperature() < thermostat.currentTemperature)
                return "setToCool";
            else
                return "setOff";
        }

    }
}
