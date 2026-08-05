package com.example.demo.strategy;

public class DiscountContext {

    public static DiscountStrategy getStrategy(String type) {

        if (type == null) {
            return new NoDiscountStrategy();
        }

        switch (type.toUpperCase()) {

            case "STUDENT":
                return new StudentDiscountStrategy();

            case "SEASONAL":
                return new SeasonalSaleStrategy();

            default:
                return new NoDiscountStrategy();
        }

    }

}