package com.bullish.electronicstore.api.controller;

import com.bullish.electronicstore.api.ShoppingBasketApi;
import com.bullish.electronicstore.dto.ShoppingBasketItemRequestDTO;
import com.bullish.electronicstore.dto.ShoppingBasketResponseDTO;
import com.bullish.electronicstore.service.ShoppingBasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
public class ShoppingBasketController implements ShoppingBasketApi {

    private final ShoppingBasketService basketService;

    @Autowired
    public ShoppingBasketController(final ShoppingBasketService basketService) {
        this.basketService = basketService;
    }


    @Override
    public ResponseEntity<ShoppingBasketResponseDTO> createNewBasket() {
        return ResponseEntity.status(HttpStatus.CREATED).body(basketService.createNewBasket());
    }

    @Override
    public ResponseEntity<ShoppingBasketResponseDTO> getBasket(final UUID basketCode) {
        return this.getShoppingBasketResponse(basketService.getBasket(basketCode));
    }

    @Override
    public ResponseEntity<ShoppingBasketResponseDTO> addProductsToBasket(final UUID basketCode, final ShoppingBasketItemRequestDTO itemRequestDTO) {
        return this.getShoppingBasketResponse(basketService.addProductToBasket(basketCode, itemRequestDTO));
    }

    @Override
    public ResponseEntity<ShoppingBasketResponseDTO> deleteProductsFromBasket(final UUID basketCode, final ShoppingBasketItemRequestDTO itemRequestDTO) {
        return this.getShoppingBasketResponse(basketService.deleteProductsFromBasket(basketCode, itemRequestDTO));
    }

    private ResponseEntity<ShoppingBasketResponseDTO> getShoppingBasketResponse(final Optional<ShoppingBasketResponseDTO> shoppingBasketResponseDTOOptional) {
        if (shoppingBasketResponseDTOOptional.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Shopping basket not found"
            );
        return ResponseEntity.ok(shoppingBasketResponseDTOOptional.get());
    }
}
