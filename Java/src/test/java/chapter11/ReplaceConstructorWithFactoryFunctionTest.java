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
        Employee candidate = createEmployee(document.get("name"), document.get("empType"));
        assertThat(candidate).isEqualTo(new Employee("John", "M"));

        Employee leadEngineer = createEmployee(document.get("leadEngineer"), "E");
        assertThat(leadEngineer).isEqualTo(new Employee("Martin", "E"));
    }
}
