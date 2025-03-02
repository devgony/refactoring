package chapter06;

import java.util.Date;

import lombok.AllArgsConstructor;

class ExtractFunction {
    @AllArgsConstructor
    static public class Invoice {
        String customer;
        Date dueDate;
        Order[] orders;
    }

    @AllArgsConstructor
    static public class Order {
        int amount;
    }

    static String printOwing(Invoice invoice) {
        int outstanding = 0;

        String result = printBanner();

        // calculate outstanding
        for (Order o : invoice.orders) {
            outstanding += o.amount;
        }

        invoice.dueDate = new Date();

        result = printDetails(invoice, outstanding);

        return result;
    }

    private static String printDetails(Invoice invoice, int outstanding) {
        String result = "";
        result += "name: " + invoice.customer + "\n";
        result += "amount: " + outstanding + "\n";
        result += "due: " + invoice.dueDate;

        return result;
    }

    private static String printBanner() {
        String result = "";
        result += "***********************\n";
        result += "**** Customer Owes ****\n";
        result += "***********************\n";
        return result;
    }
}
