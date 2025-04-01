package chapter11;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import chapter11.RemoveSettingMethod.Person;

class RemoveSettingMethodTest {
    @Test
    void client1() {
        Person martin = new Person();
        martin.name("martin");
        martin.id("1234");

        assertThat(martin.name()).isEqualTo("martin");
        assertThat(martin.id()).isEqualTo("1234");
    }

}
