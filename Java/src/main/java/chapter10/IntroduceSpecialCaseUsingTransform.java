package chapter10;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;

class IntroduceSpecialCaseUsingTransform {
    @AllArgsConstructor
    static class Site {
        Customer _customer;

        Customer customer() {
            return this._customer;
        }
    }

    @AllArgsConstructor
    static class Customer {
        String _name;
        String _billingPlan;
        PaymentHistory _paymentHistory;

        String name() {
            return _name;
        }

        void name(String arg) {
            _name = arg;
        }

        String billingPlan() {
            return _billingPlan;
        }

        void billingPlan(String arg) {
            _billingPlan = arg;
        }

        PaymentHistory paymentHistory() {
            return _paymentHistory;
        }

    }

    @AllArgsConstructor
    static class PaymentHistory {
        int weeksDelinquentInLastYear;

        int weeksDelinquentInLastYear() {
            return weeksDelinquentInLastYear;
        }
    }

    @AllArgsConstructor
    static class Registery {
        BillingPlans _billingPlans;

        BillingPlans billingPlans() {
            return _billingPlans;
        }
    }

    @AllArgsConstructor
    static class BillingPlans {
        String basic;
    }

    static String client1(JsonNode aCustomer) {
        String customerName;
        if ("unknown".equals(aCustomer.asText()))
            customerName = "occupant";
        else
            customerName = aCustomer.get("name").asText();

        return customerName;
    }

    static String client2(JsonNode aCustomer) {
        Registery registry = new Registery(new BillingPlans("basic"));
        String plan = ("unknown".equals(aCustomer.asText())) ? registry.billingPlans().basic
                : aCustomer.get("billingPlan").asText();

        return plan;
    }

    static int client3(JsonNode aCustomer) {
        int weeksDelinquent = ("unknown".equals(aCustomer.asText())) ? 0
                : aCustomer.get("paymentHistory").get("weeksDelinquentInLastYear").asInt();

        return weeksDelinquent;
    }

}
