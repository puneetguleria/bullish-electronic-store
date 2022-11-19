package com.bullish.electronicstore.api;

import com.bullish.electronicstore.dto.ShoppingBasketItemRequestDTO;
import com.bullish.electronicstore.dto.ShoppingBasketResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

public interface ShoppingBasketApi {

    @PostMapping(value = "/v1/baskets", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ShoppingBasketResponseDTO> createNewBasket();

    @GetMapping(value = "/v1/baskets/{basket-code}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ShoppingBasketResponseDTO> getBasket(@PathVariable("basket-code") UUID basketCode);

    @PostMapping(value = "/v1/baskets/{basket-code}/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ShoppingBasketResponseDTO> addProductsToBasket(@PathVariable("basket-code") UUID basketCode, @RequestBody ShoppingBasketItemRequestDTO itemRequestDTO);

    @DeleteMapping(value = "/v1/baskets/{basket-code}/products", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ShoppingBasketResponseDTO> deleteProductsFromBasket(@PathVariable("basket-code") UUID basketCode, @RequestBody ShoppingBasketItemRequestDTO itemRequestDTO);

}
