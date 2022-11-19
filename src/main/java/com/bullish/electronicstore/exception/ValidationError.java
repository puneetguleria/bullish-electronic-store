package com.bullish.electronicstore.exception;

public enum ValidationError {

    INVALID_PRICE("ESP-0001", "Product price cannot be less than zero"),
    PRODUCT_NAME_MISSING("ESP-0002", "Please provide a product name");

    private final String code;
    private final String message;


    ValidationError(final String code, final String message) {
        this.code = code;
        this.message = message;
    }

}
