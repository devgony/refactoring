package chapter11;

import org.junit.jupiter.api.Test;
import chapter11.ReplaceQueryWithParameter.HeatingPlan;
import chapter11.ReplaceQueryWithParameter.Thermostat;
import static org.assertj.core.api.Assertions.assertThat;

class ReplaceQueryWithParameterTest {
    @Test
    void client1() {
        Thermostat thermostat = new Thermostat(18, 25);
        HeatingPlan heatingPlan = new HeatingPlan(thermostat, 20, 26);
        assertThat(heatingPlan.client1()).isEqualTo("setToCool");

        thermostat = new Thermostat(25, 18);
        heatingPlan = new HeatingPlan(thermostat, 15, 26);
        assertThat(heatingPlan.client1()).isEqualTo("setToHeat");

        thermostat = new Thermostat(25, 25);
        heatingPlan = new HeatingPlan(thermostat, 15, 26);
        assertThat(heatingPlan.client1()).isEqualTo("setOff");
    }
}
