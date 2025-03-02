package chapter06;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import chapter06.IntroductionParameterObject.NumberRange;
import chapter06.IntroductionParameterObject.OperatingPlan;
import chapter06.IntroductionParameterObject.Station;
import chapter06.IntroductionParameterObject.Reading;

class IntroduceParameterObjectTest {
    @Test
    void test() {
        List<Reading> readings = Arrays.asList(
                new Reading(47, "2016-11-10 09:10"),
                new Reading(53, "2016-11-10 09:20"),
                new Reading(58, "2016-11-10 09:30"),
                new Reading(53, "2016-11-10 09:40"),
                new Reading(51, "2016-11-10 09:50"));

        Station station = new Station("ZB1", readings);
        OperatingPlan operatingPlan = new OperatingPlan(50, 55);

        NumberRange range = new NumberRange(operatingPlan.temperatureFloor, operatingPlan.temperatureCeiling);
        List<Reading> actual = IntroductionParameterObject.readingsOutsideRange(station, range);
        assertThat(actual).containsExactly(
                new Reading(47, "2016-11-10 09:10"),
                new Reading(58, "2016-11-10 09:30"));
    }

}
