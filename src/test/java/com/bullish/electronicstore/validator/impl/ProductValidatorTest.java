package com.bullish.electronicstore.validator.impl;

import com.bullish.electronicstore.dto.ProductRequestDTO;
import com.bullish.electronicstore.exception.ValidationErrors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.stream.Stream;

import static com.bullish.electronicstore.exception.ValidationError.INVALID_PRICE;
import static com.bullish.electronicstore.exception.ValidationError.PRODUCT_NAME_MISSING;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductValidatorTest {

    @InjectMocks
    private ProductValidator validator;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @ParameterizedTest
    @MethodSource("getProductsWithNoNameAndPrice")
    @DisplayName("Should return product name and price validation errors")
    void shouldReturnPriceAndNameErrors(ProductRequestDTO product) {
        var errors = validator.validate(product);
        assertNotNull(errors);
        assertIterableEquals(Arrays.asList(PRODUCT_NAME_MISSING, INVALID_PRICE), errors.getErrors());
    }

    @Test
    @DisplayName("Should return price validation error only")
    void shouldReturnPriceErrorOnly() {
        ValidationErrors errors = validator.validate(getProduct("Test", -90));
        assertIterableEquals(Arrays.asList(INVALID_PRICE), errors.getErrors());
    }

    @Test
    @DisplayName("Should return name validation error only")
    void shouldReturnNameErrorOnly() {
        ValidationErrors errors = validator.validate(getProduct(" ", 90));
        assertIterableEquals(Arrays.asList(PRODUCT_NAME_MISSING), errors.getErrors());
    }

    @Test
    @DisplayName("Should report errors")
    void shouldReportAllErrors() {
        ValidationErrors validationErrors = new ValidationErrors();
        validationErrors.addItem(PRODUCT_NAME_MISSING);
        validationErrors.addItem(INVALID_PRICE);
        var errors = assertThrows(ValidationErrors.class, () -> {
            validationErrors.reportIfAny();
        });
        assertIterableEquals(Arrays.asList(PRODUCT_NAME_MISSING, INVALID_PRICE), errors.getErrors());
    }


    private static Stream<Arguments> getProductsWithNoNameAndPrice() {
        return Stream.of(
                arguments(getProduct(" ", 0)),
                arguments(getProduct(null, -1)),
                arguments((ProductRequestDTO) null)
        );
    }

    private static ProductRequestDTO getProduct(String name, double price) {
        return ProductRequestDTO.builder()
                .name(name)
                .price(price).build();
    }

}