package com.bullish.electronicstore.validator.impl;

import com.bullish.electronicstore.dto.ProductRequestDTO;
import com.bullish.electronicstore.exception.ValidationError;
import com.bullish.electronicstore.exception.ValidationErrors;
import com.bullish.electronicstore.validator.Validator;
import org.springframework.stereotype.Component;

import static java.util.Objects.isNull;
import static org.apache.logging.log4j.util.Strings.isEmpty;
import static org.apache.logging.log4j.util.Strings.trimToNull;

@Component
public class ProductValidator implements Validator<ProductRequestDTO> {
    @Override
    public ValidationErrors validate(ProductRequestDTO me) {
        var errors = new ValidationErrors();
        if (isNull(me) || isEmpty(trimToNull(me.getName()))) {
            errors.addItem(ValidationError.PRODUCT_NAME_MISSING);
        }
        if (isNull(me) || me.getPrice() <= 0) {
            errors.addItem(ValidationError.INVALID_PRICE);
        }
        return errors;
    }
}
