package chapter10;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;

class IntroduceSpecialCaseUsingTransform {
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

    static String client1(JsonNode site) {
        JsonNode aCustomer = site.get("customer");
        String customerName;
        if ("unknown".equals(aCustomer.asText()))
            customerName = "occupant";
        else
            customerName = aCustomer.get("name").asText();

        return customerName;
    }

    static String client2(JsonNode site) {
        JsonNode aCustomer = site.get("customer");
        Registery registry = new Registery(new BillingPlans("basic"));
        String plan = ("unknown".equals(aCustomer.asText())) ? registry.billingPlans().basic
                : aCustomer.get("billingPlan").asText();

        return plan;
    }

    static int client3(JsonNode site) {
        JsonNode aCustomer = site.get("customer");
        int weeksDelinquent = ("unknown".equals(aCustomer.asText())) ? 0
                : aCustomer.get("paymentHistory").get("weeksDelinquentInLastYear").asInt();

        return weeksDelinquent;
    }

}
