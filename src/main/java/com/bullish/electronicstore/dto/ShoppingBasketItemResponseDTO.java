package com.bullish.electronicstore.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;
import java.util.UUID;

@Getter
@Builder
public class ShoppingBasketItemResponseDTO {
    private UUID productCode;
    private String name;
    private double actualPrice;
    private DiscountDealResponseDTO deal;
    private double priceAfterDiscount;

    public void setDeal(DiscountDealResponseDTO deal) {
        this.deal = deal;
    }

    public void setPriceAfterDiscount(double priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingBasketItemResponseDTO that = (ShoppingBasketItemResponseDTO) o;
        return Double.compare(that.actualPrice, actualPrice) == 0 && Double.compare(that.priceAfterDiscount, priceAfterDiscount) == 0 && productCode.equals(that.productCode) && name.equals(that.name) && Objects.equals(deal, that.deal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCode, name, actualPrice, deal, priceAfterDiscount);
    }
}
