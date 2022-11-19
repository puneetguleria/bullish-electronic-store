package com.bullish.electronicstore.service.impl;

import com.bullish.electronicstore.dto.ProductRequestDTO;
import com.bullish.electronicstore.dto.ProductResponseDTO;
import com.bullish.electronicstore.entity.ProductEntity;
import com.bullish.electronicstore.exception.ValidationErrors;
import com.bullish.electronicstore.generator.CodeGenerator;
import com.bullish.electronicstore.repository.ProductRepository;
import com.bullish.electronicstore.service.DateTimeService;
import com.bullish.electronicstore.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static com.bullish.electronicstore.exception.ValidationError.INVALID_PRICE;
import static com.bullish.electronicstore.exception.ValidationError.PRODUCT_NAME_MISSING;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ProductServiceImplTest {

    @Mock
    private CodeGenerator<UUID> codeGenerator;

    @Mock
    private DateTimeService dateTimeService;

    @Mock
    private Validator<ProductRequestDTO> validator;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should return errors when request to create product is invalid")
    void shouldReturnErrorsWhenInvalidInputOnProductCreate() {
        var productName = "Product1";
        var price = 14.234;

        var input = getMockProduct(productName, price);
        when(validator.validate(input)).thenReturn(getMockValidationErrors());
        var errors = assertThrows(ValidationErrors.class, () -> {
            productService.create(input);
        });
        assertIterableEquals(Arrays.asList(INVALID_PRICE, PRODUCT_NAME_MISSING), errors.getErrors());
    }

    @Test
    @DisplayName("should create new product")
    void shouldCreateNewProduct() {
        var code = UUID.randomUUID();
        var dateTimeNow = LocalDateTime.now();
        var productName = "Product1";
        var price = 10.234;

        when(validator.validate(any(ProductRequestDTO.class))).thenReturn(new ValidationErrors());
        when(codeGenerator.generate()).thenReturn(code);
        when(dateTimeService.now()).thenReturn(dateTimeNow);
        when(productRepository.save(any(ProductEntity.class)))
                .thenReturn(getMockProductEntity(code, productName, price, dateTimeNow));

        ArgumentCaptor<ProductEntity> productEntityArgumentCaptor = ArgumentCaptor.forClass(ProductEntity.class);

        ProductResponseDTO responseDTO = productService.create(getMockProduct(productName, price));

        verify(productRepository, times(1)).save(productEntityArgumentCaptor.capture());

        var savedEntity = productEntityArgumentCaptor.getValue();

        assertAll(
                "Checking saved entity",
                () -> assertEquals(productName, savedEntity.getName()),
                () -> assertEquals(price, savedEntity.getPrice()),
                () -> assertEquals(code, savedEntity.getCode()),
                () -> assertEquals(dateTimeNow, savedEntity.getLastModifiedAt())
        );

        assertAll(
                "Checking response object",
                () -> assertEquals(productName, responseDTO.getName()),
                () -> assertEquals(price, responseDTO.getPrice()),
                () -> assertEquals(code, responseDTO.getCode())
        );

    }

    @Test
    @DisplayName("Should delete a product")
    void shouldDeleteProduct() {
        var code = UUID.randomUUID();
        var productEntity = getMockProductEntity(code, null, -10, null);

        when(productRepository.findOneByCode(code)).thenReturn(Optional.of(productEntity));

        assertTrue(productService.delete(code));

    }

    @Test
    @DisplayName("Should fail to delete a product")
    void shouldFailToDeleteProduct() {
        var code = UUID.randomUUID();
        when(productRepository.findOneByCode(code)).thenReturn(Optional.empty());

        assertFalse(productService.delete(code));

    }

    private ProductRequestDTO getMockProduct(String name, double price) {
        return ProductRequestDTO.builder()
                .name(name)
                .price(price).build();
    }

    private ProductEntity getMockProductEntity(UUID code, String name, double price, LocalDateTime modifiedAt) {
        return ProductEntity.builder()
                .code(code)
                .name(name)
                .price(price)
                .lastModifiedAt(modifiedAt)
                .build();
    }

    private ValidationErrors getMockValidationErrors() {
        ValidationErrors errors = new ValidationErrors();
        errors.addItem(INVALID_PRICE);
        errors.addItem(PRODUCT_NAME_MISSING);
        return errors;
    }

}