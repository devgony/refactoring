package chapter06;

import org.junit.jupiter.api.Test;

import chapter06.EncapsulateVariable.Owner;
import chapter06.EncapsulateVariable.Spaceship;
import static org.assertj.core.api.Assertions.assertThat;

class EncapsulateVariableTest {
    @Test
    void testEncapsulateVariable() {
        Spaceship spaceship = new Spaceship();
        Owner defaultOwner = new Owner("Martin", "Fowler");
        spaceship.owner = defaultOwner;
        defaultOwner = new Owner("Rebecca", "Parsons");

        assertThat(spaceship.owner.getFirstName()).isEqualTo("Martin");
        assertThat(spaceship.owner.getLastName()).isEqualTo("Fowler");
        assertThat(defaultOwner.getFirstName()).isEqualTo("Rebecca");
        assertThat(defaultOwner.getLastName()).isEqualTo("Parsons");
    }

}
