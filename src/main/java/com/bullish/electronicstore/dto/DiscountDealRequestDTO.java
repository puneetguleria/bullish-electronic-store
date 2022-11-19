package com.bullish.electronicstore.dto;

import com.bullish.electronicstore.type.DealType;
import com.bullish.electronicstore.type.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class DiscountDealRequestDTO {
    private UUID productCode;
    private DiscountType discountType;
    private DealType dealType;
    private double rate;
}
