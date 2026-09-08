package com.example.demo.strategy;

public class StudentDiscountStrategy implements DiscountStrategy {

    @Override
    public double calculate(double price) {
        return price * 0.9;
    }

}