package com.bullish.electronicstore.validator;

import com.bullish.electronicstore.exception.ValidationErrors;

public interface Validator<T> {
    ValidationErrors validate(T me);
}
