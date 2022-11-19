package com.bullish.electronicstore.api.controller;

import com.bullish.electronicstore.api.DiscountDealApi;
import com.bullish.electronicstore.dto.DiscountDealRequestDTO;
import com.bullish.electronicstore.dto.DiscountDealResponseDTO;
import com.bullish.electronicstore.service.DiscountDealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiscountDealController implements DiscountDealApi {

    private final DiscountDealService discountDealService;

    @Autowired
    public DiscountDealController(final DiscountDealService discountDealService) {
        this.discountDealService = discountDealService;
    }

    @Override
    public ResponseEntity<DiscountDealResponseDTO> save(final DiscountDealRequestDTO discountDealRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(discountDealService.create(discountDealRequestDTO));
    }
}
