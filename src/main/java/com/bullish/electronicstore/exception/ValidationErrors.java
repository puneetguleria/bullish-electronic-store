package com.bullish.electronicstore.exception;

import java.util.*;

public class ValidationErrors extends RuntimeException {
    private final List<ValidationError> errors = new ArrayList<>();

    public void addItem(final ValidationError errorItem) {
        this.errors.add(errorItem);
    }

    public List<ValidationError> getErrors(){
        return this.errors;
    }

    public void reportIfAny() {
        if (!this.errors.isEmpty()) {
            throw this;
        }
    }
}

