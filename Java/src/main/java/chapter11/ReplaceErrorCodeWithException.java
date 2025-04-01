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

    Integer localShippingRules(String country) {
        Integer data = countryData.shippingRules.get(country);
        if (data != null)
            // return new ShippingRules(data);
            return shittyShippingRules(data);
        else
            return -23;
    }

    private Integer shittyShippingRules(Integer data) {
        return 0;
    }

    int calculateShippingCosts(Order anOrder) {
        // irrelevent code
        int shippingRules = localShippingRules(anOrder.country);
        if (shippingRules < 0)
            return shippingRules;
        // more irrelevent code..
        return 0;
    }
}
