package chapter11;

import java.util.Map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
class ReplaceErrorCodeWithException {
    CountryData countryData;

    @AllArgsConstructor
    static class CountryData {
        Map<String, Integer> shippingRules;
    }

    @AllArgsConstructor
    static class Order {
        String country;
    }

    Integer localShippingRules(String country) throws OrderProcessingError {
        Integer data = countryData.shippingRules.get(country);
        if (data != null)
            // return new ShippingRules(data);
            return shittyShippingRules(data);
        else
            throw new OrderProcessingError("23");
    }

    private Integer shittyShippingRules(Integer data) {
        return 0;
    }

    int calculateShippingCosts(Order anOrder) throws OrderProcessingError {
        // irrelevent code
        int shippingRules = localShippingRules(anOrder.country);
        if (shippingRules < 0)
            return shippingRules;
        // more irrelevent code..
        return 0;
    }

    static class OrderProcessingError extends Exception {
        String code;

        OrderProcessingError(String errorCode) {
            super("Order processing error " + errorCode);
            this.code = errorCode;
        }

        String name() {
            return "OrderProcessingError";
        }
    }
}
