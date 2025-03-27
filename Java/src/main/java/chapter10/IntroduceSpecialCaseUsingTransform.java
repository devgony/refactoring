package chapter10;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import lombok.AllArgsConstructor;
import utils.ObjectBuilder;

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

    static JsonNode enrichSite(JsonNode site) {
        ObjectNode result = site.deepCopy();
        ObjectNode unknownCustomer = ObjectBuilder.readValue("{\"isUnknown\": true, \"name\": \"occupant\" }");
        if (isUnknown(result.get("customer")))
            result.set("customer", unknownCustomer);
        else
            ((ObjectNode) result.get("customer")).put("isUnknown", false);

        return result;
    }

    static boolean isUnknown(JsonNode aCustomer) {
        if ("unknown".equals(aCustomer.asText()))
            return true;

        JsonNode isUnknown = aCustomer.get("isUnknown");
        if (isUnknown == null)
            return false;

        return isUnknown.asBoolean();
    }

    static String client1(JsonNode rawSite) {
        JsonNode site = enrichSite(rawSite);
        JsonNode aCustomer = site.get("customer");

        return aCustomer.get("name").asText();
    }

    static String client2(JsonNode rawSite) {
        JsonNode site = enrichSite(rawSite);
        JsonNode aCustomer = site.get("customer");
        Registery registry = new Registery(new BillingPlans("basic"));
        String plan = (isUnknown(aCustomer)) ? registry.billingPlans().basic
                : aCustomer.get("billingPlan").asText();

        return plan;
    }

    static int client3(JsonNode rawSite) {
        JsonNode site = enrichSite(rawSite);
        JsonNode aCustomer = site.get("customer");
        int weeksDelinquent = (isUnknown(aCustomer)) ? 0
                : aCustomer.get("paymentHistory").get("weeksDelinquentInLastYear").asInt();

        return weeksDelinquent;
    }

}
