package chapter10;

import lombok.AllArgsConstructor;

class IntroduceSpecialCaseUsingAnObjectLiteral {
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

    static String client1(Customer aCustomer) {
        String customerName;
        if (aCustomer.name() == "unknown")
            customerName = "occupant";
        else
            customerName = aCustomer.name();

        return customerName;
    }

    static String client2(Customer aCustomer) {
        Registery registry = new Registery(new BillingPlans("basic"));
        String plan = (aCustomer.name() == "unknown") ? registry.billingPlans().basic
                : aCustomer.billingPlan();

        return plan;
    }

    static int client3(Customer aCustomer) {
        int weeksDelinquent = (aCustomer.name() == "unknown") ? 0
                : aCustomer.paymentHistory().weeksDelinquentInLastYear();

        return weeksDelinquent;
    }
}
