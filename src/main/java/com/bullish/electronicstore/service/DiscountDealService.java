package com.bullish.electronicstore.service;

import com.bullish.electronicstore.dto.DiscountDealRequestDTO;
import com.bullish.electronicstore.dto.DiscountDealResponseDTO;

public interface DiscountDealService {
    DiscountDealResponseDTO create(DiscountDealRequestDTO discountDealRequestDTO);
}
