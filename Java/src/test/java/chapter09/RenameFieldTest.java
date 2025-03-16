package chapter09;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import chapter09.RenameField.Organization;
import utils.ObjectBuilder;

class RenameFieldTest {
    @Test
    void client1() {
        Organization organization = new Organization(
                ObjectBuilder.readValue(
                        "{\"title\": \"Acme Gooseberries\", \"country\": \"GB\"}"));

        assertThat(organization.title()).isEqualTo("Acme Gooseberries");
        assertThat(organization.country()).isEqualTo("GB");
    }
}
