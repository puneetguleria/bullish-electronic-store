package com.bullish.electronicstore.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ShoppingBasketItemRequestDTO {
    private UUID productCode;
    private int quantity;
}
