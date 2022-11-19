package com.bullish.electronicstore.api.controller;

import com.bullish.electronicstore.dto.ProductRequestDTO;
import com.bullish.electronicstore.dto.ProductResponseDTO;
import com.bullish.electronicstore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.*;

class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController controller;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    @DisplayName("should create new product")
    void shouldCreateNewProduct() {
        var code = UUID.randomUUID();
        var productName = "Product1";
        var price = 14.234;
        var productRequest = ProductRequestDTO.builder()
                .name(productName)
                .price(price).build();

        var mockProductResponse = ProductResponseDTO.builder()
                .code(code)
                .name(productName)
                .price(price)
                .build();

        when(productService.create(productRequest)).thenReturn(mockProductResponse);

        var response = controller.save(productRequest);

        assertEquals(CREATED, response.getStatusCode());

        var responseBody = response.getBody();
        assertAll(
                "Checking response body",
                () -> assertEquals(productName, responseBody.getName()),
                () -> assertEquals(price, responseBody.getPrice()),
                () -> assertEquals(code, responseBody.getCode())
        );
    }

    @Test
    @DisplayName("Should delete a product")
    void shouldDeleteProduct() {
        var code = UUID.randomUUID();
        when(productService.delete(code)).thenReturn(true);
        var response = controller.delete(code);
        assertEquals(NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DisplayName("Should throw 404 error when product not found while deleting")
    void shouldThrow404WhenProductNotFoundForDelete() {
        var code = UUID.randomUUID();
        when(productService.delete(code)).thenReturn(false);

        var exception = assertThrows(ResponseStatusException.class, () -> {
            controller.delete(code);
        });
        assertEquals(NOT_FOUND, exception.getStatus());
    }

}