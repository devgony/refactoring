package chapter06;

import java.util.ArrayList;
import java.util.List;

class InlineFunction {
    static class Driver {
        int numberOfLateDeliveries;

        public Driver(int numberOfLateDeliveries) {
            this.numberOfLateDeliveries = numberOfLateDeliveries;
        }
    }

    int rating(Driver aDriver) {
        return aDriver.numberOfLateDeliveries > 5 ? 2 : 1;
    }

    static class Customer {
        String name;
        String id;

        public Customer(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }

    static List<String> reportLines(Customer aCustomer) {
        List<String> lines = new ArrayList<>();
        gatherCustomerData(lines, aCustomer);
        return lines;
    }

    static private void gatherCustomerData(List<String> out, Customer aCustomer) {
        out.add("name: " + aCustomer.name);
        out.add("id: " + aCustomer.id);
    }

}
