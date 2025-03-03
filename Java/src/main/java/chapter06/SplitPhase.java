package chapter06;

import java.io.File;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;

class SplitPhase {
    @AllArgsConstructor
    static class Product {
        private double basePrice;
        private double discountRate;
        private double discountThreshold;
    }

    @AllArgsConstructor
    static class ShippingMethod {
        private double discountThreshold;
        private double discountedFee;
        private double feePerCase;
    }

    static class PriceData {
    }

    static double priceOrder(Product product, int quantity, ShippingMethod shippingMethod) {
        double basePrice = product.basePrice * quantity;
        double discount = Math.max(quantity - product.discountThreshold, 0) *
                product.basePrice *
                product.discountRate;
        PriceData priceData = new PriceData();
        double price = applyShipping(priceData, quantity, shippingMethod, basePrice, discount);
        return price;
    }

    private static double applyShipping(PriceData priceData, int quantity, ShippingMethod shippingMethod,
            double basePrice,
            double discount) {
        double shippingPerCase = basePrice > shippingMethod.discountThreshold
                ? shippingMethod.discountedFee
                : shippingMethod.feePerCase;
        double shippingCost = quantity * shippingPerCase;
        double price = basePrice - discount + shippingCost;
        return price;
    }

    static class Order {
        String status;
    }

    public static void main(String[] args) {
        try {
            if (args.length == 0)
                throw new RuntimeException("must supply a filename");
            String filename = args[args.length - 1];
            File input = Paths.get(filename).toFile();
            ObjectMapper mapper = new ObjectMapper();
            Order[] orders = mapper.readValue(input, Order[].class);
            if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
                System.out.println(Stream.of(orders).filter(o -> "ready".equals(o.status)).count());
            else
                System.out.println(orders.length);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

}
