package chapter11;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import chapter11.ReplaceConstructorWithFactoryFunction.Employee;
import static org.assertj.core.api.Assertions.assertThat;
import static chapter11.ReplaceConstructorWithFactoryFunction.*;

class ReplaceConstructorWithFactoryFunctionTest {
    @Test
    void client1() {

        Map<String, String> document = new HashMap<String, String>() {
            {
                put("name", "John");
                put("empType", "M");
                put("leadEngineer", "Martin");
            }
        };
        Employee candidate = createManager(document.get("name"));
        assertThat(candidate).isEqualTo(new Employee("John", "M"));

        Employee leadEngineer = createEngineer(document.get("leadEngineer"));
        assertThat(leadEngineer).isEqualTo(new Employee("Martin", "E"));
    }
}
