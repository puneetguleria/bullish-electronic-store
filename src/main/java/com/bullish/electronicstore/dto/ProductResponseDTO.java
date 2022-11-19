package com.bullish.electronicstore.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductResponseDTO {

    private UUID code;

    private String name;

    private double price;

}
