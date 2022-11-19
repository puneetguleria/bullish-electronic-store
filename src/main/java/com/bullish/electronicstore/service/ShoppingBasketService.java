package com.bullish.electronicstore.service;

import com.bullish.electronicstore.dto.ShoppingBasketItemRequestDTO;
import com.bullish.electronicstore.dto.ShoppingBasketResponseDTO;

import java.util.Optional;
import java.util.UUID;

public interface ShoppingBasketService {
    ShoppingBasketResponseDTO createNewBasket();

    Optional<ShoppingBasketResponseDTO> getBasket(UUID basketCode);

    Optional<ShoppingBasketResponseDTO> addProductToBasket(UUID basketCode, ShoppingBasketItemRequestDTO itemRequestDTO);

    Optional<ShoppingBasketResponseDTO> deleteProductsFromBasket(UUID basketCode, ShoppingBasketItemRequestDTO itemRequestDTO);
}
