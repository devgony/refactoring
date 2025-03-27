package chapter10;

import lombok.AllArgsConstructor;

class IntroduceSpecialCase {
    @AllArgsConstructor
    static class Site {
        Customer _customer;

        Customer customer() {
            return (_customer.name() == "unknown") ? new UnknownCustomer(_customer) : _customer;
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

        boolean isUnknown() {
            return false;
        }
    }

    static class UnknownCustomer extends Customer {
        UnknownCustomer(Customer customer) {
            super(customer.name(), customer.billingPlan(), customer.paymentHistory());
        }

        @Override
        boolean isUnknown() {
            return true;
        }

        @Override
        String name() {
            return "occupant";
        }

    }

    static boolean isUnknown(Customer customer) {
        return customer.isUnknown();
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
        return aCustomer.name();
    }

    static String client2(Customer aCustomer) {
        Registery registry = new Registery(new BillingPlans("basic"));
        String plan = isUnknown(aCustomer) ? registry.billingPlans().basic
                : aCustomer.billingPlan();

        return plan;
    }

    static String client3(Customer aCustomer) {
        if (!isUnknown(aCustomer))
            aCustomer.billingPlan("newPlan");

        return aCustomer.billingPlan();
    }

    static int client4(Customer aCustomer) {
        int weeksDelinquent = (isUnknown(aCustomer)) ? 0
                : aCustomer.paymentHistory().weeksDelinquentInLastYear();

        return weeksDelinquent;
    }
}
