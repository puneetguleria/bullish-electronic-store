package com.bullish.electronicstore.type;

public enum DiscountType {
    PERCENTAGE {
        public double getDiscountedPrice(double price, double discount) {
            return price - ((price * discount) / 100);
        }
    }, FLAT {
        public double getDiscountedPrice(double price, double discount) {
            return price - discount;
        }
    };
    public abstract double getDiscountedPrice(double price, double discount);
}
