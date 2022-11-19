package com.bullish.electronicstore.api.controller;

import com.bullish.electronicstore.dto.ShoppingBasketItemRequestDTO;
import com.bullish.electronicstore.dto.ShoppingBasketItemResponseDTO;
import com.bullish.electronicstore.dto.ShoppingBasketResponseDTO;
import com.bullish.electronicstore.service.ShoppingBasketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.http.HttpStatus.*;

class ShoppingBasketControllerTest {

    @Mock
    private ShoppingBasketService shoppingBasketService;

    @InjectMocks
    private ShoppingBasketController basketController;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    @DisplayName("Should create a basket")
    void shouldCreateABasket() {
        UUID code = UUID.randomUUID();
        when(shoppingBasketService.createNewBasket()).thenReturn(ShoppingBasketResponseDTO.builder().code(code).items(new ArrayList<>()).build());
        var response = basketController.createNewBasket();
        assertEquals(CREATED, response.getStatusCode());
        var responseBody = response.getBody();
        assertAll(
                "Checking basket response",
                () -> assertEquals(code, responseBody.getCode()),
                () -> assertTrue(responseBody.getItems().isEmpty()),
                () -> assertEquals(0, responseBody.getTotalPrice())
        );
    }

    @Test
    @DisplayName("Should get a basket")
    void shouldGetABasket() {
        UUID code = UUID.randomUUID();
        when(shoppingBasketService.getBasket(code))
                .thenReturn(Optional.of(ShoppingBasketResponseDTO.builder()
                        .code(code)
                        .items(new ArrayList<>())
                        .build()));
        var response = basketController.getBasket(code);
        assertEquals(OK, response.getStatusCode());
        var responseBody = response.getBody();
        assertAll(
                "Checking basket response",
                () -> assertEquals(code, responseBody.getCode()),
                () -> assertTrue(responseBody.getItems().isEmpty()),
                () -> assertEquals(0, responseBody.getTotalPrice())
        );
    }

    @Test
    @DisplayName("Should return 404 basket not found")
    void shouldReturn404WhenBasketNotFound() {
        UUID code = UUID.randomUUID();
        when(shoppingBasketService.getBasket(code)).thenReturn(Optional.empty());
        var exception = assertThrows(ResponseStatusException.class, () -> {
            basketController.getBasket(code);
        });
        assertEquals(NOT_FOUND, exception.getStatus());
    }

    @Test
    @DisplayName("Should add product to basket")
    void shouldAddProductFromBasket() {
        UUID code = UUID.randomUUID();
        UUID productCode = UUID.randomUUID();
        ShoppingBasketItemRequestDTO itemRequestDTO = new ShoppingBasketItemRequestDTO(productCode, 1);
        double actualPrice = 12.76;
        double discount = 1.30;
        when(shoppingBasketService.addProductToBasket(code, itemRequestDTO))
                .thenReturn(Optional.of(ShoppingBasketResponseDTO.builder()
                        .code(code)
                        .totalPrice(actualPrice + discount)
                        .items(List.of(getShoppingResponseItem(productCode, actualPrice, discount)))
                        .build()));
        var response = basketController.addProductsToBasket(code, itemRequestDTO);
        assertEquals(OK, response.getStatusCode());
        var responseBody = response.getBody();
        assertAll(
                "Checking basket response",
                () -> assertEquals(code, responseBody.getCode()),
                () -> assertEquals(List.of(
                        getShoppingResponseItem(productCode, actualPrice, discount)
                ), responseBody.getItems()),
                () -> assertEquals(actualPrice + discount, responseBody.getTotalPrice())
        );
    }

    @Test
    @DisplayName("Should remove product from basket")
    void shouldRemoveProductFromBasket() {
        UUID code = UUID.randomUUID();
        UUID productCode = UUID.randomUUID();
        ShoppingBasketItemRequestDTO itemRequestDTO = new ShoppingBasketItemRequestDTO(productCode, 1);
        double actualPrice = 12.76;
        double discount = 1.30;
        when(shoppingBasketService.deleteProductsFromBasket(code, itemRequestDTO))
                .thenReturn(Optional.of(ShoppingBasketResponseDTO.builder()
                        .code(code)
                        .totalPrice(actualPrice + discount)
                        .items(List.of(getShoppingResponseItem(productCode, actualPrice, discount)))
                        .build()));
        var response = basketController.deleteProductsFromBasket(code, itemRequestDTO);
        assertEquals(OK, response.getStatusCode());
        var responseBody = response.getBody();
        assertAll(
                "Checking basket response",
                () -> assertEquals(code, responseBody.getCode()),
                () -> assertEquals(List.of(
                        getShoppingResponseItem(productCode, actualPrice, discount)
                ), responseBody.getItems()),
                () -> assertEquals(actualPrice + discount, responseBody.getTotalPrice())
        );
    }

    private ShoppingBasketItemResponseDTO getShoppingResponseItem(UUID productCode, double actualPrice, double discount) {
        return ShoppingBasketItemResponseDTO.builder()
                .name(productCode.toString())
                .actualPrice(actualPrice)
                .priceAfterDiscount(actualPrice - discount)
                .productCode(productCode)
                .build();
    }

}