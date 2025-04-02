package chapter11;

import java.util.HashMap;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.ToString;

class ReplaceConstructorWithFactoryFunction {
    @ToString
    @EqualsAndHashCode
    static class Employee {
        String _name;
        String _typeCode;

        Employee(String name, String typeCode) {
            this._name = name;
            this._typeCode = typeCode;
        }

        String name() {
            return this._name;
        }

        String type() {
            return legalTypeCodes().get(this._typeCode);
        }

        static Map<String, String> legalTypeCodes() {
            return new HashMap<String, String>() {
                {
                    put("E", "Engineer");
                    put("M", "Manager");
                    put("S", "Salesman");
                }
            };
        }
    }

    static Employee createEmployee(String name, String typeCode) {
        return new Employee(name, typeCode);
    }
}
