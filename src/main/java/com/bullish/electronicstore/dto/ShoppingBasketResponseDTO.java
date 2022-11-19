package com.bullish.electronicstore.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class ShoppingBasketResponseDTO {
    private UUID code;
    private List<ShoppingBasketItemResponseDTO> items;
    private double totalPrice;
}
