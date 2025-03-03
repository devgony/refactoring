package chapter06;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
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
        double basePrice;
        int quantity;
        double discount;

        public PriceData(double basePrice, int quantity, double discount) {
            this.basePrice = basePrice;
            this.quantity = quantity;
            this.discount = discount;
        }

    }

    static double priceOrder(Product product, int quantity, ShippingMethod shippingMethod) {
        PriceData priceData = calculatePricingData(product, quantity);

        return applyShipping(priceData, shippingMethod);
    }

    private static PriceData calculatePricingData(Product product, int quantity) {
        double basePrice = product.basePrice * quantity;
        double discount = Math.max(quantity - product.discountThreshold, 0) *
                product.basePrice *
                product.discountRate;

        return new PriceData(basePrice, quantity, discount);
    }

    private static double applyShipping(PriceData priceData, ShippingMethod shippingMethod) {
        double shippingPerCase = priceData.basePrice > shippingMethod.discountThreshold
                ? shippingMethod.discountedFee
                : shippingMethod.feePerCase;
        double shippingCost = priceData.quantity * shippingPerCase;

        return priceData.basePrice - priceData.discount + shippingCost;
    }

    static class Order {
        String status;
    }

    public static void main(String[] args) {
        try {
            run(args);
        } catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
    }

    private static long run(String[] args) throws IOException, StreamReadException, DatabindException {
        if (args.length == 0)
            throw new RuntimeException("must supply a filename");
        String filename = args[args.length - 1];
        File input = Paths.get(filename).toFile();
        ObjectMapper mapper = new ObjectMapper();
        Order[] orders = mapper.readValue(input, Order[].class);
        if (Stream.of(args).anyMatch(arg -> "-r".equals(arg)))
            return Stream.of(orders).filter(o -> "ready".equals(o.status)).count();
        else
            return orders.length;
    }

}
