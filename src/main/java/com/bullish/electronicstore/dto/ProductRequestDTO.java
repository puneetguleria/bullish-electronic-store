package com.bullish.electronicstore.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductRequestDTO {
    private String name;
    private double price;
}
