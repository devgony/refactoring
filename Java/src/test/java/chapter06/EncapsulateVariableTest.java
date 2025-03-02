package chapter06;

import org.junit.jupiter.api.Test;

import chapter06.EncapsulateVariable.Owner;
import chapter06.EncapsulateVariable.Spaceship;
import static org.assertj.core.api.Assertions.assertThat;

class EncapsulateVariableTest {
    @Test
    void testEncapsulateVariable() {
        Spaceship spaceship = new Spaceship();
        spaceship.owner = Owner.defaultOwner();
        assertThat(spaceship.owner.getFirstName()).isEqualTo("Martin");
        assertThat(spaceship.owner.getLastName()).isEqualTo("Fowler");

        spaceship.setDefaultOwner(new Owner("Rebecca", "Parsons"));
        assertThat(spaceship.owner.getFirstName()).isEqualTo("Rebecca");
        assertThat(spaceship.owner.getLastName()).isEqualTo("Parsons");
    }

    @Test
    void encapsulatingTheValue() {
        Owner owner1 = Owner.defaultOwner();
        assertThat(owner1.getLastName()).isEqualTo("Fowler");

        Owner owner2 = Owner.defaultOwner();
        owner2.setLastName("Parsons");
        assertThat(owner1.getLastName()).isEqualTo("Fowler"); // preserved
        assertThat(owner2.getLastName()).isEqualTo("Parsons");
    }

}
