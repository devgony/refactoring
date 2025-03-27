package chapter10;

import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;
import utils.ObjectBuilder;

class IntroduceSpecialCaseUsingAnObjectLiteral {
    @AllArgsConstructor
    static class Site {
        ObjectNode _customer;

        ObjectNode customer() {
            return this._customer.get("name").asText().equals("unknown") ? createUnknownCustomer() : _customer;
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

    static ObjectNode createUnknownCustomer() {
        return ObjectBuilder.readValue("{\"isUnknown\": true, \"name\": \"occupant\"}");
    }

    static boolean isUnknown(ObjectNode arg) {
        return arg.get("isUnknown").asBoolean();
    }

    static String client1(ObjectNode aCustomer) {
        return aCustomer.get("name").asText();
    }

    static String client2(ObjectNode aCustomer) {
        Registery registry = new Registery(new BillingPlans("basic"));
        String plan = (isUnknown(aCustomer)) ? registry.billingPlans().basic
                : aCustomer.get("billingPlan").asText();

        return plan;
    }

    static int client3(ObjectNode aCustomer) {
        int weeksDelinquent = isUnknown(aCustomer) ? 0
                : aCustomer.get("paymentHistory").get("weeksDelinquentInLastYear").asInt();

        return weeksDelinquent;
    }
}
