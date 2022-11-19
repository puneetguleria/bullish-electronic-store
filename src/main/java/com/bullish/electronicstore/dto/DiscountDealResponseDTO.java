package com.bullish.electronicstore.dto;

import com.bullish.electronicstore.type.DealType;
import com.bullish.electronicstore.type.DiscountType;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
public class DiscountDealResponseDTO {
    private UUID code;
    private UUID productCode;
    private DiscountType discountType;
    private DealType dealType;
    private double rate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountDealResponseDTO that = (DiscountDealResponseDTO) o;
        return Double.compare(that.rate, rate) == 0 && code.equals(that.code) && productCode.equals(that.productCode) && discountType == that.discountType && dealType == that.dealType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, productCode, discountType, dealType, rate);
    }
}
